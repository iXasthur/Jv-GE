package ge.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

public class GEResizeListener {
    public interface GEResizeAction {
        void execute(double width, double height);
    }

    private static Stage stage = null;
    public static GEResizeAction resizeAction = (width, height) -> {};

    public static void init(Stage _stage) {
        if (stage != null) {
            stage.widthProperty().removeListener(stageWidthListener);
            stage.heightProperty().removeListener(stageHeightListener);
        }

        stage = _stage;
        stage.widthProperty().addListener(stageWidthListener);
        stage.heightProperty().addListener(stageHeightListener);

        resizeAction = (width, height) -> {};
    }

    public static double getWidth() {
        return stage.getWidth();
    }

    public static double getHeight() {
        return stage.getHeight();
    }

    private static final ChangeListener<Number> stageWidthListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            resizeAction.execute(stage.getWidth(), stage.getHeight());
        }
    };

    private static final ChangeListener<Number> stageHeightListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            resizeAction.execute(stage.getWidth(), stage.getHeight());
        }
    };
}
