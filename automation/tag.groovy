def AutoTag() {
    stage('AutoTag'){

        if(env.MY_BRANCH != "main"){
        sh 'echo TAG IS STARTED '
        sh 'echo ${MY_BRANCH}'
        env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()      //Store Full tag
        env.STREAM = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f1', returnStdout: true).trim()       //Store Stream Name
        env.CURRENT_MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 ', returnStdout: true).trim() //Store Main Max 
        env.idf = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f3 ', returnStdout: true).trim() //Store branch idf
        env.LINE_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim()
        env.NEW_LINE_VERSION = sh ( script: 'echo $((LINE_VERSION+1))', returnStdout: true).trim()
        env.TAG = sh ( script: 'echo "$STREAM-$CURRENT_MAX-${idf}-$NEW_LINE_VERSION"', returnStdout: true).trim()
        sh'echo we are at ${MY_BRANCH} stage'   //Print Stage
        sh 'echo ${STREAM_VERSION}'     //Print Latest Tag to the stage
        sh 'echo ${STREAM}'     //Print Stream Name
        sh 'echo ${CURRENT_MAX}'    //Print Current Main
        sh 'echo ${LINE_VERSION}'
        sh 'echo this is old ${MY_BRANCH} version ${LINE_VERSION}'      //Print old version
        sh 'echo ============'
        sh 'echo this is New ${MY_BRANCH} version ${NEW_LINE_VERSION}'
        sh 'echo New IDF'
        sh 'echo ${TAG}'
        }else if(MY_BRANCH=="main"){
            sh 'echo we are in main condition'
			env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()
			sh 'echo ${STREAM_VERSION}'

			env.STREAM = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f1', returnStdout: true).trim()
			sh 'echo ${STREAM}'

			env.MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 | cut -d "." -f1', returnStdout: true).trim()
			sh 'echo ${MAX}'
			env.MAJOR = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 | cut -d "." -f2', returnStdout: true).trim()
			sh 'echo ${MAJOR}'
			env.MINOR = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 | cut -d "." -f3', returnStdout: true).trim()
			sh 'echo ${MINOR}'


			env.NEW_MAX = sh ( script: 'echo $((MAX+1))', returnStdout: true).trim()
			sh 'echo ${NEW_MAX}'

			env.TAG = sh ( script: 'echo "$STREAM-$NEW_MAX.$MAJOR.$MINOR"', returnStdout: true).trim()
			sh 'echo echo this is new tag version  ${TAG}'

        }

    }
}
return this;