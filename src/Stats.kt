import core.Opcode
import hardware.ControlSignals

object Stats {
    private var totalInstructions: Int = 0
    private var rTypeCount: Int = 0
    private var iTypeCount: Int = 0
    private var jTypeCount: Int = 0
    private var memoryAccessCount: Int = 0
    private var totalBranches: Int = 0
    private var takenBranches: Int = 0

    fun recordCycle(opcodeInput: Int, controlSignal: ControlSignals, isBranchTaken: Boolean) {
        totalInstructions++

        val opcode = Opcode.from(opcodeInput)

        when (opcode) {
            Opcode.R_TYPE -> rTypeCount++
            Opcode.JUMP, Opcode.JAL -> jTypeCount++
            else -> iTypeCount++
        }

        if (controlSignal.memRead == true || controlSignal.memWrite == true) {
            memoryAccessCount++
        }

        if (controlSignal.branch == true) {
            totalBranches++
            if (isBranchTaken) {
                takenBranches++
            }
        }
    }

    fun printStats(result: Int) {
        println("\n===== Stats =====")
        println("Final return value: $result")
        println("Number of executed instructions: $totalInstructions")
        println("Number of R-type instructions: $rTypeCount")
        println("Number of I-type instructions: $iTypeCount")
        println("Number of J-type instructions: $jTypeCount")
        println("Number of memory access instructions: $memoryAccessCount")
        println("Number of branches (all branch instr.): $totalBranches")
        println("Number of effective taken branches (actual jumps): $takenBranches")
    }
}