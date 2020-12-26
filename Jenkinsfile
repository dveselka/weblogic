node {

    withMaven(maven:'local') {

        stage('Checkout') {
          git url: 'https://github.com/dveselka/weblogic/', credentialsId: 'dave-devops', branch: 'weblogic-14.1.1'
        }

        stage('Build') {
            sh 'cd dave-basic-webapp-ejb-project'
            sh 'mvn clean package'

            def pom = readMavenPom file:'pom.xml'
            print pom.version
            env.version = pom.version
        }
   

    }

}
