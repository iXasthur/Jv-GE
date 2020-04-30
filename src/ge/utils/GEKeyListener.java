package ge.utils;


import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class GEKeyListener {
    public interface GEAction {
        void execute(KeyEvent keyEvent);
    }

    public static GEAction action = (_keyEvent) -> {};

    public static EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            action.execute(keyEvent);
        }
    };
}
