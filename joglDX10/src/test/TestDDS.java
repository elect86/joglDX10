/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import java.awt.Dimension;

/**
 *
 * @author elect
 */
public class TestDDS implements GLEventListener {

    private static int screenIdx = 0;
    private static Dimension windowSize = new Dimension(1024, 768);
    private static boolean undecorated = false;
    private static boolean alwaysOnTop = false;
    private static boolean fullscreen = false;
    private static boolean mouseVisible = true;
    private static boolean mouseConfined = false;
    public static GLWindow glWindow;
    public static Animator animator;

    public static void main(String[] args) {

        Display display = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(display, screenIdx);
        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glWindow = GLWindow.create(screen, glCapabilities);

        glWindow.setSize((int) windowSize.getWidth(), (int) windowSize.getHeight());
        glWindow.setPosition(50, 50);
        glWindow.setUndecorated(undecorated);
        glWindow.setAlwaysOnTop(alwaysOnTop);
        glWindow.setFullscreen(fullscreen);
        glWindow.setPointerVisible(mouseVisible);
        glWindow.confinePointer(mouseConfined);
        glWindow.setVisible(true);

        TestDDS testDDS = new TestDDS();
        glWindow.addGLEventListener(testDDS);

        animator = new Animator(glWindow);
        animator.start();
    }

    private long start;

    public void init(GLAutoDrawable glad) {
        start = System.currentTimeMillis();
    }

    public void dispose(GLAutoDrawable glad) {
        System.exit(0);
    }

    public void display(GLAutoDrawable glad) {
        if ((System.currentTimeMillis() - start) > 5000) {
            TestDDS.animator.stop();
            TestDDS.glWindow.destroy();
        }
    }

    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }
}
