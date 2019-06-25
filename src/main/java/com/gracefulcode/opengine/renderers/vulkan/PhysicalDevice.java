package com.gracefulcode.opengine.renderers.vulkan;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK11.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

/**
 * A physical device is a GPU in your system.
 * <p>
 * In practice, everyone has at least one, and most gamers have two: the
 * dedicated GPU and the crappy one on their motherboard. To handle the
 * discrete/embedded situation (and also the less common one where a person has
 * multiple dedicated GPUs), we need to be able to determine what the
 * capabilities are of our various GPUs and decide among them.
 * <p>
 * This class is responsible for laying out all of the capabilities of a GPU.
 * <p>
 * NOTE: Right now, we are collecting a lot of fields in this class, marked as
 * public. These ALL will be moving at some point. We're taking the agile
 * approach and moving them only when needed.
 *
 * @author Daniel Grace <dgrace@gracefulcode.com>
 * @version 0.1
 */
public class PhysicalDevice {
	protected long id;
	protected VkPhysicalDevice vkPhysicalDevice;
	protected ArrayList<Queue> queues = new ArrayList<Queue>();

	// Base
	public int apiVersion;
	public int deviceId;
	public String deviceName;
	protected int deviceType;
	public int driverVersion;
	public int vendorId;

	// Limits
	public long bufferImageGranularity;
	public int discreteQueuePriorities;
	public int framebufferColorSampleCounts;
	public int framebufferDepthSampleCounts;
	public int framebufferNoAttachmentsSampleCounts;
	public int framebufferStencilSampleCounts;
	public float lineWidthGranularity;
	public float lineWidthMin;
	public float lineWidthMax;
	public int maxBoundDescriptorSets;
	public int maxClipDistances;
	public int maxColorAttachments;
	public int maxCombinedClipAndCullDistances;
	public int maxComputeSharedMemorySize;
	public int maxComputeWorkGroupCountX;
	public int maxComputeWorkGroupCountY;
	public int maxComputeWorkGroupCountZ;
	public int maxComputeWorkGroupInvocations;
	public int maxComputeWorkGroupSizeX;
	public int maxComputeWorkGroupSizeY;
	public int maxComputeWorkGroupSizeZ;
	public int maxCullDistances;
	public int maxDescriptorSetInputAttachments;
	public int maxDescriptorSetSampledImages;
	public int maxDescriptorSetSamplers;
	public int maxDescriptorSetStorageBuffers;
	public int maxDescriptorSetStorageBuffersDynamic;
	public int maxDescriptorSetStorageImages;
	public int maxDescriptorSetUniformBuffers;
	public int maxDescriptorSetUniformBuffersDynamic;
	public int maxDrawIndexedIndexValue;
	public int maxDrawIndirectCount;
	public int maxFragmentCombinedOutputResources;
	public int maxFragmentDualSrcAttachments;
	public int maxFragmentInputComponents;
	public int maxFragmentOutputAttachments;
	public int maxFramebufferHeight;
	public int maxFramebufferLayers;
	public int maxFramebufferWidth;
	public int maxGeometryInputComponents;
	public int maxGeometryOutputComponents;
	public int maxGeometryOutputVertices;
	public int maxGeometryShaderInvocations;
	public int maxGeometryTotalOutputComponents;
	public int maxImageArrayLayers;
	public int maxImageDimension1D;
	public int maxImageDimension2D;
	public int maxImageDimension3D;
	public int maxImageDimensionCube;
	public float maxInterpolationOffset;
	public int maxMemoryAllocationCount;
	public int maxPerStageDescriptorInputAttachments;
	public int maxPerStageDescriptorSampledImages;
	public int maxPerStageDescriptorSamplers;
	public int maxPerStageDescriptorStorageBuffers;
	public int maxPerStageDescriptorStorageImages;
	public int maxPerStageDescriptorUniformBuffers;
	public int maxPerStageResources;
	public int maxPushConstantsSize;
	public int maxSampleMaskWords;
	public int maxSamplerAllocationCount;
	public float maxSamplerAnisotropy;
	public float maxSamplerLodBias;
	public int maxStorageBufferRange;
	public int maxTessellationControlPerPatchOutputComponents;
	public int maxTessellationControlPerVertexInputComponents;
	public int maxTessellationControlPerVertexOutputComponents;
	public int maxTessellationControlTotalOutputComponents;
	public int maxTessellationEvaluationInputComponents;
	public int maxTessellationEvaluationOutputComponents;
	public int maxTessellationGenerationLevel;
	public int maxTessellationPatchSize;
	public int maxTexelBufferElements;
	public int maxTexelGatherOffset;
	public int maxTexelOffset;
	public int maxUniformBufferRange;
	public int maxVertexInputAttributeOffset;
	public int maxVertexInputAttributes;
	public int maxVertexInputBindings;
	public int maxVertexInputBindingStride;
	public int maxVertexOutputComponents;
	public int maxViewportX;
	public int maxViewportY;
	public int maxViewports;
	public float minInterpolationOffset;
	public long minMemoryMapAlignment;
	public long minStorageBufferOffsetAlignment;
	public long minTexelBufferOffsetAlignment;
	public int minTexelGatherOffset;
	public int minTexelOffset;
	public long minUniformBufferOffsetAlignment;
	public int mipmapPrecisionBits;
	public long nonCoherentAtomSize;
	public long optimalBufferCopyOffsetAlignment;
	public long optimalBufferCopyRowPitchAlignment;
	public float pointSizeGranularity;
	public float pointSizeMin;
	public float pointSizeMax;
	public int sampledImageColorSampleCounts;
	public int sampledImageDepthSampleCounts;
	public int sampledImageIntegerSampleCounts;
	public int sampledImageStencilSampleCounts;
	public long sparseAddressSpaceSize;
	public boolean standardSampleLocations;
	public int storageImageSampleCounts;
	public boolean strictLines;
	public int subPixelInterpolationOffsetBits;
	public int subPixelPrecisionBits;
	public int subTexelPrecisionBits;
	public boolean timestampComputeAndGraphics;
	public float timestampPeriod;
	public float viewportBoundsMin;
	public float viewportBoundsMax;
	public int viewportSubPixelBits;

