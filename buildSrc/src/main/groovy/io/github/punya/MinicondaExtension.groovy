package io.github.punya

import org.gradle.api.Project

class MinicondaExtension {
    String version = "3.10.1"
    String dir
    List<String> packages = []

    MinicondaExtension(Project project) {
        this.dir = "${project.buildDir}/python"

        project.afterEvaluate { p ->
            p.repositories {
                ivy {
                    url "http://repo.continuum.io"
                    layout "pattern", {
                        artifact "[organisation]/[module]-[revision]-[classifier].[ext]"
                    }
                }
            }
            p.configurations {
                minicondaInstaller
            }
            p.dependencies {
                def os = System.getProperty('os.name').replaceAll(' ', '')
                def arch = "x86_64"
                minicondaInstaller(group: "miniconda", name: "Miniconda", version: version) {
                    artifact {
                        name = "Miniconda"
                        type = "sh"
                        classifier = "$os-$arch"
                        extension = "sh"
                    }
                }
            }
            p.task("setupPython") {
                outputs.dir p.file(dir)
                doLast {
                    p.file(dir).deleteDir()
                    p.exec {
                        commandLine "bash", p.configurations.minicondaInstaller.singleFile,
                            "-b", "-p", p.file(dir)
                    }
                    project.exec {
                        executable "${p.file(dir)}/bin/conda"
                        args "install", "--yes", "--quiet"
                        args packages
                    }
                }
            }
            p.tasks.getByName("assemble").dependsOn "setupPython"
        }
    }
}
