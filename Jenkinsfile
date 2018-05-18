pipeline {
    agent any
    stages {
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
