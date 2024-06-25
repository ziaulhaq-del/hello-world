pipeline {
    agent any

    tools {
        // Define the tools to be used
        jdk 'JAVA'  // Replace 'JDK 11' with the name of your configured JDK in Jenkins
        maven 'MAVEN'  // Replace 'Maven 3.8.1' with the name of your configured Maven in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from GitHub
                git url: 'https://github.com/PallawiDutta/hello-world.git', branch: 'master'
            }
        }
        stage('Build') {
            steps {
                // Run Maven build
                sh 'mvn clean install'
            }
        }
    }
}
