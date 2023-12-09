# UnitEval

<p align="center">
  <a href="https://github.com/unit-mesh/unit-eval/actions/workflows/build.yml">
    <img src="https://github.com/unit-mesh/unit-eval/actions/workflows/build.yml/badge.svg" alt="CI/CD" />
  </a>
  <a href="https://github.com/unit-mesh/chocolate-factory">
    <img src="https://img.shields.io/badge/powered_by-chocolate_factory-blue?logo=kotlin&logoColor=fff" alt="Powered By" />
  </a>
  <a href="https://central.sonatype.com/artifact/cc.unitmesh/unit-picker">
    <img src="https://img.shields.io/maven-central/v/cc.unitmesh/unit-picker"  alt="Maven"/>
  </a>
</p>

> Leveraging LLM benchmark/evaluation tools with fine-tuning data engineering specifically tailored for Unit Mesh tools
> such as AutoDev, Studio B3, and DevOps Genius. The brilliance lies in the UnitEval functionality, which automatically
> selects and evaluates code within real projects.

This repository designs evaluate the code quality of the AI code, which will use
with [AutoDev](https://github.com/unit-mesh/auto-dev) IDE plugins.
We use [Chocolate Factory](https://github.com/unit-mesh/chocolate-factory) to build the prompt engine.

Related issue in Unit Mesh: Chocolate Factory [#9](https://github.com/unit-mesh/chocolate-factory/issues/9) and
AutoDev [#54](https://github.com/unit-mesh/auto-dev/issues/56)

## Usage

for examples, see: [examples](examples/) folder

### use CLI

see in [config-examples](examples/config-examples/)

download the latest version from [GitHub Release](https://github.com/unit-mesh/unit-eval/releases)

#### Step 1. Generate Instructions

1. config project by `processor.yml`
2. run picker: `java -jar unit-cli.jar`

#### Step 2. run Evaluate CLI (Optional)

1.config the `unit-eval.yml` file and `connection.yml`
2.run eval: `java -jar unit-eval.jar`

Connection config: [https://framework.unitmesh.cc/prompt-script/connection-config](https://framework.unitmesh.cc/prompt-script/connection-config)

### use Java API

see in [config-example](examples/project-example/)

1.add dependency

```groovy
dependencies {
    implementation("cc.unitmesh:unit-picker:0.1.2")
    implementation("cc.unitmesh:code-quality:0.1.2")
}
```

2.config the `unit-eval.yml` file and `connection.yml`

3.write code
```java
public class App {
  public static void main(String[] args) {
    List<InstructionType> builderTypes = new ArrayList<>();
    builderTypes.add(InstructionType.RELATED_CODE_COMPLETION);

    List<CodeQualityType> codeQualityTypes = new ArrayList<>();
    codeQualityTypes.add(CodeQualityType.BadSmell);
    codeQualityTypes.add(CodeQualityType.JavaService);

    PickerOption pickerOption = new PickerOption(
            "https://github.com/unit-mesh/unit-eval-testing", "master", "java",
            ".", builderTypes, codeQualityTypes, new BuilderConfig()
    );

    SimpleCodePicker simpleCodePicker = new SimpleCodePicker(pickerOption);
    List<Instruction> output = simpleCodePicker.blockingExecute();

    // handle output in here
  }
} 
```


## How it works?

![Unit Eval Overview](https://unitmesh.cc/uniteval/overview.png)

## Module

### Code Quality

- [x] Test BadSmell from [Coca](https://github.com/phodal/coca)
- [ ] BadSmell from [Coca](https://github.com/phodal/coca)
    - [x] Long Parameters, Long Method, LARGE_CLASS
    - [ ] Todos: Complex-if, Switch logic
- [x] Estimation from [ArchGuard](https://github.com/archguard/archguard)
- [ ] MVC architecture Governance: [Rule Linter](https://archguard.org/governance)
    - [x] Controller -> ArchGuard [HTTP API Rule](https://archguard.org/governance/web-api)
    - [x] Service -> Service Rule + Bad Smell
    - [x] Repository -> ArchGuard [SQL Rule](https://archguard.org/governance/sql)
    - [ ] Model -> Model Rule
    - [ ] Exception -> Exception Rule
    - [ ] Security -> Security Rule

### Code Picker

> Code Picker is a tool that can pick the code from the real project, will generate the code unit, and then it will be
> the datasets to Fine-tuning the model.

![Code Quality Workflow](https://unitmesh.cc/uniteval/code-quality-workflow.png)

- [ ] Basic Instruction
    - [x] Related Code builder
    - [ ] InBlock Code builder
    - [ ] AfterBlock Code builder
- [x] Filter by Code Quality
- [x] AST parser by [Chapi](https://github.com/phodal/chapi)
- [ ] by History analysis for incremental learning
- [ ] Language support by [Chapi](https://github.com/phodal/chapi)
    - [x] Java
    - [ ] Kotlin
    - [ ] TypeScript/JavaScript
    - [ ] Python
    - [ ] Golang

### Code Eval

> Code Eval is a tool that can evaluate the code quality of the AI code.

- [x] Code eval
    - [x] EvalScript based on PromptScript
- [x] Auto LLM Test by Chocolate Factory
- [ ] Eval Results
- Language support by TreeSitter
    - spike C/C++ ?

## LICENSE

This code is distributed under the MPL 2.0 license. See `LICENSE` in this directory.
