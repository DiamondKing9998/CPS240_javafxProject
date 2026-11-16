package com.dynamicformbuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class SetupController {
    @FXML
    private Button setupSettingsButton;
    @FXML
    private Button setupAddJsonButton; 
    @FXML
    private ChoiceBox<String> setupJsonSelector;
    @FXML
    private Label setupJsonFileName;

    private List<Path> pathJsonFiles;
    private String[] stringJsonFiles;
    private Path jsonDirectory = Path.of("dynamicformbuilder/json_configs/").toAbsolutePath();

    @FXML
    public void initialize() {
        //SceneDataStore initiation
        SceneDataStore setupStore = SceneDataStore.getInstance();

        //Define JSON files to choose from 
        try (Stream<Path> stream = Files.list(jsonDirectory)) {
            pathJsonFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                        .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stringJsonFiles = new String[pathJsonFiles.size()];
        //Add JSON files to the ChoiceBox list
        for (int i = 0; i < pathJsonFiles.size(); i++) {
            stringJsonFiles[i] = pathJsonFiles.get(i).getFileName().toString();
        }

        //Modify anything not directly modifiable in SceneBuilder
        setupJsonSelector.getItems().addAll(stringJsonFiles);
        setupJsonSelector.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 12px; -fx-font-weight: regular;");


        // Restore any saved data
        // not needed atm (??)

        //Save any inputted data


    }

    @FXML
    void toJsonButtonClick(ActionEvent event) {
        setupJsonFileName.setText("[" + setupJsonSelector.getValue() + "] selected");
        PrimaryWindow.changeWindowTitle("DynamicFormBuilder - [" + setupJsonSelector.getValue() + "] selected");
        // WILL BE USED IN FUTURE
        // PrimaryWindow.switchScene("JSON_PAGE_TEMPLATE.fxml");
    }

}