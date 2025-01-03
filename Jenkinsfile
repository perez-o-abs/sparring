pipeline {
    agent any

    environment {
        // Define environment variables
        VERSION = "${env.GIT_COMMIT[0..6]}"
        MYSQL_ROOT_PASSWORD = credentials('mysql-root-password') // Jenkins credentials for MySQL
    }

    tools {
        // Define Maven and JDK tools
        maven 'Maven 3'
        jdk 'JDK 8'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout main repository
                checkout scm
                
                // Checkout test automation repository using SSH
                dir('automation-tests') {
                    git url: 'git@gitlab.abstracta.us:Automation/trainings/sparring-automation-testcases.git',
                        branch: 'main',
                        credentialsId: 'gitlab-ssh-key'  // Jenkins credentials ID for GitLab SSH key
                }
            }
        }

        stage('Build') {
            steps {
				sh 'docker --version'
                sh 'mvn --version'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Tests') {
            steps {
                script {
                    // Start the services using docker-compose
                    sh "GIT_COMMIT=${env.GIT_COMMIT[0..6]} docker compose up -d"
                    
                    // Wait for services to be ready
                    sh 'sleep 30'
                    
                    try {
                        // TODO Run integration tests here
                        sh 'curl -f http://host.docker.internal:8081/ || exit 1'  // Basic health check
                        sh 'curl -f http://host.docker.internal:8082/ || exit 1'  // Basic health check
                        sh 'cd automation-tests'
                        sh 'mvn clean test'                        
                    } finally {
                        // cleanup
                        sh 'docker compose down -v'
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean workspace and remove unused docker images
            //cleanWs()
            sh 'docker system prune -f'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
