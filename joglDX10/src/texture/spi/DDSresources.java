/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texture.spi;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;
import static texture.spi.DDSresources.DxgiFormat.*;
import static texture.spi.DDSresources.D3dFormat.*;

/**
 *
 * @author elect
 */
public class DDSresources {

    /**
     * old D3D new D3D OpenGL D3D support OpenGL support Link DXT1 BC1 S3TC 6.0
     * EXT_texture_compression_s3tc (1) DXT3 BC2 S3TC 6.0
     * EXT_texture_compression_s3tc (1) DXT5 BC3 S3TC 6.0
     * EXT_texture_compression_s3tc (1) ATI1 BC4 RGTC1 10.0 3.0 (or via
     * extension) (2) ATI2 BC5 RGTC2 10.0 3.0 (or via extension) (2) – BC6H
     * BPTC_FLOAT 11.0 4.2 (or via extension) (3) – BC7 BPTC 11.0 4.2 (or via
     * extension) (3)
     *
     * (1) http://www.opengl.org/registry/specs/EXT/texture_compression_s3tc.txt
     * (2) http://www.opengl.org/registry/specs/EXT/texture_compression_rgtc.txt
     * (3) http://www.opengl.org/registry/specs/ARB/texture_compression_bptc.txt
     *
     * http://renderingpipeline.com/2012/07/texture-compression/
     * https://msdn.microsoft.com/en-us/library/windows/desktop/hh308955%28v=vs.85%29.aspx
     */
    //
    // Selected bits in header flags
    //
    public static final int DDSD_CAPS = 0x00000001; // Capacities are valid
    public static final int DDSD_HEIGHT = 0x00000002; // Height is valid
    public static final int DDSD_WIDTH = 0x00000004; // Width is valid
    public static final int DDSD_PITCH = 0x00000008; // Pitch is valid
    public static final int DDSD_BACKBUFFERCOUNT = 0x00000020; // Back buffer count is valid
    public static final int DDSD_ZBUFFERBITDEPTH = 0x00000040; // Z-buffer bit depth is valid (shouldn't be used in DDSURFACEDESC2)
    public static final int DDSD_ALPHABITDEPTH = 0x00000080; // Alpha bit depth is valid
    public static final int DDSD_LPSURFACE = 0x00000800; // lpSurface is valid
    public static final int DDSD_PIXELFORMAT = 0x00001000; // ddpfPixelFormat is valid
    public static final int DDSD_MIPMAPCOUNT = 0x00020000; // Mip map count is valid
    public static final int DDSD_LINEARSIZE = 0x00080000; // dwLinearSize is valid
    public static final int DDSD_DEPTH = 0x00800000; // dwDepth is valid

    public static final int DDPF_ALPHAPIXELS = 0x00000001; // Alpha channel is present
    public static final int DDPF_ALPHA = 0x00000002; // Only contains alpha information
    public static final int DDPF_FOURCC = 0x00000004; // FourCC code is valid
    public static final int DDPF_PALETTEINDEXED4 = 0x00000008; // Surface is 4-bit color indexed
    public static final int DDPF_PALETTEINDEXEDTO8 = 0x00000010; // Surface is indexed into a palette which stores indices
    // into the destination surface's 8-bit palette
    public static final int DDPF_PALETTEINDEXED8 = 0x00000020; // Surface is 8-bit color indexed
    public static final int DDPF_RGB = 0x00000040; // RGB data is present
    public static final int DDPF_COMPRESSED = 0x00000080; // Surface will accept pixel data in the format specified
    // and compress it during the write
    public static final int DDPF_RGBTOYUV = 0x00000100; // Surface will accept RGB data and translate it during
    // the write to YUV data. The format of the data to be written
    // will be contained in the pixel format structure. The DDPF_RGB
    // flag will be set.
    public static final int DDPF_YUV = 0x00000200; // Pixel format is YUV - YUV data in pixel format struct is valid

    public static final int DDPF_ZBUFFER = 0x00000400; // Pixel format is a z buffer only surface
    public static final int DDPF_PALETTEINDEXED1 = 0x00000800; // Surface is 1-bit color indexed
    public static final int DDPF_PALETTEINDEXED2 = 0x00001000; // Surface is 2-bit color indexed
    public static final int DDPF_ZPIXELS = 0x00002000; // Surface contains Z information in the pixels

    // Selected bits in DDS capabilities flags
    public static final int DDSCAPS_TEXTURE = 0x00001000; // Can be used as a texture
    public static final int DDSCAPS_MIPMAP = 0x00400000; // Is one level of a mip-map
    public static final int DDSCAPS_COMPLEX = 0x00000008; // Complex surface structure, such as a cube map

