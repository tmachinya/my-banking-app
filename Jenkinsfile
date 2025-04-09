pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials') // We will add this in Jenkins
    }

    stages {
        stage('Clone repository') {
            steps {
                git url: 'https://github.com/tmachinya/my-banking-app.git', credentialsId: 'github-credentials'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t tmachinya/banking-app:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([ credentialsId:  'dockerhub-credentials', url: '' ]) {
                    sh 'docker push tmachinya/banking-app:latest'
                }
            }
        }
    }
}
