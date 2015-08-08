/*
 * Copyright (c) 2005 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 *
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package texture.spi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import com.jogamp.opengl.GL;

import com.jogamp.common.nio.Buffers;
import com.jogamp.common.util.IOUtil;
import static texture.spi.DDSresources.D3dFormat.*;
import static texture.spi.DDSresources.*;
import static texture.spi.DDSresources.DxgiFormat.*;
import texture.spi.DDSutil.ImageInfo;

/** A reader and writer for DirectDraw Surface (.dds) files, which are
    used to describe textures. These files can contain multiple mipmap
    levels in one file. This class is currently minimal and does not
    support all of the possible file formats. 
    
    */

public class DDSImage {

    private FileInputStream fis;
    private FileChannel     chan;
    private ByteBuffer buf;
    private Header header;
    private Api api;
    private int format;

    /** Reads a DirectDraw surface from the specified file name,
        returning the resulting DDSImage.

        @param filename File name
        @return DDS image object
        @throws java.io.IOException if an I/O exception occurred
    */
    public static DDSImage read(final String filename) throws IOException {
        return read(new File(filename));
    }

    /** Reads a DirectDraw surface from the specified file, returning
        the resulting DDSImage.

        @param file File object
        @return DDS image object
        @throws java.io.IOException if an I/O exception occurred
    */
    public static DDSImage read(final File file) throws IOException {
        final DDSImage image = new DDSImage();
        image.readFromFile(file);
        return image;
    }

    /** Reads a DirectDraw surface from the specified ByteBuffer, returning
        the resulting DDSImage.

        @param buf Input data
        @return DDS image object
        @throws java.io.IOException if an I/O exception occurred
    */
    public static DDSImage read(final ByteBuffer buf) throws IOException {
        final DDSImage image = new DDSImage();
        image.readFromBuffer(buf);
        return image;
    }