	// Sparse Properties
	public boolean residencyAlignedMipSize;
	public boolean residencyNonResidentStrict;
	public boolean residencyStandard2DBlockShape;
	public boolean residencyStandard2DMultisampleBlockShape;
	public boolean residencyStandard3DBlockShape;

	// Features
	public boolean alphaToOne;
	public boolean depthBiasClamp;
	public boolean depthBounds;
	public boolean depthClamp;
	public boolean drawIndirectFirstInstance;
	public boolean dualSrcBlend;
	public boolean fillModeNonSolid;
	public boolean fragmentStoresAndAtomics;
	public boolean fullDrawIndexUint32;
	public boolean geometryShader;
	public boolean imageCubeArray;
	public boolean independentBlend;
	public boolean inheritedQueries;
	public boolean largePoints;
	public boolean logicOp;
	public boolean multiDrawIndirect;
	public boolean multiViewport;
	public boolean occlusionQueryPrecise;
	public boolean pipelineStatisticsQuery;
	public boolean robustBufferAccess;
	public boolean samplerAnisotropy;
	public boolean sampleRateShading;
	public boolean shaderClipDistance;
	public boolean shaderCullDistance;
	public boolean shaderFloat64;
	public boolean shaderImageGatherExtended;
	public boolean shaderInt16;
	public boolean shaderInt64;
	public boolean shaderResourceMinLod;
	public boolean shaderResourceResidency;
	public boolean shaderSampledImageArrayDynamicIndexing;
	public boolean shaderStorageBufferArrayDynamicIndexing;
	public boolean shaderStorageImageArrayDynamicIndexing;
	public boolean shaderStorageImageExtendedFormats;
	public boolean shaderStorageImageMultisample;
	public boolean shaderStorageImageReadWithoutFormat;
	public boolean shaderStorageImageWriteWithoutFormat;
	public boolean shaderTessellationAndGeometryPointSize;
	public boolean shaderUniformBufferArrayDynamicIndexing;
	public boolean sparseBinding;
	public boolean sparseResidency16Samples;
	public boolean sparseResidency2Samples;
	public boolean sparseResidency4Samples;
	public boolean sparseResidency8Samples;
	public boolean sparseResidencyAliased;
	public boolean sparseResidencyBuffer;
	public boolean sparseResidencyImage2D;
	public boolean sparseResidencyImage3D;
	public boolean tessellationShader;
	public boolean textureCompressionASTC_LDR;
	public boolean textureCompressionBC;
	public boolean textureCompressionETC2;
	public boolean variableMultisampleRate;
	public boolean vertexPipelineStoresAndAtomics;
	public boolean wideLines;

