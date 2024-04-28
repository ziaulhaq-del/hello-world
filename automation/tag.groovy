def AutoTag() {
    stage('AutoTag'){

        if(MY_BRANCH != "main"){
        sh 'echo TAG IS STARTED '
        sh 'echo ${MY_BRANCH}'
        env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()      //Store Full tag
        env.STREAM = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f1', returnStdout: true).trim()       //Store Stream Name
        env.CURRENT_MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 ', returnStdout: true).trim() //Store Main Max 
        env.idf = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f3 ', returnStdout: true).trim() //Store branch idf
        env.LINE_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim()
        env.NEW_LINE_VERSION = ${LINE_VERSION}+1

        sh'echo we are at ${MY_BRANCH} stage'   //Print Stage
        sh 'echo ${STREAM_VERSION}'     //Print Latest Tag to the stage
        sh 'echo ${STREAM}'     //Print Stream Name
        sh 'echo ${CURRENT_MAX}'    //Print Current Main
        sh 'echo ${LINE_VERSION}'
        sh 'echo this is old ${MY_BRANCH} version ${LINE_VERSION}'      //Print old version
        sh 'echo ============'
        sh 'echo this is New ${MY_BRANCH} version ${NEW_LINE_VERSION}'
        sh 'echo New IDF'
        sh 'echo ${STREAM}-${CURRENT_MAX}-${idf}-${NEW_LINE_VERSION}'
        }else if(MY_BRANCH=="main"){
            sh 'echo this is main'

        }

    }
}
return this;