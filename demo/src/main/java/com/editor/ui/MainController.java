package com.editor.ui;

import com.editor.compression.Compressor;
import com.editor.io.FileManager;
import com.editor.structures.xml.XmlDocument;
import com.editor.xml.converter.XmlToJson;
import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.parser.XmlParser;
import com.editor.xml.parser.XmlValidator;
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

    @FXML private void handleBrowseFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File file = chooser.showOpenDialog(getStage());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            statusLabel.setText("File selected: " + file.getName());
        }
    }

    @FXML private void handleLoadFile() {

        String filePath = filePathField.getText();
        if (filePath == null || filePath.isEmpty()) {
            statusLabel.setText("Please enter a file name");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            statusLabel.setText("File Not Found");
            return;
        }
        String content = FileManager.readFile(file.getAbsolutePath());
        if (content == null) {
            statusLabel.setText("Error reading file");
            return;
        }
        inputTextArea.setText(content);
        statusLabel.setText("File Loaded: " + file.getName());
    }

    @FXML private void handleVerify() {
        // TODO: call XmlValidator (validate only)
        statusLabel.setText("Verified");
    }

    @FXML private void handleVerifyFix() {
        // TODO: validator with fix
        statusLabel.setText("Verified & fixed");
    }

    @FXML private void handleFormat() {
        XmlDocument xmlDocument = getDocument();
        if (xmlDocument == null) {
            statusLabel.setText("Error reading file");
            return;
        }

        XmlFormatter  xmlFormatter = new XmlFormatter();
        String xmlString = xmlFormatter.format(xmlDocument);

        outputTextArea.setText(xmlString);
        statusLabel.setText("Formatted");
    }

    @FXML private void handleMinify() {
        XmlDocument xmlDocument = getDocument();
        if (xmlDocument == null) {
            statusLabel.setText("Error reading file");
            return;
        }

        XmlMinifier xmlMinifier = new XmlMinifier();
        String  xmlString = xmlMinifier.minify(xmlDocument);

        outputTextArea.setText(xmlString);
        statusLabel.setText("Minified");
    }

    @FXML private void handleToJson() {
        XmlDocument document = getDocument();
        if (document == null) {
            statusLabel.setText("Error creating XML document from file");
            return;
        }
        XmlToJson xmlToJson = new XmlToJson();
        String json = xmlToJson.toJson(document);
        outputTextArea.setText(json);
        statusLabel.setText("JSON Parsed");
    }

    @FXML private void handleCompress() {
        XmlDocument document = getDocument();
        if (document == null) {
            statusLabel.setText("Error creating XML document from file");
            return;
        }

        Compressor compressor = new Compressor();
        String compressed = compressor.compress(document);
        outputTextArea.setText(compressed);
        statusLabel.setText("Compressed");
    }

    @FXML private void handleDecompress() {
        String compressed = inputTextArea.getText();
        if (compressed == null || compressed.trim().isEmpty()) {
            statusLabel.setText("No compressed file found");
            return;
        }
        Compressor compressor = new Compressor();
        String xml = compressor.decompress(compressed);
        outputTextArea.setText(xml);
        statusLabel.setText("Decompressed");
    }

    @FXML private void handleSaveOutput() {
        String output = outputTextArea.getText();
        if (output == null || output.trim().isEmpty()) {
            statusLabel.setText("No output to save");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showSaveDialog(getStage());
        if (file == null) {
            statusLabel.setText("Save Failed");
            return;
        }
        FileManager.writeFile(file.getAbsolutePath(), output);
        statusLabel.setText("File Saved: " + file.getName());
    }

    @FXML private void handleClear() {
        inputTextArea.clear();
        outputTextArea.clear();
        statusLabel.setText("Cleared");
    }

    private Stage getStage() {
        return (Stage) inputTextArea.getScene().getWindow();
    }

    private XmlDocument getDocument() {
        String xml = inputTextArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            statusLabel.setText("No XML added");
            return null;
        }
        // TODO: Check how the errors are handled
        XmlValidator xmlValidator = new XmlValidator();
        String fixedxml = xmlValidator.fix(xml);

        XmlParser xmlParser = new XmlParser();
        XmlDocument document = xmlParser.parse(fixedxml);
        if (!xmlParser.getErrors().isEmpty()) {
            StringBuilder out = new StringBuilder();
            out.append("There were errors while parsing the XML file.\n");

            for (int i = 0; i < xmlParser.getErrors().size(); i++) {
                out.append("Line ").append(xmlParser.getErrorLineNumbers().get(i)).append(":").append(xmlParser.getErrors().get(i)).append("\n");
            }
            outputTextArea.setText(out.toString());
            statusLabel.setText("Error Parsing XML");
            return null;
        }
        return document;
    }
}
