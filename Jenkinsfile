pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'tmachinya/banking-app'
        IMAGE_TAG = 'latest'
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh 'docker build -t $DOCKER_IMAGE:$IMAGE_TAG .'
            }
        }

        stage('Test Docker Image (Optional)') {
            steps {
                echo "Running container sanity check..."
                sh 'docker run --rm $DOCKER_IMAGE:$IMAGE_TAG java -version'
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                echo "Pushing Docker image to Docker Hub..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "DockerHub username is $DOCKER_USER"'
                    sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                    sh 'docker push tmachinya/banking-app:latest'
                }
            }
        }

        stage('Clean up local Docker') {
            steps {
                echo "Cleaning up local Docker images to save space..."
                sh 'docker rmi $DOCKER_IMAGE:$IMAGE_TAG || true'
            }
        }
    }

    post {
        success {
            echo '✅ Build and push completed successfully!'
        }
        failure {
            echo '❌ Build or push failed!'
        }
    }
}
