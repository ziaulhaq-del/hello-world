pipeline{
    
    agent any;

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
                git url: "https://github.com/${GITHUB_REPO}.git", branch: 'main'
            }
        }
    }

//test
}
