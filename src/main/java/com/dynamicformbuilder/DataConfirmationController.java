package com.dynamicformbuilder;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DataConfirmationController {

    public static class tableDataTemplate {
        private final StringProperty dataName;
        private final StringProperty dataValue;

        public tableDataTemplate(String dataName, String dataValue) {
            this.dataName = new SimpleStringProperty(dataName);
            this.dataValue = new SimpleStringProperty(dataValue);
        }

        public StringProperty dataNameProperty() { return dataName; }
        public StringProperty dataValueProperty() { return dataValue; }

        public String getDataName() { return dataName.get(); }
        public String getDataValue() { return dataValue.get(); }
    }

    @FXML
    private TableView<tableDataTemplate> setup_TableView;
    @FXML
    private Button backToSetup_Button;
    @FXML
    private Button viewQRCode_Button;

    @FXML
    public void initialize() {
        SceneDataStore finalizationStore = SceneDataStore.getInstance();

        TableColumn<tableDataTemplate, String> setupNameColumn = new TableColumn<>("Description");
        setupNameColumn.setCellValueFactory(new PropertyValueFactory<>("dataName"));
        TableColumn<tableDataTemplate, String> setupValueColumn = new TableColumn<>("Value");
        setupValueColumn.setCellValueFactory(new PropertyValueFactory<>("dataValue"));
        setupNameColumn.setResizable(true);
        setupValueColumn.setResizable(true);

        ObservableList<tableDataTemplate> setupData = FXCollections.observableArrayList(
                new tableDataTemplate("Scouter Name", stringValue(finalizationStore, "scouterName")),
                new tableDataTemplate("Team Number", stringValue(finalizationStore, "teamNumber")),
                new tableDataTemplate("Match Number", stringValue(finalizationStore, "matchNumber")),
                new tableDataTemplate("Starting Color/Position", stringValue(finalizationStore, "startingColorAndPosition")),
                new tableDataTemplate("Preloaded Coral?", yesNo(finalizationStore, "preloadedCoral"))
        );
        setup_TableView.getColumns().clear();
        setup_TableView.getColumns().addAll(setupNameColumn, setupValueColumn);
        Platform.runLater(() -> {
            setup_TableView.setItems(setupData);
            setup_TableView.refresh();
        });
    }

    @FXML
    void backToSetupButtonClick(ActionEvent event) {
        PrimaryWindow.switchScene("Setup.fxml");
    }

    @FXML
    void viewQRCodeButtonClick(ActionEvent event) {
        SceneDataStore finalizationStore = SceneDataStore.getInstance();

        String matchNumber = stringValue(finalizationStore, "matchNumber");
        String startingColorAndPosition = stringValue(finalizationStore, "startingColorAndPosition");

        // One tab-separated row of the Setup fields, ready to paste into a spreadsheet
        String qrCodeScannedDataEntry = "\t" + String.join("\t",
                stringValue(finalizationStore, "scouterName"),
                stringValue(finalizationStore, "teamNumber"),
                matchNumber,
                startingColorAndPosition,
                yesNo(finalizationStore, "preloadedCoral"));

        String qrCodeFileName = "qrcode_M" + matchNumber + "_" + startingColorAndPosition + ".png";
        String qrCodeLocationDirectory = "dynamicformbuilder/qr_codes/" + qrCodeFileName;
        System.out.println("Your data reads: " + qrCodeScannedDataEntry);

        PrimaryWindow.generateQRcode(qrCodeScannedDataEntry, qrCodeFileName, qrCodeLocationDirectory, 505, 505);

        PrimaryWindow.switchScene("QRCode.fxml");
    }

    // Returns the stored value as a string, or "" if the field was never filled in
    private static String stringValue(SceneDataStore store, String key) {
        Object value = store.getValue(key);
        return value == null ? "" : value.toString();
    }

    // Returns "Yes" or "No" for a checkbox value (unset counts as "No")
    private static String yesNo(SceneDataStore store, String key) {
        return Boolean.TRUE.equals(store.getValue(key)) ? "Yes" : "No";
    }

}
