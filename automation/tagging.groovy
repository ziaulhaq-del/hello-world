def autoTag() {
    stage('Auto Tag'){
        switch(env.MY_BRANCH) {

            case develop:
                sh'echo we are at develop stage'
                env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()
                sh 'echo ${STREAM_VERSION}'

                env.STREAM = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f1', returnStdout: true).trim()
                sh 'echo ${STREAM}'

                env.MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 ', returnStdout: true).trim()
                sh 'echo ${MAX}'
                
                env.BETA_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim()
                sh 'echo this is old beta version ${BETA_VERSION}'
                
                env.NEW_BETA_VERSION = sh ( script: 'echo $((BETA_VERSION+1))', returnStdout: true).trim()
                sh 'echo echo this is new beta version ${NEW_BETA_VERSION}'

                env.TAG = sh ( script: 'echo "$STREAM-$MAX-beta-$NEW_BETA_VERSION"', returnStdout: true).trim()
                sh 'echo echo this is new tag version  ${TAG}'
                
            break
            case release:

        }
    }
    

}