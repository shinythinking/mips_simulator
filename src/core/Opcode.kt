package core;

enum class Opcode(val code: Int) {
    R_TYPE(0x00),
    JUMP(0x02),
    JAL(0x03),
    BEQ(0x04),
    BNE(0x05),
    ADDI(0x08),
    ADDIU(0x09),
    SLTI(0x0a),
    SLTIU(0x0b),
    ANDI(0x0c),
    ORI(0x0d),
    LUI(0x0f),
    LW(0x23),
    LBU(0x24),
    LBHU(0x25),
    SB(0x28),
    SHW(0x29),
    SW(0x2B);

    companion object {
        fun from(code: Int): Opcode {
            return entries.find { it.code == code } ?: throw IllegalArgumentException("알 수 없는 명령어입니다. code=$code")
        }
    }
}