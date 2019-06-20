package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;

import com.gracefulcode.opengine.core.Ternary;

import org.lwjgl.PointerBuffer;

import java.util.HashMap;

public class ExtensionConfiguration implements com.gracefulcode.opengine.core.ExtensionConfiguration<PointerBuffer, String> {
	protected HashMap<String, Ternary> extensions = new HashMap<String, Ternary>();
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

	public void setExtension(String extensionName, Ternary requireType) {
		if (!this.extensions.containsKey(extensionName)) {
			if (this.isLocked) {
				if (requireType == Ternary.YES) {
					throw new AssertionError("Extension " + extensionName + " is being marked as required, but it is not supported.");
				}
			} else {
				this.extensions.put(extensionName, requireType);
			}
			return;
		}

		Ternary previousValue = this.extensions.get(extensionName);
		switch (previousValue) {
			case UNKNOWN:
				this.extensions.put(extensionName, requireType);
				break;
			case NO:
				if (requireType == Ternary.YES) {
					throw new AssertionError(extensionName + " is both required and not desired. Cannot resolve.");
				}
				break;
			case YES:
				if (requireType == Ternary.NO) {
					throw new AssertionError(extensionName + " is both reqiuerd and not desired. Cannot resolve.");
				}
				break;
		}
	}

	public Ternary getRequireType(String extensionName) {
		if (this.extensions.containsKey(extensionName))
			return this.extensions.get(extensionName);
		return Ternary.NO;
	}

	public boolean shouldHave(String extensionName) {
		if (this.extensions.containsKey(extensionName)) {
			switch (this.extensions.get(extensionName)) {
				case YES:
					return true;
			}
		}
		return false;
	}
}