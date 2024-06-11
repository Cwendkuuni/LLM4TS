package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConvertTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testConvertConstructor() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        assertTrue(convert.m_Error == false);
        assertTrue(convert.m_Format.equals("dvd-ntsc"));
        assertTrue(convert.m_AspectRatio.equals("4:3"));
        assertNull(convert.m_GUI);
        assertEquals(0, convert.thread_track);
    }

    @Test
    public void testConvertConstructorWithQuality() {
        Convert convert = new Convert(1, "dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        assertTrue(convert.m_Error == false);
        assertTrue(convert.m_Quality == 1);
        assertTrue(convert.m_Format.equals("dvd-ntsc"));
        assertTrue(convert.m_AspectRatio.equals("4:3"));
        assertNull(convert.m_GUI);
        assertEquals(0, convert.thread_track);
    }

    @Test
    public void testInitWithThreadCheckboxUnchecked() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        assertTrue(convert.init());
        assertTrue(convert.m_Error == false);
    }

    @Test
    public void testInitWithThreadCheckboxChecked() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        assertTrue(convert.init());
        assertTrue(convert.m_Thread != null);
        assertTrue(convert.m_Thread.isAlive());
        assertTrue(convert.m_Error == false);
    }

    @Test
    public void testTranscodeCommandFileReplacement() {
        File testDir = new File("testOutputDir");
        testDir.mkdir();
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        File[] listFiles = testDir.listFiles();
        String firstFile = listFiles[0].getName();
        convert.Transcode();
        File[] newListFiles = testDir.listFiles();
        String expectedFile = firstFile.substring(0, 11) + "_transcoded";
        assertTrue(Arrays.stream(newListFiles).anyMatch(file -> file.getName().equals(expectedFile)));
    }

    @Test
    public void testTranscodeCommandExecution() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Could not locate transcode in your path.*");
        convert.Transcode();
    }

    @Test
    public void testMplexCommandExecution() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Could not locate mplex in your path.*");
        convert.Transcode();
    }

    @Test
    public void testInitWithInvalidFilesInDVDDirectory() {
        File testDir = new File("testOutputDir");
        testDir.mkdir();
        testDir.mkdir("dv");
        testDir.mkdir("dv/dv");
        testDir.mkdir("dv/dv/inp");
        testDir.mkdir("dv/dv/inp/dv");
        testDir.mkdir("dv/dv/inp/dv/dv");
        testDir.mkdir("dv/dv/inp/dv/dv/test");
        testDir.mkdir("dv/dv/inp/dv/dv/test/dv");
        testDir.mkdir("dv/dv/inp/dv/dv/test/dv/dv");
        testDir.mkdir("dv/dv/inp/dv/dv/test/dv/dv/test");
        testDir.mkdir("dv/dv/inp/dv/dv/test/dv/dv/test/test");
        testDir.mkdir("dv/dv/inp/dv/dv/test/dv/dv/test/test/test");
        testDir.mkdir("dv/dv/inp/dv/dv/test/dv/dv/test/test/test/test");

        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            // Dummy implementation of the GUI class
        });
        expectedException.expect(StringIndexOutOfBoundsException.class);
        expectedException.expectMessage("Are there non-standard (not .dv) files in the dv directory?");
        convert.init();
    }
}