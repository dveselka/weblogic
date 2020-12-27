node {

    

        stage('Checkout') {
          git url: 'https://github.com/dveselka/weblogic/', credentialsId: 'dave-devops', branch: 'weblogic-14.1.1'
        }

        stage('Build') {
            dir ('dave-basic-webapp-ejb-project') {
                withMaven(maven:'local') {
                   sh 'mvn clean package'

                   def pom = readMavenPom file:'pom.xml'
                   print pom.version
                   env.version = pom.version
                }
            }     
        }
   
   

}