    /** Closes open files and resources associated with the open
        DDSImage. No other methods may be called on this object once
        this is called. */
    public void close() {
        try {
            if (chan != null) {
                chan.close();
                chan = null;
            }
            if (fis != null) {
                fis.close();
                fis = null;
            }
            buf = null;
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new DDSImage from data supplied by the user. The
     * resulting DDSImage can be written to disk using the write()
     * method.
     *
     * @param d3dFormat the D3DFMT_ constant describing the data; it is
     *                  assumed that it is packed tightly
     * @param width  the width in pixels of the topmost mipmap image
     * @param height the height in pixels of the topmost mipmap image
     * @param mipmapData the data for each mipmap level of the resulting
     *                   DDSImage; either only one mipmap level should
     *                   be specified, or they all must be
     * @throws IllegalArgumentException if the data does not match the
     *   specified arguments
     * @return DDS image object
     */
    public static DDSImage createFromData(final int d3dFormat,
                                          final int width,
                                          final int height,
                                          final ByteBuffer[] mipmapData) throws IllegalArgumentException {
        final DDSImage image = new DDSImage();
        image.initFromData(d3dFormat, width, height, mipmapData);
        return image;
    }

    /** Determines from the magic number whether the given InputStream
        points to a DDS image. The given InputStream must return true
        from markSupported() and support a minimum of four bytes of
        read-ahead.

        @param in Stream to check
        @return true if input stream is DDS image or false otherwise
        @throws java.io.IOException if an I/O exception occurred
    */
    public static boolean isDDSImage(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!in.markSupported()) {
            throw new IOException("Can not test non-destructively whether given InputStream is a DDS image");
        }
        in.mark(4);
        int magic = 0;
        for (int i = 0; i < 4; i++) {
            final int tmp = in.read();
            if (tmp < 0) {
                in.reset();
                return false;
            }
            magic = ((magic >>> 8) | (tmp << 24));
        }
        in.reset();
        return (magic == MAGIC);
    }

    /**
     * Writes this DDSImage to the specified file name.
     * @param filename File name to write to
     * @throws java.io.IOException if an I/O exception occurred
     */
    public void write(final String filename) throws IOException {
        write(new File(filename));
    }

    /**
     * Writes this DDSImage to the specified file name.
     * @param file File object to write to
     * @throws java.io.IOException if an I/O exception occurred
     */
    public void write(final File file) throws IOException {
        final FileOutputStream stream = IOUtil.getFileOutputStream(file, true);
        final FileChannel chan = stream.getChannel();
        // Create ByteBuffer for header in case the start of our
        // ByteBuffer isn't actually memory-mapped
        final ByteBuffer hdr = ByteBuffer.allocate(header.writtenSize());
        hdr.order(ByteOrder.LITTLE_ENDIAN);
        header.write(hdr);
        hdr.rewind();
        chan.write(hdr);
        buf.position(header.writtenSize());
        chan.write(buf);
        chan.force(true);
        chan.close();
        stream.close();
    }

    /** Test for presence/absence of surface description flags (DDSD_*)
     * @param flag DDSD_* flags set to test
     * @return true if flag present or false otherwise
     */
    public boolean isSurfaceDescFlagSet(final int flag) {
        return ((header.flags & flag) != 0);
    }

    /** Test for presence/absence of pixel format flags (DDPF_*) */
    public boolean isPixelFormatFlagSet(final int flag) {
        return ((header.pfFlags & flag) != 0);
    }

    /** Gets the Dx9 pixel format of this texture. Returns DXGI_FORMAT_UNKNOWN
     * if could not recognize the pixel format. Luckily, Dx9 formats unsupported 
     * in Dx10 are also impossible to translate on GL enum, so we will skip them.
     */
    public int getPixelFormat() {
        if(isPixelFormatFlagSet(DDPF_RGBA)) {
            return DDPF_RGBA;
        }
        if(isPixelFormatFlagSet(DDPF_RGBA)) {
            return DDPF_RGBA;
        }
        return D3DFMT_UNKNOWN;
    }

    /**
     * Indicates whether this texture is cubemap
     * @return true if cubemap or false otherwise
     */
    public boolean isCubemap() {
        return ((header.ddsCaps1 & DDSCAPS_COMPLEX) != 0) && ((header.ddsCaps2 & DDSCAPS2_CUBEMAP) != 0);
    }

    /**
     * Indicates whethe this cubemap side present
     * @param side Side to test
     * @return true if side present or false otherwise
     */
    public boolean isCubemapSidePresent(final int side) {
        return isCubemap() && (header.ddsCaps2 & side) != 0;
    }

    /** Indicates whether this texture is compressed. 
     *  DX9 and previous, used to set DDS_FOURCC in the dwFlags and then save 
     *  the corresponding compression format in the DDS_PIXELFORMAT structure. 
     *  Starting from DX10, instead, we have a new header that contains the 
     *  format. There are some common variants in use where the pixel format is 
     *  set to a DDPF_FOURCC code where dwFourCC is set to a D3DFORMAT or 
     *  DXGI_FORMAT enumeration value. There is no way to tell if an enumeration
     *  value is a D3DFORMAT or a DXGI_FORMAT, so we have to check both. 
     *  The worst is that DX10 use the format field obviously also for non 
     *  compressed formats. So we need to check against every single format.
     */
    public boolean isCompressed() {
        if(!isPixelFormatFlagSet(DDPF_FOURCC)) {
            return false;
        }
        return DDSutil.isCompressed(header);
    }

    /** Width of the texture (or the top-most mipmap if mipmaps are
        present) */
    public int getWidth() {
        return header.width;
    }

    /** Height of the texture (or the top-most mipmap if mipmaps are
        present) */
    public int getHeight() {
        return header.height;
    }

    /** Total number of bits per pixel. Only valid if DDPF_RGB is
        present. For A8R8G8B8, would be 32. */
    public int getDepth() {
        return header.pfRGBBitCount;
    }

    /** Number of mip maps in the texture */
    public int getNumMipMaps() {
        if (!isSurfaceDescFlagSet(DDSD_MIPMAPCOUNT)) {
            return 0;
        }
        return header.mipMapCountOrAux;
    }

    /** Gets the <i>i</i>th mipmap data (0..getNumMipMaps() - 1)
     * @param map Mipmap index
     * @return Image object
     */
    public ImageInfo getMipMap(final int map) {
        return getMipMap( 0, map );
    }

    /**
     * Gets the <i>i</i>th mipmap data (0..getNumMipMaps() - 1)
     * @param side Cubemap side or 0 for 2D texture
     * @param map Mipmap index
     * @return Image object
     */
    public ImageInfo getMipMap(final int side, final int map) {
        if (!isCubemap() && (side != 0)) {
            throw new RuntimeException( "Illegal side for 2D texture: " + side );
        }
        if (isCubemap() && !isCubemapSidePresent(side)) {
            throw new RuntimeException( "Illegal side, side not present: " + side );
        }
        if (getNumMipMaps() > 0 &&
            ((map < 0) || (map >= getNumMipMaps()))) {
            throw new RuntimeException("Illegal mipmap number " + map + " (0.." + (getNumMipMaps() - 1) + ")");
        }

        // Figure out how far to seek
        int seek = header.writtenSize();
        if (isCubemap()) {
            seek += sideShiftInBytes(side);
        }
        for (int i = 0; i < map; i++) {
            seek += mipMapSizeInBytes(i);
        }
        buf.limit(seek + mipMapSizeInBytes(map));
        buf.position(seek);
        final ByteBuffer next = buf.slice();
        buf.position(0);
        buf.limit(buf.capacity());
        return new ImageInfo(next, mipMapWidth(map), mipMapHeight(map), isCompressed(), getCompressionFormat());
    }

    /** Returns an array of ImageInfos corresponding to all mipmap
        levels of this DDS file.
        @return Mipmap image objects set
    */
    public ImageInfo[] getAllMipMaps() {
        return getAllMipMaps(0);
    }

    /**
     * Returns an array of ImageInfos corresponding to all mipmap
     * levels of this DDS file.
     * @param side Cubemap side or 0 for 2D texture
     * @return Mipmap image objects set
     */
    public ImageInfo[] getAllMipMaps( final int side ) {
        int numLevels = getNumMipMaps();
        if (numLevels == 0) {
            numLevels = 1;
        }
        final ImageInfo[] result = new ImageInfo[numLevels];
        for (int i = 0; i < numLevels; i++) {
            result[i] = getMipMap(side, i);
        }
        return result;
    }

    /** Converts e.g. DXT1 compression format constant (see {@link
        #getCompressionFormat}) into "DXT1".
        @param compressionFormat Compression format constant
        @return String format code
    */
    public static String getCompressionFormatName(int compressionFormat) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            final char c = (char) (compressionFormat & 0xFF);
            buf.append(c);
            compressionFormat = compressionFormat >> 8;
        }
        return buf.toString();
    }

    /** Allocates a temporary, empty ByteBuffer suitable for use in a
        call to glCompressedTexImage2D. This is used by the Texture
        class to expand non-power-of-two DDS compressed textures to
        power-of-two sizes on hardware not supporting OpenGL 2.0 and the
        NPOT texture extension. The specified OpenGL internal format
        must be one of GL_COMPRESSED_RGB_S3TC_DXT1_EXT,
        GL_COMPRESSED_RGBA_S3TC_DXT1_EXT,
        GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, or
        GL_COMPRESSED_RGBA_S3TC_DXT5_EXT.
    */
    public static ByteBuffer allocateBlankBuffer(final int width,
                                                 final int height,
                                                 final int openGLInternalFormat) {
        int size = width * height;
        switch (openGLInternalFormat) {
        case GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT:
        case GL.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT:
            size /= 2;
            break;

        case GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT:
        case GL.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT:
            break;

        default:
            throw new IllegalArgumentException("Illegal OpenGL texture internal format " +
                                               openGLInternalFormat);
        }
        if (size == 0)
            size = 1;
        return Buffers.newDirectByteBuffer(size);
    }

    public void debugPrint() {
        final PrintStream tty = System.err;
        tty.println("Compressed texture: " + isCompressed());
        if (isCompressed()) {
            final int fmt = getCompressionFormat();
            final String name = getCompressionFormatName(fmt);
            tty.println("Compression format: 0x" + Integer.toHexString(fmt) + " (" + name + ")");
        }
        tty.println("Width: " + header.width + " Height: " + header.height);
        tty.println("header.pitchOrLinearSize: " + header.pitchOrLinearSize);
        tty.println("header.pfRBitMask: 0x" + Integer.toHexString(header.pfRBitMask));
        tty.println("header.pfGBitMask: 0x" + Integer.toHexString(header.pfGBitMask));
        tty.println("header.pfBBitMask: 0x" + Integer.toHexString(header.pfBBitMask));
        tty.println("SurfaceDesc flags:");
        boolean recognizedAny = false;
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_CAPS, "DDSD_CAPS");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_HEIGHT, "DDSD_HEIGHT");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_WIDTH, "DDSD_WIDTH");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_PITCH, "DDSD_PITCH");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_BACKBUFFERCOUNT, "DDSD_BACKBUFFERCOUNT");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_ZBUFFERBITDEPTH, "DDSD_ZBUFFERBITDEPTH");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_ALPHABITDEPTH, "DDSD_ALPHABITDEPTH");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_LPSURFACE, "DDSD_LPSURFACE");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_PIXELFORMAT, "DDSD_PIXELFORMAT");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_MIPMAPCOUNT, "DDSD_MIPMAPCOUNT");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_LINEARSIZE, "DDSD_LINEARSIZE");
        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_DEPTH, "DDSD_DEPTH");
        if (!recognizedAny) {
            tty.println("(none)");
        }
        tty.println("Raw SurfaceDesc flags: 0x" + Integer.toHexString(header.flags));
        tty.println("Pixel format flags:");
        recognizedAny = false;
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ALPHAPIXELS, "DDPF_ALPHAPIXELS");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ALPHA, "DDPF_ALPHA");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_FOURCC, "DDPF_FOURCC");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED4, "DDPF_PALETTEINDEXED4");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXEDTO8, "DDPF_PALETTEINDEXEDTO8");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED8, "DDPF_PALETTEINDEXED8");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_RGB, "DDPF_RGB");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_COMPRESSED, "DDPF_COMPRESSED");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_RGBTOYUV, "DDPF_RGBTOYUV");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_YUV, "DDPF_YUV");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ZBUFFER, "DDPF_ZBUFFER");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED1, "DDPF_PALETTEINDEXED1");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED2, "DDPF_PALETTEINDEXED2");
        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ZPIXELS, "DDPF_ZPIXELS");
        if (!recognizedAny) {
            tty.println("(none)");
        }
        tty.println("Raw pixel format flags: 0x" + Integer.toHexString(header.pfFlags));
        tty.println("Depth: " + getDepth());
        tty.println("Number of mip maps: " + getNumMipMaps());
        final int fmt = getPixelFormat();
        tty.print("Pixel format: ");
        switch (fmt) {
        case D3DFMT_R8G8B8:   tty.println("D3DFMT_R8G8B8"); break;
        case D3DFMT_A8R8G8B8: tty.println("D3DFMT_A8R8G8B8"); break;
        case D3DFMT_X8R8G8B8: tty.println("D3DFMT_X8R8G8B8"); break;
        case D3DFMT_DXT1:     tty.println("D3DFMT_DXT1"); break;
        case D3DFMT_DXT2:     tty.println("D3DFMT_DXT2"); break;
        case D3DFMT_DXT3:     tty.println("D3DFMT_DXT3"); break;
        case D3DFMT_DXT4:     tty.println("D3DFMT_DXT4"); break;
        case D3DFMT_DXT5:     tty.println("D3DFMT_DXT5"); break;
        case D3DFMT_UNKNOWN:  tty.println("D3DFMT_UNKNOWN"); break;
        default:              tty.println("(unknown pixel format " + fmt + ")"); break;
        }
    }

    //----------------------------------------------------------------------
    // Internals only below this point
    //

    private static final int MAGIC = 0x20534444;

    static class Header {
        int size;                 // size of the DDSURFACEDESC structure
        int flags;                // determines what fields are valid
        int height;               // height of surface to be created
        int width;                // width of input surface
        int pitchOrLinearSize;
        int backBufferCountOrDepth;
        int mipMapCountOrAux;     // number of mip-map levels requested (in this context)
        int alphaBitDepth;        // depth of alpha buffer requested
        int reserved1;            // reserved
        int surface;              // pointer to the associated surface memory
        // NOTE: following two entries are from DDCOLORKEY data structure
        // Are overlaid with color for empty cubemap faces (unused in this reader)
        int colorSpaceLowValue;
        int colorSpaceHighValue;
        int destBltColorSpaceLowValue;
        int destBltColorSpaceHighValue;
        int srcOverlayColorSpaceLowValue;
        int srcOverlayColorSpaceHighValue;
        int srcBltColorSpaceLowValue;
        int srcBltColorSpaceHighValue;
        // NOTE: following entries are from DDPIXELFORMAT data structure
        // Are overlaid with flexible vertex format description of vertex
        // buffers (unused in this reader)
//        int pfSize;                 // size of DDPIXELFORMAT structure
//        int pfFlags;                // pixel format flags
//        int pfFourCC;               // (FOURCC code)
//        // Following five entries have multiple interpretations, not just
//        // RGBA (but that's all we support right now)
//        int pfRGBBitCount;          // how many bits per pixel
//        int pfRBitMask;             // mask for red bits
//        int pfGBitMask;             // mask for green bits
//        int pfBBitMask;             // mask for blue bits
//        int pfABitMask;             // mask for alpha channel
        DDSutil.PixelFormat pixelFormat;
        int ddsCaps1;               // Texture and mip-map flags
        int ddsCaps2;               // Advanced capabilities including cubemap support
        int ddsCapsReserved1;
        int ddsCapsReserved2;
        int textureStage;           // stage in multitexture cascade
        // DXT10 Header
        int dxgiFormat;
        int resourceDimension;
        int miscFlag;
        int arraySize;
        int miscFlag2;

        void read(final ByteBuffer buf) throws IOException {
            final int magic                     = buf.getInt();
            if (magic != MAGIC) {
                throw new IOException("Incorrect magic number 0x" +
                                      Integer.toHexString(magic) +
                                      " (expected " + MAGIC + ")");
            }
            size                          = buf.getInt();
            flags                         = buf.getInt();
            height                        = buf.getInt();
            width                         = buf.getInt();
            pitchOrLinearSize             = buf.getInt();
            backBufferCountOrDepth        = buf.getInt();
            mipMapCountOrAux              = buf.getInt();
            alphaBitDepth                 = buf.getInt();
            reserved1                     = buf.getInt();
            surface                       = buf.getInt();
            colorSpaceLowValue            = buf.getInt();
            colorSpaceHighValue           = buf.getInt();
            destBltColorSpaceLowValue     = buf.getInt();
            destBltColorSpaceHighValue    = buf.getInt();
            srcOverlayColorSpaceLowValue  = buf.getInt();
            srcOverlayColorSpaceHighValue = buf.getInt();
            srcBltColorSpaceLowValue      = buf.getInt();
            srcBltColorSpaceHighValue     = buf.getInt();
            pixelFormat.dwSize            = buf.getInt();
            pixelFormat.dwFlags           = buf.getInt();
            pixelFormat.dwFourCC          = buf.getInt();
            pixelFormat.dwRGBBitCount     = buf.getInt();
            pixelFormat.dwRBitMask        = buf.getInt();
            pixelFormat.dwGBitMask        = buf.getInt();
            pixelFormat.dwBBitMask        = buf.getInt();
            pixelFormat.dwABitMask        = buf.getInt();
            ddsCaps1                      = buf.getInt();
            ddsCaps2                      = buf.getInt();
            ddsCapsReserved1              = buf.getInt();
            ddsCapsReserved2              = buf.getInt();
            textureStage                  = buf.getInt();
            
            if(pixelFormat.dwFourCC == D3DFMT_DX10) {
                dxgiFormat        = buf.getInt();
                resourceDimension = buf.getInt();
                miscFlag          = buf.getInt();
                arraySize         = buf.getInt();
                miscFlag2         = buf.getInt();
            }
        }

        // buf must be in little-endian byte order
        void write(final ByteBuffer buf) {
            buf.putInt(MAGIC);
            buf.putInt(size);
            buf.putInt(flags);
            buf.putInt(height);
            buf.putInt(width);
            buf.putInt(pitchOrLinearSize);
            buf.putInt(backBufferCountOrDepth);
            buf.putInt(mipMapCountOrAux);
            buf.putInt(alphaBitDepth);
            buf.putInt(reserved1);
            buf.putInt(surface);
            buf.putInt(colorSpaceLowValue);
            buf.putInt(colorSpaceHighValue);
            buf.putInt(destBltColorSpaceLowValue);
            buf.putInt(destBltColorSpaceHighValue);
            buf.putInt(srcOverlayColorSpaceLowValue);
            buf.putInt(srcOverlayColorSpaceHighValue);
            buf.putInt(srcBltColorSpaceLowValue);
            buf.putInt(srcBltColorSpaceHighValue);
            buf.putInt(pfSize);
            buf.putInt(pfFlags);
            buf.putInt(pfFourCC);
            buf.putInt(pfRGBBitCount);
            buf.putInt(pfRBitMask);
            buf.putInt(pfGBitMask);
            buf.putInt(pfBBitMask);
            buf.putInt(pfABitMask);
            buf.putInt(ddsCaps1);
            buf.putInt(ddsCaps2);
            buf.putInt(ddsCapsReserved1);
            buf.putInt(ddsCapsReserved2);
            buf.putInt(textureStage);
            
            if(pfFourCC == D3DFMT_DX10) {
                buf.putInt(dxgiFormat);
                buf.putInt(resourceDimension);
                buf.putInt(miscFlag);
                buf.putInt(arraySize);
                buf.putInt(miscFlag2);
            }
        }

        private int size() {
            return 124 + (pfFourCC == D3DFMT_DX10 ? 20 : 0);
        }

        private int pfSize() {
            return 32;
        }

        private int writtenSize() {
            return 128 + (pfFourCC == D3DFMT_DX10 ? 20 : 0);
        }
    }

    private DDSImage() {
    }

    private void readFromFile(final File file) throws IOException {
        fis = new FileInputStream(file);
        chan = fis.getChannel();
        final ByteBuffer buf = chan.map(FileChannel.MapMode.READ_ONLY,
                                  0, (int) file.length());
        readFromBuffer(buf);
    }

    private void readFromBuffer(final ByteBuffer buf) throws IOException {
        this.buf = buf;
        buf.order(ByteOrder.LITTLE_ENDIAN);
        header = new Header();
        header.read(buf);
        fixupHeader();
    }

    private void initFromData(final int d3dFormat,
                              final int width,
                              final int height,
                              final ByteBuffer[] mipmapData) throws IllegalArgumentException {
        // Check size of mipmap data compared against format, width and
        // height
        int topmostMipmapSize = width * height;
        int pitchOrLinearSize = width;
        boolean isCompressed = false;
        switch (d3dFormat) {
        case D3DFMT_R8G8B8:   topmostMipmapSize *= 3; pitchOrLinearSize *= 3; break;
        case D3DFMT_A8R8G8B8: topmostMipmapSize *= 4; pitchOrLinearSize *= 4; break;
        case D3DFMT_X8R8G8B8: topmostMipmapSize *= 4; pitchOrLinearSize *= 4; break;
        case D3DFMT_DXT1:
        case D3DFMT_DXT2:
        case D3DFMT_DXT3:
        case D3DFMT_DXT4:
        case D3DFMT_DXT5:
            topmostMipmapSize = computeCompressedBlockSize(width, height, 1, d3dFormat);
            pitchOrLinearSize = topmostMipmapSize;
            isCompressed = true;
            break;
        default:
            throw new IllegalArgumentException("d3dFormat must be one of the known formats");
        }

        // Now check the mipmaps against this size
        int curSize = topmostMipmapSize;
        int mipmapWidth = width;
        int mipmapHeight = height;
        int totalSize = 0;
        for (int i = 0; i < mipmapData.length; i++) {
            if (mipmapData[i].remaining() != curSize) {
                throw new IllegalArgumentException("Mipmap level " + i +
                                                   " didn't match expected data size (expected " + curSize + ", got " +
                                                   mipmapData[i].remaining() + ")");
            }
            // Compute next mipmap size
            if (mipmapWidth > 1) mipmapWidth /= 2;
            if (mipmapHeight > 1) mipmapHeight /= 2;
            curSize = computeBlockSize(mipmapWidth, mipmapHeight, 1, d3dFormat);
            totalSize += mipmapData[i].remaining();
        }

        // OK, create one large ByteBuffer to hold all of the mipmap data
        totalSize += header.writtenSize();
        final ByteBuffer buf = ByteBuffer.allocate(totalSize);
        buf.position(header.writtenSize());
        for (int i = 0; i < mipmapData.length; i++) {
            buf.put(mipmapData[i]);
        }
        this.buf = buf;

        // Allocate and initialize a Header
        header = new Header();
        header.size = header.size();
        header.flags = DDSD_CAPS | DDSD_HEIGHT | DDSD_WIDTH | DDSD_PIXELFORMAT;
        if (mipmapData.length > 1) {
            header.flags |= DDSD_MIPMAPCOUNT;
            header.mipMapCountOrAux = mipmapData.length;
        }
        header.width = width;
        header.height = height;
        if (isCompressed) {
            header.flags |= DDSD_LINEARSIZE;
            header.pfFlags |= DDPF_FOURCC;
            header.pfFourCC = d3dFormat;
        } else {
            header.flags |= DDSD_PITCH;
            // Figure out the various settings from the pixel format
            header.pfFlags |= DDPF_RGB;
            switch (d3dFormat) {
            case D3DFMT_R8G8B8:   header.pfRGBBitCount = 24; break;
            case D3DFMT_A8R8G8B8: header.pfRGBBitCount = 32; header.pfFlags |= DDPF_ALPHAPIXELS; break;
            case D3DFMT_X8R8G8B8: header.pfRGBBitCount = 32; break;
            }
            header.pfRBitMask = 0x00FF0000;
            header.pfGBitMask = 0x0000FF00;
            header.pfBBitMask = 0x000000FF;
            if (d3dFormat == D3DFMT_A8R8G8B8) {
                header.pfABitMask = 0xFF000000;
            }
        }
        header.pitchOrLinearSize = pitchOrLinearSize;
        header.pfSize = header.pfSize();
        // Not sure whether we can get away with leaving the rest of the
        // header blank
    }

    // Microsoft doesn't follow their own specifications and the
    // simplest conversion using the DxTex tool to e.g. a DXT3 texture
    // results in an illegal .dds file without either DDSD_PITCH or
    // DDSD_LINEARSIZE set in the header's flags. This code, adapted
    // from the DevIL library, fixes up the header in these situations.
    private void fixupHeader() {
        if (isCompressed() && !isSurfaceDescFlagSet(DDSD_LINEARSIZE)) {
            // Figure out how big the linear size should be
            int depth = header.backBufferCountOrDepth;
            if (depth == 0) {
                depth = 1;
            }

            header.pitchOrLinearSize = computeCompressedBlockSize(getWidth(), getHeight(), depth, getCompressionFormat());
            header.flags |= DDSD_LINEARSIZE;
        }
    }

    private static int computeCompressedBlockSize(final int width,
                                                  final int height,
                                                  final int depth,
                                                  final int compressionFormat) {
        int blockSize = ((width + 3)/4) * ((height + 3)/4) * ((depth + 3)/4);
        switch (compressionFormat) {
        case D3DFMT_DXT1:  blockSize *=  8; break;
        default:           blockSize *= 16; break;
        }
        return blockSize;
    }

    private static int computeBlockSize(final int width,
                                        final int height,
                                        final int depth,
                                        final int pixelFormat) {
        int blocksize;
        switch (pixelFormat) {
        case D3DFMT_R8G8B8:
            blocksize = width*height*3;
            break;
        case D3DFMT_A8R8G8B8:
        case D3DFMT_X8R8G8B8:
            blocksize = width*height*4;
            break;
        case D3DFMT_DXT1:
        case D3DFMT_DXT2:
        case D3DFMT_DXT3:
        case D3DFMT_DXT4:
        case D3DFMT_DXT5:
            blocksize = computeCompressedBlockSize(width, height, 1, pixelFormat);
            break;
        default:
            throw new IllegalArgumentException("d3dFormat must be one of the known formats");
        }
        return blocksize;
    }

    private int mipMapWidth(final int map) {
        int width = getWidth();
        for (int i = 0; i < map; i++) {
            width >>= 1;
        }
        return Math.max(width, 1);
    }

    private int mipMapHeight(final int map) {
        int height = getHeight();
        for (int i = 0; i < map; i++) {
            height >>= 1;
        }
        return Math.max(height, 1);
    }

    private int mipMapSizeInBytes(final int map) {
        final int width  = mipMapWidth(map);
        final int height = mipMapHeight(map);
        if (isCompressed()) {
            final int blockSize = (getCompressionFormat() == D3DFMT_DXT1 ? 8 : 16);
            return ((width+3)/4)*((height+3)/4)*blockSize;
        } else {
            return width * height * (getDepth() / 8);
        }
    }

    private int sideSizeInBytes() {
        int numLevels = getNumMipMaps();
        if (numLevels == 0) {
            numLevels = 1;
        }

        int size = 0;
        for (int i = 0; i < numLevels; i++) {
            size += mipMapSizeInBytes(i);
        }

        return size;
    }

    private int sideShiftInBytes(final int side) {
        final int[] sides = {
            DDSCAPS2_CUBEMAP_POSITIVEX,
            DDSCAPS2_CUBEMAP_NEGATIVEX,
            DDSCAPS2_CUBEMAP_POSITIVEY,
            DDSCAPS2_CUBEMAP_NEGATIVEY,
            DDSCAPS2_CUBEMAP_POSITIVEZ,
            DDSCAPS2_CUBEMAP_NEGATIVEZ
        };

        int shift = 0;
        final int sideSize = sideSizeInBytes();
        for (int i = 0; i < sides.length; i++) {
            final int temp = sides[i];
            if ((temp & side) != 0) {
                return shift;
            }

            shift += sideSize;
        }

        throw new RuntimeException("Illegal side: " + side);
    }

    private boolean printIfRecognized(final PrintStream tty, final int flags, final int flag, final String what) {
        if ((flags & flag) != 0) {
            tty.println(what);
            return true;
        }
        return false;
    }
    
    public enum Api {

        dx9,
        dx10,
        size
    }
}
