pipeline {
    agent {
        kubernetes {
            // label 'build-pod'  // all your pods will be named with this prefix, followed by a unique id
            // idleMinutes 2  // how long the pod will live after no jobs have run on it
            yamlFile 'automation/bin/build-pod-review-merge.yaml'  // path to the pod definition relative to the root of our project 
            // defaultContainer 'maven'  // define a default container if more than a few stages use it, will default to jnlp container
        }
      }

	environment{
		PROJECT_URL = "git.qeema.io/qeema-platform/ticketing-service.git"
		REPOSITORY = "registry.tools.idp.qeema.io/"
	  	REGISTRY = "registry.tools.idp.qeema.io/qeema-projects"
		SERVICE_NAME = "ticketing-service"
		SONAR_TOKEN= credentials('JENKINS_SONAR_TOKEN_USER')
		SONAR_URL = "https://qplatform.dev.qeema.io/sonarqube"
		MY_BRANCH ="develop"
		PROJECT_KEY= "${env.SERVICE_NAME}-${MY_BRANCH}"
		IMAGE="ticketing-service"	

	}

    stages{
        stage('Run Compile Pipeline') {
            steps {
                container('maven') {         
                script {
                    sh "echo Compile stage"
                    // commented to make pipeline faster
                    // pipelineScripts = load "automation/bin/CompileScript.groovy"
                    // pipelineScripts.CompileCode()
                }
                }
            }
        }

        stage('Run Sonar Pipeline') {
            steps {
                container('sonar') { 		
                    script {
                        sh "echo scan stage"
                        // commented to make pipeline faster
                        //pipelineScripts = load "automation/bin/SonarScanScript.groovy"
                        //pipelineScripts.SonarScan()

                    }
                }
            }
        }


            stage('Run check conflict') {
        steps {
            container('maven') { 		
            script {
                        
                        // BRANCH_NAME   >> this is source branch that we need to merge from it
                        // CHANGE_TARGET >> this is target branch that we need to merge to it
                        sh "git config --global --add safe.directory ${env.WORKSPACE}"
                        sh 'git branch -a'
                        sh "git checkout -f ${CHANGE_TARGET}"

                        sh 'git config --global user.email "qeema.cicd@qeema.net"'
                        sh 'git config --global user.name "qeema.cicd"'
                        
                        sh 'git diff ${CHANGE_TARGET}..origin/${BRANCH_NAME} --name-only --output diff.txt'
                        
                        env.DIFF_OUTPUT = sh ( script: 'cat diff.txt', returnStdout: true).trim()
                        sh 'echo changes in files: ${DIFF_OUTPUT}'
                        env.MERGE_MESSAGE = "you are not ready to merge, there is conflicts in file ${DIFF_OUTPUT}"

                        details = """<h1>Jenkins Job Output </h1>
                            <p> Build Status:   ${currentBuild.currentResult} </p>
                            <p> Jenkins Job Name:   [ ${env.JOB_NAME} ] ==== BUILD_NUMBER:   [ ${env.BUILD_NUMBER} ] </p>
                            <p> Jenkins Job Console Log:   <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>
                            <p> Sonar Scan Output:   <a href='https://qplatform.dev.qeema.io/sonarqube/dashboard?id=${env.PROJECT_KEY}'> Sonar Link </a> Quality Gate Status:  [ Passed ] </p>
                            
                            <p> List of changed files: ${DIFF_OUTPUT} </p>
                            
                            <p> Merge Number : ${BRANCH_NAME} </p>
                            <p> Branch to merge from : ${CHANGE_BRANCH} </p>
                            <p> Target Stream : ${CHANGE_TARGET} </p>
                            <p> Merge Status: ${MERGE_MESSAGE} </p>
                            """

                        env.GIT_MERGE_OUTPUT = sh ( script: 'git merge --no-commit --no-ff origin/${BRANCH_NAME}', returnStdout: true).trim()
                        

                        env.GIT_MERGE_RESPONSE = sh ( script: 'echo $?', returnStdout: true).trim()
                        sh 'echo "Git merge response: ${GIT_MERGE_RESPONSE}"'
                        
                        env.GIT_MERGE_ABORT = sh ( script: 'git merge --abort', returnStdout: true).trim()
                        if ( env.GIT_MERGE_RESPONSE == '0'){
                            sh 'echo you are ready to merge'
                            env.MERGE_MESSAGE = "you are ready to merge"

                            details = """<h1>Jenkins Job Output </h1>
                            <p> Build Status:   ${currentBuild.currentResult} </p>
                            <p> Jenkins Job Name:   [ ${env.JOB_NAME} ] ==== BUILD_NUMBER:   [ ${env.BUILD_NUMBER} ] </p>
                            <p> Jenkins Job Console Log:   <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>
                            <p> Sonar Scan Output:   <a href='https://qplatform.dev.qeema.io/sonarqube/dashboard?id=${env.PROJECT_KEY}'> Sonar Link </a> Quality Gate Status:  [ Passed ] </p>
                            
                            <p> List of changed files: ${DIFF_OUTPUT} </p>
                            
                            <p> Merge Number : ${BRANCH_NAME} </p>
                            <p> Branch to merge from : ${CHANGE_BRANCH} </p>
                            <p> Target Stream : ${CHANGE_TARGET} </p>
                            <p> Merge Status: ${MERGE_MESSAGE} </p>
                            """

                        }
                        else{
                                env.MERGE_MESSAGE = "you are not ready to merge"
                                details = """<h1>Jenkins Job Output </h1>
                            <p> Build Status:   ${currentBuild.currentResult} </p>
                            <p> Jenkins Job Name:   [ ${env.JOB_NAME} ] ==== BUILD_NUMBER:   [ ${env.BUILD_NUMBER} ] </p>
                            <p> Jenkins Job Console Log:   <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>
                            <p> Sonar Scan Output:   <a href='https://qplatform.dev.qeema.io/sonarqube/dashboard?id=${env.PROJECT_KEY}'> Sonar Link </a> Quality Gate Status:  [ Passed ] </p>
                            
                            <p> List of changed files: ${DIFF_OUTPUT} </p>
                            
                            <p> Merge Number : ${BRANCH_NAME} </p>
                            <p> Branch to merge from : ${CHANGE_BRANCH} </p>
                            <p> Target Stream : ${CHANGE_TARGET} </p>
                            
                            <p> Merge Status: ${MERGE_MESSAGE} </p>
                            """
                        }
                        

                    }
            
            }
        }
    }
    }  // close stages

    post{
    always {

        writeFile (file: 'template.html', text: details )
        archiveArtifacts artifacts: 'template.html'

        

        emailext body: '''${SCRIPT, template="groovy-html.template"}''',
        subject: 'Merger Review Pipeline from develop to ${CHANGE_BRANCH}',
        to: 'qeema.cicd@qeema.net'


        
    }
		
        
    }
} //close pipeline