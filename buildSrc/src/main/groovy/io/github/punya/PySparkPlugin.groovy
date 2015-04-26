package io.github.punya

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

class PySparkPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.apply([plugin: "java"])
        project.apply([plugin: "io.github.punya.miniconda"])
        project.apply([plugin: "io.github.punya.spark"])

        project.afterEvaluate { p ->
            def minicondaDir = p.extensions.getByName("miniconda").dir
            def sparkDir = p.extensions.getByName("spark").dir
            def pyegg = p.task("pyegg", type: Exec) {
                doFirst { file("dist").deleteDir() }
                outputs.dir "dist"
                executable "$minicondaDir/bin/python"
                args "setup.py", "bdist_egg"
            }

            def jar = project.tasks.getByName("jar")

            def pytest = p.task("pytest", dependsOn: [jar, pyegg]) << {
                def pathSeparator = System.getProperty("path.separator")
                def classpath = ([] + p.configurations.compile + jar.outputs.files).join(pathSeparator)
                def eggs = (pyegg.outputs.files.singleFile.listFiles() as List<String>).join(pathSeparator)

                p.exec {
                    environment "PYSPARK_DRIVER_PYTHON", "$minicondaDir/bin/py.test"
                    environment "PYSPARK_DRIVER_PYTHON_OPTS", "artichoke tests --doctest-modules --verbose --color=yes"
                    environment "PYSPARK_PYTHON", "$minicondaDir/bin/python"
                    environment "SPARK_CONF_DIR", "${p.projectDir}/conf"
                    executable "$sparkDir/bin/pyspark"
                    args "--jars", classpath
                    args "--driver-class-path", classpath
                    args "--py-files", eggs
                }
            }

            p.tasks.getByName("test").dependsOn pytest

            def pylint = p.task("pylint", type: Exec) {
                commandLine "$minicondaDir/bin/pylint", "artichoke", "--output-format=colorized", "--reports=n"
            }

            p.tasks.getByName("check").dependsOn pylint
        }
    }
}

