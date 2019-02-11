package de.codecentric.dk;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class MainAppInterfaceController extends BorderPane {

    @FXML
    private Button start;

    @FXML
    private Button pause;

    @FXML
    private Button resume;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea infoArea;

    private SpeechRecognizer speechRecognition;

    {
        try {
            speechRecognition = new SpeechRecognizer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {

        start.disableProperty().bind(speechRecognition.speechRecognizerThreadRunningProperty());
        start.setOnAction(a -> {
           statusLabel.setText("Status : [Running]");
           speechRecognition.startSpeechRecognition();
        });

        pause.disableProperty().bind(speechRecognition.ignoreSpeechRecognitionResultsProperty().or(start.disabledProperty().not()));
        pause.setOnAction(a -> {
            statusLabel.setText("Status : [Paused]");
            speechRecognition.ignoreSpeechRecognitionResults();
        });

        resume.disableProperty().bind(speechRecognition.ignoreSpeechRecognitionResultsProperty().not());
        resume.setOnAction(a -> {
            statusLabel.setText("Status : [Running]");
            speechRecognition.stopIgnoreSpeechRecognitionResults();
        });

        infoArea.textProperty().bind(Bindings.createStringBinding(() -> infoArea.getText() + "\n" + speechRecognition.getSpeechRecognitionResultProperty().get(), speechRecognition.getSpeechRecognitionResultProperty()));
    }

    @Override
    public Node getStyleableNode() {
        return null;
    }
}
