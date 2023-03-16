pipeline {
    agent {
        kubernetes {
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
                container('gradle') {
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
        }
        stage('Test Calculator') {
            steps {
                container('gradle') {
                sh '''
                cd Chapter09/sample3
                chmod +x gradlew
                ./gradlew build
                ./gradlew acceptanceTest -Dcalculator.url=http://calculator-service:8080
                '''
                }
            }
        }
    }
    post {
        sucess {
            steps {
                publishHTML (target: [
                    reportDir: 'Chapter09/sample3/build/reports/tests/acceptanceTest',
                    reportFiles: 'index.html',
                    reportName: "Acceptance Report"
                    ])                       
            }
        }
    }
}