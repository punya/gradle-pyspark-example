package io.github.punya

import org.gradle.api.Project
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.Copy

class SparkExtension {
    String version = "1.3.1"
    String dir

    SparkExtension(Project project) {
        this.dir = "${project.buildDir}/spark"

        project.afterEvaluate { p ->
            p.repositories {
                ivy {
                    url "http://mirrors.ibiblio.org"
                    layout "pattern", {
                        artifact "[organisation]/[module]-[revision]/[module]-[revision]-[classifier].[ext]"
                        m2compatible = true
                    }
                }
            }
            p.configurations {
                sparkDistribution
            }
            p.dependencies {
                sparkDistribution(group: "apache.spark", name: "spark", version: "1.3.1") {
                    artifact {
                        name = "spark"
                        type = "tgz"
                        classifier = "bin-hadoop2.4"
                        extension = "tgz"
                    }
                }
            }
            p.task("setupSpark", type: Copy) {
                from p.tarTree(p.configurations.sparkDistribution.singleFile)
                into dir
                eachFile { it.relativePath = stripParent(it.relativePath) }
            }
        }
    }

    private static RelativePath stripParent(RelativePath p) {
        return new RelativePath(p.endsWithFile, p.segments[1..-1] as String[])
    }
}

