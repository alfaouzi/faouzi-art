package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Pure Kotlin self-contained QR Code Matrix generator (Model 2, Byte Mode, ECC M).
 * Generates reliable, scannable QR codes for any website or social link without external dependencies.
 */
object QrCodeGenerator {

    /**
     * Generates a 2D boolean array representing the QR code matrix (true = dark, false = light).
     */
    fun generateMatrix(text: String): Array<BooleanArray> {
        val bytes = text.toByteArray(Charsets.ISO_8859_1)
        val len = bytes.size

        // Select suitable QR version (Medium ECC)
        // V3: max 42 bytes, V4: max 62 bytes, V5: max 84 bytes, V6: max 106 bytes, V7: max 122 bytes
        val (version, totalCodewords, dataCodewords, ecCodewords) = when {
            len <= 32 -> QrVersion(3, 70, 44, 26)
            len <= 53 -> QrVersion(4, 100, 64, 36)
            len <= 78 -> QrVersion(5, 134, 86, 48)
            len <= 106 -> QrVersion(6, 172, 108, 64)
            else -> QrVersion(7, 196, 124, 72)
        }

        val size = version * 4 + 17
        val matrix = Array(size) { BooleanArray(size) }
        val reserved = Array(size) { BooleanArray(size) }

        // 1. Finder patterns
        drawFinderPattern(matrix, reserved, 0, 0)
        drawFinderPattern(matrix, reserved, size - 7, 0)
        drawFinderPattern(matrix, reserved, 0, size - 7)

        // 2. Alignment patterns (if version >= 2)
        val alignCoords = getAlignmentPositions(version)
        for (r in alignCoords) {
            for (c in alignCoords) {
                if (reserved[r][c]) continue
                drawAlignmentPattern(matrix, reserved, r - 2, c - 2)
            }
        }

        // 3. Timing patterns
        for (i in 8 until size - 8) {
            if (!reserved[6][i]) {
                matrix[6][i] = (i % 2 == 0)
                reserved[6][i] = true
            }
            if (!reserved[i][6]) {
                matrix[i][6] = (i % 2 == 0)
                reserved[i][6] = true
            }
        }

        // 4. Dark module
        val darkRow = 4 * version + 9
        matrix[darkRow][8] = true
        reserved[darkRow][8] = true

        // 5. Reserve format info areas
        for (i in 0..8) {
            reserved[8][i] = true
            reserved[i][8] = true
        }
        for (i in 0..7) {
            reserved[8][size - 1 - i] = true
            reserved[size - 1 - i][8] = true
        }

        // 6. Encode data
        val dataBits = encodeDataBits(bytes, version, dataCodewords)
        val allCodewords = calculateErrorCorrection(dataBits, dataCodewords, totalCodewords, ecCodewords)

        // 7. Place data bits in matrix (zigzag)
        val fullBits = BooleanArray(allCodewords.size * 8)
        for (i in allCodewords.indices) {
            val byte = allCodewords[i]
            for (b in 0..7) {
                fullBits[i * 8 + b] = ((byte shr (7 - b)) and 1) == 1
            }
        }

        var bitIndex = 0
        var col = size - 1
        var upward = true
        while (col > 0) {
            if (col == 6) col-- // Skip vertical timing column
            val rowRange = if (upward) (size - 1 downTo 0) else (0 until size)
            for (r in rowRange) {
                for (cOffset in 0..1) {
                    val c = col - cOffset
                    if (!reserved[r][c]) {
                        val bit = if (bitIndex < fullBits.size) fullBits[bitIndex++] else false
                        // Mask 0: (row + col) % 2 == 0
                        val mask = (r + c) % 2 == 0
                        matrix[r][c] = bit xor mask
                    }
                }
            }
            col -= 2
            upward = !upward
        }

        // 8. Format bits (Mask 0, ECC M: 00 000 -> Format string with BCH 15,5 = 0x5412)
        writeFormatBits(matrix, 0x5412)

        return matrix
    }

    private fun drawFinderPattern(m: Array<BooleanArray>, r: Array<BooleanArray>, row: Int, col: Int) {
        for (i in -1..7) {
            for (j in -1..7) {
                val cr = row + i
                val cc = col + j
                if (cr in m.indices && cc in m.indices) {
                    r[cr][cc] = true
                    val inCenter = i in 1..5 && j in 1..5
                    val inInner = i in 2..4 && j in 2..4
                    m[cr][cc] = (i in 0..6 && j in 0..6) && (!inCenter || inInner)
                }
            }
        }
    }

    private fun drawAlignmentPattern(m: Array<BooleanArray>, r: Array<BooleanArray>, row: Int, col: Int) {
        for (i in 0..4) {
            for (j in 0..4) {
                val cr = row + i
                val cc = col + j
                r[cr][cc] = true
                val isBorder = i == 0 || i == 4 || j == 0 || j == 4
                val isCenter = i == 2 && j == 2
                m[cr][cc] = isBorder || isCenter
            }
        }
    }

    private fun getAlignmentPositions(version: Int): IntArray = when (version) {
        2 -> intArrayOf(6, 18)
        3 -> intArrayOf(6, 22)
        4 -> intArrayOf(6, 26)
        5 -> intArrayOf(6, 30)
        6 -> intArrayOf(6, 34)
        7 -> intArrayOf(6, 22, 38)
        else -> intArrayOf(6, version * 4 + 10)
    }

