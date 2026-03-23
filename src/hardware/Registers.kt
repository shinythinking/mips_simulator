package hardware

class Registers {
    private val registers = IntArray(32)

    init {
        registers[31] = 0xFFFFFFFF.toInt()
        registers[29] = 0x1000000
    }

    fun getRegisterFromOperand(index: Int): Int {
        require(index in 0..31) { "Register에 대한 잘못된 접근입니다.: $index" }
        return registers[index]
    }

    fun setRegister(index: Int, value: Int) {
        require(index in 0..31) { "Register에 대한 잘못된 접근입니다.: $index" }
        registers[index] = value
    }

    fun showAll(): String {
        return buildString {
            appendLine(registers.mapIndexed { index, value ->
                "register[$index] = $value"
            }.joinToString("\n"))
        }
    }

//    companion object {
//        val mapper = mapOf(
//            "zero" to 0, "AT" to 1,
//            "V0" to 2, "V1" to 3,
//            "A0" to 4, "A1" to 5, "A2" to 6, "A3" to 7,
//            "T0" to 8, "T1" to 9, "T2" to 10, "T3" to 11, "T4" to 12, "T5" to 13, "T6" to 14, "T7" to 15,
//            "R0" to 8, "R1" to 9, "R2" to 10, "R3" to 11, "R4" to 12, "R5" to 13, "R6" to 14, "R7" to 15,
//            "S0" to 16, "S1" to 17, "S2" to 18, "S3" to 19, "S4" to 20, "S5" to 21, "S6" to 22, "S7" to 23,
//            "T8" to 24, "T9" to 25,
//            "R8" to 24, "R9" to 25,
//            "K0" to 26, "K1" to 27,
//            "GP" to 28, "SP" to 29, "FP" to 30, "RA" to 31,
//        )
//    }
}