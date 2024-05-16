pipeline {
    agent any;

    environment {
        SONAR_TOKEN= credentials('JENKINS_SONAR_TOKEN_USER')
        CONFIG_FILE = 'automation/generic_config.yaml'  
        ENV_VARS_FILE = 'automation/environment_vars.yaml'
        GITHUB_REPO = "MohamedHamdy404/devops"
       // currentBuildCommitHash = "55555"

        def details = """ <h1>Jenkins Job Output </h1>
			<p> Build Status:   ${currentBuild.currentResult} </p>
			<p> Jenkins Job Name:   [ ${env.JOB_NAME} ] </p> 
			<p> BUILD_NUMBER:   [ ${env.BUILD_NUMBER} ] </p>
			<p> Jenkins Job Console Log:   <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>
			<p> New Tag Version: [${env.TAG}] </p>
			"""
    }

    stages {
        /*
        stage("checkout") {
            steps {
                git url: "https://github.com/${GITHUB_REPO}.git", branch: 'develop'
                sh 'cat flag.txt'
            }
        }
        /*
        stage("tagging") {
            steps {
                script {

					sh "git config --global --add safe.directory ${env.WORKSPACE}"
                    sh 'echo "autotag started"'
                    pipelineScripts = load "automation/tag.groovy"
					pipelineScripts.AutoTag()
                    
                    sh 'echo ${TAG}'
                }
            }
        }
        */
            
        stage('Load Environment Variables') {
            steps {
                // Load environment-specific variables
                script {
                    def envVars = readYaml(file: ENV_VARS_FILE)
                    envVars.each { microservice, values ->
                        values.each { key, value ->
                            env."${microservice}_${key}" = value
                        }
                    }
                }
            }
        }
        stage('Load config Variables') {
            steps {
                // Load environment-specific variables
                script {
                    def configVar = readYaml(file: CONFIG_FILE)
                    configVar.each { conf, values ->
                        values.each { key, value ->
                            env."${conf}_${key}" = value
                        }
                    }
                env.SERVICE_NAME = "${env.pipelinedemo_SERVICE_NAME}"
                env.MY_BRANCH = "${env.develop_MY_BRANCH}"
                PROJECT_KEY= "${env.SERVICE_NAME}-${env.MY_BRANCH}"
                }
            }
        }

            stage('Use Environment Variables') {
            steps {
                // Now you can access the environment variables in your pipeline
                //${env.microservice_1_JENKINS_SERVER_URL}
                
                echo "Project URL for microservice_1: ${env.develop_NAMESPACE}"

                
                echo "Micro_1 Project Key for microservice_1: ${env.pipelinedemo_REPOSITORY}"


                echo "Micro_1 Project Key : ${env.PROJECT_KEY}"
                //echo "Jenkins server URL for microservice_2: ${env.microservice_2_JENKINS_SERVER_URL}"
            }
        }    

        stage("TEST") {
            steps {
                script {
                    
                    sh "git config --global --add safe.directory ${env.WORKSPACE}"
                    
                    def currentBuildCommitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    echo "Commit hash of the current build: ${currentBuildCommitHash}"
                    // Fetch the previous build information
                    def previousBuildInfo = currentBuild.rawBuild.getPreviousSuccessfulBuild()
                    if (previousBuildInfo) {
                        // Extract the commit hash from the previous build
                        def previousBuildCommitHash = previousBuildInfo.actions.find { it instanceof hudson.plugins.git.util.BuildData }.lastBuiltRevision.sha1String
                        echo "Commit hash of the previous successful build: ${previousBuildCommitHash}"
                        
                        // Get the commit hash of the current build

                        
                        // Compare the commit hashes
                        if (currentBuildCommitHash == previousBuildCommitHash) {
                            echo "The commit hashes match. No changes since the previous successful build."
                            return params.SKIP_NEXT_STAGES == 'true'
                        } else {
                            echo "The commit hashes are different. Changes detected since the previous successful build."
                        }
                    } else {
                        echo "No previous successful build found."
                    }
                    

                    //env.PROJECT_URL = envi.services.service[1].PROJECT_UR
                    //echo "Jenkins server URL for microservice_2: ${env.jenkins_server_url}"
                   
                    
                }
            }
        }

        stage('publish report template'){
			steps{
				script{
                    if(params.SKIP_NEXT_STAGES == 'true')
                    {
                        return
                    }else {
                    sh "echo 'stage to publish report'"
                    sh "echo 'stage to publish report'"
                    sh "echo 'stage to publish report'"
                    sh "echo 'stage to publish report'"
                    }
                    /*
                    details = """ <h1>Jenkins Job Output </h1>
                    <p> Build Status:   ${currentBuild.currentResult} </p>
                    <p> Jenkins Job Name:   [ ${env.JOB_NAME} ] </p> 
                    <p> BUILD_NUMBER:   [ ${env.BUILD_NUMBER} ] </p>
                    <p> Jenkins Job Console Log:   <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>
                    <p> New Tag Version: [${env.TAG}] </p>
                    """
                    */
                }
			}
		}
    }
    
    post{
		always {

			writeFile (file: 'template.html', text: details )
			archiveArtifacts artifacts: 'template.html'	
            script{	
                try{
                    currentBuild.description = "Generated Version: s"
                // junit 'target/**/*.xml'
                }catch (Exception e){
                    echo "An exception occurred: ${e.message}"
                }
            }
        }
		
        
    }
}