package org.apache.commons.mp3; // Assuming the package name for the MP3 class is org.apache.commons.mp3

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testMP3Construction() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
        assertNull(mp3.in);
        assertNull(mp3.din);
        assertEquals("test.mp3", mp3.filename);
    }

    @Test(expected = NullPointerException.class)
    public void testMP3NullFilename() {
        new MP3(null);
    }

    @Test
    public void testMP3Play() throws Exception {
        MP3 mp3 = new MP3("test.mp3");
        mp3.join();
    }

    @Test(expected = IOException.class)
    public void testMP3InvalidFile() {
        MP3 mp3 = new MP3("non_existing_file.mp3");
        mp3.join();
    }

    @Test
    public void testMP3Format() throws Exception {
        MP3 mp3 = new MP3("test.mp3");
        mp3.join();

        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
    }

    @Test
    public void testMP3ThreadInterruption() throws Exception {
        MP3 mp3 = new MP3("test.mp3");
        Thread interrupter = new Thread(() -> mp3.interrupt());
        interrupter.start();
        mp3.join(100);
        interrupter.join();

        assertNull(mp3.in);
        assertNull(mp3.din);
    }
}