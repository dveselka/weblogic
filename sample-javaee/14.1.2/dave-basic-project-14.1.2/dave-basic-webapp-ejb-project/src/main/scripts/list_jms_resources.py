
try:
    connect('weblogic', 'weblogic123', 't3://127.0.0.1:7001')
    edit()
    startEdit()
    
    print("\n--- Listing JMSSystemResources ---")
    ls('/JMSSystemResources')
    
    cancelEdit('y')
    
except Exception as e:
    print("Error occurred: " + str(e))
    cancelEdit('y')
    exit(exitcode=1)

disconnect()
exit()
