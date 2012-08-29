package de.schouten.demo.guibeans;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import de.schouten.store.DomainKey;
import de.schouten.store.Store;
import de.schouten.store.UpdateableStoreElement;

/**
 * Holds the actual time.
 */
public class Time extends UpdateableStoreElement {

    /**
     * Timer to update this gui time element..
     */
    private static final Timer GUI_TIMER = new Timer("gui time updater", true);

    /**
     * The time and date value.
     */
    private String value;

    /**
     * The seconds of the minute.
     */
    private int seconds;

    /**
     * The minutes of the hour.
     */
    private int minutes;

    /**
     * The hours of the day.
     */
    private int hours;

    /**
     * @see UpdateableStoreElement#UpdateableStoreElement(Store, DomainKey)
     */
    public Time(Store store, DomainKey domainKey) {
        super(store, domainKey);

        GUI_TIMER.schedule(new TimerTask() {

            @Override
            public void run() {
                update();
            }
        }, 0L, 1000L);
    }

    /**
     * Constructor used by clone method.
     */
    private Time(Store store, DomainKey domainKey, String value, int hours, int minutes, int seconds) {
        super(store, domainKey);
        this.value = value;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {"SINGLETON"};
    }

    @Override
    public Time clone() {
        return new Time(store, domainKey, value, hours, minutes, seconds);
    }

    /**
     * @see de.schouten.store.UpdateableStoreElement#update()
     */
    @Override
    public void update() {
        GregorianCalendar now = new GregorianCalendar();
        value = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, new Locale("de")).format(now.getTime());
        seconds = now.get(Calendar.SECOND);
        minutes = now.get(Calendar.MINUTE);
        hours = now.get(Calendar.HOUR);
        super.update();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }

}
