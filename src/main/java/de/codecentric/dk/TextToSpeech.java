package de.codecentric.dk;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TextToSpeech {

    private AudioPlayer audioPlayer;
    private MaryInterface marytts;


    public TextToSpeech() {
        try {
            marytts = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", e);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "IO Exception", e);
        } catch (InterruptedException e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Interrupted ", e);
        }
    }
}