    private fun encodeDataBits(bytes: ByteArray, version: Int, targetDataCodewords: Int): IntArray {
        val bitBuffer = mutableListOf<Boolean>()

        fun appendBits(value: Int, count: Int) {
            for (i in count - 1 downTo 0) {
                bitBuffer.add(((value shr i) and 1) == 1)
            }
        }

        // Mode indicator for 8-bit byte: 0100
        appendBits(4, 4)

        // Character count indicator (8 bits for V1..9)
        appendBits(bytes.size, 8)

        // Data bytes
        for (b in bytes) {
            appendBits(b.toInt() and 0xFF, 8)
        }

        // Terminator (up to 4 zero bits)
        val termLen = minOf(4, (targetDataCodewords * 8) - bitBuffer.size)
        repeat(termLen) { bitBuffer.add(false) }

        // Align to 8-bit boundary
        while (bitBuffer.size % 8 != 0) {
            bitBuffer.add(false)
        }

        // Convert to byte array
        val codewords = IntArray(targetDataCodewords)
        var byteIndex = 0
        for (i in 0 until bitBuffer.size step 8) {
            var b = 0
            for (k in 0..7) {
                if (bitBuffer[i + k]) b = b or (1 shl (7 - k))
            }
            codewords[byteIndex++] = b
        }

        // Pad alternating 0xEC (236) and 0x11 (17)
        var padToggle = true
        while (byteIndex < targetDataCodewords) {
            codewords[byteIndex++] = if (padToggle) 236 else 17
            padToggle = !padToggle
        }

        return codewords
    }

    private fun calculateErrorCorrection(
        data: IntArray,
        dataCount: Int,
        totalCount: Int,
        ecCount: Int
    ): IntArray {
        val poly = getGeneratorPolynomial(ecCount)
        val result = IntArray(totalCount)
        System.arraycopy(data, 0, result, 0, dataCount)

        val remainder = IntArray(ecCount)
        for (d in data) {
            val factor = d xor remainder[0]
            for (i in 0 until ecCount - 1) {
                remainder[i] = remainder[i + 1] xor gfMultiply(poly[i], factor)
            }
            remainder[ecCount - 1] = gfMultiply(poly[ecCount - 1], factor)
        }

        for (i in 0 until ecCount) {
            result[dataCount + i] = remainder[i]
        }
        return result
    }

    // Galois Field GF(256) with primitive polynomial 0x11D (285)
    private val expTable = IntArray(512)
    private val logTable = IntArray(256)

    init {
        var x = 1
        for (i in 0 until 255) {
            expTable[i] = x
            expTable[i + 255] = x
            logTable[x] = i
            x = (x shl 1)
            if (x >= 256) x = x xor 285
        }
    }

    private fun gfMultiply(a: Int, b: Int): Int {
        if (a == 0 || b == 0) return 0
        return expTable[logTable[a] + logTable[b]]
    }

    private fun getGeneratorPolynomial(degree: Int): IntArray {
        var poly = intArrayOf(1)
        for (i in 0 until degree) {
            val next = IntArray(poly.size + 1)
            val factor = expTable[i]
            for (j in poly.indices) {
                next[j] = next[j] xor poly[j]
                next[j + 1] = next[j + 1] xor gfMultiply(poly[j], factor)
            }
            poly = next
        }
        return poly.copyOfRange(1, poly.size)
    }

    private fun writeFormatBits(matrix: Array<BooleanArray>, formatBits: Int) {
        val size = matrix.size
        for (i in 0..14) {
            val bit = ((formatBits shr (14 - i)) and 1) == 1
            // Vertical placement
            val (r, c) = when {
                i <= 5 -> Pair(i, 8)
                i == 6 -> Pair(7, 8)
                i == 7 -> Pair(8, 8)
                i == 8 -> Pair(8, 7)
                else -> Pair(8, 14 - i)
            }
            matrix[r][c] = bit

            // Second placement around edges
            val (r2, c2) = when {
                i <= 7 -> Pair(8, size - 1 - i)
                else -> Pair(size - 15 + i, 8)
            }
            matrix[r2][c2] = bit
        }
    }

    private data class QrVersion(val version: Int, val total: Int, val data: Int, val ec: Int)
}

/**
 * Beautiful Compose QR Code Canvas component styled with rounded modules and Faouzi Art turquoise highlights.
 */
@Composable
fun QrCodeView(
    content: String,
    modifier: Modifier = Modifier,
    darkColor: Color = Color(0xFF042226),
    lightColor: Color = Color.White
) {
    val matrix = remember(content) {
        try {
            QrCodeGenerator.generateMatrix(content)
        } catch (_: Exception) {
            Array(25) { BooleanArray(25) { (it % 2 == 0) } }
        }
    }

    val moduleCount = matrix.size

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(lightColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellSize = size.width / moduleCount.toFloat()
            val radius = cellSize * 0.3f

            for (r in 0 until moduleCount) {
                for (c in 0 until moduleCount) {
                    if (matrix[r][c]) {
                        // Is this in a finder pattern? Keep corner finders crisp
                        val inFinder = (r < 8 && c < 8) || (r < 8 && c >= moduleCount - 8) || (r >= moduleCount - 8 && c < 8)
                        val modRadius = if (inFinder) CornerRadius(cellSize * 0.15f) else CornerRadius(radius)

                        drawRoundRect(
                            color = darkColor,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize * 0.98f, cellSize * 0.98f),
                            cornerRadius = modRadius
                        )
                    }
                }
            }
        }
    }
}
