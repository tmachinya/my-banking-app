pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'tmachinya/banking-app:latest'
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/tmachinya/my-banking-app.git'
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
                sh 'kubectl --kubeconfig=/var/jenkins_home/.kube/config apply -f k8s/deployment.yaml'
                sh 'kubectl --kubeconfig=/var/jenkins_home/.kube/config apply -f k8s/service.yaml'
            }
        }
    }
}
