def AutoTag() {
    stage('AutoTag'){
        sh 'TAG IS STARTED '
        sh '${env.MY_BRANCH}'
        switch(env.MY_BRANCH) {                     // Main Switch is Required to be on top // Double Switch Cases is suggested // Using (IF else )
            // Auto Increment Stage Tag
            env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()      //Store Full tag
            STREAM = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f1', returnStdout: true).trim()       //Store Stream Name
            CURRENT_MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 ', returnStdout: true).trim() //Store Main Max 
            sh'echo we are at ${env.MY_BRANCH} stage'   //Print Stage
            sh 'echo ${STREAM_VERSION}'     //Print Latest Tag to the stage
            sh 'echo ${STREAM}'     //Print Stream Name
            sh 'echo ${CURRENT_MAX}'    //Print Main Max
            sh 'echo this is old ${env.MY_BRANCH} version ${LINE_VERSION}'      //Print old version
            // Switch cases based on branch name 
            case develop:
                // Develop Tag HINT= develop-1.0.0-dv-1
                LINE_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim() //Store Suffix No. for StreamLine
                NEW_LINE_VERSION = sh ( script: 'echo $((LINE_VERSION+1))', returnStdout: true).trim()      // Store New Suffix No. for StreamLine
                sh 'echo echo this is new beta version ${NEW_LINE_VERSION}'
                TAG = sh ( script: 'echo "$STREAM-$CURRENT_MAX-beta-$NEW_LINE_VERSION"', returnStdout: true).trim()     //Generates New Tag for Beta
                sh 'echo echo this is new tag version  ${TAG}'
            break
            case release:
                // Release Tag HINT= release-1.0.0-rc-1
                LINE_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim() //Store Suffix No. for StreamLine
                NEW_LINE_VERSION = sh ( script: 'echo $((LINE_VERSION+1))', returnStdout: true).trim()      // Store New Suffix No. for StreamLine
                sh 'echo echo this is new RC version ${NEW_LINE_VERSION}'
                TAG = sh ( script: 'echo "$STREAM-$CURRENT_MAX-rc-$NEW_LINE_VERSION"', returnStdout: true).trim()       //Generates New Tag for Release
                sh 'echo echo this is new tag version  ${TAG}'
            case preprod:
                // Pre-Production Tag HINT= preprod-1.0.0-pp-1
                LINE_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim() //Store Suffix No. for StreamLine
                NEW_LINE_VERSION = sh ( script: 'echo $((LINE_VERSION+1))', returnStdout: true).trim()      // Store New Suffix No. for StreamLine
                sh 'echo echo this is new PP version ${NEW_LINE_VERSION}'
                TAG = sh ( script: 'echo "$STREAM-$CURRENT_MAX-pp-$NEW_LINE_VERSION"', returnStdout: true).trim()       //Generates New Tag for Pre-Prod
                sh 'echo echo this is new tag version  ${TAG}'
            case main:
                // Main Tag HINT = 1.0.0
                sh 'echo we are in main condition'
                MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 | cut -d "." -f1', returnStdout: true).trim()
                sh 'echo ${MAX}'
                MAJOR = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 | cut -d "." -f2', returnStdout: true).trim()
                sh 'echo ${MAJOR}'
                MINOR = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 | cut -d "." -f3', returnStdout: true).trim()
                sh 'echo ${MINOR}'
                NEW_MAX = sh ( script: 'echo $((MAX+1))', returnStdout: true).trim()
                sh 'echo ${NEW_MAX}'
                TAG = sh ( script: 'echo "$STREAM-$NEW_MAX.$MAJOR.$MINOR"', returnStdout: true).trim()
                sh 'echo echo this is new tag version  ${TAG}'
        }
    }
}
return this;