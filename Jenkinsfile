pipeline {
    agent any

    environment {
        IMAGE_NAME = "tmachinya/banking-app"
        IMAGE_TAG = "${BUILD_NUMBER}"
        DEPLOYMENT_NAME = "banking-app"
        KUBECONFIG_PATH = "/var/jenkins_home/.kube/config"
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

        stage('Deploy Postgres to Kubernetes') {
            steps {
                sh '''
                    kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/postgres-deployment.yaml
                    kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/postgres-service.yaml
                    kubectl --kubeconfig=${KUBECONFIG_PATH} rollout status deployment/postgres
                '''
            }
        }

        stage('Deploy Banking App to Kubernetes') {
            steps {
                sh '''
                    sed 's|image: .*$|image: ${IMAGE_NAME}:${BUILD_NUMBER}|' k8s/deployment.yaml > k8s/deployment-temp.yaml

                    kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/deployment-temp.yaml
                    kubectl --kubeconfig=${KUBECONFIG_PATH} apply -f k8s/service.yaml
                    kubectl --kubeconfig=${KUBECONFIG_PATH} rollout status deployment/${DEPLOYMENT_NAME}
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
