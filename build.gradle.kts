plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.appdistribution) apply false
}

// Global detekt config
detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom(files("$projectDir/detekt.yml"))
}

tasks.register<Copy>("installGitHook") {
    description = "Installs the pre-commit git hook"
    group = "help"
    from(File(rootProject.rootDir, ".githooks/pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks"))
    fileMode = 0b111101101 // chmod 755
}

