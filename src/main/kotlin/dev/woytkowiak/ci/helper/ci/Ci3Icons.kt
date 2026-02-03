package dev.woytkowiak.ci.helper.ci

import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.imageio.ImageIO

/**
 * Ikony pluginu CodeIgniter 3 Helper.
 * Pliki: `src/main/resources/icons/`
 * - ci3.png (dowolny rozmiar — zawsze rysowane jako 16×16)
 * - Ci3 — kolorowa, na pasku statusu
 * - Ci3Menu — w kolorze kontekstu (dopasowana do menu, jak PHP File / File)
 */
object Ci3Icons {
    private const val SIZE = 16

    @JvmField
    val Ci3: Icon = loadFixedSizeIcon("/icons/ci3.png")

    /** Ikona w kolorze kontekstu (menu/toolbar) — kształt z ci3.png, kolor z IDE. */
    @JvmField
    val Ci3Menu: Icon = loadMenuStyleIcon("/icons/ci3.png")

    /** Ikona o stałym rozmiarze 16×16 — nie rozciąga się przy rysowaniu. */
    private fun loadFixedSizeIcon(path: String): Icon {
        val url = Ci3Icons::class.java.getResource(path) ?: return ImageIcon()
        val image = ImageIO.read(url) ?: return ImageIcon()
        val scaled = image.getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH)
        return object : Icon {
            override fun paintIcon(c: Component?, g: Graphics, x: Int, y: Int) {
                g.drawImage(scaled, x, y, SIZE, SIZE, null)
            }
            override fun getIconWidth(): Int = SIZE
            override fun getIconHeight(): Int = SIZE
        }
    }

    /** Ikona rysowana w kolorze Graphics (dopasowana do menu / paska narzędzi). */
    private fun loadMenuStyleIcon(path: String): Icon {
        val url = Ci3Icons::class.java.getResource(path) ?: return ImageIcon()
        val source = ImageIO.read(url) ?: return ImageIcon()
        val mask = BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB)
        (mask.createGraphics() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
            setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            drawImage(source, 0, 0, SIZE, SIZE, null)
            dispose()
        }
        return object : Icon {
            override fun paintIcon(c: Component?, g: Graphics, x: Int, y: Int) {
                val color = g.color
                val out = BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB)
                for (py in 0 until SIZE) {
                    for (px in 0 until SIZE) {
                        val a = (mask.getRGB(px, py) shr 24) and 0xFF
                        out.setRGB(px, py, (a shl 24) or (color.rgb and 0x00_FF_FF_FF))
                    }
                }
                (g as? Graphics2D)?.apply {
                    setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
                    setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
                }?.drawImage(out, x, y, SIZE, SIZE, null)
            }
            override fun getIconWidth(): Int = SIZE
            override fun getIconHeight(): Int = SIZE
        }
    }
}
