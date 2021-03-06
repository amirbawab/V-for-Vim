// -------------------------------
// adapted from Kevin T. Manley
// CSE 593
//
package rm;

import inter.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RMISecurityManager;

public class RM {

    // Logger
    private static final Logger logger = LogManager.getLogger(ResourceManagerImpl.class);

    // Constants
    private static final int CODE_ERROR=1;

    // Store items in a hash table
    private final RMHashtable m_itemHT = new RMHashtable();

    public static void main(String args[]) {
        // Figure out where server is running
        if (args.length != 2) {
            System.err.println ("Wrong usage");
            System.out.println("    Usage: java ResImpl.ResourceManagerImpl [port] [obj-ref]");
            System.exit(CODE_ERROR);
        }

        // Constants
        final int BIND_SLEEP = 5000;

        // Read port
        int port = Integer.parseInt(args[0]);
        String objRef = args[1];

        // Bind object to reference key
        boolean binded = false;
        ResourceManagerImpl obj = new ResourceManagerImpl(objRef);
        ResourceManager rm = null;
        while (!binded) {
            try {

                // If already export
                if(rm == null) {
                    // Create a new Server object
                    // Dynamically generate the stub (client proxy)
                    rm = (ResourceManager) UnicastRemoteObject.exportObject(obj, 0);
                }

                // Bind the remote object's stub in the registry
                Registry registry = LocateRegistry.getRegistry(port);
                registry.rebind(objRef, rm);
                logger.info("Server ready");
                binded = true;
            } catch (Exception e) {
                logger.error("Server exception: " + e.getMessage());
                try {
                    logger.info("Trying again in " + BIND_SLEEP + " ms");
                    Thread.sleep(BIND_SLEEP);
                } catch (InterruptedException e1) {
                    logger.error("Failed to put thread to sleep. Message: " + e1.getMessage());
                }
            }
        }

        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
    }
}
