
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()
    
    queuePath = '/JMSSystemResources/myJmsSystemResource/JMSResource/myJmsSystemResource/Queues/myJmsQueue/MessageLoggingParams/myJmsQueue'
    
    print("Navigating to MessageLoggingParams...")
    try:
        cd(queuePath)
    except:
        print("Direct path failed, trying list...")
        cd('/JMSSystemResources/myJmsSystemResource/JMSResource/myJmsSystemResource/Queues/myJmsQueue/MessageLoggingParams')
        ls()
        # Assume the single child is the one we want
        # In WLST, sometimes we need to pick the specific name if it wasn't auto-named
        kids = cmo.getMessageLoggingParams()
        # But in edit tree we cd.
        # Let's try cd to the first child found in ls() if possible, but manual is safer
        cd('myJmsQueue')
        
    print("Enabling Message Logging...")
    cmo.setMessageLoggingEnabled(True)
    cmo.setMessageLoggingFormat("%header%,%properties%")
    
    save()
    activate()
    print("JMS Queue Message Logging enabled successfully.")
    
except Exception as e:
    print("Error: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
