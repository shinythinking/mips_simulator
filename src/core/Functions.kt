package core;

enum class Functions(val funct: Int) {
    ADD(0x20), ADDU(0x21),
    AND(0x24), NOR(0x27), OR(0x25),
    JUMPR(0x08), JALR(0x09),
    SLT(0x2A), SLTU(0x2b),
    SLL(0x00), SRL(0x02),
    SUB(0x22), SUBU(0x23);

    companion object {
        fun from(funct: Int?) = entries.find { it.funct == funct }
            ?: throw IllegalArgumentException("알 수 없는 명령어입니다. funt = $funct")
    }
}
