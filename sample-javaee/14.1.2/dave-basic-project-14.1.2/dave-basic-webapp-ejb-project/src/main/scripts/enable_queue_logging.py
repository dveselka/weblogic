
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()
    
    # Path to the queue
    queuePath = '/JMSSystemResources/myJmsSystemResource/JMSResource/myJmsSystemResource/Queues/myJmsQueue'
    
    print("Navigating to Queue: " + queuePath)
    cd(queuePath)
    
    # Enable Logging
    # Common attribute names: JMSCreateDestinationIdentifier, etc.
    # Looking for logging.
    
    # Check if attribute exists
    try:
        current = cmo.getJMSMessageLoggingEnabled()
        print("Current logging status: " + str(current))
    except:
        print("Attribute JMSMessageLoggingEnabled not found, trying generic MessageLoggingEnabled")
    
    print("Enabling JMSMessageLoggingEnabled...")
    # This attribute enables logging of message lifecycle events to the JMS message log file.
    cmo.setJMSMessageLoggingEnabled(True)
    
    save()
    activate()
    print("Logging enabled.")
    
except Exception as e:
    print("Error occurred: " + str(e))
    # Print available attributes to debug
    try:
        ls()
    except:
        pass
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
