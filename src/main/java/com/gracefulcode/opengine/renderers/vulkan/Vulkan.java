package com.gracefulcode.opengine.renderers.vulkan;

import com.gracefulcode.opengine.core.Platform;
import com.gracefulcode.opengine.core.Renderer;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.EXTDebugReport.*;
import static org.lwjgl.vulkan.KHRDisplaySwapchain.*;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
// import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

/**
 * The vulkan renderer renders ... using vulkan. It does this using lwjgl3,
 * which currently only works on desktop (I think?). I plan on seeing if I can
 * genericize this to work on android as well.
 *
 * @author Daniel Grace <dgrace@gracefulcode.com>
 */
public class Vulkan implements Renderer {
	protected ExtensionConfiguration extensionConfiguration;
	protected LayerConfiguration layerConfiguration;
	protected VkInstance vkInstance;
	protected ArrayList<PhysicalDevice> physicalDevices = new ArrayList<PhysicalDevice>();

	/**
	 * Initializes Vulkan in the default way. Currently the only thing
	 * supported.
	 */
	public Vulkan(Platform<Vulkan> platform, String applicationName, int majorVersion, int minorVersion, int patchVersion) {
		this.vkInstance = new VkInstance(applicationName, majorVersion, minorVersion, patchVersion, platform);
		this.enumeratePhysicalDevices();
	}

	protected void enumeratePhysicalDevices() {
		// IntBuffer ib = memAllocInt(1);
		// int err = vkEnumeratePhysicalDevices(this.vkInstance, ib, null);
		// if (err != VK_SUCCESS) {
		// 	throw new AssertionError("Could not enumerate physical devices: " + Vulkan.translateVulkanResult(err));
		// }

		// int numPhysicalDevices = ib.get(0);

		// PointerBuffer pPhysicalDevices = memAllocPointer(numPhysicalDevices);
		// err = vkEnumeratePhysicalDevices(this.vkInstance, ib, pPhysicalDevices);
		// memFree(ib);
		// if (err != VK_SUCCESS) {
		// 	throw new AssertionError("Could not enumerate physical devices: " + Vulkan.translateVulkanResult(err));
		// }

		// for (int i = 0; i < numPhysicalDevices; i++) {
		// 	long physicalDeviceId = pPhysicalDevices.get(i);
		// 	PhysicalDevice physicalDevice = new PhysicalDevice(physicalDeviceId);

		// 	this.physicalDevices.add(physicalDevice);
		// }
		// memFree(pPhysicalDevices);
	}

	public void dispose() {
		this.vkInstance.dispose();
	}

	/**
	 * When we get an error code back, translate that into plain text.
	 *
	 * @param result The error code returned from vulkan.
	 * @return The string version fo that error.
	 */
	public static String translateVulkanResult(int result) {
		switch (result) {
			// Success codes
			case VK_SUCCESS:
				return "Command successfully completed.";
			case VK_NOT_READY:
				return "A fence or query has not yet completed.";
			case VK_TIMEOUT:
				return "A wait operation has not completed in the specified time.";
			case VK_EVENT_SET:
				return "An event is signaled.";
			case VK_EVENT_RESET:
				return "An event is unsignaled.";
			case VK_INCOMPLETE:
				return "A return array was too small for the result.";
			case VK_SUBOPTIMAL_KHR:
				return "A swapchain no longer matches the surface properties exactly, but can still be used to present to the surface successfully.";

			// Error codes
			case VK_ERROR_OUT_OF_HOST_MEMORY:
				return "A host memory allocation has failed.";
			case VK_ERROR_OUT_OF_DEVICE_MEMORY:
				return "A device memory allocation has failed.";
			case VK_ERROR_INITIALIZATION_FAILED:
				return "Initialization of an object could not be completed for implementation-specific reasons.";
			case VK_ERROR_DEVICE_LOST:
				return "The logical or physical device has been lost.";
			case VK_ERROR_MEMORY_MAP_FAILED:
				return "Mapping of a memory object has failed.";
			case VK_ERROR_LAYER_NOT_PRESENT:
				return "A requested layer is not present or could not be loaded.";
			case VK_ERROR_EXTENSION_NOT_PRESENT:
				return "A requested extension is not supported.";
			case VK_ERROR_FEATURE_NOT_PRESENT:
				return "A requested feature is not supported.";
			case VK_ERROR_INCOMPATIBLE_DRIVER:
				return "The requested version of Vulkan is not supported by the driver or is otherwise incompatible for implementation-specific reasons.";
			case VK_ERROR_TOO_MANY_OBJECTS:
				return "Too many objects of the type have already been created.";
			case VK_ERROR_FORMAT_NOT_SUPPORTED:
				return "A requested format is not supported on this device.";
			case VK_ERROR_SURFACE_LOST_KHR:
				return "A surface is no longer available.";
			case VK_ERROR_NATIVE_WINDOW_IN_USE_KHR:
				return "The requested window is already connected to a VkSurfaceKHR, or to some other non-Vulkan API.";
			case VK_ERROR_OUT_OF_DATE_KHR:
				return "A surface has changed in such a way that it is no longer compatible with the swapchain, and further presentation requests using the "
					+ "swapchain will fail. Applications must query the new surface properties and recreate their swapchain if they wish to continue" + "presenting to the surface.";
			case VK_ERROR_INCOMPATIBLE_DISPLAY_KHR:
				return "The display used by a swapchain does not use the same presentable image layout, or is incompatible in a way that prevents sharing an" + " image.";
			case VK_ERROR_VALIDATION_FAILED_EXT:
				return "A validation layer found an error.";
			default:
				return String.format("%s [%d]", "Unknown", Integer.valueOf(result));
		}
	}
}