package id.deanbahari.minesweeper

import org.liquidengine.cbchain.impl.ChainErrorCallback
import org.liquidengine.legui.animation.AnimatorProvider
import org.liquidengine.legui.component.Frame
import org.liquidengine.legui.listener.processor.EventProcessorProvider
import org.liquidengine.legui.style.Style
import org.liquidengine.legui.style.color.ColorConstants
import org.liquidengine.legui.system.context.CallbackKeeper
import org.liquidengine.legui.system.context.Context
import org.liquidengine.legui.system.context.DefaultCallbackKeeper
import org.liquidengine.legui.system.handler.processor.SystemEventProcessor
import org.liquidengine.legui.system.handler.processor.SystemEventProcessorImpl
import org.liquidengine.legui.system.layout.LayoutManager
import org.liquidengine.legui.system.renderer.nvg.NvgRenderer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWWindowCloseCallbackI
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL.setCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

open class Window {
    private lateinit var keeper: CallbackKeeper
    private lateinit var systemEventProcessor: SystemEventProcessor
    private lateinit var frame: Frame
    private lateinit var context: Context

    private lateinit var mainThread: Thread
    private lateinit var rendererThread: Thread
    private lateinit var eventProcessorThread: Thread
    private lateinit var leguiEventProcessorThread: Thread

    private var window = 0L
    private var running = false

    fun run() {
        mainThread = Thread({
            initialize()
            startRenderer()
            startSystemEventProcessor()
            startLeguiEventProcessor()
            handleSystemEvents()
            destroy()
        }, "MAIN_WINDOW")

        mainThread.start()
        while (!running) {
            Thread.yield()
        }
    }

    fun initialize() {
        if (!glfwInit()) {
            throw RuntimeException("Can't initialize GLFW")
        }

        val errorCallback = ChainErrorCallback()
        errorCallback.add(GLFWErrorCallback.createPrint(System.err))
        errorCallback.add(GLFWErrorCallback.createThrow())
        glfwSetErrorCallback(errorCallback)

        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE)

        val glfwKeyCallbackI = GLFWKeyCallbackI {
                window, key, scancode, action, mods -> running = !(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        }
        val glfwWindowCloseCallbackI = GLFWWindowCloseCallbackI { running = false }

        val width = 400
        val height = 400

        frame = Frame(width.toFloat(), height.toFloat())

        window = glfwCreateWindow(width, height, "Test", NULL, NULL)
        glfwSetWindowPos(window, 50, 50)
        glfwShowWindow(window)
        createGuiElements(frame)

        context = Context(window)
        keeper = DefaultCallbackKeeper()

        CallbackKeeper.registerCallbacks(window, keeper)
        keeper.chainKeyCallback.add(glfwKeyCallbackI)
        keeper.chainWindowCloseCallback.add(glfwWindowCloseCallbackI)

        systemEventProcessor = SystemEventProcessorImpl()
        SystemEventProcessor.addDefaultCallbacks(keeper, systemEventProcessor)

        running = true
    }

    private fun render() {
        glfwMakeContextCurrent(window)
        val glCapabilities = createCapabilities()
        glfwSwapInterval(0)

        val renderer = NvgRenderer()
        renderer.initialize()

        glfwMakeContextCurrent(window)
        setCapabilities(glCapabilities)
        glfwSwapInterval(0)

        while (running) {
            try {
                context.updateGlfwWindow()
                val frameBufferSize = context.framebufferSize

                glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
                glViewport(0, 0, frameBufferSize.x, frameBufferSize.y)
                glClear(GL_COLOR_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

                renderer.render(frame, context)

                glfwPollEvents()
                glfwSwapBuffers(window)
                update()

                LayoutManager.getInstance().layout(frame)
                AnimatorProvider.getAnimator().runAnimations()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        renderer.destroy()
    }

    private fun startRenderer() {
        rendererThread = Thread(this::render, "GUI_RENDERER")
        rendererThread.start()
    }

    private fun startSystemEventProcessor() {
        eventProcessorThread = Thread({
            while (running) {
                systemEventProcessor.processEvents(frame, context)
            }
        }, "GUI_SYSTEM_EVENT_PROCESSOR")
        eventProcessorThread.start()
    }

    private fun startLeguiEventProcessor() {
        leguiEventProcessorThread = Thread({
            while (running) {
                EventProcessorProvider.getInstance().processEvents()
            }
        }, "GUI_EVENT_PROCESSOR")
        leguiEventProcessorThread.start()
    }

    private fun handleSystemEvents() {
        while (running) {
            glfwWaitEvents()
        }
    }

    private fun destroy() {
        glfwDestroyWindow(window)
        glfwTerminate()
    }

    open fun update() {
    }

    open fun createGuiElements(frame: Frame) {
        val gui = Gui(400, 400)
        gui.isFocusable = false
        gui.style.setMinWidth(100f)
        gui.style.setMinHeight(100f)
        gui.style.flexStyle.flexGrow = 1
        gui.style.position = Style.PositionType.RELATIVE
        gui.style.background.color = ColorConstants.transparent()

        frame.container.style.display = Style.DisplayType.FLEX
        frame.container.add(gui)
    }
}