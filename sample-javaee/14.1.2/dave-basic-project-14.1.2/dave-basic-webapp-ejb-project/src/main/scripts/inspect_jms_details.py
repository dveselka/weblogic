
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()

    print("\n--- JMS Server Log File Configuration ---")
    try:
        cd('/JMSServers/myJMSServer/JMSMessageLogFile/myJMSServer')
        ls()
    except Exception as e:
        print("Could not navigate to JMSMessageLogFile: " + str(e))

    print("\n--- JMS Queue Configuration ---")
    try:
        cd('/JMSSystemResources/myJmsModule/JMSResource/myJmsModule/Queues/myJmsQueue')
        ls()
    except Exception as e:
        print("Could not navigate to Queue: " + str(e))
        
    cancelEdit('y')
    
except Exception as e:
    print("Error occurred: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
