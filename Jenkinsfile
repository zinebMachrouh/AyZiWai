pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'tsukiiya101/ayziwai'
        SONAR_PROJECT_KEY = 'AyZiWai'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    dir('C:\\Users\\ssngn\\Documents\\Youcode\\AyZiWai') {
                        bat 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    dir('C:\\Users\\ssngn\\Documents\\Youcode\\AyZiWai') {
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                            bat """
                                mvn clean verify sonar:sonar \
                                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                -Dsonar.projectName=${SONAR_PROJECT_KEY} \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.token=%SONAR_TOKEN%
                            """
                        }
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    dir('C:\\Users\\ssngn\\Documents\\Youcode\\AyZiWai') {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    dir('C:\\Users\\ssngn\\Documents\\Youcode\\AyZiWai') {
                        withCredentials([usernamePassword(
                            credentialsId: 'dockerhub-credentials',
                            usernameVariable: 'DOCKER_USERNAME',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )]) {
                            bat "docker build -t ${DOCKER_HUB_REPO}:${BUILD_NUMBER} ."
                            bat "docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%"
                            bat "docker push ${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    dir('C:\\Users\\ssngn\\Documents\\Youcode\\AyZiWai') {
                        withCredentials([usernamePassword(
                            credentialsId: 'dockerhub-credentials',
                            usernameVariable: 'DOCKER_USERNAME',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )]) {
                            bat 'docker-compose up -d'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            bat "docker logout"
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}   