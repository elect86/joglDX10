/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texture.spi;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.ByteBuffer;
import texture.spi.DDSImage.Api;
import static texture.spi.DDSresources.*;
import static texture.spi.DDSresources.D3dFormat.*;
import static texture.spi.DDSresources.DxgiFormat.*;

/**
 *
 * @author elect
 */
public class DDSutil {

    /**
     * Simple class describing images and data; does not encapsulate image
     * format information. User is responsible for transmitting that information
     * in another way.
     */
    public static class ImageInfo {

        private final ByteBuffer data;
        private final int width;
        private final int height;
        private final Api api;
        private final boolean isCompressed;
        private final int format;

        public ImageInfo(final ByteBuffer data, final int width, final int height,
                final Api api, final boolean compressed, final int format) {
            this.data = data;
            this.width = width;
            this.height = height;
            this.api = api;
            this.isCompressed = compressed;
            this.format = format;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ByteBuffer getData() {
            return data;
        }

        public boolean isCompressed() {
            return isCompressed;
        }

//        public int getCompressionFormat() {
//            if (!isCompressed()) {
//                throw new RuntimeException("Should not call unless compressed");
//            }
//            return format;
//        }
    }

    public static class PixelFormat {

        public static int sizeOf = 8 * GLBuffers.SIZEOF_INT;

        public int dwSize;
        public int dwFlags;
        public int dwFourCC;
        public int dwRGBBitCount;
        public int dwRBitMask;
        public int dwGBitMask;
        public int dwBBitMask;
        public int dwABitMask;

        public PixelFormat(int dwSize, int dwFlags, int dwFourCC, int dwRGBBitCount,
                int dwRBitMask, int dwGBitMask, int dwBBitMask, int dwABitMask) {
            this.dwSize = dwSize;
            this.dwFlags = dwFlags;
            this.dwFourCC = dwFourCC;
            this.dwRGBBitCount = dwRGBBitCount;
            this.dwRBitMask = dwRBitMask;
            this.dwGBitMask = dwGBitMask;
            this.dwBBitMask = dwBBitMask;
            this.dwABitMask = dwABitMask;
        }
    }

