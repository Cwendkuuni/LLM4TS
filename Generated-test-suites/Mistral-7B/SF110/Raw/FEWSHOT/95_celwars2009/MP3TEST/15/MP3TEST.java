package org.apache.commons.mp3; // Assuming the package name for the MP3 class is org.apache.commons.mp3

import static org.junit.Assert.*;
import org.junit.Test;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertEquals(filename, mp3.filename);
    }

    @Test(expected = NullPointerException.class)
    public void testMP3ConstructorNullFilename() {
        new MP3(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3ConstructorInvalidFilename() {
        String filename = "invalid_file.mp3";
        new MP3(filename);
    }

    @Test
    public void testMP3ThreadStart() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testMP3ThreadJoin() throws InterruptedException, IOException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.join();
        assertNull(mp3.in);
        assertNull(mp3.din);
        assertEmpty(mp3.filename);
    }

    @Test
    public void testMP3ThreadInterrupt() throws InterruptedException, IOException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.interrupt();
        assertFalse(mp3.isAlive());
    }

    @Test
    public void testMP3ThreadStop() throws InterruptedException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.stop();
        assertFalse(mp3.isAlive());
    }

    private static boolean arrayEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static void assertEmpty(String str) {
        assertEquals("", str);
    }
}