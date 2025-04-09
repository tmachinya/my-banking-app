pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
        IMAGE_NAME = 'tmachinya/banking-app'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/tmachinya/my-banking-app.git'
                script {
                    COMMIT_HASH = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    BUILD_TIMESTAMP = sh(script: "date +%Y%m%d%H%M%S", returnStdout: true).trim()
                    IMAGE_TAG = "${BUILD_TIMESTAMP}-${COMMIT_HASH}"
                    env.IMAGE_TAG = IMAGE_TAG
                    env.DOCKER_IMAGE = "${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([ credentialsId: 'dockerhub-credentials', url: '' ]) {
                    sh 'docker push $DOCKER_IMAGE'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Replace image tag in deployment.yaml dynamically
                    sh """
                        sed 's|image: .*$|image: $DOCKER_IMAGE|' k8s/deployment.yaml > k8s/deployment-temp.yaml
                        kubectl --kubeconfig=/var/jenkins_home/.kube/config apply -f k8s/deployment-temp.yaml
                        kubectl --kubeconfig=/var/jenkins_home/.kube/config apply -f k8s/service.yaml
                    """
                }
            }
        }
    }
}