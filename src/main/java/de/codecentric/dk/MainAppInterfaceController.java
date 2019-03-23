package de.codecentric.dk;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;



public class MainAppInterfaceController extends BorderPane {

    @FXML
    private Button start;

    @FXML
    private Button stop;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea infoArea;

    private DukeSpeech dukeSpeech = new DukeSpeech();


    @FXML
    private void initialize() {

        start.setOnAction(a -> {
           statusLabel.setText("Status : [Running]");
           infoArea.appendText("Speech Recognizer started\n");
           dukeSpeech.startSpeechThread();
        });

        stop.setOnAction(a -> {
            statusLabel.setText("Status : [Stopped]");
            infoArea.appendText("Speech Recognizer stopped\n");
            dukeSpeech.stopSpeechThread();
        });


    }

    @Override
    public Node getStyleableNode() {
        return null;
    }
}
