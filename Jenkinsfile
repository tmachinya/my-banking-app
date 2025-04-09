pipeline {
    agent any

    environment {
        IMAGE_NAME = "tmachinya/banking-app:latest"
    }

    stages {
        stage('Clone repository') {
            steps {
                git url: 'https://github.com/tmachinya/my-banking-app.git', branch: 'main'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Login & Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh 'docker push $IMAGE_NAME'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
    }
}
