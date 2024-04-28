def AutoTag() {
    stage('AutoTag'){
        
        if(env.MY_BRANCH != "main"){
            sh 'echo AutoTag IS STARTED'
            sh 'echo we are in ${MY_BRANCH} condition'
            env.STREAM_VERSION = sh ( script: 'git describe --abbrev=0 --tags --match=$MY_BRANCH*', returnStdout: true).trim()      //Store Full tag
            env.STREAM = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f1', returnStdout: true).trim()       //Store Stream Name
            env.CURRENT_MAX = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f2 ', returnStdout: true).trim() //Store Main Max 
            env.idf = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f3 ', returnStdout: true).trim() //Store branch idf (beta || rc || pp)
            env.LINE_VERSION = sh ( script: 'echo $STREAM_VERSION | cut -d "-" -f4', returnStdout: true).trim() //Store current Line Version
            env.NEW_LINE_VERSION = sh ( script: 'echo $((LINE_VERSION+1))', returnStdout: true).trim()  //Store New Line Version
            env.TAG = sh ( script: 'echo "$STREAM-$CURRENT_MAX-${idf}-$NEW_LINE_VERSION"', returnStdout: true).trim()   //Generates New Tag
            sh'echo we are at ${MY_BRANCH} stage'   //Print Stage
            sh 'echo ${STREAM_VERSION}'     //Print Latest Tag to the stage
            sh 'echo ${STREAM}'     //Print Stream Name
            sh 'echo ${CURRENT_MAX}'    //Print Current Main
            sh 'echo this is old ${MY_BRANCH} version ${LINE_VERSION}'      //Print old version
            sh 'echo this is New ${MY_BRANCH} version ${NEW_LINE_VERSION}'  //Print new version
            sh 'echo this is new tag version ${TAG}'    //Print New Tag
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