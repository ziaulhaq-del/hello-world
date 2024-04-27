pipeline {
    agent any;

    environment {
        GITHUB_REPO = "MohamedHamdy404/devops"
        DOCKER_REGISTRY = ""
        DOCKER_IMAGE = ""
        imageTag = ""
        imageName = ""
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
                    sh "git config --global --add safe.directory ${env.WORKSPACE}"
                    env.MY_BRANCH= "develop"
                    env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()
                    sh 'echo ${STREAM_VERSION}'
                    sh 'echo "TAG IS STARTED" '
                    sh 'echo ${MY_BRANCH}'
                    
                }
            }
        }
    }
}