package id.deanbahari.minesweeper

import org.liquidengine.legui.component.*
import org.liquidengine.legui.component.event.tooltip.TooltipTextSizeChangeEvent
import org.liquidengine.legui.component.optional.align.HorizontalAlign
import org.liquidengine.legui.component.optional.align.VerticalAlign
import org.liquidengine.legui.event.FocusEvent
import org.liquidengine.legui.event.KeyEvent
import org.liquidengine.legui.event.MouseClickEvent
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction
import org.liquidengine.legui.listener.FocusEventListener
import org.liquidengine.legui.listener.KeyEventListener
import org.liquidengine.legui.listener.MouseClickEventListener
import org.lwjgl.glfw.GLFW


class Gui(width: Int, height: Int) : Panel(0f, 0f, width.toFloat(), height.toFloat()) {

    private val mouseTargetLabel: Label

    init {
        createNinePanels()

        mouseTargetLabel = Label("Hello Label 1", 10f, (height - 30).toFloat(), (width - 20).toFloat(), 20f)
        this.add(mouseTargetLabel)
        this.add(createButtonWithTooltip())

        val textInput = TextInput(250f, 130f, 100f, 30f)
        textInput.listenerMap.addListener(
            KeyEvent::class.java,
            KeyEventListener { event: KeyEvent<*> ->
                if (event.key == GLFW.GLFW_KEY_F1 && event.action == GLFW.GLFW_RELEASE) {
                    textInput.style.horizontalAlign = HorizontalAlign.LEFT
                } else if (event.key == GLFW.GLFW_KEY_F2 && event.action == GLFW.GLFW_RELEASE) {
                    textInput.style.horizontalAlign = HorizontalAlign.CENTER
                } else if (event.key == GLFW.GLFW_KEY_F3 && event.action == GLFW.GLFW_RELEASE) {
                    textInput.style.horizontalAlign = HorizontalAlign.RIGHT
                } else if (event.key == GLFW.GLFW_KEY_F4 && event.action == GLFW.GLFW_RELEASE) {
                    textInput.style.verticalAlign = VerticalAlign.TOP
                } else if (event.key == GLFW.GLFW_KEY_F5 && event.action == GLFW.GLFW_RELEASE) {
                    textInput.style.verticalAlign = VerticalAlign.MIDDLE
                } else if (event.key == GLFW.GLFW_KEY_F6 && event.action == GLFW.GLFW_RELEASE) {
                    textInput.style.verticalAlign = VerticalAlign.BOTTOM
                }
            })
        this.add(textInput)
    }

    private fun createNinePanels() {
        val p1 = Panel((1 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p1)
        p1.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p2 = Panel((2 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p2)
        p2.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p3 = Panel((3 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p3)
        p3.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p4 = Panel((4 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p4)
        p4.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p5 = Panel((5 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p5)
        p5.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p6 = Panel((6 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p6)
        p6.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p7 = Panel((7 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p7)
        p7.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p8 = Panel((8 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p8)
        p8.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
        val p9 = Panel((9 * 20).toFloat(), 10f, 10f, 10f)
        this.add(p9)
        p9.listenerMap.addListener(
            FocusEvent::class.java,
            FocusEventListener { x: FocusEvent<*>? -> println(x) })
    }

    private fun createButtonWithTooltip(): Button {
        val button = Button(
            20f, 170f, 50f,
            20f
        ) /*button.getStyle().getBackground().setColor(new Vector4f(1));*/

        button.listenerMap
            .addListener(MouseClickEvent::class.java, MouseClickEventListener { println(it) })
        val tooltip = Tooltip("Just button")
        button.tooltip = tooltip
        tooltip.setPosition(25f, 25f)
        tooltip.size.set(200f, 200f)
        tooltip.style.setPadding(4f)
        tooltip.listenerMap
            .addListener(TooltipTextSizeChangeEvent::class.java) { e -> tooltip.setSize(200f, 200f) }
        val idv = intArrayOf(0)
        button.listenerMap
            .addListener(
                MouseClickEvent::class.java,
                MouseClickEventListener {
                    if (it.action.equals(MouseClickAction.CLICK)) {
                        idv[0]++
                        val h: HorizontalAlign
                        val v: VerticalAlign
                        val hh = idv[0] % 3
                        val vv = idv[0] / 3 % 3
                        h = when (hh) {
                            0 -> HorizontalAlign.LEFT
                            1 -> HorizontalAlign.CENTER
                            2 -> HorizontalAlign.RIGHT
                            else -> HorizontalAlign.RIGHT
                        }
                        v = when (vv) {
                            0 -> VerticalAlign.TOP
                            1 -> VerticalAlign.MIDDLE
                            2 -> VerticalAlign.BOTTOM
                            else -> VerticalAlign.BOTTOM
                        }
                        println("$h $v")
                        tooltip.style.horizontalAlign = h
                        tooltip.style.verticalAlign = v
                    }
                } as MouseClickEventListener?)
        return button
    }

}