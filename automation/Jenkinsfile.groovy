pipeline {
    agent any;

    environment {
        GITHUB_REPO = "MohamedHamdy404/devops"
        DOCKER_REGISTRY = ""
        DOCKER_IMAGE = ""
        imageTag = ""
        imageName = ""
        MY_BRANCH = "release"
        SERVICE_NAME = "Demo-service"
        PROJECT_KEY= "${env.SERVICE_NAME}-${MY_BRANCH}"
        //CONFIG_FILE = 'path/to/generic_config.yaml'  
        ENV_VARS_FILE = 'automation/environment_vars.yaml'
        microservice_2 = ""
        microservice_1= ""    

        def envVars = readYaml(file: ENV_VARS_FILE)
            envVars.each { microservice, values ->
                values.each { key, value ->
                    env."${microservice}_${key}" = value
                }
            }

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
        /*    
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
        */
            stage('Use Environment Variables') {
            steps {
                // Now you can access the environment variables in your pipeline
                //${env.ticketing_JENKINS_SERVER_URL}
                
                echo "Jenkins server URL for microservice_1: ${env.ticketing_PROJECT_URL}"
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
                    sh "========================"
                    sh "echo ${env.ticketing_IMAGE}"
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