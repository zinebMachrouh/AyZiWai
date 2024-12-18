pipeline {
    agent {
        docker {
            image 'docker:24-dind'
            args '--privileged -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        GITHUB_REPO = 'https://github.com/zinebMachrouh/SoundWave.git'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        DOCKER_IMAGE = 'soundwave-app'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    credentialsId: '5f3d727a-2284-4bd6-aa3d-b19a8624474e',
                    url: "${GITHUB_REPO}"
            }
        }

        stage('Install Docker Compose') {
            steps {
                script {
                    sh '''
                    apk add --no-cache curl
                    curl -L "https://github.com/docker/compose/releases/download/v2.27.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                    chmod +x /usr/local/bin/docker-compose
                    docker-compose --version
                    '''
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    sh '''
                    docker build -t ${DOCKER_IMAGE} .
                    '''
                }
            }
        }

        stage('Deploy Using Docker Compose') {
            steps {
                script {
                    sh '''
                    docker-compose down
                    docker-compose uhhhhp -d
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    sh '''
                    docker ps
                    curl -f http://localhost:8085 || exit 1
                    '''
                }
            }
        }

        stage('Clean Up Unused Images') {
            steps {
                script {
                    sh '''
                    docker image prune -f
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed. Check for any issues above.'
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for details.'
        }
    }
}
