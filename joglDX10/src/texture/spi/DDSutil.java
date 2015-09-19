/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texture.spi;

import static com.jogamp.opengl.GL2ES3.*;
import static com.jogamp.opengl.GL2GL3.*;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.ByteBuffer;
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
        private final boolean isCompressed;
        private final ResourceFormat resourceFormat;

        public ImageInfo(final ByteBuffer data, final int width, final int height,
                final boolean compressed, final ResourceFormat resourceFormat) {
            this.data = data;
            this.width = width;
            this.height = height;
            this.isCompressed = compressed;
            this.resourceFormat = resourceFormat;
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

    public static class ResourceFormat {

        public Api api;
        public int format;

        public ResourceFormat(Api api, int format) {
            this.api = api;
            this.format = format;
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

            switch (header.pixelFormat.dwRGBBitCount) {

                case 32:

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
                        // Also with DDS_RGB
                        format = preferDx10Format ? DXGI_FORMAT_R16G16_UNORM : D3DFMT_G16R16;
                    }
                    if (/*---*/header.pixelFormat.dwRBitMask == 0x000003ff
                            && header.pixelFormat.dwGBitMask == 0x000ffc00
                            && header.pixelFormat.dwBBitMask == 0x3ff00000
                            && header.pixelFormat.dwABitMask == 0x00000000) {
                        // Same dx10 mapping here and below, different dx9
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
                        // Same dx10 mapping here and above, different dx9
                        format = preferDx10Format ? DXGI_FORMAT_R10G10B10A2_UNORM : D3DFMT_A2R10G10B10;
                    }
                    break;

                case 16:

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
                    break;
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_RGB) != 0) {

            switch (header.pixelFormat.dwRGBBitCount) {

                case 32:

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
                    if (/*---*/header.pixelFormat.dwRBitMask == 0xffffffff
                            && header.pixelFormat.dwGBitMask == 0x00000000
                            && header.pixelFormat.dwBBitMask == 0x00000000
                            && header.pixelFormat.dwABitMask == 0x00000000) {
                        // Same dx9 mapping as D3DFMT_R32F in dwFourCC
                        format = preferDx10Format ? DXGI_FORMAT_R32_FLOAT : D3DFMT_R32F;
                    }
                    break;

                case 24:

                    if (/*---*/header.pixelFormat.dwRBitMask == 0x00ff0000
                            && header.pixelFormat.dwGBitMask == 0x0000ff00
                            && header.pixelFormat.dwBBitMask == 0x000000ff
                            && header.pixelFormat.dwABitMask == 0x00000000) {
                        api = Api.dx9;
                        format = D3DFMT_R8G8B8;
                    }
                    break;

                case 16:

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
                    break;
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_ALPHA) != 0) {

            switch (header.pixelFormat.dwRGBBitCount) {

                case 8:

                    if (/*---*/header.pixelFormat.dwRBitMask == 0x00000000
                            && header.pixelFormat.dwGBitMask == 0x00000000
                            && header.pixelFormat.dwBBitMask == 0x00000000
                            && header.pixelFormat.dwABitMask == 0x000000ff) {
                        format = preferDx10Format ? DXGI_FORMAT_A8_UNORM : D3DFMT_A8;
                    }
                    break;
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_LUMINANCE) != 0) {

            switch (header.pixelFormat.dwRGBBitCount) {

                case 16:

                    if (/*---*/header.pixelFormat.dwRBitMask == 0x000000ff
                            && header.pixelFormat.dwGBitMask == 0x00000000
                            && header.pixelFormat.dwBBitMask == 0x00000000
                            && header.pixelFormat.dwABitMask == 0x0000ff00) {
                        /**
                         * According to this
                         * https://msdn.microsoft.com/en-us/library/windows/apps/jj651550.aspx
                         * this maps in dx10 too as following
                         */
                        format = preferDx10Format ? DXGI_FORMAT_R8G8_UNORM : D3DFMT_A8L8;
                    }
                    if (/*---*/header.pixelFormat.dwRBitMask == 0x0000ffff
                            && header.pixelFormat.dwGBitMask == 0x00000000
                            && header.pixelFormat.dwBBitMask == 0x00000000
                            && header.pixelFormat.dwABitMask == 0x00000000) {
                        format = preferDx10Format ? DXGI_FORMAT_R16_UNORM : D3DFMT_L16;
                    }
                    break;

                case 8:

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
                    break;
            }
        }
        if ((header.pixelFormat.dwFlags & DDS_FOURCC) != 0) {

            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '1')) {

                format = preferDx10Format ? DXGI_FORMAT_BC1_UNORM : D3DFMT_DXT1;
            }
            // Same dxgi format for DXT2
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '3')) {

                format = preferDx10Format ? DXGI_FORMAT_BC2_UNORM : D3DFMT_DXT3;
            }
            // Same dxgi format for DXT4
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '5')) {

                format = preferDx10Format ? DXGI_FORMAT_BC3_UNORM : D3DFMT_DXT5;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '4', 'U')
                    || header.pixelFormat.dwFourCC == DDSresources.makeFourCC('A', 'T', 'I', '1')) {

                format = preferDx10Format ? DXGI_FORMAT_BC4_UNORM : D3DFMT_ATI1;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '4', 'S')) {

                format = preferDx10Format ? DXGI_FORMAT_BC4_SNORM : D3DFMT_AT1N;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('A', 'T', 'I', '2')
                    || header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '5', 'U')) {

                format = preferDx10Format ? DXGI_FORMAT_BC5_UNORM : D3DFMT_ATI2;
            }
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('B', 'C', '5', 'S')) {

                format = preferDx10Format ? DXGI_FORMAT_BC5_SNORM : D3DFMT_AT2N;
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
            // Same dx9 mapping as 32b DDS_RGB 
            if (header.pixelFormat.dwFourCC == D3DFMT_R32F) {

                format = preferDx10Format ? DXGI_FORMAT_R32_FLOAT : D3DFMT_R32F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_G32R32F) {

                format = preferDx10Format ? DXGI_FORMAT_R32G32_FLOAT : D3DFMT_G32R32F;
            }
            if (header.pixelFormat.dwFourCC == D3DFMT_A32B32G32R32F) {

                format = preferDx10Format ? DXGI_FORMAT_R32G32B32A32_FLOAT : D3DFMT_A32B32G32R32F;
            }
            /**
             * DXT2 is equal to BC2, that is DXT3, it just indicates
             * pre-multiplied alpha values.
             */
            if (header.pixelFormat.dwFourCC == DDSresources.makeFourCC('D', 'X', 'T', '2')) {

                format = preferDx10Format ? DXGI_FORMAT_BC2_UNORM : D3DFMT_DXT2;
            }
            /**
             * DXT4 is equal to BC3, that is DXT5, it just indicates
             * pre-multiplied alpha values.
             */
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

    public static int computeCompressedBlockSize(int width, int height, int depth,
            DDSutil.ResourceFormat compressionFormat) {

        int blockSize = ((width + 3) / 4) * ((height + 3) / 4) * ((depth + 3) / 4);

        if (compressionFormat.api == Api.dx9) {

            switch (compressionFormat.format) {

                case D3DFMT_DXT1:
                case D3DFMT_ATI1:
                    return blockSize *= 8;

                case D3DFMT_DXT2:
                case D3DFMT_DXT3:
                case D3DFMT_DXT4:
                case D3DFMT_DXT5:
                case D3DFMT_ATI2:
                    return blockSize *= 16;

                default:
                    throw new IllegalArgumentException("compressed dx9 format "
                            + compressionFormat.format + "not supported");
            }
        } else {

            switch (compressionFormat.format) {

                case DXGI_FORMAT_BC1_UNORM:
                case DXGI_FORMAT_BC1_UNORM_SRGB:
                case DXGI_FORMAT_BC1_TYPELESS:
                case DXGI_FORMAT_BC4_UNORM:
                case DXGI_FORMAT_BC4_SNORM:
                case DXGI_FORMAT_BC4_TYPELESS:
                    return blockSize *= 8;

                case DXGI_FORMAT_BC2_UNORM:
                case DXGI_FORMAT_BC2_UNORM_SRGB:
                case DXGI_FORMAT_BC2_TYPELESS:
                case DXGI_FORMAT_BC3_UNORM:
                case DXGI_FORMAT_BC3_UNORM_SRGB:
                case DXGI_FORMAT_BC3_TYPELESS:
                case DXGI_FORMAT_BC5_UNORM:
                case DXGI_FORMAT_BC5_SNORM:
                case DXGI_FORMAT_BC5_TYPELESS:
                case DXGI_FORMAT_BC6H_UF16:
                case DXGI_FORMAT_BC6H_SF16:
                case DXGI_FORMAT_BC6H_TYPELESS:
                case DXGI_FORMAT_BC7_UNORM:
                case DXGI_FORMAT_BC7_UNORM_SRGB:
                case DXGI_FORMAT_BC7_TYPELESS:
                    return blockSize *= 16;

                default:
                    throw new IllegalArgumentException("compressed dx10 format "
                            + compressionFormat.format + "not supported");
            }
        }
    }

    public static int computeBlockSize(int width, int height, int depth,
            DDSutil.ResourceFormat resourceFormat) {

        int blocksize;

        if (resourceFormat.api == Api.dx9) {

            switch (resourceFormat.format) {

                case D3DFMT_A8:
                case D3DFMT_L8:
                case D3DFMT_A4L4:
                    blocksize = width * height * 1;
                    break;

                case D3DFMT_A1R5G5B5:
                case D3DFMT_R5G6B5:
                case D3DFMT_X1R5G5B5:
                case D3DFMT_A4R4G4B4:
                case D3DFMT_X4R4G4B4:
                case D3DFMT_A8R3G3B2:
                case D3DFMT_A8L8:
                case D3DFMT_L16:
                    blocksize = width * height * 2;
                    break;

                case D3DFMT_R8G8B8:
                    blocksize = width * height * 3;
                    break;

                case D3DFMT_A8B8G8R8:
                case D3DFMT_G16R16:
                case D3DFMT_A2B10G10R10:
                case D3DFMT_A8R8G8B8:
                case D3DFMT_X8R8G8B8:
                case D3DFMT_X8B8G8R8:
                case D3DFMT_A2R10G10B10:
                    blocksize = width * height * 4;
                    break;

                case D3DFMT_DXT1:
                case D3DFMT_DXT2:
                case D3DFMT_DXT3:
                case D3DFMT_DXT4:
                case D3DFMT_DXT5:
                case D3DFMT_ATI1:
                case D3DFMT_ATI2:
                    blocksize = DDSutil.computeCompressedBlockSize(width, height, 1, resourceFormat);
                    break;

                default:
                    throw new IllegalArgumentException("dx9 format " + resourceFormat
                            + " not supported");
            }
        } else {

            switch (resourceFormat.format) {

                case DXGI_FORMAT_A8_UNORM:
                case DXGI_FORMAT_R8_UNORM:
                    blocksize = width * height * 1;
                    break;

                case DXGI_FORMAT_B5G5R5A1_UNORM:
                case DXGI_FORMAT_B5G6R5_UNORM:
                case DXGI_FORMAT_B4G4R4A4_UNORM:
                case DXGI_FORMAT_R16_UNORM:
                    blocksize = width * height * 2;
                    break;

                case DXGI_FORMAT_R8G8B8A8_UNORM:
                case DXGI_FORMAT_R16G16_UNORM:
                case DXGI_FORMAT_R10G10B10A2_UNORM:
                case DXGI_FORMAT_B8G8R8A8_UNORM:
                case DXGI_FORMAT_B8G8R8X8_UNORM:
                    blocksize = width * height * 4;
                    break;

                case DXGI_FORMAT_BC1_UNORM:
                case DXGI_FORMAT_BC1_UNORM_SRGB:
                case DXGI_FORMAT_BC1_TYPELESS:
                case DXGI_FORMAT_BC2_UNORM:
                case DXGI_FORMAT_BC2_UNORM_SRGB:
                case DXGI_FORMAT_BC2_TYPELESS:
                case DXGI_FORMAT_BC3_UNORM:
                case DXGI_FORMAT_BC3_UNORM_SRGB:
                case DXGI_FORMAT_BC3_TYPELESS:
                case DXGI_FORMAT_BC4_UNORM:
                case DXGI_FORMAT_BC4_SNORM:
                case DXGI_FORMAT_BC4_TYPELESS:
                case DXGI_FORMAT_BC5_UNORM:
                case DXGI_FORMAT_BC5_SNORM:
                case DXGI_FORMAT_BC5_TYPELESS:
                case DXGI_FORMAT_BC6H_UF16:
                case DXGI_FORMAT_BC6H_SF16:
                case DXGI_FORMAT_BC6H_TYPELESS:
                case DXGI_FORMAT_BC7_UNORM:
                case DXGI_FORMAT_BC7_UNORM_SRGB:
                case DXGI_FORMAT_BC7_TYPELESS:
                    blocksize = DDSutil.computeCompressedBlockSize(width, height, 1, resourceFormat);
                    break;

                default:
                    throw new IllegalArgumentException("dx10 format " + resourceFormat.format
                            + " not supported");
            }
        }
        return blocksize;
    }

    /**
     * Only for non compressed formats.
     *
     * @param resourceFormat
     * @return
     */
    public static int getGlPixelFormat(DDSutil.ResourceFormat resourceFormat) {

        if (resourceFormat.api == Api.dx9) {

            switch (resourceFormat.format) {

                case D3DFMT_A8B8G8R8:
                case D3DFMT_X8B8G8R8:
                    return GL_RGBA;

                case D3DFMT_A2B10G10R10:
                case D3DFMT_A2R10G10B10:
                    return GL_RGB10_A2;

                case D3DFMT_A1R5G5B5:
                    return GL_RGB5_A1;

                case D3DFMT_R5G6B5:
                    return GL_RGB;

                case D3DFMT_A8R3G3B2:   // guessing
                    return GL_R3_G3_B2;

                case D3DFMT_A8R8G8B8:
                case D3DFMT_A4R4G4B4:
                case D3DFMT_X4R4G4B4:   // guessing
                case D3DFMT_X8R8G8B8:
                case D3DFMT_X1R5G5B5:
                    return GL_BGRA;

                case D3DFMT_R8G8B8:
                    return GL_BGR;

                case D3DFMT_G16R16:
                    return GL_RG;

                case D3DFMT_R32F:
                    return GL_RED;

                case D3DFMT_A8:
                    return GL_ALPHA;

                case D3DFMT_L16:
                case D3DFMT_L8:
                    return GL_LUMINANCE;

                case D3DFMT_A8L8:
                    return GL_LUMINANCE_ALPHA;

                case D3DFMT_A4L4:
                default:
                    return GL_NONE;
            }
        } else {
            switch (resourceFormat.format) {

                case DXGI_FORMAT_R8G8B8A8_UNORM:
                    return GL_RGBA;

                case DXGI_FORMAT_R10G10B10A2_UNORM:
                    return GL_RGB10_A2;

                case DXGI_FORMAT_B5G5R5A1_UNORM:
                    return GL_RGB5_A1;

                case DXGI_FORMAT_B5G6R5_UNORM:
                    return GL_RGB;

                case DXGI_FORMAT_B8G8R8A8_UNORM:
                case DXGI_FORMAT_B4G4R4A4_UNORM:
                case DXGI_FORMAT_B8G8R8X8_UNORM:
                    return GL_BGRA;

                case DXGI_FORMAT_R16G16_UNORM:
                    return GL_RG;

                case DXGI_FORMAT_R32_FLOAT:
                    return GL_RED;

                case DXGI_FORMAT_A8_UNORM:
                    return GL_ALPHA;

                case DXGI_FORMAT_R16_UNORM: // guessing
                case DXGI_FORMAT_R8_UNORM:
                    return GL_LUMINANCE;

                case DXGI_FORMAT_R8G8_UNORM:
                    return GL_LUMINANCE_ALPHA;

                default:
                    return GL_NONE;
            }
        }
    }

    public static int getGlCompressedFormat(DDSutil.ResourceFormat resourceFormat) {

        if (resourceFormat.api == Api.dx9) {

            switch(resourceFormat.format) {
                
                
            }
            
        } else {

        }
    }

    public enum Api {

        dx9,
        dx10,
        size
    }
}
