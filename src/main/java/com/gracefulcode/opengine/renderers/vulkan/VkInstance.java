package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

import com.gracefulcode.opengine.core.Platform;

import java.nio.IntBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

public class VkInstance {
	protected long id;
	protected ExtensionConfiguration extensionConfiguration;
	protected LayerConfiguration layerConfiguration;
	protected org.lwjgl.vulkan.VkInstance vkInstance;

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
				ExtensionConfiguration.RequireType.DONT_CARE
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
		memFree(ib);
	}

	public void dispose() {
		vkDestroyInstance(this.vkInstance, null);
	}
}