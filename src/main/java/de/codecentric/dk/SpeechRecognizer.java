package de.codecentric.dk;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SpeechRecognizer {

    private LiveSpeechRecognizer liveSpeechRecognizer;

    private String speechRecognitionResult;

    private StringProperty speechRecognitionResultProperty = new SimpleStringProperty("");

    private SimpleBooleanProperty ignoreSpeechRegnitionResults = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty speechRecognizerThreadRunning = new SimpleBooleanProperty(false);

    private boolean resourcesThreadRunning;

    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);

    public SpeechRecognizer() throws IOException {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");


        liveSpeechRecognizer = new LiveSpeechRecognizer(configuration);

        startRecouresThread();
    }

    public synchronized void startSpeechRecognition() {

        if (speechRecognizerThreadRunning.get()) {

        } else {
            eventsExecutorService.submit(() -> {
                Platform.runLater(() -> {
                    speechRecognizerThreadRunning.set(true);
                    ignoreSpeechRegnitionResults.set(false);
                });

                liveSpeechRecognizer.startRecognition(true);

                try {
                    while (speechRecognizerThreadRunning.get()) {
                        SpeechResult speechResult = liveSpeechRecognizer.getResult();

                        if (!ignoreSpeechRegnitionResults.get()) {
                            if (speechResult == null) {

                            } else {
                                speechRecognitionResult = speechResult.getHypothesis();

                                System.out.println("You said : [" + speechRecognitionResult + "]");

                                Platform.runLater(() -> speechRecognitionResultProperty.set(speechRecognitionResult));
                                makeDecision(speechRecognitionResult, speechResult.getWords());
                            }
                        } else {

                        }
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> speechRecognizerThreadRunning.set(false));
                }
            });
        }


    }

    public synchronized void stopIgnoreSpeechRecognitionResults() {
        Platform.runLater(() -> ignoreSpeechRegnitionResults.set(false));
    }

    public synchronized void ignoreSpeechRecognitionResults() {
        Platform.runLater(() -> ignoreSpeechRegnitionResults.set(true));
    }

    private void startRecouresThread() {
        if (resourcesThreadRunning) {

        } else {
            eventsExecutorService.submit(() -> {
                try {
                    resourcesThreadRunning = true;
                    while (true) {
                        if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {

                        }
                        Thread.sleep(350);
                    }
                } catch (InterruptedException e) {
                    resourcesThreadRunning = false;
                }
            });
        }
    }

    private void makeDecision(String speechResult, List<WordResult> words) {
        System.out.println(speechResult);
    }

    public SimpleBooleanProperty ignoreSpeechRecognitionResultsProperty() {
        return ignoreSpeechRegnitionResults;
    }

    public SimpleBooleanProperty speechRecognizerThreadRunningProperty() {
        return speechRecognizerThreadRunning;
    }

    public StringProperty getSpeechRecognitionResultProperty() {
        return speechRecognitionResultProperty;
    }
}
