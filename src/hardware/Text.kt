package hardware

import java.nio.ByteBuffer
import java.nio.ByteOrder

class Text(
    input: ByteArray,
) {
    private val buffer = ByteBuffer.wrap(input).apply {
        order(ByteOrder.BIG_ENDIAN)
    }

    fun getLine(pc: Int): Int {

        require(pc % 4 == 0) {
            "Address Error: PC(0x${pc.toString(16)})는 4의 배수여야 합니다."
        }

        require(pc in 0 until buffer.capacity() - 3) {
            "Segmentation Fault: PC(0x${pc.toString(16)})가 메모리 범위를 벗어났습니다."
        }

        return buffer.getInt(pc)

//        val b1 = (codeLines[pc].toInt() and 0xFF) shl 24
//        val b2 = (codeLines[pc + 1].toInt() and 0xFF) shl 16
//        val b3 = (codeLines[pc + 2].toInt() and 0xFF) shl 8
//        val b4 = (codeLines[pc + 3].toInt() and 0xFF)
//        return b1 or b2 or b3 or b4
    }
}