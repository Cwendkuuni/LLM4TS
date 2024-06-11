package org.apache.commons.mp3; // assure the test class is in the same package as MP3 class

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MP3Test {

    private File testFile;
    private AudioInputStream testIn;
    private AudioInputStream testDin;
    private MP3 testMP3;

    @Before
    public void setup() {
        testFile = new File("test.mp3");
        testIn = mock(AudioInputStream.class);
        testDin = mock(AudioInputStream.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(new MP3("test.mp3"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullFilename() {
        new MP3(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyFilename() {
        new MP3("");
    }

    @Test(expected = FileNotFoundException.class)
    public void testRunFileNotFound() {
        when(testIn.getFormat()).thenThrow(new FileNotFoundException());
        new MP3("test.mp3");
    }

    @Test
    public void testRun() throws Exception {
        when(testIn.getFormat()).thenReturn(new AudioFormat(1, 44100, 16, 1, 2, 44100, false));
        doReturn(testIn).when(this).getIn();
        doReturn(testDin).when(this).getDin();

        testMP3 = new MP3("test.mp3");

        testMP3.join();

        verify(testIn).close();
        verify(testDin).close();
    }

    @Test
    public void testRunIOException() throws Exception {
        when(testIn.getFormat()).thenReturn(new AudioFormat(1, 44100, 16, 1, 2, 44100, false));
        doThrow(IOException.class).when(testDin).close();
        doReturn(testIn).when(this).getIn();
        doReturn(testDin).when(this).getDin();

        testMP3 = new MP3("test.mp3");

        testMP3.join();

        verify(testIn, times(2)).close();
        verify(testDin, times(1)).close();
    }

    @Test
    public void testRunNullDin() {
        when(testIn.getFormat()).thenReturn(new AudioFormat(1, 44100, 16, 1, 2, 44100, false));
        doReturn(testIn).when(this).getIn();
        doReturn(null).when(this).getDin();

        testMP3 = new MP3("test.mp3");

        testMP3.join();

        verify(testDin, never()).close();
    }

    @Test
    public void testRunEmptyDin() {
        when(testIn.getFormat()).thenReturn(new AudioFormat(1, 44100, 16, 1, 2, 44100, false));
        doReturn(new ByteArrayInputStream(new byte[0])).when(testDin).read(anyByteArray(), anyInt(), anyInt());
        doReturn(testIn).when(this).getIn();
        doReturn(testDin).when(this).getDin();

        testMP3 = new MP3("test.mp3");

        testMP3.join();

        verify(testDin, times(1)).close();
    }

    @Test
    public void testRunDinClosed() throws Exception {
        when(testIn.getFormat()).thenReturn(new AudioFormat(1, 44100, 16, 1, 2, 44100, false));
        doReturn(testIn).when(this).getIn();
        doReturn(testDin).when(this).getDin();
        doReturn(testDin).when(testDin).isClosed();

        testMP3 = new MP3("test.mp3");

        testMP3.join();

        verify(testDin, times(1)).close();
    }

    @Test
    public void testRunFileIsDirectory() {
        when(testIn.getFormat()).thenThrow(new IOException("Directory exists"));
        new MP3("test.dir");
    }

    @Test
    public void testRunFileIsNotReadable() {
        when(testIn.getFormat()).thenThrow(new SecurityException("File is not readable"));
        new MP3("test.mp3");
    }

    @Test
    public void testRunInvalidFormat() {
        when(testIn.getFormat()).thenReturn(new AudioFormat(1, 44100, 1, 1, 2, 44100, false));
        new MP3("test.mp3");
    }
}