
MW_HOME = '/app/weblogic-14.1.2'
WLS_TEMPLATE = MW_HOME + '/wlserver/common/templates/wls/wls.jar'

DOMAIN_NAME = 'base_domain'
DOMAIN_HOME = '/app/weblogic-14.1.2/user_projects/domains/base_domain'
ADMIN_SERVER_NAME = 'AdminServer'
ADMIN_PORT = 7002
CLUSTER_NAME = 'app-cluster'
MANAGED_SERVERS = [
	('managed-server1', 7003),
	('managed-server2', 7004),
]
ADMIN_USER = 'weblogic'
ADMIN_PASSWORD = 'weblogic123'

print('Reading template: ' + WLS_TEMPLATE)
readTemplate(WLS_TEMPLATE)

print('Configuring AdminServer...')
cd('Servers/AdminServer')
set('Name', ADMIN_SERVER_NAME)
set('ListenPort', ADMIN_PORT)

print('Creating cluster...')
cd('/')
create(CLUSTER_NAME, 'Cluster')

print('Creating managed servers...')
cd('/')
for server_name, server_port in MANAGED_SERVERS:
	create(server_name, 'Server')
	cd('/Servers/' + server_name)
	set('ListenPort', server_port)
	cd('/')

for server_name, _ in MANAGED_SERVERS:
	assign('Server', server_name, 'Cluster', CLUSTER_NAME)

print('Setting admin user credentials...')
cd('/')
cd('Security/base_domain/User/weblogic')
cmo.setName(ADMIN_USER)
cmo.setPassword(ADMIN_PASSWORD)

setOption('OverwriteDomain', 'true')
setOption('ServerStartMode', 'dev')

print('Writing domain to: ' + DOMAIN_HOME)
writeDomain(DOMAIN_HOME)

closeTemplate()
print('Domain created successfully.')
exit()
