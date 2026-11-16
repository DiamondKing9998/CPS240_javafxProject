module com.dynamicformbuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;


    opens com.dynamicformbuilder to javafx.fxml;

    exports com.dynamicformbuilder;
}