    /**
     * Common DDS File Resource Formats
     * https://msdn.microsoft.com/en-us/library/windows/desktop/bb943991%28v=vs.85%29.aspx
     * Legacy Formats: Map Direct3D 9 Formats to Direct3D 10
     * https://msdn.microsoft.com/en-us/library/windows/desktop/cc308051%28v=vs.85%29.aspx
     *
     * @param header
     * @param preferDx10Format
     * @return
     */
    public static ResourceFormat getResourceFormat(DDSImage.Header header, boolean preferDx10Format) {

        Api api = preferDx10Format ? Api.dx10 : Api.dx9;
        int format = preferDx10Format ? DXGI_FORMAT_UNKNOWN : D3DFMT_UNKNOWN;

        if ((header.pixelFormat.dwFlags & DDS_RGBA) != 0) {

            if (header.pixelFormat.dwRGBBitCount == 32) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x000000ff
                        && header.pixelFormat.dwGBitMask == 0x0000ff00
                        && header.pixelFormat.dwBBitMask == 0x00ff0000
                        && header.pixelFormat.dwABitMask == 0xff000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R8G8B8A8_UNORM : D3DFMT_A8B8G8R8;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x0000ffff
                        && header.pixelFormat.dwGBitMask == 0xffff0000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R16G16_UNORM : D3DFMT_G16R16;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x000003ff
                        && header.pixelFormat.dwGBitMask == 0x000ffc00
                        && header.pixelFormat.dwBBitMask == 0x3ff00000
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R10G10B10A2_UNORM : D3DFMT_A2B10G10R10;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x00ff0000
                        && header.pixelFormat.dwGBitMask == 0x0000ff00
                        && header.pixelFormat.dwBBitMask == 0x000000ff
                        && header.pixelFormat.dwABitMask == 0xff000000) {
                    format = preferDx10Format ? DXGI_FORMAT_B8G8R8A8_UNORM : D3DFMT_A8R8G8B8;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x3ff00000
                        && header.pixelFormat.dwGBitMask == 0x000ffc00
                        && header.pixelFormat.dwBBitMask == 0x000003ff
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R10G10B10A2_UNORM : D3DFMT_A2R10G10B10;
                }
            }
            if (header.pixelFormat.dwRGBBitCount == 16) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x00007c00
                        && header.pixelFormat.dwGBitMask == 0x000003e0
                        && header.pixelFormat.dwBBitMask == 0x0000001f
                        && header.pixelFormat.dwABitMask == 0x00008000) {
                    format = preferDx10Format ? DXGI_FORMAT_B5G5R5A1_UNORM : D3DFMT_A1R5G5B5;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x00000f00
                        && header.pixelFormat.dwGBitMask == 0x000000f0
                        && header.pixelFormat.dwBBitMask == 0x0000000f
                        && header.pixelFormat.dwABitMask == 0x0000f000) {
                    format = preferDx10Format ? DXGI_FORMAT_B4G4R4A4_UNORM : D3DFMT_A4R4G4B4;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x000000e0
                        && header.pixelFormat.dwGBitMask == 0x0000001c
                        && header.pixelFormat.dwBBitMask == 0x00000003
                        && header.pixelFormat.dwABitMask == 0x0000ff00) {
                    api = Api.dx9;
                    format = D3DFMT_A8R3G3B2;
                }
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_RGB) != 0) {

            if (header.pixelFormat.dwRGBBitCount == 32) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x0000ffff
                        && header.pixelFormat.dwGBitMask == 0xffff0000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R16G16_UNORM : D3DFMT_G16R16;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x00ff0000
                        && header.pixelFormat.dwGBitMask == 0x0000ff00
                        && header.pixelFormat.dwBBitMask == 0x000000ff
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_B8G8R8X8_UNORM : D3DFMT_X8R8G8B8;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x000000ff
                        && header.pixelFormat.dwGBitMask == 0x0000ff00
                        && header.pixelFormat.dwBBitMask == 0x00ff0000
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    api = Api.dx9;
                    format = D3DFMT_X8B8G8R8;
                }
            }
            if (header.pixelFormat.dwRGBBitCount == 24) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x00ff0000
                        && header.pixelFormat.dwGBitMask == 0x0000ff00
                        && header.pixelFormat.dwBBitMask == 0x000000ff
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    api = Api.dx9;
                    format = D3DFMT_R8G8B8;
                }
            }
            if (header.pixelFormat.dwRGBBitCount == 16) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x0000f800
                        && header.pixelFormat.dwGBitMask == 0x000007e0
                        && header.pixelFormat.dwBBitMask == 0x0000001f
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_B5G6R5_UNORM : D3DFMT_R5G6B5;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x00007c00
                        && header.pixelFormat.dwGBitMask == 0x000003e0
                        && header.pixelFormat.dwBBitMask == 0x0000001f
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    api = Api.dx9;
                    format = D3DFMT_X1R5G5B5;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x00000f00
                        && header.pixelFormat.dwGBitMask == 0x000000f0
                        && header.pixelFormat.dwBBitMask == 0x0000000f
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    api = Api.dx9;
                    format = D3DFMT_X4R4G4B4;
                }
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_ALPHA) != 0) {

            if (header.pixelFormat.dwRGBBitCount == 8) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x00000000
                        && header.pixelFormat.dwGBitMask == 0x00000000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x000000ff) {
                    format = preferDx10Format ? DXGI_FORMAT_A8_UNORM : D3DFMT_A8;
                }
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_LUMINANCE) != 0) {

            if (header.pixelFormat.dwRGBBitCount == 16) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x000000ff
                        && header.pixelFormat.dwGBitMask == 0x00000000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x0000ff00) {
                    api = Api.dx9;
                    format = D3DFMT_A8L8;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x0000ffff
                        && header.pixelFormat.dwGBitMask == 0x00000000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R16_UNORM : D3DFMT_L16;
                }
            }
            if (header.pixelFormat.dwRGBBitCount == 8) {

                if (/*---*/header.pixelFormat.dwRBitMask == 0x000000ff
                        && header.pixelFormat.dwGBitMask == 0x00000000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x00000000) {
                    format = preferDx10Format ? DXGI_FORMAT_R8_UNORM : D3DFMT_L8;
                }
                if (/*---*/header.pixelFormat.dwRBitMask == 0x0000000f
                        && header.pixelFormat.dwGBitMask == 0x00000000
                        && header.pixelFormat.dwBBitMask == 0x00000000
                        && header.pixelFormat.dwABitMask == 0x000000f0) {
                    api = Api.dx9;
                    format = D3DFMT_A4L4;
                }
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_FOURCC) != 0) {

            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '1')) {

                format = preferDx10Format ? DXGI_FORMAT_BC1_UNORM : D3DFMT_DXT1;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '3')) {

                format = preferDx10Format ? DXGI_FORMAT_BC2_UNORM : D3DFMT_DXT3;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '5')) {

                format = preferDx10Format ? DXGI_FORMAT_BC3_UNORM : D3DFMT_DXT5;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '4', 'U')) {

                api = Api.dx10;
                format = DXGI_FORMAT_BC4_UNORM;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '4', 'S')) {

                api = Api.dx10;
                format = DXGI_FORMAT_BC4_SNORM;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('A', 'T', 'I', '2')) {

                api = Api.dx10;
                format = DXGI_FORMAT_BC5_UNORM;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '5', 'S')) {

                api = Api.dx10;
                format = DXGI_FORMAT_BC5_SNORM;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('R', 'G', 'B', 'G')) {

                format = preferDx10Format ? DXGI_FORMAT_R8G8_B8G8_UNORM : D3DFMT_R8G8_B8G8;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('G', 'R', 'G', 'B')) {

                format = preferDx10Format ? DXGI_FORMAT_G8R8_G8B8_UNORM : D3DFMT_G8R8_G8B8;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_A16B16G16R16) {

                format = preferDx10Format ? DXGI_FORMAT_R16G16B16A16_UNORM : D3DFMT_A16B16G16R16;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_Q16W16V16U16) {

                format = preferDx10Format ? DXGI_FORMAT_R16G16B16A16_SNORM : D3DFMT_Q16W16V16U16;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_R16F) {

                format = preferDx10Format ? DXGI_FORMAT_R16_FLOAT : D3DFMT_R16F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_G16R16F) {

                format = preferDx10Format ? DXGI_FORMAT_R16G16_FLOAT : D3DFMT_G16R16F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_A16B16G16R16F) {

                format = preferDx10Format ? DXGI_FORMAT_R16G16B16A16_FLOAT : D3DFMT_A16B16G16R16F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_R32F) {

                format = preferDx10Format ? DXGI_FORMAT_R32_FLOAT : D3DFMT_R32F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_G32R32F) {

                format = preferDx10Format ? DXGI_FORMAT_R32G32_FLOAT : D3DFMT_G32R32F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_A32B32G32R32F) {

                format = preferDx10Format ? DXGI_FORMAT_R32G32B32A32_FLOAT : D3DFMT_A32B32G32R32F;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '2')) {

                format = preferDx10Format ? DXGI_FORMAT_BC2_UNORM : D3DFMT_DXT2;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '4')) {

                format = preferDx10Format ? DXGI_FORMAT_BC3_UNORM : D3DFMT_DXT4;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('U', 'Y', 'V', 'Y')) {

                api = Api.dx9;
                format = D3DFMT_UYVY;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('Y', 'U', 'Y', '2')) {

                api = Api.dx9;
                format = D3DFMT_YUY2;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_CxV8U8) {

                api = Api.dx9;
                format = D3DFMT_CxV8U8;
            }
        }
        return new ResourceFormat(api, format);
    }

    public static class ResourceFormat {

        public Api api;
        public int format;

        public ResourceFormat(Api api, int format) {
            this.api = api;
            this.format = format;
        }
    }

//    public static boolean isCompressed(DDSImage.Header header) {
//        DDSutil.Compression compression = DDSutil.getCompression(header);
//        if(compression.api == DDSutil.Api.dx10) {
//            return  compression.format != DXGI_FORMAT_UNKNOWN;
//        }
//        return  compression.format != D3DFMT_UNKNOWN;
//    }
//    
//    /** If this surface is compressed, returns the kind of compression
//        used (DXT1..DXT5). */
//    public static Compression getCompression(DDSImage.Header header) {
//        
//        for (int dx9compressedFormat : DDSresources.D3dFormat.compressedFormats) {
//            if (header.pfFourCC == dx9compressedFormat) {
//                return new Compression(Api.dx9, dx9compressedFormat);
//            }
//        }
//        if (header.pfFourCC == D3DFMT_DX10) {
//            for (int dx10compressedFormat : DDSresources.DxgiFormat.compressedFormats) {
//                if (header.dxgiFormat == dx10compressedFormat) {
//                    return new Compression(Api.dx10, dx10compressedFormat);
//                }
//            }
//        }
//        return new Compression(Api.dx10, DDSresources.DxgiFormat.DXGI_FORMAT_UNKNOWN);    
//    }
}
