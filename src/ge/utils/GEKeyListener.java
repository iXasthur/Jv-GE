package ge.utils;


import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Vector;

public class GEKeyListener {
    public interface GEKeyAction {
        void execute(KeyEvent keyEvent);
    }

    private static Stage stage = null;

    public static GEKeyAction pressAction = (keyEvent) -> {};
    public static GEKeyAction releaseAction = (keyEvent) -> {};

    private static Vector<KeyCode> blockedKeyCodes = new Vector<>(0);

    public static void init(Stage _stage) {
        if (stage != null) {
            stage.removeEventHandler(KeyEvent.KEY_PRESSED, GEKeyListener.keyPressHandler);
            stage.removeEventHandler(KeyEvent.KEY_RELEASED, GEKeyListener.keyReleaseHandler);
        }

        stage = _stage;
        stage.addEventHandler(KeyEvent.KEY_PRESSED, GEKeyListener.keyPressHandler);
        stage.addEventHandler(KeyEvent.KEY_RELEASED, GEKeyListener.keyReleaseHandler);

        blockedKeyCodes = new Vector<>(0);

        pressAction = (keyEvent) -> {};
        releaseAction = (keyEvent) -> {};
    }

    private static final EventHandler<KeyEvent> keyPressHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (!blockedKeyCodes.contains(keyEvent.getCode())) {
//                System.out.println("Pressed " + keyEvent.getCode());
                blockedKeyCodes.add(keyEvent.getCode());

                pressAction.execute(keyEvent);
            }
        }
    };

    private static final EventHandler<KeyEvent> keyReleaseHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
//            System.out.println("Released " + keyEvent.getCode());
            blockedKeyCodes.remove(keyEvent.getCode());

            releaseAction.execute(keyEvent);
        }
    };
}
