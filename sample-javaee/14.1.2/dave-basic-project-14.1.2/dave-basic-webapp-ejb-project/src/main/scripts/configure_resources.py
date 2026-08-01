print('Starting cluster JMS resource configuration...')

# Admin connection
username = 'weblogic'
password = 'weblogic123'
url = 't3://127.0.0.1:7002'

# Cluster/domain targets
clusterName = 'app-cluster'
managedServers = ['managed-server1', 'managed-server2']

# JMS resource names
jmsServerPrefix = 'myJMSServer'
jmsModuleName = 'myJmsSystemResource'
subDeploymentName = 'myJmsSubDeployment'
connectionFactoryName = 'myConnectionFactory'
connectionFactoryJndi = 'jms/myConnectionFactory'
queueName = 'myJmsQueue'
queueJndi = 'jms/myJmsQueue'


def has_target(bean, target):
  targets = bean.getTargets()
  if targets is None:
    return 0
  for t in targets:
    if t.getName() == target.getName():
      return 1
  return 0


connect(username, password, url)
edit()
startEdit()
domainConfig()

try:
  clusterMBean = getMBean('/Clusters/' + clusterName)
  if clusterMBean is None:
    raise Exception('Cluster not found: ' + clusterName)

  createdJmsServers = []

  # Create one JMS Server per managed server and target it locally.
  for managedServerName in managedServers:
    serverMBean = getMBean('/Servers/' + managedServerName)
    if serverMBean is None:
      raise Exception('Managed server not found: ' + managedServerName)

    jmsServerName = jmsServerPrefix + '_' + managedServerName
    jmsServer = cmo.lookupJMSServer(jmsServerName)
    if jmsServer is None:
      print('Creating JMS Server: ' + jmsServerName)
      jmsServer = create(jmsServerName, 'JMSServer')
      jmsServer.setMessagesMaximum(10000)
      jmsServer.setBytesMaximum(100000000)

    if not has_target(jmsServer, serverMBean):
      print('Targeting ' + jmsServerName + ' to ' + managedServerName)
      jmsServer.addTarget(serverMBean)

    createdJmsServers.append(jmsServer)

  # Create (or reuse) the JMS module and target it to the cluster.
  jmsModule = cmo.lookupJMSSystemResource(jmsModuleName)
  if jmsModule is None:
    print('Creating JMS Module: ' + jmsModuleName)
    jmsModule = create(jmsModuleName, 'JMSSystemResource')

  if not has_target(jmsModule, clusterMBean):
    print('Targeting JMS Module to cluster: ' + clusterName)
    jmsModule.addTarget(clusterMBean)

  subDeployment = jmsModule.lookupSubDeployment(subDeploymentName)
  if subDeployment is None:
    print('Creating SubDeployment: ' + subDeploymentName)
    subDeployment = jmsModule.createSubDeployment(subDeploymentName)

  for jmsServer in createdJmsServers:
    if not has_target(subDeployment, jmsServer):
      print('Targeting SubDeployment to JMS Server: ' + jmsServer.getName())
      subDeployment.addTarget(jmsServer)

  jmsResource = jmsModule.getJMSResource()

  # Connection Factory
  connectionFactory = jmsResource.lookupConnectionFactory(connectionFactoryName)
  if connectionFactory is None:
    print('Creating ConnectionFactory: ' + connectionFactoryName)
    connectionFactory = jmsResource.createConnectionFactory(connectionFactoryName)

  connectionFactory.setJNDIName(connectionFactoryJndi)
  connectionFactory.setSubDeploymentName(subDeploymentName)

  # Uniform Distributed Queue for cluster-safe message distribution.
  udq = jmsResource.lookupUniformDistributedQueue(queueName)
  if udq is None:
    print('Creating UniformDistributedQueue: ' + queueName)
    udq = jmsResource.createUniformDistributedQueue(queueName)

  udq.setJNDIName(queueJndi)
  udq.setSubDeploymentName(subDeploymentName)
  udq.setLoadBalancingPolicy('Round-Robin')

  save()
  activate(block='true')
  print('Cluster JMS resources configured successfully.')
except:
  print('Error while configuring JMS resources.')
  dumpStack()
  cancelEdit('y')
  raise
finally:
  disconnect()