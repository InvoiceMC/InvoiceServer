import org.invoice.config.ConfigManager

fun main() {
    val structured = ConfigManager("src/test/resources/test.toml").Structured<TestConfig>()
    val unstructured = ConfigManager("src/test/resources/test.toml").UnStructured()

    val unstructuredConfig = unstructured.getConfig()

    println(unstructuredConfig)
}