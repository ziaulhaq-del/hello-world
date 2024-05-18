def getChanges() {
    script{
     def currentBuildCommitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        echo "Commit hash of the current build: ${currentBuildCommitHash}"
        // Fetch the previous build information
        def previousBuildInfo = currentBuild.rawBuild.getPreviousSuccessfulBuild()
        if (previousBuildInfo) {
            // Extract the commit hash from the previous build
            def previousBuildCommitHash = previousBuildInfo.actions.find { it instanceof hudson.plugins.git.util.BuildData }.lastBuiltRevision.sha1String
            echo "Commit hash of the previous successful build: ${previousBuildCommitHash}"
            
            // Get the commit hash of the current build

            
            // Compare the commit hashes
            if (currentBuildCommitHash == previousBuildCommitHash) {
                echo "The commit hashes match. No changes since the previous successful build."
                env.flag = "red"
            } else {
                echo "The commit hashes are different. Changes detected since the previous successful build."
            }
        } else {
            echo "No previous successful build found."
        }
                    
    }
}
return this;