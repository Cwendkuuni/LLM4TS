package org.apache.commons.cli; // Assuming the MP3 class is in the same package as Util for test sake

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.junit.Before;
import org.junit.Test;

public class MP3Test {

    private String filename;
    private MP3 mp3;
    private AudioInputStream mockedIn;
    private AudioInputStream mockedDin;
    private DataLine mockedLine;

    @Before
    public void setUp() {
        filename = "test.mp3";
        mockedIn = mock(AudioInputStream.class);
        mockedDin = mock(AudioInputStream.class);
        mockedLine = mock(DataLine.class);
    }

    @Test
    public void testMP3() {
        when(mockedIn.getFormat()).thenReturn(new AudioFormat(1, 8000, 16, 1, 16, 8000, false));

        when(AudioSystem.getAudioInputStream(new File(filename))).thenReturn(mockedIn);
        when(AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 16, 8000, false), mockedIn)).thenReturn(mockedDin);
        when(AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, new AudioFormat(1, 8000, 16, 1, 16, 8000, false)))).thenReturn(mockedLine);

        mp3 = new MP3(filename);
        mp3.run();

        verifyAudioStreams();
    }

    @Test(expected = NullPointerException.class)
    public void testMP3WithNullFilename() {
        new MP3(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3WithEmptyFilename() {
        new MP3("");
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3WithNonExistentFile() {
        when(mockedIn.getFormat()).thenThrow(new FileNotFoundException());
        new MP3(filename);
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testMP3WithUnsupportedFile() {
        when(mockedIn.getFormat()).thenReturn(new AudioFormat(AudioFormat.Encoding.MP3, 8000, 16, 1, 16, 8000, false));
        new MP3(filename);
    }

    @Test(expected = IOException.class)
    public void testMP3WithIOException() {
        when(mockedDin.close()).thenThrow(new IOException());
        new MP3(filename);
    }

    @Test
    public void testMP3WithLineUnavailableException() {
        when(mockedLine.open(any(AudioFormat.class))).thenThrow(new LineUnavailableException());
        new MP3(filename);
    }

    private void verifyAudioStreams() {
        assertFalse(mockedIn.isClosed());
        assertFalse(mockedDin.isClosed());

        assertSame(mockedIn, mp3.in);
        assertSame(mockedDin, mp3.din);
        assertSame(filename, mp3.filename);
    }
}