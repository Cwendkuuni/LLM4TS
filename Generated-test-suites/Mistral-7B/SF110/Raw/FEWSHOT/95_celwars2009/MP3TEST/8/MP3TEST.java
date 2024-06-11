package org.apache.commons.mp3; // Assuming the package name is org.apache.commons.mp3 for the MP3 class

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String validFileName = "test.mp3";
        MP3 mp3 = new MP3(validFileName);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertTrue(mp3.isAlive());
    }

    @Test(expected = NullPointerException.class)
    public void testMP3ConstructorNullFilename() {
        new MP3(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3ConstructorInvalidFilename() {
        String invalidFileName = "does_not_exist.mp3";
        new MP3(invalidFileName);
    }

    @Test
    public void testMP3Run() {
        String validFileName = "test.mp3";
        MP3 mp3 = new MP3(validFileName);
        mp3.join(); // Wait for the thread to finish
    }

    @Test
    public void testMP3Close() {
        String validFileName = "test.mp3";
        MP3 mp3 = new MP3(validFileName);
        mp3.join();
        assertNull(mp3.in);
        assertNull(mp3.din);
    }
}