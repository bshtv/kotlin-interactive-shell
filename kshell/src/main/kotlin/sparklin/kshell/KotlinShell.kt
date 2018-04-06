package sparklin.kshell

import com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import sparklin.kshell.configuration.CachedInstance
import sparklin.kshell.configuration.Configuration
import sparklin.kshell.configuration.ConfigurationImpl
import java.io.File

object KotlinShell {
    @JvmStatic
    fun main(args: Array<String>) {
        val messageCollector: MessageCollector = PrintingMessageCollector(System.out, MessageRenderer.WITHOUT_PATHS, false)
        val moduleName = "my-module"
        val additionalClasspath = listOf<File>()
        val classpath = Util.findJars(
                includeScriptEngine = false,
                includeKotlinCompiler = false,
                includeStdLib = true)

        val conf = Util.createCompilerConfiguration(classpath, additionalClasspath, moduleName, messageCollector)
        val baseClassloader = Util.baseClassloader(conf)
        val repl = KShell(Disposer.newDisposable(),
                configuration(), conf, messageCollector, classpath, baseClassloader)

        Runtime.getRuntime().addShutdownHook(Thread({
            println("\nBye!")
            repl.cleanUp()
        }))

        repl.doRun()
    }

    fun configuration(): Configuration {
        val instance = CachedInstance<Configuration>()
        val klassName: String? = System.getProperty("config.class")

        return if (klassName != null) {
            instance.load(klassName, Configuration::class)
        } else {
            instance.get { ConfigurationImpl() }
        }
    }
}