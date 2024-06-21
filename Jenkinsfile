pipeline {
  agent any
  stages {
    stage('dev') {
      steps {
        echo 'Hello this is message 1'
      }
    }

    stage('Prod') {
      agent any
      steps {
        echo 'This is message 2'
        echo 'Hu'
      }
    }

  }
}