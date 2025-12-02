package com.editor.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    @FXML private TextField filePathField;
    @FXML private TextArea inputTextArea;
    @FXML private TextArea outputTextArea;
    @FXML private Label statusLabel;

    // ---------- FILE OPERATIONS ----------
    @FXML private void handleBrowseFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File file = chooser.showOpenDialog(getStage());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            statusLabel.setText("File selected: " + file.getName());
        }
    }

    @FXML private void handleLoadFile() {
        statusLabel.setText("File loaded (connect FileManager here)");
        // TODO: load file content into inputTextArea
    }

    // ---------- OPERATIONS ----------
    @FXML private void handleVerify() {
        // TODO: call XmlValidator (validate only)
        statusLabel.setText("Verified");
    }

    @FXML private void handleVerifyFix() {
        // TODO: validator with fix
        statusLabel.setText("Verified & fixed");
    }

    @FXML private void handleFormat() {
        // TODO: XmlFormatter
        statusLabel.setText("Formatted");
    }

    @FXML private void handleMinify() {
        // TODO: XmlMinifier
        statusLabel.setText("Minified");
    }

    @FXML private void handleToJson() {
        // TODO: XmlToJson
        statusLabel.setText("Converted to JSON");
    }

    @FXML private void handleCompress() {
        // TODO: Compressor
        statusLabel.setText("Compressed");
    }

    @FXML private void handleDecompress() {
        // TODO: Decompressor
        statusLabel.setText("Decompressed");
    }

    @FXML private void handleSaveOutput() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text/XML/JSON", "*.*"));
        File file = chooser.showSaveDialog(getStage());
        if (file != null) {
            // TODO: save outputTextArea.getText()
            statusLabel.setText("Output saved");
        }
    }

    @FXML private void handleClear() {
        inputTextArea.clear();
        outputTextArea.clear();
        statusLabel.setText("Cleared");
    }

    private Stage getStage() {
        return (Stage) inputTextArea.getScene().getWindow();
    }
}
