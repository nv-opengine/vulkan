package com.gracefulcode.opengine.renderers.vulkan;

import com.gracefulcode.opengine.core.Platform;

public interface VulkanPlatform extends Platform<Vulkan> {
	public void setInstance(VkInstance instance);
}