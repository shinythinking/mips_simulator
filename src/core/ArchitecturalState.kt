package core

import hardware.Registers
import hardware.Stack
import hardware.Text

class ArchitecturalState(codes: ByteArray) {
    var pc: Int = 0
        private set
    private val textSegment = Text(codes)
    private val stack = Stack()
    private val registers = Registers()
    private val changedRegister = mutableListOf<Int>()
    private val changedMemory = mutableMapOf<Int, Int>()

    fun getNextLine(): Int {
        val nextLine = textSegment.getLine(pc)
        return nextLine
    }

    fun movePc(addr: Int) {
        pc = addr
    }

    fun setRegister(register: Int, number: Int) {
        if (register == 0) return
        registers.setRegister(register, number)
        changedRegister.add(register)
    }

    fun getRegister(register: Int): Int {
        return registers.getRegisterFromOperand(register)
    }

    fun readMemory(addrress: Int): Int {
        return stack.load(addrress)
    }

    fun writeMemory(address: Int, data: Int) {
        changedMemory[address] = data
        stack.store(address, data)
    }

    fun showAll(): String {
        return buildString {
            appendLine("< PC >")
            appendLine("pc = 0x${pc.toString(16)}")

            appendLine("< Register >")
            appendLine(registers.showAll())

//            appendLine("< Memory >")
        }
    }

    override fun toString(): String {
        val ans = buildString {
            appendLine("< PC >")
            appendLine("pc = 0x${pc.toString(16)}")
            if (changedRegister.isNotEmpty()) {
                appendLine("< Register >")
                changedRegister.sorted().forEach {
                    appendLine("$it = 0x${registers.getRegisterFromOperand(it).toString(16)}")
                }
                changedRegister.clear()
            }
            if (changedMemory.isNotEmpty()) {
                appendLine("< Memory >")
                changedMemory.forEach { key, value ->
                    appendLine("0x${key.toString(16)} <- $value")
                }
                changedMemory.clear()
            }
        }

        return ans
    }
}