	public PhysicalDevice(org.lwjgl.vulkan.VkInstance vkInstance, long id) {
		this.id = id;
		this.vkPhysicalDevice = new VkPhysicalDevice(id, vkInstance);

		this.setupProperties();
		this.setupFeatures();
		this.setupQueues();
	}

	public String toString() {
		return this.deviceName;
	}

	public VkPhysicalDevice getPhysicalDevice() {
		return this.vkPhysicalDevice;
	}

	public boolean isDiscreteGpu() {
		return this.deviceType == VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU;
	}

	protected void setupProperties() {
		VkPhysicalDeviceProperties properties = VkPhysicalDeviceProperties.calloc();

		vkGetPhysicalDeviceProperties(this.vkPhysicalDevice, properties);

		this.apiVersion = properties.apiVersion();
		this.deviceId = properties.deviceID();
		this.deviceName = properties.deviceNameString();
		this.deviceType = properties.deviceType();
		this.driverVersion = properties.driverVersion();
		this.vendorId = properties.vendorID();

		this.bufferImageGranularity = properties.limits().bufferImageGranularity();
		this.discreteQueuePriorities = properties.limits().discreteQueuePriorities();
		this.framebufferColorSampleCounts = properties.limits().framebufferColorSampleCounts();
		this.framebufferDepthSampleCounts = properties.limits().framebufferDepthSampleCounts();
		this.framebufferNoAttachmentsSampleCounts = properties.limits().framebufferNoAttachmentsSampleCounts();
		this.framebufferStencilSampleCounts = properties.limits().framebufferStencilSampleCounts();
		this.lineWidthGranularity = properties.limits().lineWidthGranularity();
		this.lineWidthMin = properties.limits().lineWidthRange(0);
		this.lineWidthMax = properties.limits().lineWidthRange(1);
		this.maxBoundDescriptorSets = properties.limits().maxBoundDescriptorSets();
		this.maxClipDistances = properties.limits().maxClipDistances();
		this.maxColorAttachments = properties.limits().maxColorAttachments();
		this.maxCombinedClipAndCullDistances = properties.limits().maxCombinedClipAndCullDistances();
		this.maxComputeSharedMemorySize = properties.limits().maxComputeSharedMemorySize();
		this.maxComputeWorkGroupCountX = properties.limits().maxComputeWorkGroupCount(0);
		this.maxComputeWorkGroupCountY = properties.limits().maxComputeWorkGroupCount(1);
		this.maxComputeWorkGroupCountZ = properties.limits().maxComputeWorkGroupCount(2);
		this.maxComputeWorkGroupInvocations = properties.limits().maxComputeWorkGroupInvocations();
		this.maxComputeWorkGroupSizeX = properties.limits().maxComputeWorkGroupSize(0);
		this.maxComputeWorkGroupSizeY = properties.limits().maxComputeWorkGroupSize(1);
		this.maxComputeWorkGroupSizeZ = properties.limits().maxComputeWorkGroupSize(2);
		this.maxCullDistances = properties.limits().maxCullDistances();
		this.maxDescriptorSetInputAttachments = properties.limits().maxDescriptorSetInputAttachments();
		this.maxDescriptorSetSampledImages = properties.limits().maxDescriptorSetSampledImages();
		this.maxDescriptorSetSamplers = properties.limits().maxDescriptorSetSamplers();
		this.maxDescriptorSetStorageBuffers = properties.limits().maxDescriptorSetStorageBuffers();
		this.maxDescriptorSetStorageBuffersDynamic = properties.limits().maxDescriptorSetStorageBuffersDynamic();
		this.maxDescriptorSetStorageImages = properties.limits().maxDescriptorSetSampledImages();
		this.maxDescriptorSetUniformBuffers = properties.limits().maxDescriptorSetUniformBuffers();
		this.maxDescriptorSetUniformBuffersDynamic = properties.limits().maxDescriptorSetUniformBuffersDynamic();
		this.maxDrawIndexedIndexValue = properties.limits().maxDrawIndexedIndexValue();
		this.maxDrawIndirectCount = properties.limits().maxDrawIndirectCount();
		this.maxFragmentCombinedOutputResources = properties.limits().maxFragmentCombinedOutputResources();
		this.maxFragmentDualSrcAttachments = properties.limits().maxFragmentDualSrcAttachments();
		this.maxFragmentInputComponents = properties.limits().maxFragmentInputComponents();
		this.maxFragmentOutputAttachments = properties.limits().maxFragmentOutputAttachments();
		this.maxFramebufferHeight = properties.limits().maxFramebufferHeight();
		this.maxFramebufferLayers = properties.limits().maxFramebufferLayers();
		this.maxFramebufferWidth = properties.limits().maxFramebufferWidth();
		this.maxGeometryInputComponents = properties.limits().maxGeometryInputComponents();
		this.maxGeometryOutputComponents = properties.limits().maxGeometryOutputComponents();
		this.maxGeometryOutputVertices = properties.limits().maxGeometryOutputVertices();
		this.maxGeometryShaderInvocations = properties.limits().maxGeometryShaderInvocations();
		this.maxGeometryTotalOutputComponents = properties.limits().maxGeometryTotalOutputComponents();
		this.maxImageArrayLayers = properties.limits().maxImageArrayLayers();
		this.maxImageDimension1D = properties.limits().maxImageDimension1D();
		this.maxImageDimension2D = properties.limits().maxImageDimension2D();
		this.maxImageDimension3D = properties.limits().maxImageDimension3D();
		this.maxImageDimensionCube = properties.limits().maxImageDimensionCube();
		this.maxInterpolationOffset = properties.limits().maxInterpolationOffset();
		this.maxMemoryAllocationCount = properties.limits().maxMemoryAllocationCount();
		this.maxPerStageDescriptorInputAttachments = properties.limits().maxPerStageDescriptorInputAttachments();
		this.maxPerStageDescriptorSampledImages = properties.limits().maxPerStageDescriptorSampledImages();
		this.maxPerStageDescriptorSamplers = properties.limits().maxPerStageDescriptorSamplers();
		this.maxPerStageDescriptorStorageBuffers = properties.limits().maxPerStageDescriptorStorageBuffers();
		this.maxPerStageDescriptorStorageImages = properties.limits().maxPerStageDescriptorStorageImages();
		this.maxPerStageDescriptorUniformBuffers = properties.limits().maxPerStageDescriptorUniformBuffers();
		this.maxPerStageResources = properties.limits().maxPerStageResources();
		this.maxPushConstantsSize = properties.limits().maxPushConstantsSize();
		this.maxSampleMaskWords = properties.limits().maxSampleMaskWords();
		this.maxSamplerAllocationCount = properties.limits().maxSamplerAllocationCount();
		this.maxSamplerAnisotropy = properties.limits().maxSamplerAnisotropy();
		this.maxSamplerLodBias = properties.limits().maxSamplerLodBias();
		this.maxStorageBufferRange = properties.limits().maxStorageBufferRange();
		this.maxTessellationControlPerPatchOutputComponents = properties.limits().maxTessellationControlPerPatchOutputComponents();
		this.maxTessellationControlPerVertexInputComponents = properties.limits().maxTessellationControlPerVertexInputComponents();
		this.maxTessellationControlPerVertexOutputComponents = properties.limits().maxTessellationControlPerVertexOutputComponents();
		this.maxTessellationControlTotalOutputComponents = properties.limits().maxTessellationControlTotalOutputComponents();
		this.maxTessellationEvaluationInputComponents = properties.limits().maxTessellationEvaluationInputComponents();
		this.maxTessellationEvaluationOutputComponents = properties.limits().maxTessellationEvaluationOutputComponents();
		this.maxTessellationGenerationLevel = properties.limits().maxTessellationGenerationLevel();
		this.maxTessellationPatchSize = properties.limits().maxTessellationPatchSize();
		this.maxTexelBufferElements = properties.limits().maxTexelBufferElements();
		this.maxTexelGatherOffset = properties.limits().maxTexelGatherOffset();
		this.maxTexelOffset = properties.limits().maxTexelOffset();
		this.maxUniformBufferRange = properties.limits().maxUniformBufferRange();
		this.maxVertexInputAttributeOffset = properties.limits().maxVertexInputAttributeOffset();
		this.maxVertexInputAttributes = properties.limits().maxVertexInputAttributes();
		this.maxVertexInputBindings = properties.limits().maxVertexInputBindings();
		this.maxVertexInputBindingStride = properties.limits().maxVertexInputBindingStride();
		this.maxVertexOutputComponents = properties.limits().maxVertexOutputComponents();
		this.maxViewportX = properties.limits().maxViewportDimensions(0);
		this.maxViewportY = properties.limits().maxViewportDimensions(1);
		this.maxViewports = properties.limits().maxViewports();
		this.minInterpolationOffset = properties.limits().minInterpolationOffset();
		this.minMemoryMapAlignment = properties.limits().minMemoryMapAlignment();
		this.minStorageBufferOffsetAlignment = properties.limits().minStorageBufferOffsetAlignment();
		this.minTexelBufferOffsetAlignment = properties.limits().minTexelBufferOffsetAlignment();
		this.minTexelGatherOffset = properties.limits().minTexelGatherOffset();
		this.minTexelOffset = properties.limits().minTexelOffset();
		this.minUniformBufferOffsetAlignment = properties.limits().minUniformBufferOffsetAlignment();
		this.mipmapPrecisionBits = properties.limits().mipmapPrecisionBits();
		this.nonCoherentAtomSize = properties.limits().nonCoherentAtomSize();
		this.optimalBufferCopyOffsetAlignment = properties.limits().optimalBufferCopyOffsetAlignment();
		this.optimalBufferCopyRowPitchAlignment = properties.limits().optimalBufferCopyRowPitchAlignment();
		this.pointSizeGranularity = properties.limits().pointSizeGranularity();
		this.pointSizeMin = properties.limits().pointSizeRange(0);
		this.pointSizeMax = properties.limits().pointSizeRange(1);
		this.sampledImageColorSampleCounts = properties.limits().sampledImageColorSampleCounts();
		this.sampledImageDepthSampleCounts = properties.limits().sampledImageDepthSampleCounts();
		this.sampledImageIntegerSampleCounts = properties.limits().sampledImageIntegerSampleCounts();
		this.sampledImageStencilSampleCounts = properties.limits().sampledImageStencilSampleCounts();
		this.sparseAddressSpaceSize = properties.limits().sparseAddressSpaceSize();
		this.standardSampleLocations = properties.limits().standardSampleLocations();
		this.storageImageSampleCounts = properties.limits().storageImageSampleCounts();
		this.strictLines = properties.limits().strictLines();
		this.subPixelInterpolationOffsetBits = properties.limits().subPixelInterpolationOffsetBits();
		this.subPixelPrecisionBits = properties.limits().subPixelPrecisionBits();
		this.subTexelPrecisionBits = properties.limits().subTexelPrecisionBits();
		this.timestampComputeAndGraphics = properties.limits().timestampComputeAndGraphics();
		this.timestampPeriod = properties.limits().timestampPeriod();
		this.viewportBoundsMin = properties.limits().viewportBoundsRange(0);
		this.viewportBoundsMax = properties.limits().viewportBoundsRange(1);
		this.viewportSubPixelBits = properties.limits().viewportSubPixelBits();

		this.residencyAlignedMipSize = properties.sparseProperties().residencyAlignedMipSize();
		this.residencyNonResidentStrict = properties.sparseProperties().residencyNonResidentStrict();
		this.residencyStandard2DBlockShape = properties.sparseProperties().residencyStandard2DBlockShape();
		this.residencyStandard2DMultisampleBlockShape = properties.sparseProperties().residencyStandard2DMultisampleBlockShape();
		this.residencyStandard3DBlockShape = properties.sparseProperties().residencyStandard3DBlockShape();

		properties.free();
	}

