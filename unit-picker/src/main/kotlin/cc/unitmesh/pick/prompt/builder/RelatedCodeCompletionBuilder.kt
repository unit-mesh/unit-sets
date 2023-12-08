package cc.unitmesh.pick.prompt.builder

import cc.unitmesh.pick.ext.toUml
import cc.unitmesh.pick.prompt.Instruction
import cc.unitmesh.pick.prompt.InstructionBuilder
import cc.unitmesh.pick.prompt.InstructionContext
import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable

@Serializable
data class RelatedCodeCompletionIns(
    val language: String,
    val beforeCursor: String,
    val relatedCode: String,
    val output: String,
)

class RelatedCodeCompletionBuilder(private val context: InstructionContext) :
    InstructionBuilder<RelatedCodeCompletionIns> {

    override fun convert(): List<RelatedCodeCompletionIns> {
        val language = context.job.fileSummary.language.lowercase()
        val container = context.job.container ?: return emptyList()

        // 1. collection all related data structure by imports if exists in a file tree
        val relatedDataStructure = container.Imports.mapNotNull {
            context.fileTree[it.Source]?.container?.DataStructures
        }.flatten()

        // 2. convert all related data structure to uml
        val relatedCode = relatedDataStructure.joinToString("\n", transform = CodeDataStruct::toUml)

        return container.DataStructures.map { ds ->
            ds.Functions.map {
                val position = it.Position
                val beforeCursor = context.job.codeLines.subList(0, position.StartLine).joinToString("\n")

                val stopLine = if (position.StopLine == 0) {
                    context.job.codeLines.size
                } else {
                    position.StopLine
                }

                val afterCursor = context.job.codeLines.subList(position.StartLine, stopLine).joinToString("\n")


                RelatedCodeCompletionIns(
                    language = language,
                    beforeCursor = beforeCursor,
                    relatedCode = relatedCode,
                    output = afterCursor
                )
            }
        }.flatten()
    }

    override fun build(): List<Instruction> {
        return this.convert().map {
            Instruction(
                instruction = "Complete ${it.language} code, return rest code, no explaining",
                output = it.output,
                input = """
                |```${it.language}
                |${it.relatedCode}
                |```
                |
                |Code:
                |```${it.language}
                |${it.beforeCursor}
                |```""".trimMargin()
            )
        }
    }
}

