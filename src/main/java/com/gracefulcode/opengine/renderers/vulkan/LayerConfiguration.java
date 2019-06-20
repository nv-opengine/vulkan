package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;

import com.gracefulcode.opengine.core.Ternary;

import org.lwjgl.PointerBuffer;

import java.util.HashMap;

public class LayerConfiguration implements com.gracefulcode.opengine.core.LayerConfiguration<PointerBuffer, String> {
	protected HashMap<String, Ternary> layers = new HashMap<String, Ternary>();
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

	public void setLayer(String layerName, Ternary requireType) {
		if (!this.layers.containsKey(layerName)) {
			if (this.isLocked) {
				if (requireType == Ternary.YES) {
					throw new AssertionError("Layer " + layerName + " is being marked as required, but it is not supported.");
				}
			} else {
				this.layers.put(layerName, requireType);
			}
			return;
		}

		Ternary previousValue = this.layers.get(layerName);
		switch (previousValue) {
			case UNKNOWN:
				this.layers.put(layerName, requireType);
				break;
			case NO:
				if (requireType == Ternary.YES) {
					throw new AssertionError(layerName + " is both required and not desired. Cannot resolve.");
				}
				break;
			case YES:
				if (requireType == Ternary.NO) {
					throw new AssertionError(layerName + " is both reqiuerd and not desired. Cannot resolve.");
				}
				break;
		}
	}

	public Ternary getRequireType(String layerName) {
		if (this.layers.containsKey(layerName))
			return this.layers.get(layerName);
		return Ternary.UNKNOWN;
	}

	public boolean shouldHave(String layerName) {
		if (this.layers.containsKey(layerName)) {
			switch (this.layers.get(layerName)) {
				case YES:
					return true;
			}
		}
		return false;
	}
}