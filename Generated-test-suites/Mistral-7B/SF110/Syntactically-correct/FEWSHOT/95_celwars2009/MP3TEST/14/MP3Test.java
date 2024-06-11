package org.apache.commons.mp3; //Assuming the package name is org.apache.commons.mp3 for the MP3 class

import static org.junit.Assert.*;
import org.junit.Test;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testMP3Construction() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.filename);
        assertEquals(filename, mp3.filename);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3InvalidFilename() {
        MP3 mp3 = new MP3(null);
    }

    @Test
    public void testMP3Playing() throws Exception {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.join();
    }

    @Test(expected = IOException.class)
    public void testMP3IOException() {
        String filename = "nonexistent.mp3";
        MP3 mp3 = new MP3(filename);
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testMP3UnsupportedAudioFileException() {
        String filename = "test.wav"; // Assuming MP3 class does not support WAV files
        MP3 mp3 = new MP3(filename);
    }

    @Test
    public void testMP3Closing() throws Exception {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.join();
        mp3 = new MP3(filename); // Testing if the file is reopened when a new instance is created
        mp3.join();
    }
}