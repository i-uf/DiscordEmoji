package com.i_uf

import java.awt.*
import java.awt.Color
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*


fun main() {
    App.isVisible = true
}
object App : JFrame("DiscordEmoji v1.0.3") {
    private fun readResolve(): Any = App
    init {
        contentPane.removeAll()
        var result: Array<String>? = null
        var file: File? = null
        var imageRaw = false
        contentPane.preferredSize = Dimension(640, 640)
        isResizable = false
        contentPane.layout = null
        val upload = JButton("Upload")
        upload.bounds = Rectangle(640, 160)
        contentPane.add(upload)
        val fileChooser = FileDialog(this, "이미지 업로드", FileDialog.LOAD)
        fileChooser.file = "*.png"
        val label = contentPane.add(JLabel("Drag an Image here")) as JLabel
        label.horizontalAlignment = JLabel.CENTER
        label.bounds = Rectangle(0, 160, 480, 480)
        label.isOpaque = true
        label.background = Color.WHITE
        val buttonGroup = contentPane.add(JPanel()) as JPanel
        buttonGroup.layout = GridLayout(0, 1)
        buttonGroup.bounds = Rectangle(480, 160, 160, 480)
        val add = contentPane.add(JPanel()) as JPanel
        add.layout = GridLayout(0, 1)
        add.bounds = Rectangle(640, 0, 320, 160)
        val button1 = buttonGroup.add(JButton("Copy Top")) as JButton
        val button2 = buttonGroup.add(JButton("Copy Bottom")) as JButton
        val button3 = buttonGroup.add(JButton("Mode: Discord")) as JButton
        fun loadImage(file1: File) {
            file = file1
            val image = resizeImage(ImageIO.read(file), 16, 16)
            if(image.width != image.height) { upload.text = "Only 1:1! ( ${image.width} x ${image.height} )" ; return}
            upload.text = "<html>Upload ( ${file!!.name} )<br>${image.width} x ${image.height} ${file!!.length()} bytes"
            buttonGroup.isVisible = true
            label.text = ""
            label.icon = ImageIcon(resizeImage(emojiColor(image, imageRaw), 480, 480))
            result = read16x16(image)
        }
        button1.addActionListener {
            result!!.let {
                val stringSelection = StringSelection(it[0])
                val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
            }
        }
        button2.addActionListener {
            result!!.let {
                val stringSelection = StringSelection(it[1])
                val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
            }
        }
        button3.addActionListener {
            imageRaw = !imageRaw
            file?.let { loadImage(it) }
            button3.text = if(imageRaw) "Mode: Image Raw" else "Mode: Discord"
        }
        buttonGroup.isVisible = false
        upload.addActionListener{
            fileChooser.isVisible = true
            if (fileChooser.file != null) loadImage(File(fileChooser.directory + fileChooser.file))
        }
        label.transferHandler = object : TransferHandler() {
            override fun importData(info: TransferSupport): Boolean {
                if (!canImport(info)) return false
                val files = info.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                if (files.isNotEmpty()) {
                    val imageFile = files[0]
                    loadImage(imageFile)
                    return true
                }
                return false
            }

            override fun canImport(info: TransferSupport): Boolean {
                return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
            }
        }
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
    }
}
fun resizeImage(originalImage: BufferedImage, width: Int, height: Int): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g = image.createGraphics()
    g.drawImage(originalImage, 0, 0, width, height, null)
    g.dispose()
    return image
}
fun emojiColor(originalImage: BufferedImage, doNothing: Boolean): BufferedImage {
    if(doNothing) return originalImage
    val image = BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB)
    read16x16C(resizeImage(originalImage, 16, 16)).forEachIndexed { x, b ->
        b.forEachIndexed { y, color ->
            image.setRGB(x, y, (color.r shl 16) or (color.g shl 8) or color.b)
        }
    }
    return image
}