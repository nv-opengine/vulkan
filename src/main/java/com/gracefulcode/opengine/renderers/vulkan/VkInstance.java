package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

import com.gracefulcode.opengine.core.Platform;
import com.gracefulcode.opengine.core.Ternary;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

/**
 * VkInstance is a wrapper around the VkInstance from lwjgl.
 * <p>
 * It should not expose anything lwjgl-specific, however as we are trying to be
 * pretty agnostic and be able to change platforms out in the future.
 *
 * @author Daniel Grace <dgrace@gracefulcode.com>
 * @version 0.1
 * @since 0.1
 */
public class VkInstance {
	/**
	 * Vulkan long identifying this instance.
	 */
	protected long id;

	/**
	 * We use this to collect supported and needed extensions.
	 * <p>
	 * After initialization it holds the extensions that have active.
	 */
	protected ExtensionConfiguration extensionConfiguration;

	/**
	 * Currently unused as layers seem to only exist for debugging and
	 * debugging isn't yet done.
	 */
	protected LayerConfiguration layerConfiguration;

	/**
	 * Many of the lwjgl functions require their own VkInstance passed in. We
	 * thus need to create and store this for that.
	 */
	org.lwjgl.vulkan.VkInstance vkInstance;

	/**
	 * The physical devices that this instance has access to.
	 * <p>
	 * We will be choosing from among these when we create all of our queues
	 * and whatnot later. Once we get to there we may be keeping these in a
	 * dictionary of Devices to what has been initialized.
	 */
	protected ArrayList<PhysicalDevice> physicalDevices = new ArrayList<PhysicalDevice>();

	/**
	 * Initialize a VkInstance. There should be one per application.
	 *
	 * @param applicationName The name of your application. Not really used
	 *        anywhere, but you should fill it out.
	 * @param majorVersion The major version of your application. Not really
	 *        used anywhere, but you should fill it out.
	 * @param minorVersion The minor version of your application. Not really
	 *        used anywhere, but you should fill it out.
	 * @param patchVersion The patch version of your application. Not really
	 *        used anywhere, but you should fill it out.
	 * @param platform The platform that this VkInstance is being created for.
	 *        The platform will be able to alter VkIinstance creation to fit
	 *        its requirements.
	 */
	public VkInstance(String applicationName, int majorVersion, int minorVersion, int patchVersion, Platform<Vulkan> platform) {
		this.extensionConfiguration = new ExtensionConfiguration();
		this.layerConfiguration = new LayerConfiguration();

		/**
		 * appInfo is basic information about the application itself. There
		 * isn't anything super important here, though we do let Vulkan know
		 * about both the engine and the particular game so that it can change
		 * its behavior if there's a particular popular engine/game.
		 */
		VkApplicationInfo appInfo = VkApplicationInfo.calloc();
		appInfo.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
		appInfo.pApplicationName(memUTF8(applicationName));
		appInfo.applicationVersion(VK_MAKE_VERSION(majorVersion, minorVersion, patchVersion));
		appInfo.pEngineName(memUTF8("Opengine"));
		appInfo.engineVersion(1);
		appInfo.apiVersion(VK_MAKE_VERSION(1, 0, 2));

		/**
		 * Create info is pretty basic right now.
		 */
		VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc();
		createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
		createInfo.pApplicationInfo(appInfo);
		createInfo.pNext(NULL);

		/**
		 * Configure the extensions that we need to handle.
		 */
		IntBuffer ib = memAllocInt(1);
		vkEnumerateInstanceExtensionProperties((CharSequence)null, ib, null);
		VkExtensionProperties.Buffer extensionProperties = VkExtensionProperties.calloc(ib.get(0));
		vkEnumerateInstanceExtensionProperties((CharSequence)null, ib, extensionProperties);
		for (int i = 0; i < extensionProperties.limit(); i++) {
			extensionProperties.position(i);
			this.extensionConfiguration.setExtension(
				extensionProperties.extensionNameString(),
				Ternary.UNKNOWN
			);
		}
		this.extensionConfiguration.lock();

		platform.configureRendererExtensions(this.extensionConfiguration);

		PointerBuffer configuredExtensions = this.extensionConfiguration.getConfiguredExtensions();
		createInfo.ppEnabledExtensionNames(configuredExtensions);

		PointerBuffer configuredLayers = this.layerConfiguration.getConfiguredLayers();
		createInfo.ppEnabledLayerNames(configuredLayers);

		PointerBuffer pInstance = memAllocPointer(1);
		int err = vkCreateInstance(createInfo, null, pInstance);
		this.id = pInstance.get(0);
		memFree(pInstance);

		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create VkInstance: " + Vulkan.translateVulkanResult(err));
		}

		this.vkInstance = new org.lwjgl.vulkan.VkInstance(this.id, createInfo);

		err = vkEnumeratePhysicalDevices(this.vkInstance, ib, null);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Could not enumerate physical devices: " + Vulkan.translateVulkanResult(err));
		}

		int numPhysicalDevices = ib.get(0);
		System.out.println("Num physical devices: " + numPhysicalDevices);

		PointerBuffer pPhysicalDevices = memAllocPointer(numPhysicalDevices);
		err = vkEnumeratePhysicalDevices(this.vkInstance, ib, pPhysicalDevices);
		memFree(ib);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Could not enumerate physical devices: " + Vulkan.translateVulkanResult(err));
		}

		for (int i = 0; i < numPhysicalDevices; i++) {
			long physicalDeviceId = pPhysicalDevices.get(i);
			PhysicalDevice physicalDevice = new PhysicalDevice(this.vkInstance, physicalDeviceId);

			this.physicalDevices.add(physicalDevice);
		}
		memFree(pPhysicalDevices);
	}

	public org.lwjgl.vulkan.VkInstance getInstance() {
		return this.vkInstance;
	}

	public PhysicalDevice getFirst(Comparator<PhysicalDevice> comparator) {
		PhysicalDevice ret = this.physicalDevices.get(0);
		for (int i = 0; i < this.physicalDevices.size(); i++) {
			PhysicalDevice contendor = this.physicalDevices.get(i);
			if (comparator.compare(ret, contendor) < 0) {
				ret = contendor;
			}
		}
		return ret;
	}

	public void dispose() {
		vkDestroyInstance(this.vkInstance, null);
	}
}