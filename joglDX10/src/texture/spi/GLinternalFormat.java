/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texture.spi;

import static com.jogamp.opengl.GL.GL_BYTE;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_HALF_FLOAT;
import static com.jogamp.opengl.GL.GL_R16F;
import static com.jogamp.opengl.GL.GL_R32F;
import static com.jogamp.opengl.GL.GL_R8;
import static com.jogamp.opengl.GL.GL_RG16F;
import static com.jogamp.opengl.GL.GL_RG32F;
import static com.jogamp.opengl.GL.GL_RG8;
import static com.jogamp.opengl.GL.GL_RGB;
import static com.jogamp.opengl.GL.GL_RGB10_A2;
import static com.jogamp.opengl.GL.GL_RGB16F;
import static com.jogamp.opengl.GL.GL_RGB32F;
import static com.jogamp.opengl.GL.GL_RGB565;
import static com.jogamp.opengl.GL.GL_RGB5_A1;
import static com.jogamp.opengl.GL.GL_RGB8;
import static com.jogamp.opengl.GL.GL_RGBA;
import static com.jogamp.opengl.GL.GL_RGBA16F;
import static com.jogamp.opengl.GL.GL_RGBA32F;
import static com.jogamp.opengl.GL.GL_RGBA4;
import static com.jogamp.opengl.GL.GL_RGBA8;
import static com.jogamp.opengl.GL.GL_SHORT;
import static com.jogamp.opengl.GL.GL_SRGB8_ALPHA8;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT_10F_11F_11F_REV;
import static com.jogamp.opengl.GL.GL_UNSIGNED_SHORT;
import static com.jogamp.opengl.GL.GL_UNSIGNED_SHORT_4_4_4_4;
import static com.jogamp.opengl.GL.GL_UNSIGNED_SHORT_5_5_5_1;
import static com.jogamp.opengl.GL.GL_UNSIGNED_SHORT_5_6_5;
import static com.jogamp.opengl.GL2ES2.GL_INT;
import static com.jogamp.opengl.GL2ES2.GL_RED;
import static com.jogamp.opengl.GL2ES2.GL_RG;
import static com.jogamp.opengl.GL2ES2.GL_UNSIGNED_INT_2_10_10_10_REV;
import static com.jogamp.opengl.GL2ES3.GL_R11F_G11F_B10F;
import static com.jogamp.opengl.GL2ES3.GL_R16I;
import static com.jogamp.opengl.GL2ES3.GL_R16UI;
import static com.jogamp.opengl.GL2ES3.GL_R32I;
import static com.jogamp.opengl.GL2ES3.GL_R32UI;
import static com.jogamp.opengl.GL2ES3.GL_R8I;
import static com.jogamp.opengl.GL2ES3.GL_R8UI;
import static com.jogamp.opengl.GL2ES3.GL_R8_SNORM;
import static com.jogamp.opengl.GL2ES3.GL_RED_INTEGER;
import static com.jogamp.opengl.GL2ES3.GL_RG16I;
import static com.jogamp.opengl.GL2ES3.GL_RG16UI;
import static com.jogamp.opengl.GL2ES3.GL_RG32I;
import static com.jogamp.opengl.GL2ES3.GL_RG32UI;
import static com.jogamp.opengl.GL2ES3.GL_RG8I;
import static com.jogamp.opengl.GL2ES3.GL_RG8UI;
import static com.jogamp.opengl.GL2ES3.GL_RG8_SNORM;
import static com.jogamp.opengl.GL2ES3.GL_RGB10_A2UI;
import static com.jogamp.opengl.GL2ES3.GL_RGB16I;
import static com.jogamp.opengl.GL2ES3.GL_RGB16UI;
import static com.jogamp.opengl.GL2ES3.GL_RGB32I;
import static com.jogamp.opengl.GL2ES3.GL_RGB32UI;
import static com.jogamp.opengl.GL2ES3.GL_RGB8I;
import static com.jogamp.opengl.GL2ES3.GL_RGB8UI;
import static com.jogamp.opengl.GL2ES3.GL_RGB8_SNORM;
import static com.jogamp.opengl.GL2ES3.GL_RGB9_E5;
import static com.jogamp.opengl.GL2ES3.GL_RGBA16I;
import static com.jogamp.opengl.GL2ES3.GL_RGBA16UI;
import static com.jogamp.opengl.GL2ES3.GL_RGBA32I;
import static com.jogamp.opengl.GL2ES3.GL_RGBA32UI;
import static com.jogamp.opengl.GL2ES3.GL_RGBA8I;
import static com.jogamp.opengl.GL2ES3.GL_RGBA8UI;
import static com.jogamp.opengl.GL2ES3.GL_RGBA8_SNORM;
import static com.jogamp.opengl.GL2ES3.GL_RGBA_INTEGER;
import static com.jogamp.opengl.GL2ES3.GL_RGB_INTEGER;
import static com.jogamp.opengl.GL2ES3.GL_RG_INTEGER;
import static com.jogamp.opengl.GL2ES3.GL_SRGB8;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_5_9_9_9_REV;