	protected void setupFeatures() {
		VkPhysicalDeviceFeatures features = VkPhysicalDeviceFeatures.calloc();

		vkGetPhysicalDeviceFeatures(this.vkPhysicalDevice, features);

		this.alphaToOne = features.alphaToOne();
		this.depthBiasClamp = features.depthBiasClamp();
		this.depthBounds = features.depthBounds();
		this.depthClamp = features.depthClamp();
		this.drawIndirectFirstInstance = features.drawIndirectFirstInstance();
		this.dualSrcBlend = features.dualSrcBlend();
		this.fillModeNonSolid = features.fillModeNonSolid();
		this.fragmentStoresAndAtomics = features.fragmentStoresAndAtomics();
		this.fullDrawIndexUint32 = features.fullDrawIndexUint32();
		this.geometryShader = features.geometryShader();
		this.imageCubeArray = features.imageCubeArray();
		this.independentBlend = features.independentBlend();
		this.inheritedQueries = features.inheritedQueries();
		this.largePoints = features.largePoints();
		this.logicOp = features.logicOp();
		this.multiDrawIndirect = features.multiDrawIndirect();
		this.multiViewport = features.multiViewport();
		this.occlusionQueryPrecise = features.occlusionQueryPrecise();
		this.pipelineStatisticsQuery = features.pipelineStatisticsQuery();
		this.robustBufferAccess = features.robustBufferAccess();
		this.samplerAnisotropy = features.samplerAnisotropy();
		this.sampleRateShading = features.sampleRateShading();
		this.shaderClipDistance = features.shaderClipDistance();
		this.shaderCullDistance = features.shaderCullDistance();
		this.shaderFloat64 = features.shaderFloat64();
		this.shaderImageGatherExtended = features.shaderImageGatherExtended();
		this.shaderInt16 = features.shaderInt16();
		this.shaderInt64 = features.shaderInt64();
		this.shaderResourceMinLod = features.shaderResourceMinLod();
		this.shaderResourceResidency = features.shaderResourceResidency();
		this.shaderSampledImageArrayDynamicIndexing = features.shaderSampledImageArrayDynamicIndexing();
		this.shaderStorageBufferArrayDynamicIndexing = features.shaderStorageBufferArrayDynamicIndexing();
		this.shaderStorageImageArrayDynamicIndexing = features.shaderStorageImageArrayDynamicIndexing();
		this.shaderStorageImageExtendedFormats = features.shaderStorageImageExtendedFormats();
		this.shaderStorageImageMultisample = features.shaderStorageImageMultisample();
		this.shaderStorageImageReadWithoutFormat = features.shaderStorageImageReadWithoutFormat();
		this.shaderStorageImageWriteWithoutFormat = features.shaderStorageImageWriteWithoutFormat();
		this.shaderTessellationAndGeometryPointSize = features.shaderTessellationAndGeometryPointSize();
		this.shaderUniformBufferArrayDynamicIndexing = features.shaderUniformBufferArrayDynamicIndexing();
		this.sparseBinding = features.sparseBinding();
		this.sparseResidency16Samples = features.sparseResidency16Samples();
		this.sparseResidency2Samples = features.sparseResidency2Samples();
		this.sparseResidency4Samples = features.sparseResidency4Samples();
		this.sparseResidency8Samples = features.sparseResidency8Samples();
		this.sparseResidencyAliased = features.sparseResidencyAliased();
		this.sparseResidencyBuffer = features.sparseResidencyBuffer();
		this.sparseResidencyImage2D = features.sparseResidencyImage2D();
		this.sparseResidencyImage3D = features.sparseResidencyImage3D();
		this.tessellationShader = features.tessellationShader();
		this.textureCompressionASTC_LDR = features.textureCompressionASTC_LDR();
		this.textureCompressionBC = features.textureCompressionBC();
		this.textureCompressionETC2 = features.textureCompressionETC2();
		this.variableMultisampleRate = features.variableMultisampleRate();
		this.vertexPipelineStoresAndAtomics = features.vertexPipelineStoresAndAtomics();
		this.wideLines = features.wideLines();

		features.free();
	}

