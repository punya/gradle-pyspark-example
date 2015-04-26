package io.github.punya

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class SparkPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create("spark", SparkExtension, project)
    }
}

