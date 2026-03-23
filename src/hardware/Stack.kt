package hardware

class Stack(
    private val baseAddress: Int = 0x1000000,
    private val size: Int = 4_000_000
) {
    private val data = IntArray(size / 4)
    private val minAddress = baseAddress - size

    fun store(
        address: Int,
        word: Int,
    ) {
        require(address % 4 == 0) {
            "hardware.Stack Address Error: (0x${address.toString(16)})는 4의 배수여야 합니다."
        }
        require(address in minAddress until baseAddress) {
            "hardware.Stack Overflow/Underflow: (0x${address.toString(16)})가 스택 범위를 벗어났습니다."
        }

        val index = (address - minAddress) / 4
        data[index] = word
    }

    fun load(address: Int): Int {
        require(address % 4 == 0) {
            "hardware.Stack Address Error: (0x${address.toString(16)})는 4의 배수여야 합니다."
        }
        require(address in minAddress until baseAddress) {
            "Segmentation Fault: (0x${address.toString(16)})가 스택 범위를 벗어났습니다."
        }

        val index = (address - minAddress) / 4
        return data[index]
    }
}