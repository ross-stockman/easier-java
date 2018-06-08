pipeline {
    agent any
    def userInput = input(
    	id: 'Repo', message: 'repo name?', ok: 'generate', parameters: [string(defaultValue: '', description: '', name: 'repo_name', trim: true)]
    )
    echo (userInput)
    stages {
        stage ('Compile Stage') {
            steps {
                withMaven(maven : 'maven_local') {
                	sh 'echo ${env.repo_name}'
                    sh 'mvn clean compile'
                }
            }
        }
        stage ('Test Stage') {
            steps {
                withMaven(maven : 'maven_local') {
                    sh 'mvn test'
                }
            }
        }
    }
}
