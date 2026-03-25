package hardware

import core.Functions
import core.Opcode

object ControlUnit {
    fun decode(opcodeInput: Int, functInput: Int): ControlSignals {
        val opcode = Opcode.from(opcodeInput)
        return when (opcode) {
            Opcode.R_TYPE -> getRTypeSignal(opcode, functInput)
            Opcode.ADDI, Opcode.ADDIU, Opcode.ANDI, Opcode.ORI,
            Opcode.LUI, Opcode.LW, Opcode.LBU, Opcode.LBHU,
            Opcode.SB, Opcode.SHW, Opcode.SW, Opcode.BEQ, Opcode.BNE,
            Opcode.SLTIU, Opcode.SLTI
                -> getITypeSignal(opcode)

            Opcode.JUMP, Opcode.JAL -> getJTypeSignal(opcode)
        }
    }
}

data class ControlSignals(
    val regDst: Boolean?,
    val jump: Boolean?,
    val branch: Boolean? = false,
    val regWrite: Boolean?,
    val memRead: Boolean?,
    val memWrite: Boolean?,
    val memToReg: Boolean?,
    val aluSrc: Boolean?,
    val aluOp: AluOperation = AluOperation.NONE,
    val isLink: Boolean? = false,
    val isJr: Boolean? = false,
    val isShift: Boolean? = false,
    val isSlt: Boolean? = false,
    val isLui: Boolean? = false,
)

private fun getRTypeSignal(
    opcode: Opcode,
    functInput: Int
): ControlSignals {
    val funct = Functions.from(functInput)
    val rTypeSignal = ControlSignals(
        regDst = true,
        jump = false,
        regWrite = true,
        memRead = false,
        memWrite = false,
        memToReg = false,
        aluSrc = false,
        aluOp = AluDecoder.decode(opcode, functInput),
    )

    return when(funct) {
        Functions.JUMPR -> rTypeSignal.copy(regWrite = false, isJr = true)
        Functions.JALR -> rTypeSignal.copy(regWrite = true, isJr = true, isLink = true)
        Functions.SLL, Functions.SRL -> rTypeSignal.copy(isShift = true)
        Functions.SLTU, Functions.SLT -> rTypeSignal.copy(isSlt = true)
        else -> rTypeSignal
    }
}

private fun getITypeSignal(
    opcode: Opcode,
): ControlSignals {
    val iTypeSignal = ControlSignals(
        regDst = false,
        jump = false,
        regWrite = true,
        memRead = false,
        memWrite = false,
        memToReg = false,
        aluSrc = true,
        aluOp = AluDecoder.decode(opcode, null)
    )

    return when (opcode) {
        Opcode.BEQ, Opcode.BNE -> iTypeSignal.copy(
            branch = true,
            regWrite = false,
            aluSrc = false,
        )

        Opcode.LW -> iTypeSignal.copy(
            memRead = true,
            memToReg = true,
        )

        Opcode.SW, Opcode.SB, Opcode.SHW -> iTypeSignal.copy(
            regWrite = false,
            memWrite = true,
        )

        Opcode.SLTIU, Opcode.SLTI, -> iTypeSignal.copy(
            isSlt = true
        )

        Opcode.LUI -> iTypeSignal.copy(
            isLui = true
        )

        else -> iTypeSignal
    }
}

private fun getJTypeSignal(opcode: Opcode): ControlSignals {
    val jTypeSignal = ControlSignals(
        regDst = null,
        jump = true,
        branch = null,
        regWrite = false,
        memRead = false,
        memWrite = false,
        memToReg = null,
        aluSrc = null,
        aluOp = AluDecoder.decode(opcode, null),
    )
    return when (opcode) {
        Opcode.JUMP -> jTypeSignal
        Opcode.JAL -> jTypeSignal.copy(
            regWrite = true,
            isLink = true
        )

        else -> throw IllegalArgumentException("알 수 없는 명령어입니다. core.opcode: $opcode")
    }
}
