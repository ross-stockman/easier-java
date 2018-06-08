pipeline {
    agent any
    stages {
    stage 'promotion' 
def userInput = input(
 id: 'userInput', message: 'Let\'s promote?', parameters: [
 [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env']
])
echo ("Env: "+userInput)
        stage ('Compile Stage') {
            steps {
                withMaven(maven : 'maven_local') {
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
