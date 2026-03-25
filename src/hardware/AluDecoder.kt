package hardware

import core.Functions
import core.Opcode

object AluDecoder {
    fun decode(opcode: Opcode, functInput: Int?): AluOperation {
        return if (opcode == Opcode.R_TYPE) {
            val funct = Functions.from(functInput)
            when (funct) {
                Functions.ADDU, Functions.ADD -> AluOperation.ADD
                Functions.AND -> AluOperation.AND
                Functions.NOR -> AluOperation.NOR
                Functions.OR -> AluOperation.OR
                Functions.SLT, Functions.SLTU -> AluOperation.SLT
                Functions.SLL -> AluOperation.SLL
                Functions.SRL -> AluOperation.SRL
                Functions.SUB, Functions.SUBU -> AluOperation.SUB
                Functions.JALR, Functions.JUMPR -> AluOperation.NONE
            }
        } else {
            when (opcode) {
                Opcode.ADDI, Opcode.ADDIU, Opcode.LW, Opcode.LBU,
                Opcode.LBHU, Opcode.SW, Opcode.SB, Opcode.SHW -> AluOperation.ADD

                Opcode.BEQ, Opcode.BNE -> AluOperation.SUB

                Opcode.ANDI -> AluOperation.AND
                Opcode.ORI -> AluOperation.OR
                Opcode.SLTI, Opcode.SLTIU -> AluOperation.SLT
                else -> AluOperation.NONE
            }
        }
    }
}