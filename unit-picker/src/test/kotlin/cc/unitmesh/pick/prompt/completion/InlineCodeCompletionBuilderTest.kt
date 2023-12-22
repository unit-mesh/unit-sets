package cc.unitmesh.pick.prompt.completion;

import cc.unitmesh.pick.builder.BuilderConfig
import cc.unitmesh.pick.builder.InstructionFileJob
import cc.unitmesh.core.completion.CodeCompletionIns
import cc.unitmesh.core.completion.CompletionBuilderType
import cc.unitmesh.pick.worker.JobContext
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodePosition
import io.kotest.matchers.shouldBe
import org.archguard.scanner.analyser.count.FileJob
import org.junit.jupiter.api.Test

class InlineCodeCompletionBuilderTest {
    @Test
    fun should_return_list_of_code_completion_ins() {
        // Given
        val codeFunction = CodeFunction(
            Position = CodePosition(1, 0, 3)
        )
        val codeLines = """
            fun myFunction() {
                println("Hello, world!")
            }
            // other code here
        """.trimIndent().lines()
        val job = InstructionFileJob(
            fileSummary = FileJob(),
            codeLines = codeLines,
            code = codeLines.joinToString("\n")
        )
        val jobContext = JobContext(job, emptyList(), hashMapOf("" to job), BuilderConfig(), emptyList(), 3)
        val builder = InlineCodeCompletionBuilder(jobContext)

        // When
        val result = builder.build(codeFunction)

        // Then
        result.size shouldBe 1
        result shouldBe listOf(
            CodeCompletionIns(
                beforeCursor = "println(\"",
                afterCursor = "Hello, world!\")",
                completionBuilderType = CompletionBuilderType.INLINE_COMPLETION
            )
        )
    }
}
