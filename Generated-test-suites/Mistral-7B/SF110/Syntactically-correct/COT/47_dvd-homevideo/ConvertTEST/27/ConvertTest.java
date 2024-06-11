package com.example.convert;

import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormat() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = mock(GUI.class);

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);

        assertEquals(format, convert.m_Format);
        assertEquals(aspectRatio, convert.m_AspectRatio);
        assertNull(convert.m_Quality);
    }

    @Test
    public void testConvertConstructorWithFormatAndQuality() {
        int quality = 2;
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = mock(GUI.class);

        Convert convert = new Convert(quality, format, aspectRatio, DVD_GUI);

        assertEquals(quality, convert.m_Quality);
        assertEquals(format, convert.m_Format);
        assertEquals(aspectRatio, convert.m_AspectRatio);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertInitWithNullGUI() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = null;

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);

        convert.init();
    }

    @Test
    public void testConvertInitWithoutThreadJoin() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = mock(GUI.class);

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        convert.init();

        verify(DVD_GUI, times(1)).Transcode();
    }

    @Test
    public void testConvertInitWithThreadJoin() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = mock(GUI.class);

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        convert.init();

        try {
            convert.m_Thread.join();
        } catch (InterruptedException e) {
            fail("Thread should not be interrupted during join.");
        }

        verify(DVD_GUI, times(1)).MessageBox("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertTranscodeWithNullVideoFiles() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = mock(GUI.class);
        File listDir = null;

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        try {
            convert.Transcode();
        } catch (NullPointerException e) {
            Assert.assertEquals("ListDir is marked non-null but is null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testConvertTranscodeWithInvalidFormat() {
        String format = "invalid";
        String aspectRatio = "4:3";
        GUI DVD_GUI = mock(GUI.class);

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        try {
            convert.Transcode();
            fail("Expected exception during transcode execution.");
        } catch (UndeclaredThrowableException e) {
            String message = e.getPrevious().getMessage();
            assertTrue(message.contains("Error executing Runtime.getRuntime().exec(\"/bin/sh\", \"-c\", "));
            assertTrue(message.contains("transcode: command not found"));
        }
    }
}