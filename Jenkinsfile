pipeline {
    agent {
        kubernetes {
        yaml '''
apiVersion: v1 
kind: Pod 
spec: 
  containers: 
  - name: gradle 
    image: gradle:6.3-jdk14 
    command: 
    - sleep 
    args: 
    - 99d 
    volumeMounts: 
    - name: shared-storage 
      mountPath: /mnt         
  - name: kaniko 
    image: gcr.io/kaniko-project/executor:debug 
    command: 
    - sleep 
    args: 
    - 9999999 
    volumeMounts: 
    - name: shared-storage 
      mountPath: /mnt 
    - name: kaniko-secret 
      mountPath: /kaniko/.docker 
  restartPolicy: Never 
  volumes: 
  - name: shared-storage 
    persistentVolumeClaim: 
      claimName: jenkins-pv-claim-new 
  - name: kaniko-secret 
    secret: 
        secretName: dockercred 
        items: 
        - key: .dockerconfigjson 
          path: config.json
        '''
        }
    }
    stages {
        stage('Build Grade') {
            steps {
                container('gradle') {    
                sh '''
                echo 'Building Gradle...'
                cd Chapter09/sample3
                chmod +x gradlew
                ./gradlew build
                cp ./build/libs/calculator-0.0.1-SNAPSHOT.jar /mnt
                '''
                }
            }
        }
        stage('Build Image') {
            steps {
                container('kaniko') {
                sh '''
                echo 'Pushing Image to Dockerhub...'
                echo 'FROM openjdk:8-jre' > Dockerfile
                echo 'COPY ./calculator-0.0.1-SNAPSHOT.jar app.jar' >> Dockerfile
                echo 'ENTRYPOINT ["java", "-jar", "app.jar"]' >> Dockerfile
                mv /mnt/calculator-0.0.1-SNAPSHOT.jar .
                /kaniko/executor --context `pwd` --destination esoro25/calculator-div:1.0
                '''    
                }
            }
        }
        stage('Run Calculator') {
            steps {
                sh '''
                cd ../../Chapter08/sample1
                echo 'Starting Calculator...'
                kubectl apply -f calculator.yaml
                echo 'Starting Hazelcast...'
                kubectl apply -f hazelcast.yaml
                '''
            }
        }
        stage('Testing Calculator') {
            steps {
                sh '''
                test $(curl calculator-service:8080/sum?a=6\\&b=2) -eq 3 && echo 'pass' || 'fail'
                '''
            }
        }
    }
}