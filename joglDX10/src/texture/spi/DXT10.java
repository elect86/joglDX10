/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texture.spi;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;

/**
 *
 * @author elect
 */
public class DXT10 {

    public static class Header {

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
            public final static int DXGI_FORMAT_FORCE_UINT = 0xffffffff;
        }

        public static class D3d10ResourceDimension {

            public static final int DDS_DIMENSION_TEXTURE1D = 2;
            public static final int DDS_DIMENSION_TEXTURE2D = 3;
            public static final int DDS_DIMENSION_TEXTURE3D = 4;
        }

        public static class Uint {

            /**
             * miscFlag.
             */
            public static final int D3D10_RESOURCE_MISC_GENERATE_MIPS = (int) 0x1L;
            public static final int D3D10_RESOURCE_MISC_SHARED = (int) 0x2L;
            public static final int D3D10_RESOURCE_MISC_TEXTURECUBE = (int) 0x4L;
            public static final int D3D10_RESOURCE_MISC_SHARED_KEYEDMUTEX = (int) 0x10L;
            public static final int D3D10_RESOURCE_MISC_GDI_COMPATIBLE = (int) 0x20L;
            public static final int DDS_RESOURCE_MISC_TEXTURECUBE = (int) 0x4;
            /**
             * miscFlag2.
             */
            public static final int DDS_ALPHA_MODE_UNKNOWN = (int) 0x0;
            public static final int DDS_ALPHA_MODE_STRAIGHT = (int) 0x1;
            public static final int DDS_ALPHA_MODE_PREMULTIPLIED = (int) 0x2;
            public static final int DDS_ALPHA_MODE_OPAQUE = (int) 0x3;
            public static final int DDS_ALPHA_MODE_CUSTOM = (int) 0x4;
        }
    }

    /**
     * Only "broad hardware support" formats supported
     * https://msdn.microsoft.com/en-us/library/windows/desktop/bb943991%28v=vs.85%29.aspx
     *
     * @param dxgiFormat
     * @return
     */
    public static int getGLinternalFormat(int dxgiFormat) {

        switch (dxgiFormat) {

            case Header.DxgiFormat.DXGI_FORMAT_R8G8B8A8_UNORM:
                return GL2ES3.GL_RGBA8UI;
                
            case Header.DxgiFormat.DXGI_FORMAT_R8G8B8A8_UNORM_SRGB:
                return GL.GL_SRGB8_ALPHA8;
                
            case Header.DxgiFormat.DXGI_FORMAT_R8G8B8A8_SNORM:
                return  GL2ES3.GL_RGBA8_SNORM;
                
            case Header.DxgiFormat.DXGI_FORMAT_B8G8R8A8_UNORM:
                return GL.GL_BGRA8;
                
            case Header.DxgiFormat.DXGI_FORMAT_R16G16_SNORM:
                return GL2ES3.GL_RG16I;
                
            case Header.DxgiFormat.DXGI_FORMAT_R8G8_SNORM:
                return GL2ES3.GL_RG8_SNORM;
                
            case Header.DxgiFormat.DXGI_FORMAT_R8_UNORM:
                return GL2ES3.GL_R8UI;
        }
        return 0;
    }
}
