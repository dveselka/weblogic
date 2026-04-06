
MW_HOME = '/app/weblogic-14.1.2'
WLS_TEMPLATE = MW_HOME + '/wlserver/common/templates/wls/wls.jar'

DOMAIN_NAME = 'basicWebappEjbDomain'
DOMAIN_HOME = '/git/weblogic/dave-basic-project-14.1.2/dave-basic-webapp-ejb-project/domains/' + DOMAIN_NAME
ADMIN_SERVER_NAME = 'AdminServer'
ADMIN_PORT = 7002
ADMIN_USER = 'weblogic'
ADMIN_PASSWORD = 'weblogic123'

print('Reading template: ' + WLS_TEMPLATE)
readTemplate(WLS_TEMPLATE)

print('Configuring AdminServer...')
cd('Servers/AdminServer')
set('Name', ADMIN_SERVER_NAME)
set('ListenPort', ADMIN_PORT)

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