    // Selected bits in DDS extended capabilities flags
    public static final int DDSCAPS2_CUBEMAP = 0x00000200;
    public static final int DDSCAPS2_CUBEMAP_POSITIVEX = 0x00000400;
    public static final int DDSCAPS2_CUBEMAP_NEGATIVEX = 0x00000800;
    public static final int DDSCAPS2_CUBEMAP_POSITIVEY = 0x00001000;
    public static final int DDSCAPS2_CUBEMAP_NEGATIVEY = 0x00002000;
    public static final int DDSCAPS2_CUBEMAP_POSITIVEZ = 0x00004000;
    public static final int DDSCAPS2_CUBEMAP_NEGATIVEZ = 0x00008000;
    // http://gli.g-truc.net/0.6.0/api/a00001.html
    public static final int DDPF_LUMINANCE = 0x00020000;
    public static final int DDPF_LUMINANCE_ALPHA = DDPF_LUMINANCE | DDPF_ALPHA;
    public static final int DDPF_FOURCC_ALPHAPIXELS = DDPF_FOURCC | DDPF_ALPHAPIXELS;
    public static final int DDPF_RGBAPIXELS = DDPF_RGB | DDPF_ALPHAPIXELS;
    public static final int DDPF_RGBA = DDPF_RGB | DDPF_ALPHA;
    public static final int DDPF_LUMINANCE_ALPHAPIXELS = DDPF_LUMINANCE | DDPF_ALPHAPIXELS;

    /**
     * https://msdn.microsoft.com/en-us/library/windows/desktop/bb172558%28v=vs.85%29.aspx
     *
     * "Data in a FOURCC format is compressed data." = makeFourCC
     * https://msdn.microsoft.com/en-us/library/windows/desktop/bb172558%28v=vs.85%29.aspx
     */
    public static class D3dFormat {

        public static final int D3DFMT_UNKNOWN = 0;

        public static final int D3DFMT_R8G8B8 = 20;
        public static final int D3DFMT_A8R8G8B8 = 21;
        public static final int D3DFMT_X8R8G8B8 = 22;
        public static final int D3DFMT_R5G6B5 = 23;
        public static final int D3DFMT_X1R5G5B5 = 24;
        public static final int D3DFMT_A1R5G5B5 = 25;
        public static final int D3DFMT_A4R4G4B4 = 26;
        public static final int D3DFMT_R3G3B2 = 27;
        public static final int D3DFMT_A8 = 28;
        public static final int D3DFMT_A8R3G3B2 = 29;
        public static final int D3DFMT_X4R4G4B4 = 30;
        public static final int D3DFMT_A2B10G10R10 = 31;
        public static final int D3DFMT_A8B8G8R8 = 32;
        public static final int D3DFMT_X8B8G8R8 = 33;
        public static final int D3DFMT_G16R16 = 34;
        public static final int D3DFMT_A2R10G10B10 = 35;
        public static final int D3DFMT_A16B16G16R16 = 36;

        public static final int D3DFMT_A8P8 = 40;
        public static final int D3DFMT_P8 = 41;

        public static final int D3DFMT_L8 = 50;
        public static final int D3DFMT_A8L8 = 51;
        public static final int D3DFMT_A4L4 = 52;

        public static final int D3DFMT_V8U8 = 60;
        public static final int D3DFMT_L6V5U5 = 61;
        public static final int D3DFMT_X8L8V8U8 = 62;
        public static final int D3DFMT_Q8W8V8U8 = 63;
        public static final int D3DFMT_V16U16 = 64;
        public static final int D3DFMT_A2W10V10U10 = 67;

