pipeline{
    
    agent any;

    environment {
        
        GITHUB_REPO = ""
        DOCKER_REGISTRY = ""
        DOCKER_IMAGE = ""
        imageTag = ""
        imageName = ""
        
    }

    stages{

        stage ("checkout"){

            steps {
                git url: "https://github.com/$(GITHUB_REPO).git", branch: 'main'
            }
        }

        stage("Unit Test"){
            
            agent {
                label 'sonarQube'
            }
            
            steps{
                withSonarQubeEnv(){
                    
                    
                }
                
            }
        }
    }


}