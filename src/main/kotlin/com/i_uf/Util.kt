package com.i_uf

import java.awt.image.BufferedImage
import kotlin.math.*
fun read16x16(image: BufferedImage): Array<String> {
    val result = Array(2){""}
    for (i in 0 until 16) {
        for (j in 0 until 16) {
            val pixel = image.getRGB(j, i)
            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF
            result[i / 8] += findClosestColor(red, green, blue).text
        }
        result[i / 8] += "\n"
    }
    return result
}
fun read16x16C(image: BufferedImage): Array<Array<Color>> {
    val result = Array(16){Array(16){ RED } }
    for (i in 0 until 16) {
        for (j in 0 until 16) {
            val pixel = image.getRGB(j, i)
            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF
            result[j][i] = findClosestColor(red, green, blue)
        }
    }
    return result
}
fun colorDistance(c1: Color, r: Int, g: Int, b: Int): Double {
    return sqrt((c1.r - r).toDouble().pow(2) +
            (c1.g - g).toDouble().pow(2) +
            (c1.b - b).toDouble().pow(2))
}
fun findClosestColor(red: Int, green: Int, blue: Int): Color {
    val colorList = arrayOf(*colors.toTypedArray(), *custom_colors.toTypedArray())
    var closestColor = RED
    var minDistance = colorDistance(closestColor, red, green, blue)

    for (color in colorList) {
        val distance = colorDistance(color, red, green, blue)
        if (distance < minDistance) {
            closestColor = color
            minDistance = distance
        }
    }

    return closestColor
}
data class Color(val text: String, val r: Int, val g: Int, val b: Int) {
    fun rgb() = r shl 16 or (g shl 8) or b
    operator fun invoke(other: Color) = rgb() == other.rgb() || text == other.text
}
val RED = Color(":red_square:", 221, 46, 68)
val BROWN = Color(":brown_square:", 193, 105, 79)
val ORANGE = Color(":orange_square:", 244, 144, 13)
val YELLOW = Color(":yellow_square:", 253, 203, 89)
val GREEN = Color(":green_square:", 120, 177, 89)
val BLUE = Color(":blue_square:", 84, 171, 237)
val PURPLE = Color(":purple_square:", 170, 141, 215)
val BLACK = Color(":black_large_square:", 49, 55, 61)
val WHITE = Color(":white_large_square:", 230, 231, 232)
private val colors = mutableListOf(
    RED, BROWN, ORANGE, YELLOW, GREEN,
    BLUE, PURPLE, BLACK, WHITE
)
val custom_colors: MutableList<Color> = mutableListOf()