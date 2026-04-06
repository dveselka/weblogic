print('starting the script ....')

# connect WLST to the Admin Examples Server
username = 'weblogic'
password = 'weblogic123'
url = 't3://127.0.0.1:7001'
targetServerName = 'AdminServer'
connect(username, password, url)
edit()
startEdit()

servermb=getMBean('Servers/' + targetServerName)

# create JMS Server
myJMSServer = cmo.lookupJMSServer('myJMSServer')
if myJMSServer == None:
  myJMSServer = create('myJMSServer', 'JMSServer')
  myJMSServer.setMessagesMaximum(10000)
  myJMSServer.setBytesMaximum(100000000)
  myJMSServer.addTarget(servermb)

#create JMS System Resource
myJmsSystemResource = cmo.lookupJMSSystemResource('myJmsSystemResource')
if myJmsSystemResource == None:
  myJmsSystemResource = create('myJmsSystemResource', 'JMSSystemResource')
  myJmsSystemResource.addTarget(servermb)
  subDep = myJmsSystemResource.createSubDeployment('subDep')
  subDep.addTarget(myJMSServer)

#create JMS connection factory
JMSResource = myJmsSystemResource.getJMSResource()
myConnectionFactory = JMSResource.lookupConnectionFactory('myConnectionFactory')
if myConnectionFactory == None:
  myConnectionFactory = JMSResource.createConnectionFactory('myConnectionFactory')
  myConnectionFactory.setJNDIName('myConnectionFactory')
  myConnectionFactory.setSubDeploymentName('subDep')

# create JMS queue
myJmsQueue = JMSResource.lookupQueue('myJmsQueue')
if myJmsQueue == None:
  myJmsQueue = JMSResource.createQueue('myJmsQueue')
  myJmsQueue.setJNDIName('myJmsQueue')
  myJmsQueue.setSubDeploymentName('subDep')

# save and activate editor, and disconnect
try:
    save()
    activate(block = 'true')
    print('script returns SUCCESS')
    disconnect()    
except:
    print('Error while trying to save and/or activate!!!')
    dumpStack()
    cancelEdit('y')