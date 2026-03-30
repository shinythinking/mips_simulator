package simulator

import core.*
import hardware.Alu
import hardware.ControlUnit

class Simulator() {
    operator fun invoke(
        architecturalState: ArchitecturalState
    ) {
            // IF
            val instruction = architecturalState.getNextLine()

            // ID
            val prevAddress = architecturalState.pc
            val opcode = instruction.opcode
            val rs = instruction.rs
            val rt = instruction.rt
            val rd = instruction.rd
            val shamt = instruction.shamt
            val funct = instruction.funct
            val immediate = instruction.immediate
            val address = instruction.address

            val controlSignal = ControlUnit.decode(opcode, funct)

            // ALU Execution
            val registerSrc = architecturalState.getRegister(rs)
            val registerTar = architecturalState.getRegister(rt)
            val signExtendedImmediate = immediate.toShort().toInt()
            val zeroExtendedImmediate = immediate

            val finalImmediate = if (opcode == Opcode.ANDI.code || opcode == Opcode.ORI.code) zeroExtendedImmediate
            else signExtendedImmediate

            val aluInputD1 = mux(controlSignal.isShift, registerTar, registerSrc)
            var aluInputD2 = mux(controlSignal.aluSrc, finalImmediate, registerTar)
            aluInputD2 = mux(controlSignal.isShift, shamt, aluInputD2)

            val aluResult = Alu(aluInputD1, aluInputD2, controlSignal.aluOp)

            // Memory
            val memoryAddress = aluResult.result
            val writeDataMemory = registerTar

            if (controlSignal.memWrite == true) architecturalState.writeMemory(memoryAddress, writeDataMemory)
            val memoryRead = if (controlSignal.memRead == true) architecturalState.readMemory(memoryAddress) else 0

            var writeDataRegister = mux(controlSignal.memToReg, memoryRead, aluResult.result)

            val sltResult = (aluResult.result shr 31) and 0x1
            writeDataRegister = mux(controlSignal.isSlt, sltResult, writeDataRegister)
            writeDataRegister = mux(controlSignal.isLink, architecturalState.pc + 8, writeDataRegister)

            val luiImmediate = immediate shl 16
            writeDataRegister = mux(controlSignal.isLui, luiImmediate, writeDataRegister)

            // WriteBack
            var writeRegister = mux(controlSignal.regDst, rd, rt)
            writeRegister = mux(controlSignal.isLink, 31, writeRegister)

            if (controlSignal.regWrite == true) {
                architecturalState.setRegister(writeRegister, writeDataRegister)
            }

            // Jump
            val nextInstructionAddress = architecturalState.pc + 4
            val jTypeAddress = (nextInstructionAddress and 0xF0000000.toInt()) or (address shl 2)
            val branchAddress = nextInstructionAddress + (signExtendedImmediate shl 2)

            val bcond: Boolean = when (Opcode.from(opcode)) {
                Opcode.BEQ -> aluResult.isZero
                Opcode.BNE -> !aluResult.isZero
                else -> false
            }

            var nextAddress = mux((controlSignal.branch == true && bcond), branchAddress, nextInstructionAddress)
            nextAddress = mux(controlSignal.jump, jTypeAddress, nextAddress)
            nextAddress = mux(controlSignal.isJr, registerSrc, nextAddress)

            architecturalState.movePc(nextAddress)

            stats.recordCycle(
                opcodeInput = opcode,
                controlSignal = controlSignal,
                isBranchTaken = bcond
            )

            asStat.appendLine("===== core.ArchitecturalState =====")
//            println("core.opcode = ${core.opcode.toString(16)} -> \ncore.funct = ${core.funct.core.funct.toString(16) }\n->result:0x${aluResult.result.toString(16)}")
            asStat.append("0x${prevAddress.toString(16)}:  ")
            if (Opcode.from(opcode) == Opcode.R_TYPE) {
                asStat.appendLine(Functions.from(funct))
            } else {
                asStat.appendLine(Opcode.from(opcode))
            }
            asStat.appendLine(architecturalState)
            println(asStat.toString())
            asStat.clear()
        }
    }
}

fun mux(signal: Boolean?, input1: Int, input2: Int): Int {
    return if (signal == true) input1 else input2
}
