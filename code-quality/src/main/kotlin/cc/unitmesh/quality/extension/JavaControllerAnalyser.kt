package cc.unitmesh.quality.extension

import cc.unitmesh.quality.QualityAnalyser
import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.webapi.WebApiRuleSetProvider
import org.archguard.linter.rule.webapi.WebApiRuleVisitor
import org.archguard.rule.core.Issue
import org.archguard.scanner.analyser.backend.JavaApiAnalyser

class JavaControllerAnalyser(thresholds: Map<String, Int> = mapOf()) : QualityAnalyser {
    private val webApiRuleSetProvider = WebApiRuleSetProvider()

    override fun analysis(nodes: List<CodeDataStruct>): List<Issue> {
        val apiAnalyser = JavaApiAnalyser()

        nodes.forEach { data ->
            apiAnalyser.analysisByNode(data, "")
        }
        val services = apiAnalyser.toContainerServices()
        return WebApiRuleVisitor(services).visitor(listOf(webApiRuleSetProvider.get()))
    }
}