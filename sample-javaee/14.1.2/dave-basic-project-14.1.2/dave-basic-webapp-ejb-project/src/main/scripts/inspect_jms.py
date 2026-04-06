
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()

    print("Navigating to JMS Server myJMSServer...")
    cd('/JMSServers/myJMSServer')
    ls()
    
    # Check if we can find the Logging attribute
    # cmo.setMessageLifecycleLoggingEnabled(True)

    cancelEdit('y')
    
except Exception as e:
    print("Error occurred: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
