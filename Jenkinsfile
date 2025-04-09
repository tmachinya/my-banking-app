pipeline {
    agent any

    environment {
        IMAGE_NAME = "tmachinya/banking-app"
        IMAGE_TAG = "${BUILD_NUMBER}" // Note: env. is implicit
    }

    stages {
        stage('Clone repository') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} -t ${IMAGE_NAME}:latest .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([ credentialsId: 'dockerhub-credentials', url: '' ]) {
                    sh '''
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker push ${IMAGE_NAME}:latest
                    '''
                }
            }
        }

        stage('Clean up Docker images') {
            steps {
                sh '''
                    docker rmi ${IMAGE_NAME}:${BUILD_NUMBER} || true
                    docker rmi ${IMAGE_NAME}:latest || true
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    kubectl --kubeconfig=/var/jenkins_home/.kube/config apply -f k8s/deployment.yaml
                    kubectl --kubeconfig=/var/jenkins_home/.kube/config apply -f k8s/service.yaml
                    kubectl --kubeconfig=/var/jenkins_home/.kube/config rollout status deployment/banking-app
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! 🎉'
        }
        failure {
            echo 'Pipeline failed. Please check the logs. 🚨'
        }
    }
}
