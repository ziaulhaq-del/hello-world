pipeline {
    agent any;

    environment {

        PROJECT_KEY= "${env.pipelinedemo_SERVICE_NAME}-${env.develop_MY_BRANCH}"
        SONAR_TOKEN= credentials('JENKINS_SONAR_TOKEN_USER')
        CONFIG_FILE = 'automation/generic_config.yaml'  
        ENV_VARS_FILE = 'automation/environment_vars.yaml'
        

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
                git url: "https://github.com/${GITHUB_REPO}.git", branch: 'main'
                sh 'cat flag.txt'
            }
        }
        
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
                    sh 'echo "autotag started"'

                    sh ' echo "READING YAML"'
                    //sh ". ${ENV_VARS_FILE} && export \$(cut -d= -f1 ${ENV_VARS_FILE} | xargs)"
                    /*pipelineScripts = load "automation/tag.groovy"
                    pipelineScripts.AutoTag()
                    
                    sh 'echo ${TAG}'
                    */
                    sh "cat ${ENV_VARS_FILE}"
                    sh ' echo "LOADED YAML "'
                    //env.PROJECT_URL = envi.services.service[1].PROJECT_UR
                    //echo "Jenkins server URL for microservice_2: ${env.jenkins_server_url}"
                   
                    
                }
            }
        }

        /*stage('publish report template'){
			steps{
				script{
                    sh "echo 'stage to publish report'"
                    details = """ <h1>Jenkins Job Output </h1>
                    <p> Build Status:   ${currentBuild.currentResult} </p>
                    <p> Jenkins Job Name:   [ ${env.JOB_NAME} ] </p> 
                    <p> BUILD_NUMBER:   [ ${env.BUILD_NUMBER} ] </p>
                    <p> Jenkins Job Console Log:   <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>
                    <p> New Tag Version: [${env.TAG}] </p>
                    """
                }
			}
		}*/
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