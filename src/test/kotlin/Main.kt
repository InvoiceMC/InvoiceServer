import org.invoice.config.ConfigManager

fun main() {
    val manager = ConfigManager("src/test/resources/test.toml")
    val structured = manager.getConfig<TestConfig>()
    val unstructured = manager.getConfig()

    println(structured.name)
    println(unstructured)
}