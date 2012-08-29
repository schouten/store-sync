package de.schouten.demo.jmx;

public interface MainManagementMXBean {

    void quit();

    void reloadAllClients();

    void alertAll(String message);

    void alertTom(String message);

    void alertJerry(String message);

    void generateTomMessages(int amount);

    void generateJerryMessages(final int amount);
}