        public static final int D3DFMT_UYVY = 1498831189;//makeFourCC('U', 'Y', 'V', 'Y');
        public static final int D3DFMT_R8G8_B8G8 = 1195525970;//makeFourCC('R', 'G', 'B', 'G');
        public static final int D3DFMT_YUY2 = 844715353;//makeFourCC('Y', 'U', 'Y', '2');
        public static final int D3DFMT_G8R8_G8B8 = 1111970375;//makeFourCC('G', 'R', 'G', 'B');
        public static final int D3DFMT_DXT1 = 827611204;//makeFourCC('D', 'X', 'T', '1');
        public static final int D3DFMT_DXT2 = 844388420;//makeFourCC('D', 'X', 'T', '2');
        public static final int D3DFMT_DXT3 = 861165636;//makeFourCC('D', 'X', 'T', '3');
        public static final int D3DFMT_DXT4 = 877942852;//makeFourCC('D', 'X', 'T', '4');
        public static final int D3DFMT_DXT5 = 894720068;//makeFourCC('D', 'X', 'T', '5');
        // http://gli.g-truc.net/0.6.0/api/a00001.html
        public static final int D3DFMT_ATI1 = 826889281;//makeFourCC('A', 'T', 'I', '1');
        public static final int D3DFMT_AT1N = 1311855681;//makeFourCC('A', 'T', '1', 'N');
        public static final int D3DFMT_ATI2 = 843666497;//makeFourCC('A', 'T', 'I', '2');
        public static final int D3DFMT_AT2N = 1311921217;//makeFourCC('A', 'T', '2', 'N');
        public static final int D3DFMT_ETC = 541283397;//makeFourCC('E', 'T', 'C', ' ');
        public static final int D3DFMT_ETC1 = 826496069;//makeFourCC('E', 'T', 'C', '1');
        public static final int D3DFMT_ATC = 541283393;//makeFourCC('A', 'T', 'C', ' ');
        public static final int D3DFMT_ATCA = 1094931521;//makeFourCC('A', 'T', 'C', 'A');
        public static final int D3DFMT_ATCI = 1229149249;//makeFourCC('A', 'T', 'C', 'I');
        public static final int D3DFMT_POWERVR_2BPP = 843273296;//makeFourCC('P', 'T', 'C', '2');
        public static final int D3DFMT_POWERVR_4BPP = 876827728;//makeFourCC('P', 'T', 'C', '4');

        public static final int D3DFMT_D16_LOCKABLE = 70;
        public static final int D3DFMT_D32 = 71;
        public static final int D3DFMT_D15S1 = 73;
        public static final int D3DFMT_D24S8 = 75;
        public static final int D3DFMT_D24X8 = 77;
        public static final int D3DFMT_D24X4S4 = 79;
        public static final int D3DFMT_D16 = 80;

        public static final int D3DFMT_D32F_LOCKABLE = 82;
        public static final int D3DFMT_D24FS8 = 83;

        public static final int D3DFMT_D32_LOCKABLE = 84;
        public static final int D3DFMT_S8_LOCKABLE = 85;

        public static final int D3DFMT_L16 = 81;

        public static final int D3DFMT_VERTEXDATA = 100;
        public static final int D3DFMT_INDEX16 = 101;
        public static final int D3DFMT_INDEX32 = 102;

        public static final int D3DFMT_Q16W16V16U16 = 110;

        public static final int D3DFMT_MULTI2_ARGB8 = 827606349;//makeFourCC('M', 'E', 'T', '1');

        public static final int D3DFMT_R16F = 111;
        public static final int D3DFMT_G16R16F = 112;
        public static final int D3DFMT_A16B16G16R16F = 113;

        public static final int D3DFMT_R32F = 114;
        public static final int D3DFMT_G32R32F = 115;
        public static final int D3DFMT_A32B32G32R32F = 116;

        public static final int D3DFMT_CxV8U8 = 117;

        public static final int D3DFMT_A1 = 118;
        public static final int D3DFMT_A2B10G10R10_XR_BIAS = 119;
        public static final int D3DFMT_BINARYBUFFER = 199;

        public static final int D3DFMT_DX10 = 808540228;//makeFourCC('D', 'X', '1', '0');

        public static final int D3DFMT_FORCE_DWORD = 0x7fffffff;

    }

    /**
     * https://msdn.microsoft.com/en-us/library/windows/desktop/bb173059%28v=vs.85%29.aspx
     */
    public static class DxgiFormat {

