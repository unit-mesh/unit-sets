package cc.unitmesh.pick.threshold

import cc.unitmesh.core.SupportedLang
import cc.unitmesh.pick.worker.WorkerContext
import cc.unitmesh.pick.worker.job.InstructionFileJob
import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.Encoding
import com.knuddels.jtokkit.api.EncodingRegistry
import com.knuddels.jtokkit.api.EncodingType
import org.archguard.scanner.analyser.count.LanguageService
import org.slf4j.Logger

class ThresholdChecker(private val context: WorkerContext) {
    private var registry: EncodingRegistry = Encodings.newDefaultEncodingRegistry()
    private var enc: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)

    private val language: LanguageService = LanguageService()

    private val supportedExtensions: Set<String> = setOf(
        language.getExtension(SupportedLang.JAVA.name.lowercase()),
    )

    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ThresholdChecker::class.java)

    fun isMetThreshold(job: InstructionFileJob): Boolean {
        val summary = job.fileSummary
        if (!supportedExtensions.contains(summary.extension)) {
            return false
        }

        if (summary.complexity > context.qualityThreshold.complexity) {
            logger.info("skip file ${summary.location} for complexity ${summary.complexity}")
            return false
        }

        // like js minified file
        if (summary.binary || summary.generated || summary.minified) {
            return false
        }

        // if the file size is too large, we just try 64k
        if (summary.bytes > context.qualityThreshold.fileSize) {
            logger.info("skip file ${summary.location} for size ${summary.bytes}")
            return false
        }

        // limit by token length
        val encoded = enc.encode(job.code)
        val length = encoded.size
        if (length > context.qualityThreshold.maxTokenLength) {
            logger.info("skip file ${summary.location} for over ${context.qualityThreshold.maxTokenLength} tokens")
            println("| filename: ${summary.filename} |  tokens: $length | complexity: ${summary.complexity} | code: ${summary.lines} | size: ${summary.bytes} | location: ${summary.location} |")
            return false
        }

        return true
    }
}