pipeline {
    agent any

    environment {
        // Define environment variables
        VERSION = '1.0-SNAPSHOT'
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
                checkout scm
            }
        }

        stage('Build') {
            steps {
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

        stage('Build Docker Images') {
            steps {
                script {
                    // Build Docker images for each service
                    def services = ['sparring-admin', 'sparring-web', 'sparring-db']
                    
                    services.each { service ->
                        dir(service) {
                            def imageName = "${service}:${VERSION}"
                            // Build Docker image
                            docker.build(imageName, '-f Dockerfile .')
                        }
                    }
                }
            }
        }

        stage('Integration Tests') {
            steps {
                script {
                    // Start the services using docker-compose
                    sh 'docker compose up -d'
                    
                    // Wait for services to be ready
                    sh 'sleep 30'  // Adjust time as needed
                    
                    try {
                        // Run integration tests here
                        sh 'curl -f http://localhost:8080/health || exit 1'  // Basic health check
                        sh 'curl -f http://localhost:8081/health || exit 1'  // Basic health check
                    } finally {
                        // Always cleanup
                        sh 'docker-compose down -v'
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean workspace and remove unused docker images
            cleanWs()
            sh 'docker system prune -f'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
            // You can add notifications here (email, Slack, etc.)
        }
    }
}