        public final static int DXGI_FORMAT_UNKNOWN = 0;
        public final static int DXGI_FORMAT_R32G32B32A32_TYPELESS = 1;
        public final static int DXGI_FORMAT_R32G32B32A32_FLOAT = 2;
        public final static int DXGI_FORMAT_R32G32B32A32_UINT = 3;
        public final static int DXGI_FORMAT_R32G32B32A32_SINT = 4;
        public final static int DXGI_FORMAT_R32G32B32_TYPELESS = 5;
        public final static int DXGI_FORMAT_R32G32B32_FLOAT = 6;
        public final static int DXGI_FORMAT_R32G32B32_UINT = 7;
        public final static int DXGI_FORMAT_R32G32B32_SINT = 8;
        public final static int DXGI_FORMAT_R16G16B16A16_TYPELESS = 9;
        public final static int DXGI_FORMAT_R16G16B16A16_FLOAT = 10;
        public final static int DXGI_FORMAT_R16G16B16A16_UNORM = 11;
        public final static int DXGI_FORMAT_R16G16B16A16_UINT = 12;
        public final static int DXGI_FORMAT_R16G16B16A16_SNORM = 13;
        public final static int DXGI_FORMAT_R16G16B16A16_SINT = 14;
        public final static int DXGI_FORMAT_R32G32_TYPELESS = 15;
        public final static int DXGI_FORMAT_R32G32_FLOAT = 16;
        public final static int DXGI_FORMAT_R32G32_UINT = 17;
        public final static int DXGI_FORMAT_R32G32_SINT = 18;
        public final static int DXGI_FORMAT_R32G8X24_TYPELESS = 19;
        public final static int DXGI_FORMAT_D32_FLOAT_S8X24_UINT = 20;
        public final static int DXGI_FORMAT_R32_FLOAT_X8X24_TYPELESS = 21;
        public final static int DXGI_FORMAT_X32_TYPELESS_G8X24_UINT = 22;
        public final static int DXGI_FORMAT_R10G10B10A2_TYPELESS = 23;
        public final static int DXGI_FORMAT_R10G10B10A2_UNORM = 24;
        public final static int DXGI_FORMAT_R10G10B10A2_UINT = 25;
        public final static int DXGI_FORMAT_R11G11B10_FLOAT = 26;
        public final static int DXGI_FORMAT_R8G8B8A8_TYPELESS = 27;
        public final static int DXGI_FORMAT_R8G8B8A8_UNORM = 28;
        public final static int DXGI_FORMAT_R8G8B8A8_UNORM_SRGB = 29;
        public final static int DXGI_FORMAT_R8G8B8A8_UINT = 30;
        public final static int DXGI_FORMAT_R8G8B8A8_SNORM = 31;
        public final static int DXGI_FORMAT_R8G8B8A8_SINT = 32;
        public final static int DXGI_FORMAT_R16G16_TYPELESS = 33;
        public final static int DXGI_FORMAT_R16G16_FLOAT = 34;
        public final static int DXGI_FORMAT_R16G16_UNORM = 35;
        public final static int DXGI_FORMAT_R16G16_UINT = 36;
        public final static int DXGI_FORMAT_R16G16_SNORM = 37;
        public final static int DXGI_FORMAT_R16G16_SINT = 38;
        public final static int DXGI_FORMAT_R32_TYPELESS = 39;
        public final static int DXGI_FORMAT_D32_FLOAT = 40;
        public final static int DXGI_FORMAT_R32_FLOAT = 41;
        public final static int DXGI_FORMAT_R32_UINT = 42;
        public final static int DXGI_FORMAT_R32_SINT = 43;
        public final static int DXGI_FORMAT_R24G8_TYPELESS = 44;
        public final static int DXGI_FORMAT_D24_UNORM_S8_UINT = 45;
        public final static int DXGI_FORMAT_R24_UNORM_X8_TYPELESS = 46;
        public final static int DXGI_FORMAT_X24_TYPELESS_G8_UINT = 47;
        public final static int DXGI_FORMAT_R8G8_TYPELESS = 48;
        public final static int DXGI_FORMAT_R8G8_UNORM = 49;
        public final static int DXGI_FORMAT_R8G8_UINT = 50;
        public final static int DXGI_FORMAT_R8G8_SNORM = 51;
        public final static int DXGI_FORMAT_R8G8_SINT = 52;
        public final static int DXGI_FORMAT_R16_TYPELESS = 53;
        public final static int DXGI_FORMAT_R16_FLOAT = 54;
        public final static int DXGI_FORMAT_D16_UNORM = 55;
        public final static int DXGI_FORMAT_R16_UNORM = 56;
        public final static int DXGI_FORMAT_R16_UINT = 57;
        public final static int DXGI_FORMAT_R16_SNORM = 58;
        public final static int DXGI_FORMAT_R16_SINT = 59;
        public final static int DXGI_FORMAT_R8_TYPELESS = 60;
        public final static int DXGI_FORMAT_R8_UNORM = 61;
        public final static int DXGI_FORMAT_R8_UINT = 62;
        public final static int DXGI_FORMAT_R8_SNORM = 63;
        public final static int DXGI_FORMAT_R8_SINT = 64;
        public final static int DXGI_FORMAT_A8_UNORM = 65;
        public final static int DXGI_FORMAT_R1_UNORM = 66;
        public final static int DXGI_FORMAT_R9G9B9E5_SHAREDEXP = 67;
        public final static int DXGI_FORMAT_R8G8_B8G8_UNORM = 68;
        public final static int DXGI_FORMAT_G8R8_G8B8_UNORM = 69;
        public final static int DXGI_FORMAT_BC1_TYPELESS = 70;
        public final static int DXGI_FORMAT_BC1_UNORM = 71;
        public final static int DXGI_FORMAT_BC1_UNORM_SRGB = 72;
        public final static int DXGI_FORMAT_BC2_TYPELESS = 73;
        public final static int DXGI_FORMAT_BC2_UNORM = 74;
        public final static int DXGI_FORMAT_BC2_UNORM_SRGB = 75;
        public final static int DXGI_FORMAT_BC3_TYPELESS = 76;
        public final static int DXGI_FORMAT_BC3_UNORM = 77;
        public final static int DXGI_FORMAT_BC3_UNORM_SRGB = 78;
        public final static int DXGI_FORMAT_BC4_TYPELESS = 79;
        public final static int DXGI_FORMAT_BC4_UNORM = 80;
        public final static int DXGI_FORMAT_BC4_SNORM = 81;
        public final static int DXGI_FORMAT_BC5_TYPELESS = 82;
        public final static int DXGI_FORMAT_BC5_UNORM = 83;
        public final static int DXGI_FORMAT_BC5_SNORM = 84;
        public final static int DXGI_FORMAT_B5G6R5_UNORM = 85;
        public final static int DXGI_FORMAT_B5G5R5A1_UNORM = 86;
        public final static int DXGI_FORMAT_B8G8R8A8_UNORM = 87;
        public final static int DXGI_FORMAT_B8G8R8X8_UNORM = 88;
        public final static int DXGI_FORMAT_R10G10B10_XR_BIAS_A2_UNORM = 89;
        public final static int DXGI_FORMAT_B8G8R8A8_TYPELESS = 90;
        public final static int DXGI_FORMAT_B8G8R8A8_UNORM_SRGB = 91;
        public final static int DXGI_FORMAT_B8G8R8X8_TYPELESS = 92;
        public final static int DXGI_FORMAT_B8G8R8X8_UNORM_SRGB = 93;
        public final static int DXGI_FORMAT_BC6H_TYPELESS = 94;
        public final static int DXGI_FORMAT_BC6H_UF16 = 95;
        public final static int DXGI_FORMAT_BC6H_SF16 = 96;
        public final static int DXGI_FORMAT_BC7_TYPELESS = 97;
        public final static int DXGI_FORMAT_BC7_UNORM = 98;
        public final static int DXGI_FORMAT_BC7_UNORM_SRGB = 99;
        public final static int DXGI_FORMAT_AYUV = 100;
        public final static int DXGI_FORMAT_Y410 = 101;
        public final static int DXGI_FORMAT_Y416 = 102;
        public final static int DXGI_FORMAT_NV12 = 103;
        public final static int DXGI_FORMAT_P010 = 104;
        public final static int DXGI_FORMAT_P016 = 105;
        public final static int DXGI_FORMAT_420_OPAQUE = 106;
        public final static int DXGI_FORMAT_YUY2 = 107;
        public final static int DXGI_FORMAT_Y210 = 108;
        public final static int DXGI_FORMAT_Y216 = 109;
        public final static int DXGI_FORMAT_NV11 = 110;
        public final static int DXGI_FORMAT_AI44 = 111;
        public final static int DXGI_FORMAT_IA44 = 112;
        public final static int DXGI_FORMAT_P8 = 113;
        public final static int DXGI_FORMAT_A8P8 = 114;
        public final static int DXGI_FORMAT_B4G4R4A4_UNORM = 115;
        public final static int DXGI_FORMAT_P208 = 130;
        public final static int DXGI_FORMAT_V208 = 131;
        public final static int DXGI_FORMAT_V408 = 132;
        public final static int DXGI_FORMAT_ASTC_4X4_TYPELESS = 133;
        public final static int DXGI_FORMAT_ASTC_4X4_UNORM = 134;
        public final static int DXGI_FORMAT_ASTC_4X4_UNORM_SRGB = 135;
        public final static int DXGI_FORMAT_ASTC_5X4_TYPELESS = 137;
        public final static int DXGI_FORMAT_ASTC_5X4_UNORM = 138;
        public final static int DXGI_FORMAT_ASTC_5X4_UNORM_SRGB = 139;
        public final static int DXGI_FORMAT_ASTC_5X5_TYPELESS = 141;
        public final static int DXGI_FORMAT_ASTC_5X5_UNORM = 142;
        public final static int DXGI_FORMAT_ASTC_5X5_UNORM_SRGB = 143;
        public final static int DXGI_FORMAT_ASTC_6X5_TYPELESS = 145;
        public final static int DXGI_FORMAT_ASTC_6X5_UNORM = 146;
        public final static int DXGI_FORMAT_ASTC_6X5_UNORM_SRGB = 147;
        public final static int DXGI_FORMAT_ASTC_6X6_TYPELESS = 149;
        public final static int DXGI_FORMAT_ASTC_6X6_UNORM = 150;
        public final static int DXGI_FORMAT_ASTC_6X6_UNORM_SRGB = 151;
        public final static int DXGI_FORMAT_ASTC_8X5_TYPELESS = 153;
        public final static int DXGI_FORMAT_ASTC_8X5_UNORM = 154;
        public final static int DXGI_FORMAT_ASTC_8X5_UNORM_SRGB = 155;
        public final static int DXGI_FORMAT_ASTC_8X6_TYPELESS = 157;
        public final static int DXGI_FORMAT_ASTC_8X6_UNORM = 158;
        public final static int DXGI_FORMAT_ASTC_8X6_UNORM_SRGB = 159;
        public final static int DXGI_FORMAT_ASTC_8X8_TYPELESS = 161;
        public final static int DXGI_FORMAT_ASTC_8X8_UNORM = 162;
        public final static int DXGI_FORMAT_ASTC_8X8_UNORM_SRGB = 163;
        public final static int DXGI_FORMAT_ASTC_10X5_TYPELESS = 165;
        public final static int DXGI_FORMAT_ASTC_10X5_UNORM = 166;
        public final static int DXGI_FORMAT_ASTC_10X5_UNORM_SRGB = 167;
        public final static int DXGI_FORMAT_ASTC_10X6_TYPELESS = 169;
        public final static int DXGI_FORMAT_ASTC_10X6_UNORM = 170;
        public final static int DXGI_FORMAT_ASTC_10X6_UNORM_SRGB = 171;
        public final static int DXGI_FORMAT_ASTC_10X8_TYPELESS = 173;
        public final static int DXGI_FORMAT_ASTC_10X8_UNORM = 174;
        public final static int DXGI_FORMAT_ASTC_10X8_UNORM_SRGB = 175;
        public final static int DXGI_FORMAT_ASTC_10X10_TYPELESS = 177;
        public final static int DXGI_FORMAT_ASTC_10X10_UNORM = 178;
        public final static int DXGI_FORMAT_ASTC_10X10_UNORM_SRGB = 179;
        public final static int DXGI_FORMAT_ASTC_12X10_TYPELESS = 181;
        public final static int DXGI_FORMAT_ASTC_12X10_UNORM = 182;
        public final static int DXGI_FORMAT_ASTC_12X10_UNORM_SRGB = 183;
        public final static int DXGI_FORMAT_ASTC_12X12_TYPELESS = 185;
        public final static int DXGI_FORMAT_ASTC_12X12_UNORM = 186;
        public final static int DXGI_FORMAT_ASTC_12X12_UNORM_SRGB = 187;
        public final static int DXGI_FORMAT_LAST = DXGI_FORMAT_ASTC_12X12_UNORM_SRGB;
        public final static int DXGI_FORMAT_FORCE_UINT = 0xffffffff;
    }

//        public static class D3d10ResourceDimension {
//
//            public static final int DDS_DIMENSION_TEXTURE1D = 2;
//            public static final int DDS_DIMENSION_TEXTURE2D = 3;
//            public static final int DDS_DIMENSION_TEXTURE3D = 4;
//        }
//
//        public static class Uint {
//
//            /**
//             * miscFlag.
//             */
//            public static final int D3D10_RESOURCE_MISC_GENERATE_MIPS = (int) 0x1L;
//            public static final int D3D10_RESOURCE_MISC_SHARED = (int) 0x2L;
//            public static final int D3D10_RESOURCE_MISC_TEXTURECUBE = (int) 0x4L;
//            public static final int D3D10_RESOURCE_MISC_SHARED_KEYEDMUTEX = (int) 0x10L;
//            public static final int D3D10_RESOURCE_MISC_GDI_COMPATIBLE = (int) 0x20L;
//            public static final int DDS_RESOURCE_MISC_TEXTURECUBE = (int) 0x4;
//            /**
//             * miscFlag2.
//             */
//            public static final int DDS_ALPHA_MODE_UNKNOWN = (int) 0x0;
//            public static final int DDS_ALPHA_MODE_STRAIGHT = (int) 0x1;
//            public static final int DDS_ALPHA_MODE_PREMULTIPLIED = (int) 0x2;
//            public static final int DDS_ALPHA_MODE_OPAQUE = (int) 0x3;
//            public static final int DDS_ALPHA_MODE_CUSTOM = (int) 0x4;
//        }
    public static int[] compressedFormats = new int[]{
        // dx9
        D3DFMT_UYVY,
        D3DFMT_R8G8_B8G8,
        D3DFMT_YUY2,
        D3DFMT_G8R8_G8B8,
        D3DFMT_DXT1,
        D3DFMT_DXT2,
        D3DFMT_DXT3,
        D3DFMT_DXT4,
        D3DFMT_DXT5,
        D3DFMT_MULTI2_ARGB8,
        D3DFMT_ATI1,
        D3DFMT_AT1N,
        D3DFMT_ATI2,
        D3DFMT_AT2N,
        D3DFMT_ETC,
        D3DFMT_ETC1,
        D3DFMT_ATC,
        D3DFMT_ATCA,
        D3DFMT_ATCI,
        D3DFMT_POWERVR_2BPP,
        D3DFMT_POWERVR_4BPP,
        //dx10
        DXGI_FORMAT_R9G9B9E5_SHAREDEXP,
        DXGI_FORMAT_R8G8_B8G8_UNORM,
        DXGI_FORMAT_G8R8_G8B8_UNORM,
        DXGI_FORMAT_BC1_TYPELESS,
        DXGI_FORMAT_BC1_UNORM,
        DXGI_FORMAT_BC1_UNORM_SRGB,
        DXGI_FORMAT_BC2_TYPELESS,
        DXGI_FORMAT_BC2_UNORM,
        DXGI_FORMAT_BC2_UNORM_SRGB,
        DXGI_FORMAT_BC3_TYPELESS,
        DXGI_FORMAT_BC3_UNORM,
        DXGI_FORMAT_BC3_UNORM_SRGB,
        DXGI_FORMAT_BC4_TYPELESS,
        DXGI_FORMAT_BC4_UNORM,
        DXGI_FORMAT_BC4_SNORM,
        DXGI_FORMAT_BC5_TYPELESS,
        DXGI_FORMAT_BC5_UNORM,
        DXGI_FORMAT_BC5_SNORM,
        DXGI_FORMAT_B5G6R5_UNORM,
        DXGI_FORMAT_B5G5R5A1_UNORM,
        DXGI_FORMAT_B8G8R8A8_UNORM,
        DXGI_FORMAT_B8G8R8X8_UNORM,
        DXGI_FORMAT_R10G10B10_XR_BIAS_A2_UNORM,
        DXGI_FORMAT_B8G8R8A8_TYPELESS,
        DXGI_FORMAT_B8G8R8A8_UNORM_SRGB,
        DXGI_FORMAT_B8G8R8X8_TYPELESS,
        DXGI_FORMAT_B8G8R8X8_UNORM_SRGB,
        DXGI_FORMAT_BC6H_TYPELESS,
        DXGI_FORMAT_BC6H_UF16,
        DXGI_FORMAT_BC6H_SF16,
        DXGI_FORMAT_BC7_TYPELESS,
        DXGI_FORMAT_BC7_UNORM,
        DXGI_FORMAT_BC7_UNORM_SRGB,
        DXGI_FORMAT_AYUV,
        DXGI_FORMAT_Y410,
        DXGI_FORMAT_Y416,
        DXGI_FORMAT_NV12,
        DXGI_FORMAT_P010,
        DXGI_FORMAT_P016,
        DXGI_FORMAT_420_OPAQUE,
        DXGI_FORMAT_YUY2,
        DXGI_FORMAT_Y210,
        DXGI_FORMAT_Y216,
        DXGI_FORMAT_NV11,
        DXGI_FORMAT_AI44,
        DXGI_FORMAT_IA44,
        DXGI_FORMAT_P8,
        DXGI_FORMAT_A8P8,
        DXGI_FORMAT_B4G4R4A4_UNORM,
        DXGI_FORMAT_P208,
        DXGI_FORMAT_V208,
        DXGI_FORMAT_V408,
        DXGI_FORMAT_ASTC_4X4_TYPELESS,
        DXGI_FORMAT_ASTC_4X4_UNORM,
        DXGI_FORMAT_ASTC_4X4_UNORM_SRGB,
        DXGI_FORMAT_ASTC_5X4_TYPELESS,
        DXGI_FORMAT_ASTC_5X4_UNORM,
        DXGI_FORMAT_ASTC_5X4_UNORM_SRGB,
        DXGI_FORMAT_ASTC_5X5_TYPELESS,
        DXGI_FORMAT_ASTC_5X5_UNORM,
        DXGI_FORMAT_ASTC_5X5_UNORM_SRGB,
        DXGI_FORMAT_ASTC_6X5_TYPELESS,
        DXGI_FORMAT_ASTC_6X5_UNORM,
        DXGI_FORMAT_ASTC_6X5_UNORM_SRGB,
        DXGI_FORMAT_ASTC_6X6_TYPELESS,
        DXGI_FORMAT_ASTC_6X6_UNORM,
        DXGI_FORMAT_ASTC_6X6_UNORM_SRGB,
        DXGI_FORMAT_ASTC_8X5_TYPELESS,
        DXGI_FORMAT_ASTC_8X5_UNORM,
        DXGI_FORMAT_ASTC_8X5_UNORM_SRGB,
        DXGI_FORMAT_ASTC_8X6_TYPELESS,
        DXGI_FORMAT_ASTC_8X6_UNORM,
        DXGI_FORMAT_ASTC_8X6_UNORM_SRGB,
        DXGI_FORMAT_ASTC_8X8_TYPELESS,
        DXGI_FORMAT_ASTC_8X8_UNORM,
        DXGI_FORMAT_ASTC_8X8_UNORM_SRGB,
        DXGI_FORMAT_ASTC_10X5_TYPELESS,
        DXGI_FORMAT_ASTC_10X5_UNORM,
        DXGI_FORMAT_ASTC_10X5_UNORM_SRGB,
        DXGI_FORMAT_ASTC_10X6_TYPELESS,
        DXGI_FORMAT_ASTC_10X6_UNORM,
        DXGI_FORMAT_ASTC_10X6_UNORM_SRGB,
        DXGI_FORMAT_ASTC_10X8_TYPELESS,
        DXGI_FORMAT_ASTC_10X8_UNORM,
        DXGI_FORMAT_ASTC_10X8_UNORM_SRGB,
        DXGI_FORMAT_ASTC_10X10_TYPELESS,
        DXGI_FORMAT_ASTC_10X10_UNORM,
        DXGI_FORMAT_ASTC_10X10_UNORM_SRGB,
        DXGI_FORMAT_ASTC_12X10_TYPELESS,
        DXGI_FORMAT_ASTC_12X10_UNORM,
        DXGI_FORMAT_ASTC_12X10_UNORM_SRGB,
        DXGI_FORMAT_ASTC_12X12_TYPELESS,
        DXGI_FORMAT_ASTC_12X12_UNORM,
        DXGI_FORMAT_ASTC_12X12_UNORM_SRGB
    };