/**
 * Table extracted from here
 * https://www.khronos.org/opengles/sdk/docs/man3/html/glTexImage2D.xhtml
 *
 * @author elect
 */
public class GLinternalFormat {

    public static int get(int format, int type) {

        return get(format, type, false, false);
    }

    public static int get(int format, int type, boolean textureFilterable, boolean sRGB) {

        switch (format) {

            case GL_RED:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return GL_R8;

                    case GL_BYTE:
                        return GL_R8_SNORM;

                    case GL_HALF_FLOAT:
                        return GL_R16F;

                    case GL_FLOAT:
                        return textureFilterable ? GL_R16F : GL_R32F;
                }
                break;

            case GL_RED_INTEGER:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return GL_R8UI;

                    case GL_BYTE:
                        return GL_R8I;

                    case GL_UNSIGNED_SHORT:
                        return GL_R16UI;

                    case GL_SHORT:
                        return GL_R16I;

                    case GL_UNSIGNED_INT:
                        return GL_R32UI;

                    case GL_INT:
                        return GL_R32I;
                }
                break;

            case GL_RG:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return GL_RG8;

                    case GL_BYTE:
                        return GL_RG8_SNORM;

                    case GL_HALF_FLOAT:
                        return GL_RG16F;

                    case GL_FLOAT:
                        return textureFilterable ? GL_RG16F : GL_RG32F;
                }
                break;

            case GL_RG_INTEGER:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return GL_RG8UI;

                    case GL_BYTE:
                        return GL_RG8I;

                    case GL_UNSIGNED_SHORT:
                        return GL_RG16UI;

                    case GL_SHORT:
                        return GL_RG16I;

                    case GL_UNSIGNED_INT:
                        return GL_RG32UI;

                    case GL_INT:
                        return GL_RG32I;
                }
                break;

            case GL_RGB:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return sRGB ? GL_SRGB8 : GL_RGB8;

                    case GL_UNSIGNED_SHORT_5_6_5:
                        return GL_RGB565;

                    case GL_BYTE:
                        return GL_RGB8_SNORM;

                    case GL_UNSIGNED_INT_10F_11F_11F_REV:
                        return GL_R11F_G11F_B10F;

                    case GL_UNSIGNED_INT_5_9_9_9_REV:
                        return GL_RGB9_E5;

                    case GL_HALF_FLOAT:
                        return GL_RGB16F;

                    case GL_FLOAT:
                        return textureFilterable ? GL_RGB16F : GL_RGB32F;
                }
                break;

            case GL_RGB_INTEGER:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return GL_RGB8UI;

                    case GL_BYTE:
                        return GL_RGB8I;

                    case GL_UNSIGNED_SHORT:
                        return GL_RGB16UI;

                    case GL_SHORT:
                        return GL_RGB16I;

                    case GL_UNSIGNED_INT:
                        return GL_RGB32UI;

                    case GL_INT:
                        return GL_RGB32I;
                }
                break;

            case GL_RGBA:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return sRGB ? GL_SRGB8_ALPHA8 : GL_RGBA8;

                    case GL_BYTE:
                        return GL_RGBA8_SNORM;

                    case GL_UNSIGNED_SHORT_5_5_5_1:
                        return GL_RGB5_A1;

                    case GL_UNSIGNED_SHORT_4_4_4_4:
                        return GL_RGBA4;

                    case GL_UNSIGNED_INT_2_10_10_10_REV:
                        return GL_RGB10_A2;

                    case GL_HALF_FLOAT:
                        return GL_RGBA16F;

                    case GL_FLOAT:
                        return textureFilterable ? GL_RGBA16F : GL_RGBA32F;
                }
                break;

            case GL_RGBA_INTEGER:

                switch (type) {

                    case GL_UNSIGNED_BYTE:
                        return GL_RGBA8UI;

                    case GL_BYTE:
                        return GL_RGBA8I;

                    case GL_UNSIGNED_INT_2_10_10_10_REV:
                        return GL_RGB10_A2UI;

                    case GL_UNSIGNED_SHORT:
                        return GL_RGBA16UI;

                    case GL_SHORT:
                        return GL_RGBA16I;

                    case GL_INT:
                        return GL_RGBA32I;

                    case GL_UNSIGNED_INT:
                        return GL_RGBA32UI;
                }
                break;
        }
        return 0;
    }
}
