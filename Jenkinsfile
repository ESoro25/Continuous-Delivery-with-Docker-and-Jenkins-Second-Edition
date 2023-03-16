pipeline {
    agent any
    stages {
        stage('Run Calculator') {
            steps {
                sh '''
                cd Chapter08/sample1
                curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                chmod +x kubectl
                echo 'Starting Calculator...'
                ./kubectl apply -f calculator.yaml
                echo 'Starting Hazelcast...'
                ./kubectl apply -f hazelcast.yaml
                '''
            }
        }
        stage('Test Calculator') {
            steps {
                sh '''
                test $(curl  172.17.0.7:8080/sum?a=6\\&b=2) -eq 3 && echo 'pass' || 'fail'
                '''
            }
        }
    }
}