package ge.utils;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class GEMouseEvent {
    public EventType<MouseEvent> type;
    public EventHandler<MouseEvent> handler;

    public GEMouseEvent(EventType<MouseEvent> type, EventHandler<MouseEvent> handler){
        this.type = type;
        this.handler = handler;
    }
}
