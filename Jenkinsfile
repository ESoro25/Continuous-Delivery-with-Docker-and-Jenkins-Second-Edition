pipeline {
    agent {
        kubernetes {
        defaultContainer 'gradle'
        yaml '''
apiVersion: v1 
kind: Pod 
spec: 
  containers: 
  - name: gradle 
    image: gradle:jdk8 
    command: 
    - sleep 
    args: 
    - 99d 
  restartPolicy: Never 
        '''
        }
    }
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
                cd Chapter09/sample3
                chmod +x gradlew
                ./gradlew acceptanceTest -Dcalculator.url=http://calculator-service:8080
                '''
            }
        }
    }
}