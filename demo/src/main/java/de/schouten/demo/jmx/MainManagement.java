package de.schouten.demo.jmx;

import java.lang.management.ManagementFactory;
import java.util.Random;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import de.schouten.demo.Main;

/**
 * Manaement support for the demo main class.
 */
public class MainManagement implements MainManagementMXBean {

    /**
     * This will be managed.
     */
    private Main demoMain;

    /**
     * The thread that is running the main method (and currently sleeping).
     */
    private Thread mainThread;

    /**
     * @param demoMain This will be managed.
     * @throws JMException
     */
    public MainManagement(Main demoMain, Thread mainThread, String managementObjectName) throws JMException {
        super();
        this.demoMain = demoMain;
        this.mainThread = mainThread;
        ObjectName objectName = new ObjectName(managementObjectName);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(this, objectName);
    }

    public void alertAll(String message) {
        demoMain.alertAll(message);
    }

    public void reloadAllClients() {
        demoMain.reloadAllClients();
    }

    public void alertTom(String message) {
        demoMain.alertTom(message);
    }

    public void alertJerry(String message) {
        demoMain.alertJerry(message);
    }

    public void generateTomMessages(final int amount) {
        new Thread() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < amount; i++) {
                    demoMain.addTomMessage(System.currentTimeMillis(), String.format("Nachricht für Tom #%s", i), random.nextBoolean());
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    public void generateJerryMessages(final int amount) {
        new Thread() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < amount; i++) {
                    demoMain.addJerryMessage(System.currentTimeMillis(), String.format("Nachricht für Jerry #%s", i), random.nextBoolean());
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    public void quit() {
        mainThread.interrupt();
    }
}
