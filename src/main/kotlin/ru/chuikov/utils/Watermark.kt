package ru.chuikov.utils

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.filters.Caption
import net.coobird.thumbnailator.geometry.Position
import net.coobird.thumbnailator.geometry.Positions
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO


@Component
class Watermark(

) {
    fun addWatermark(byteArray: ByteArray, text: String): ByteArray {

        // Set up the caption properties
        val font = Font("Monospaced", Font.ITALIC, 30)
        val c = Color.DARK_GRAY
        val position: Position = Positions.CENTER
        val insetPixels = 0
        // Apply caption to the image
        val filter = Caption(text, font, c, position, insetPixels)
        var small  = Thumbnails.of(createImageFromBytes(byteArray)).size(200,200).asBufferedImage()
        var captionedImage = filter.apply(small)
        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(captionedImage, "jpg", baos)
        return baos.toByteArray()
    }

    private fun createImageFromBytes(imageData: ByteArray): BufferedImage {
        val bais = ByteArrayInputStream(imageData)
        try {
            return ImageIO.read(bais)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

}