
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()

    print("Navigating to JMS Server myJMSServer...")
    cd('/JMSServers/myJMSServer')
    
    print("Enabling Message Lifecycle Logging...")
    cmo.setMessageLifecycleLoggingEnabled(True)
    
    # Also check the Log MBean configuration
    # cd('Log/myJMSServer')
    # print("Log configuration: " + str(cmo.getFileName()))

    save()
    activate()
    print("JMS Logging enabled successfully.")
    
except Exception as e:
    print("Error occurred: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
