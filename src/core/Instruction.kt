package core

val Int.opcode: Int
    get() = (this ushr 26) and 0x3F

val Int.rs: Int
    get() = (this ushr 21) and 0x1F

val Int.rt: Int
    get() = (this ushr 16) and 0x1F

val Int.rd: Int
    get() = (this ushr 11) and 0x1F

val Int.shamt: Int
    get() = (this ushr 6) and 0x1F

val Int.funct: Int
    get() = this and 0x3F

val Int.immediate: Int
    get() = this and 0xFFFF

val Int.address: Int
    get() = this and 0x3FFFFFF