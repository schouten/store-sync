package de.schouten.demo;

import java.io.File;
import java.io.FilenameFilter;
import de.schouten.demo.guibeans.Alerter;
import de.schouten.demo.guibeans.Message;
import de.schouten.demo.guibeans.PhoneNumberDisplay;
import de.schouten.demo.guibeans.Reload;
import de.schouten.demo.guibeans.Time;
import de.schouten.demo.jmx.MainManagement;
import de.schouten.demo.web.MessageManager;
import de.schouten.demo.web.ServerLauncher;
import de.schouten.store.DomainKey;
import de.schouten.store.JMXBeanSupportForStore;
import de.schouten.store.Store;
import de.schouten.store.StoreImpl;

public class Main {
    private Store store = new StoreImpl();

    private static final DomainKey ROOT = new DomainKey("root");
    private static final DomainKey TOM_KEY = new DomainKey("root", "tom");
    private static final DomainKey JERRY_KEY = new DomainKey("root", "jerry");

    private Main() throws Exception {
        super();
        File appHome = findAppHome();
        System.out.println("appHome is found here: " + appHome);
        MessageManager.init(store);
        JMXBeanSupportForStore supportForStore = new JMXBeanSupportForStore(store);
        supportForStore.register("Demo:type=Store");

        new Time(store, new DomainKey("root"));
        long time = System.currentTimeMillis();
        addTomMessage(time - 1000L * 60L * 10L, "Anruf angenommen.", false);
        addTomMessage(time - 1000L * 60L * 9L, "Anruf aufgelegt.", false);
        addTomMessage(time - 1000L * 60L * 2L, "Anruf abgewiesen.", true);

        addJerryMessage(time - 1000L * 60L * 10L, "Kurzwahl A.", false);
        addJerryMessage(time - 1000L * 60L * 9L, "Kurzwahl B.", false);
        addJerryMessage(time - 1000L * 60L * 8L, "Kurzwahl C.", false);
        addJerryMessage(time - 1000L * 60L * 7L, "Kurzwahl D.", false);
        addJerryMessage(time - 1000L * 60L * 6L, "Kurzwahl E.", false);
        addJerryMessage(time - 1000L * 60L * 5L, "Kurzwahl F.", false);
        addJerryMessage(time - 1000L * 60L * 4L, "Kurzwahl G.", false);

        PhoneNumberDisplay phoneNumberDisplayTomAp1 = new PhoneNumberDisplay(store, new DomainKey("root", "tom", "TOM_AP1"));
        new PhoneNumberDisplay(store, new DomainKey("root", "tom", "TOM_AP2"));
        new PhoneNumberDisplay(store, new DomainKey("root", "jerry", "JERRY_AP1"));

        phoneNumberDisplayTomAp1.appendDigit(0);
        phoneNumberDisplayTomAp1.appendDigit(2);
        phoneNumberDisplayTomAp1.appendDigit(8);
        phoneNumberDisplayTomAp1.appendDigit(2);
        phoneNumberDisplayTomAp1.appendDigit(1);

        ServerLauncher serverLauncher = new ServerLauncher();
        serverLauncher.start(appHome, store);

        new MainManagement(this, Thread.currentThread(), "Demo:type=DemoMain");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
        serverLauncher.stop();
        System.out.println("done");
    }

    private File findAppHome() {
        File search = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        while (search != null && search.isDirectory() && search.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.equalsIgnoreCase("static");
            }
        }).length != 1) {
            search = search.getParentFile();
        }
        if (search == null) {
            return new File(".");
        } else {
            return search;
        }
    }

    public void addTomMessage(long l, String string, boolean urgent) {
        MessageManager.getManagerTom().addMessage(new Message(l, string, urgent));
    }

    public void addJerryMessage(long l, String string, boolean urgent) {
        MessageManager.getManagerJerry().addMessage(new Message(l, string, urgent));
    }

    public void reloadAllClients() {
        new Reload(store, ROOT);
    }

    public void alertAll(String message) {
        new Alerter(store, ROOT, message);
    }

    public void alertTom(String message) {
        new Alerter(store, TOM_KEY, message);
    }

    public void alertJerry(String message) {
        new Alerter(store, JERRY_KEY, message);
    }

    public static void main(String[] args) throws Exception {
        new Main();
    }
}
