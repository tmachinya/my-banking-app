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
                withDockerRegistry([credentialsId: "$DOCKER_CREDENTIALS", url: '']) {
                    sh 'docker push $DOCKER_IMAGE:$IMAGE_TAG'
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
