
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()

    print("\n--- JMS System Resource Configuration ---")
    cd('/JMSSystemResources/myJmsModule')
    ls()
    
    print("\n--- Entering JMSResource ---")
    # JMSResource is usually a child Bean, but might not be a 'folder' in the same way depending on WLST version.
    # It usually shows up as a child in ls() output.
    
    cancelEdit('y')
    
except Exception as e:
    print("Error occurred while inspecting: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
