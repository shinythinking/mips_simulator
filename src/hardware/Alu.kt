package hardware

object Alu {
    operator fun invoke(
        d1: Int,
        d2: Int,
        aluOp: AluOperation
    ): AluResult {
        val result = when (aluOp) {
            AluOperation.ADD -> d1 + d2
            AluOperation.SUB -> d1 - d2
            AluOperation.AND -> d1 and d2
            AluOperation.NOR -> (d1 or d2).inv()
            AluOperation.OR -> d1 or d2
            AluOperation.SLT -> d1 - d2
            AluOperation.SLL -> d1 shl d2
            AluOperation.SRL -> d1 shr d2
            AluOperation.NONE -> 0
        }
        val isZero = result == 0
        return AluResult(result, isZero)
    }
}

data class AluResult(
    val result: Int,
    val isZero: Boolean,
)

enum class AluOperation {
    ADD, SUB,
    AND, NOR, OR,
    SLT,
    SLL, SRL,
    NONE;
}
