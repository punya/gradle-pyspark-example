package io.github.punya

import org.gradle.api.Plugin
import org.gradle.api.Project

class ProvidedPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.configurations {
            provided
        }
        project.afterEvaluate { p ->
            p.sourceSets {
                main.compileClasspath += p.configurations.provided
                test.compileClasspath += p.configurations.provided
                test.runtimeClasspath += p.configurations.provided
            }
        }
    }
}
