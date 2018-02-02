package sparklin.kshell

import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import java.io.File
import kotlin.script.templates.standard.ScriptTemplateWithArgs

object KotlinShell {
    @JvmStatic
    fun main(args: Array<String>) {
        val defs =  //StandardScriptDefinition
                KotlinScriptDefinitionEx(ScriptTemplateWithArgs::class,
                ScriptArgsWithTypes(EMPTY_SCRIPT_ARGS, EMPTY_SCRIPT_ARGS_TYPES),
                listOf(Shared::class.qualifiedName + ".*"))

        val repl = KShell(additionalClasspath=findClassJarsOrEmpty(sparklin.kshell.Shared::class),
                sharedHostClassLoader = this.javaClass.classLoader,
                scriptDefinition = defs//,
                /*shellHistory = hist*/)

        Runtime.getRuntime().addShutdownHook(Thread({
            println("\nBye!")
            repl.cleanUp()
        }))

        repl.doRun()
    }
}