import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LwjglTest {

    // The window handle
    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Freeing callbacks (after game loop)
        glfwSetWindowPosCallback(window, null).free();
        glfwSetWindowSizeCallback(window, null).free();
        glfwSetWindowCloseCallback(window, null).free();
        glfwSetWindowFocusCallback(window, null).free();
        glfwSetWindowIconifyCallback(window, null).free();
        glfwSetFramebufferSizeCallback(window, null).free();
        glfwSetKeyCallback(window, null).free();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(640, 480, "My glfwApp", 0, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically


        // Registering callbacks (before game loop)
        glfwSetWindowPosCallback(window, (window, x, y) -> windowMoved(x, y));
        glfwSetWindowSizeCallback(window, (window, width, height) -> windowResized(width, height));
        glfwSetWindowCloseCallback(window, window -> windowClosing());
        glfwSetWindowFocusCallback(window, (window, focused) -> windowFocusChanged(focused));
        glfwSetWindowIconifyCallback(window, (window, iconified) -> windowIconfyChanged(iconified));
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> framebufferResized(width, height));
        glfwSetKeyCallback(window, (window, key, scan, action, mode) -> keyCallback(key, scan, action, mode));

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }
    public void keyCallback(int key, int scancode, int action, int mods)
    {
        switch (key)
        {
            case GLFW_KEY_UP:
                //moveUp = (action != GLFW_RELEASE);
                break;
        }
    }
    public void windowMoved(int x, int y) { /* snipped */ }
    public void windowResized(int width, int height) { /* snipped */ }
    public void windowClosing() { /* snipped */ }
    public void windowFocusChanged(boolean focused) { /* snipped */ }
    public void windowIconfyChanged(boolean iconified) { /* snipped */ }
    public void framebufferResized(int width, int height) { /* snipped */ }
    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.1f, 0.8f, 0.1f, 0.0f);
        float now, last = 0;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // Get the time
            now = (float) glfwGetTime();
            float delta = now - last;
            last = now;

            // Update and render
            glDrawPixels(100, 200, 100, 200, 1);
            //update(delta);
            //render(delta);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new LwjglTest().run();
    }
}