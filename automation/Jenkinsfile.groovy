pipeline{
    
    agent {
    kubernetes {
			// label 'build-pod'  // all your pods will be named with this prefix, followed by a unique id
			// idleMinutes 30  // how long the pod will live after no jobs have run on it
			 yamlFile 'automation/bin/build-pod-maven.yaml'  // path to the pod definition relative to the root of our project 
			// defaultContainer 'maven'  // define a default container if more than a few stages use it, will default to jnlp container
    	}
    }

    environment {
        
        GITHUB_REPO = "MohamedHamdy404/devops"
        DOCKER_REGISTRY = ""
        DOCKER_IMAGE = ""
        imageTag = ""
        imageName = ""
        
    }

    stages{

        stage ("checkout"){

            steps {
                git url: "https://github.com/${GITHUB_REPO}.git", branch: 'develop'
                sh 'cat flag.txt'
            }
        }
        stage ("tagging"){
            steps {
                container('maven') {
                    script{
                        sh ' echo "autotag started" '
                        sh "git config --global --add safe.directory ${env.WORKSPACE}"

                        pipelineScripts = load "automation/tagging.groovy"
                        pipelineScripts.AutoTag();
                    }
                }
            }
        }
    }

//test
}
