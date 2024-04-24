def AutoTag() {
    stage('Auto Tag'){
        sh 'TAG IS STARTED '
        sh '${env.MY_BRANCH}'
        
    }
}
return this;