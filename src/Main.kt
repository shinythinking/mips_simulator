import core.ArchitecturalState
import simulator.Simulator
import java.io.File
import java.io.FileNotFoundException

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("오류: 실행 시 파일 이름을 인수로 전달해주세요.")
        return
    }

    val filename = args[0]

    val codes = readFile(filename)

    val architecturalState = ArchitecturalState(codes)

    val simulator = Simulator()
    simulator(architecturalState)
}

fun readFile(filename: String): ByteArray {
    try {
        val file = File(filename)
        val bytes = file.readBytes()

        return bytes

    } catch (fileNotFound: FileNotFoundException) {
        throw FileNotFoundException("File not found: $filename: ${fileNotFound.message}")
    }
}