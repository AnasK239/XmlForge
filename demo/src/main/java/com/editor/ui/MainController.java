package com.editor.ui;

import com.editor.compression.Compressor;
import com.editor.io.FileManager;
import com.editor.structures.xml.XmlDocument;
import com.editor.xml.converter.XmlToJson;
import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.parser.XmlParser;
import com.editor.xml.parser.XmlValidator;
import com.editor.xml.converter.JsonToXml;
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
    @FXML private ComboBox<String> typeComboBox;

    @FXML private void initialize() {
        typeComboBox.getItems().addAll("XML", "JSON");
        typeComboBox.getSelectionModel().select(0);
    }

    @FXML private void handleBrowseFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML or JSON or TXT Files", "*.xml", "*.json", "*.txt"));

        File file = chooser.showOpenDialog(getStage());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            statusLabel.setText("File selected: " + file.getName());
        }
    }

    @FXML private void handleLoadFile() {
        String filePath = filePathField.getText();
        if (filePath == null || filePath.isEmpty()) {
            statusLabel.setText("Please enter a file path");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            statusLabel.setText("File Not Found");
            return;
        }
        if (!getType(file).equals("TXT")) {
            typeComboBox.getSelectionModel().select(getType(file));
        }
        String content = FileManager.readFile(file.getAbsolutePath());

        inputTextArea.setText(content);
        statusLabel.setText("File Loaded: " + file.getName());
    }

    @FXML private void handleVerify() {
        String xml = inputTextArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            statusLabel.setText("No XML added");
            return;
        }
        XmlParser xmlParser = new XmlParser();
        xmlParser.parse(xml);
        if (xmlParser.getErrors().isEmpty()){
            outputTextArea.setText("Document has been verified and valid");
            statusLabel.setText("Valid Xml");
            return;
        }
        else {
            String[] lines = xml.split("\n", -1);
            StringBuilder out = new StringBuilder();

            for (int i = 0; i < lines.length; i++) {
                int lineNo = i + 1;
                out.append(lines[i]);

                for (int j = 0; j < xmlParser.getErrors().size(); j++) {
                    if (xmlParser.getErrorLineNumbers().get(j) == lineNo) {
                        out.append("  ← ").append(xmlParser.getErrors().get(j));
                        break;
                    }
                }
                out.append("\n");
            }
            outputTextArea.setText(out.toString());
            statusLabel.setText("Invalid XML");
        }
    }

    @FXML private void handleVerifyFix() {
        String xml = inputTextArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            statusLabel.setText("No XML added");
            return;
        }
        XmlValidator xmlValidator = new XmlValidator();
        String fixedXml = xmlValidator.fix(xml);

        XmlParser xmlParser = new XmlParser();
        XmlDocument document = xmlParser.parse(fixedXml);

        if (xmlParser.getErrors().isEmpty()){
            XmlFormatter  xmlFormatter = new XmlFormatter();
            String xmlString = xmlFormatter.format(document);
            outputTextArea.setText(xmlString);
            statusLabel.setText("Verified & fixed");
        }
        else {
            StringBuilder out = new StringBuilder();
            out.append("There is errors that can't be handled.\n");

            for (int i = 0; i < xmlParser.getErrors().size(); i++) {
                out.append("Line ").append(xmlParser.getErrorLineNumbers().get(i)).append(":").append(xmlParser.getErrors().get(i)).append("\n");
            }
            outputTextArea.setText(out.toString());
            statusLabel.setText("Error: Can't fix");
        }
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
        String xmlString = xmlMinifier.minify(xmlDocument);

        outputTextArea.setText(xmlString);
        statusLabel.setText("Minified");
    }

    @FXML private void handleToJson() {
        if (typeComboBox.getSelectionModel().getSelectedItem().equals("JSON")) {
            statusLabel.setText("The file is already JSON.");
            return;
        }
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

    @FXML private void handleToXml() {
        if (typeComboBox.getSelectionModel().getSelectedItem().equals("XML")) {
            statusLabel.setText("The file is already XML.");
            return;
        }
        String json = inputTextArea.getText();
        if (json == null || json.isEmpty()) {
            statusLabel.setText("Empty field");
            return;
        }
        JsonToXml jsonToXml = new JsonToXml();
        XmlDocument document = jsonToXml.toXmlDocument(json);

        XmlFormatter  xmlFormatter = new XmlFormatter();
        String xmlString = xmlFormatter.format(document);

        outputTextArea.setText(xmlString);
        statusLabel.setText("XML Parsed");
    }

    @FXML private void handleCompress() {
        Compressor compressor = new Compressor();
        String type = typeComboBox.getValue();
        if ("JSON".equals(type)) {
            String jsonString = compressor.compressJson(inputTextArea.getText());
            outputTextArea.setText(jsonString);
            statusLabel.setText("Compressed");
            saveOutput(true);
        }
        else if ("XML".equals(type)) {
            XmlDocument document = getDocument();
            if (document == null) {
                return;
            }
            String compressed = compressor.compress(document);
            outputTextArea.setText(compressed);
            statusLabel.setText("Compressed");
            saveOutput(true);
        }
        else {
            statusLabel.setText("Compression not supported");
        }
    }

    @FXML private void handleDecompress() {
        Compressor compressor = new Compressor();
        String type = typeComboBox.getValue();
        String input = inputTextArea.getText();
        if (input == null || input.trim().isEmpty()) {
            statusLabel.setText("No compressed file found");
            return;
        }
        if ("JSON".equals(type)) {
            String jsonString = compressor.decompressToJson(input);
            outputTextArea.setText(jsonString);
            statusLabel.setText("Decompressed");
        }
        else if ("XML".equals(type)) {
            String xml = compressor.decompress(input);
            outputTextArea.setText(xml);
            statusLabel.setText("Decompressed");
        }
        else {
            statusLabel.setText("Choose Either JSON or XML");
        }
    }

    @FXML private void handleSaveOutput() {
        saveOutput(false);
    }

    @FXML private void handleClear() {
        inputTextArea.clear();
        outputTextArea.clear();
        statusLabel.setText("Cleared");
    }

    @FXML private void handleMoveOutToIn() {
        String out = outputTextArea.getText();
        if (out == null || out.trim().isEmpty()) {
            statusLabel.setText("Empty field");
            return;
        }
        inputTextArea.setText(out);
        outputTextArea.setText("Moved text.");
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

        XmlParser xmlParser = new XmlParser();
        XmlDocument document = xmlParser.parse(xml);

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

    private String getType(File file) {
        String name = file.getName().toUpperCase();
        int index = name.lastIndexOf('.');
        return name.substring(index + 1).toUpperCase();
    }

    private void saveOutput(boolean TXT) {
        String output = outputTextArea.getText();
        if (output == null || output.trim().isEmpty()) {
            statusLabel.setText("No output to save");
            return;
        }

        FileChooser chooser = new FileChooser();
        if (TXT) {
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            chooser.setInitialFileName("output.txt");
        } else {
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                    new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            chooser.setInitialFileName("output");
        }

        File file = chooser.showSaveDialog(getStage());
        if (file == null) {
            statusLabel.setText("Save Failed");
            return;
        }
        FileManager.writeFile(file.getAbsolutePath(), output);
        statusLabel.setText("File Saved: " + file.getName());
    }

}