	public static class Queue {
		protected int index;
		protected int count;
		protected int timestampValidBits;
		protected int depth;
		protected int width;
		protected int height;

		protected boolean graphics;
		protected boolean compute;
		protected boolean transfer;
		protected boolean sparse;
		protected boolean prot;

		private Queue(int index, int count, int flags, int timestampValidBits, int depth, int width, int height) {
			this.index = index;
			this.count = count;
			this.timestampValidBits = timestampValidBits;
			this.depth = depth;
			this.width = width;
			this.height = height;

			this.graphics = (flags & VK_QUEUE_GRAPHICS_BIT) != 0;
			this.compute = (flags & VK_QUEUE_COMPUTE_BIT) != 0;
			this.transfer = (flags & VK_QUEUE_TRANSFER_BIT) != 0;
			this.sparse = (flags & VK_QUEUE_SPARSE_BINDING_BIT) != 0;
			this.prot = (flags & VK_QUEUE_PROTECTED_BIT) != 0;
		}

		public String toString() {
			return "Queue<index: " + this.index + ", count:" + this.count + ", transfer:<" + this.depth + ", " + this.width + ", " + this.height + ">, graphics: " + this.graphics + ", compute: " + this.compute + ", transfer: " + this.transfer + ", sparse: " + this.sparse + ", protected: " + this.prot + ">";
		}
	}

	protected void setupQueues() {
		IntBuffer numQueues = memAllocInt(1);

		vkGetPhysicalDeviceQueueFamilyProperties(this.vkPhysicalDevice, numQueues, null);
		VkQueueFamilyProperties.Buffer queueProps = VkQueueFamilyProperties.calloc(numQueues.get(0));
		vkGetPhysicalDeviceQueueFamilyProperties(this.vkPhysicalDevice, numQueues, queueProps);

		for (int index = 0; index < numQueues.get(0); index++) {
			VkQueueFamilyProperties props = queueProps.get(index);

			Queue queue = new Queue(index, props.queueCount(), props.queueFlags(), props.timestampValidBits(), props.minImageTransferGranularity().depth(), props.minImageTransferGranularity().width(), props.minImageTransferGranularity().height());
			this.queues.add(queue);
		}
		System.out.println(this.queues);

		memFree(numQueues);
		queueProps.free();
	}

	protected void dispose() {
	}
}