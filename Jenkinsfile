pipeline {
    agent any

    environment {
        IMAGE_NAME = "tmachinya/banking-app"
        IMAGE_TAG = "${BUILD_NUMBER}"
        KUBECONFIG_PATH = "/var/jenkins_home/.kube/config"
        DEPLOYMENT_NAME = "banking-app"
    }

    stages {
        stage('Clone repository') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([ credentialsId: 'dockerhub-credentials', url: '' ]) {
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Clean up Docker images') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
                sh "docker rmi ${IMAGE_NAME}:latest || true"
            }
        }

        stage('Deploy Postgres to Kubernetes') {
            steps {
                sh "kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/postgres-deployment.yaml"
                sh "kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/postgres-service.yaml"
                sh "kubectl --kubeconfig=${KUBECONFIG_PATH} rollout status deployment/postgres"
            }
        }

        stage('Deploy Banking App to Kubernetes') {
            steps {
                sh """
                  sed "s|image: .*|image: ${IMAGE_NAME}:${IMAGE_TAG}|" k8s/deployment.yaml > k8s/deployment-temp.yaml
                  kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/deployment-temp.yaml
                  kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/service.yaml
                  kubectl --kubeconfig=${KUBECONFIG_PATH} rollout status deployment/${DEPLOYMENT_NAME}
                """
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '🚨 Pipeline failed. Please check the logs.'
        }
    }
}