    /**
     * Only "broad hardware support" formats supported
     * https://msdn.microsoft.com/en-us/library/windows/desktop/bb943991%28v=vs.85%29.aspx
     *
     * @param dxgiFormat
     * @return
     */
//    public static int getGLinternalFormat(int dxgiFormat) {
//
//        switch (dxgiFormat) {
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_R8G8B8A8_UNORM:
//                return GL2ES3.GL_RGBA8UI;
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_R8G8B8A8_UNORM_SRGB:
//                return GL.GL_SRGB8_ALPHA8;
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_R8G8B8A8_SNORM:
//                return GL2ES3.GL_RGBA8_SNORM;
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_B8G8R8A8_UNORM:
//                return GL.GL_BGRA8;
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_R16G16_SNORM:
//                return GL2ES3.GL_RG16I;
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_R8G8_SNORM:
//                return GL2ES3.GL_RG8_SNORM;
//
//            case Dx10.DxgiFormat.DXGI_FORMAT_R8_UNORM:
//                return GL2ES3.GL_R8UI;
//        }
//        return 0;
//    }

//    public static final int makeFourCC(char ch0, char ch1, char ch2, char ch3) {
//        return (((int) ch0)) | (((int) ch1) << 8) | (((int) ch2) << 16) | (((int) ch3) << 24);
//    }

//    public static void main(String[] args) {
//        System.out.println("" + makeFourCC('U', 'Y', 'V', 'Y'));
//        System.out.println("" + makeFourCC('R', 'G', 'B', 'G'));
//        System.out.println("" + makeFourCC('Y', 'U', 'Y', '2'));
//        System.out.println("" + makeFourCC('G', 'R', 'G', 'B'));
//        System.out.println("" + makeFourCC('D', 'X', 'T', '1'));
//        System.out.println("" + makeFourCC('D', 'X', 'T', '2'));
//        System.out.println("" + makeFourCC('D', 'X', 'T', '3'));
//        System.out.println("" + makeFourCC('D', 'X', 'T', '4'));
//        System.out.println("" + makeFourCC('D', 'X', 'T', '5'));
//        System.out.println("" + makeFourCC('D', 'X', '1', '0'));
//        System.out.println("" + makeFourCC('M', 'E', 'T', '1'));
//        System.out.println("" + makeFourCC('A', 'T', 'I', '1'));
//        System.out.println("" + makeFourCC('A', 'T', '1', 'N'));
//        System.out.println("" + makeFourCC('A', 'T', 'I', '2'));
//        System.out.println("" + makeFourCC('A', 'T', '2', 'N'));
//        System.out.println("" + makeFourCC('E', 'T', 'C', ' '));
//        System.out.println("" + makeFourCC('E', 'T', 'C', '1'));
//        System.out.println("" + makeFourCC('A', 'T', 'C', ' '));
//        System.out.println("" + makeFourCC('A', 'T', 'C', 'A'));
//        System.out.println("" + makeFourCC('A', 'T', 'C', 'I'));
//        System.out.println("" + makeFourCC('P', 'T', 'C', '2'));
//        System.out.println("" + makeFourCC('P', 'T', 'C', '4'));
//    }
}
