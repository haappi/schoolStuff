module io.github.haappi.ProductivityApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.lifecycle;
    requires com.gluonhq.attach.util;


    opens io.github.haappi to javafx.fxml;
    exports io.github.haappi;
}