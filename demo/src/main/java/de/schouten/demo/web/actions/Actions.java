package de.schouten.demo.web.actions;

import com.google.gson.Gson;
import de.schouten.demo.web.DemoSession;
import de.schouten.store.web.Action;

/**
 * Enumerates a possible gui actions.
 */
public enum Actions {

    login(LoginAction.class), logout(LogoutAction.class), dialdigit(DialDigitAction.class), dialclear(DialClearAction.class), dial(DialAction.class),
    switchtab(SwitchTabAction.class), sendmessage(SendMessageForm.class);

    /**
     * Decodes the json strings.
     */
    private static final Gson GSON = new Gson();

    /**
     * The type of the action to create.
     */
    private Class<? extends Action<DemoSession>> actionType;

    /**
     * @param actionType The type of the action to create.
     */
    private Actions(Class<? extends Action<DemoSession>> actionType) {
        this.actionType = actionType;
    }

    /**
     * @param json the json data for this action.
     * @return the created action.
     */
    public Action<DemoSession> create(String json) {
        return GSON.fromJson(json, actionType);
    }

}
