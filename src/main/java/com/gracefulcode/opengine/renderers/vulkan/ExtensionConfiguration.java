package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.PointerBuffer;

import java.util.HashMap;

public class ExtensionConfiguration implements com.gracefulcode.opengine.core.ExtensionConfiguration<PointerBuffer, String> {
	protected HashMap<String, RequireType> extensions = new HashMap<String, RequireType>();
	protected boolean isLocked = false;

	public void lock() {
		this.isLocked = true;
	}

	public String toString() {
		return this.extensions.toString();
	}

	public PointerBuffer getConfiguredExtensions() {
		int i = 0;
		for (String key: this.extensions.keySet()) {
			if (this.shouldHave(key)) i++;
		}

		PointerBuffer ret = memAllocPointer(i);
		for (String key: this.extensions.keySet()) {
			if (this.shouldHave(key)) ret.put(memUTF8(key));
		}
		ret.flip();

		return ret;
	}

	public void setExtension(String extensionName, RequireType requireType) {
		if (!this.extensions.containsKey(extensionName)) {
			if (this.isLocked) {
				if (requireType == RequireType.REQUIRED) {
					throw new AssertionError("Extension " + extensionName + " is being marked as required, but it is not supported.");
				}
			} else {
				this.extensions.put(extensionName, requireType);
			}
			return;
		}

		RequireType previousValue = this.extensions.get(extensionName);
		switch (previousValue) {
			case DONT_CARE:
			case DESIRED:
				this.extensions.put(extensionName, requireType);
				break;
			case NOT_DESIRED:
				if (requireType == RequireType.REQUIRED) {
					throw new AssertionError(extensionName + " is both required and not desired. Cannot resolve.");
				}
				break;
			case REQUIRED:
				if (requireType == RequireType.NOT_DESIRED) {
					throw new AssertionError(extensionName + " is both reqiuerd and not desired. Cannot resolve.");
				}
				break;
		}
	}

	public RequireType getRequireType(String extensionName) {
		if (this.extensions.containsKey(extensionName))
			return this.extensions.get(extensionName);
		return RequireType.DONT_CARE;
	}

	public boolean shouldHave(String extensionName) {
		if (this.extensions.containsKey(extensionName)) {
			switch (this.extensions.get(extensionName)) {
				case REQUIRED:
				case DESIRED:
					return true;
			}
		}
		return false;
	}
}