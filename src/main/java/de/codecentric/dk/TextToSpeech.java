package de.codecentric.dk;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.util.data.audio.AudioPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Collection;


public class TextToSpeech {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private AudioPlayer audioPlayer;
    private MaryInterface marytts;

    public TextToSpeech() {
        try {
            marytts = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            logger.debug("Exception thrown", e);
        }
    }

    public Collection<Voice> getAvailableVoices() {
        return Voice.getAvailableVoices();
    }

    public void setVoice(String voice) {
        marytts.setVoice(voice);
    }


    public void speak(String text, boolean daemon, boolean join) {
        if (audioPlayer != null) {
            audioPlayer.cancel();
        }

        try (AudioInputStream audioInputStream = marytts.generateAudio(text)) {
            audioPlayer = new AudioPlayer();
            audioPlayer.setAudio(audioInputStream);
            audioPlayer.setDaemon(daemon);
            audioPlayer.start();
            if (join)
                audioPlayer.join();
        } catch (SynthesisException e) {
            logger.warn("Error saying phrase.", e);
        } catch (IOException e) {
            logger.warn("IO Exception", e);
        } catch (InterruptedException e) {
            logger.warn("Interrupted ", e);
        }
    }
}
