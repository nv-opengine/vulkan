package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.PointerBuffer;

import java.util.HashMap;

public class LayerConfiguration implements com.gracefulcode.opengine.core.LayerConfiguration<PointerBuffer, String> {
	protected HashMap<String, RequireType> layers = new HashMap<String, RequireType>();
	protected boolean isLocked = false;

	public void lock() {
		this.isLocked = true;
	}

	public String toString() {
		return this.layers.toString();
	}

	public PointerBuffer getConfiguredLayers() {
		int i = 0;
		for (String key: this.layers.keySet()) {
			if (this.shouldHave(key)) i++;
		}

		PointerBuffer ret = memAllocPointer(i);
		for (String key: this.layers.keySet()) {
			if (this.shouldHave(key)) ret.put(memUTF8(key));
		}
		ret.flip();

		return ret;
	}

	public void setLayer(String layerName, RequireType requireType) {
		if (!this.layers.containsKey(layerName)) {
			if (this.isLocked) {
				if (requireType == RequireType.REQUIRED) {
					throw new AssertionError("Layer " + layerName + " is being marked as required, but it is not supported.");
				}
			} else {
				this.layers.put(layerName, requireType);
			}
			return;
		}

		RequireType previousValue = this.layers.get(layerName);
		switch (previousValue) {
			case DONT_CARE:
			case DESIRED:
				this.layers.put(layerName, requireType);
				break;
			case NOT_DESIRED:
				if (requireType == RequireType.REQUIRED) {
					throw new AssertionError(layerName + " is both required and not desired. Cannot resolve.");
				}
				break;
			case REQUIRED:
				if (requireType == RequireType.NOT_DESIRED) {
					throw new AssertionError(layerName + " is both reqiuerd and not desired. Cannot resolve.");
				}
				break;
		}
	}

	public RequireType getRequireType(String layerName) {
		if (this.layers.containsKey(layerName))
			return this.layers.get(layerName);
		return RequireType.DONT_CARE;
	}

	public boolean shouldHave(String layerName) {
		if (this.layers.containsKey(layerName)) {
			switch (this.layers.get(layerName)) {
				case REQUIRED:
				case DESIRED:
					return true;
			}
		}
		return false;
	}
}