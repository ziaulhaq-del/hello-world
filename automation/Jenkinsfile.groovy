pipeline {
    agent any;

    environment {
        GITHUB_REPO = "MohamedHamdy404/devops"
        DOCKER_REGISTRY = ""
        DOCKER_IMAGE = ""
        imageTag = ""
        imageName = ""
        MY_BRANCH= "develop"
    }

    stages {
        stage("checkout") {
            steps {
                git url: "https://github.com/${GITHUB_REPO}.git", branch: 'develop'
                sh 'cat flag.txt'
            }
        }

        stage("tagging") {
            steps {
                script {
                    sh 'echo "autotag started"'
                    pipelineScripts = load "automation/tag.groovy"
					pipelineScripts.AutoTag()
                    
                }
            }
        }
    }
}