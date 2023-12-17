package cc.unitmesh.pick.builder

import kotlinx.serialization.Serializable


@Serializable
data class BuilderConfig(
    /**
     * For different generic data in [cc.unitmesh.pick.prompt.CodeStrategyBuilder]
     */
    val withGenPureData: Boolean = true,
    val mergeFinalOutput: Boolean = true,
)