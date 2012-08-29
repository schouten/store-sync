package de.schouten.store.web;

import de.schouten.store.DomainKey;
import de.schouten.store.Store;
import de.schouten.store.StoreElement;

/**
 * A form contains data that can be submitted. It is a combination of an @link{StoreElement} - so the data can be stored in the {@link Store} and an
 * {@link Action} so the form can be submitted.
 */
public abstract class Form<S extends StoreSyncWebSocketSession> implements StoreElement, Action<S> {

    /**
     * All submit types.
     */
    public enum SubmitType {
        /** Only update the values. */
        update,
        /** Update the values and perform the submit. */
        submit,
        /** Resets the form values to the initial state. */
        reset;
    }

    /**
     * The submit type.
     */
    private SubmitType submitType;

    /**
     * @return a clone of the form.
     */
    @Override
    public abstract Form<S> clone();

    @Override
    public final void perform(StoreSyncContext<S> context, Subscriptions subscriptions, S session) throws Exception {
        switch (submitType) { // first call one of the methods.
            case submit:
                submit(context, subscriptions, session);
                break;
            case reset:
                reset(context, subscriptions, session);
                break;
            default: // update
                update(context, subscriptions, session);
                break;
        }
        // then store the result into the store.
        context.getStore().put(getDomainKey(context, session), this);
    }

    /**
     * @param context the current context.
     * @param session the session for the web socket connection.
     * @return the domain key for this form.
     */
    public abstract DomainKey getDomainKey(StoreSyncContext<S> context, S session);

    /**
     * The form was submitted. Perform submit actions here.
     * 
     * @param context the current context.
     * @param subscriptions the manager for store subscriptions.
     * @param session the session for the web socket connection.
     * 
     * @throws Exception on errors.
     */
    public abstract void submit(StoreSyncContext<S> context, Subscriptions subscriptions, S session) throws Exception;

    /**
     * The form was reseted. Perform reset actions here. Only implement this method if required.
     * 
     * @param context the current context.
     * @param subscriptions the manager for store subscriptions.
     * @param session the session for the web socket connection.
     * 
     * @throws Exception on errors.
     */
    public void reset(StoreSyncContext<S> context, Subscriptions subscriptions, S session) throws Exception {
        // Only implement this method if required.
    }

    /**
     * The form was updated. Normally nothing to do here.
     * 
     * @param context the current context.
     * @param subscriptions the manager for store subscriptions.
     * @param session the session for the web socket connection.
     * 
     * @throws Exception on errors.
     */
    public void update(StoreSyncContext<S> context, Subscriptions subscriptions, S session) throws Exception {
        // Only implement this method if required.
    }
}
