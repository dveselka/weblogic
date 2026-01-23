
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()
    
    queuePath = '/JMSSystemResources/myJmsSystemResource/JMSResource/myJmsSystemResource/Queues/myJmsQueue'
    cd(queuePath)
    ls()
    
    cancelEdit('y')
    
except Exception as e:
    print("Error: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
