package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInitWithNoThread() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        assertTrue(convert.init());
    }

    @Test
    public void testInitWithThread() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI(), true);
        Thread thread = convert.m_Thread;
        assertTrue(thread.isAlive());

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(convert.init());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeCommandWithInvalidDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File invalidDir = new File("invalid_directory");
        convert.video_files = invalidDir.list();
        convert.Transcode();
    }

    @Test
    public void testTranscodeCommandWithValidDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File validDir = new File("valid_directory");
        convert.video_files = validDir.list();
        convert.Transcode();
    }

    @Test
    public void testTranscodeSuccess() throws IOException, InterruptedException, NoSuchMethodException, InvocationTargetException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File validDir = new File("valid_directory");
        convert.video_files = validDir.list();
        convert.Transcode();

        Process p = Runtime.getRuntime().exec("/bin/sh -c \"grep -c 'encoding' transcode.log\"");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        assertEquals("1", in.readLine());
        in.close();
    }

    @Test
    public void testTranscodeFailure() throws IOException, InterruptedException, NoSuchMethodException, InvocationTargetException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File invalidDir = new File("invalid_directory");
        convert.video_files = invalidDir.list();
        convert.Transcode();

        Process p = Runtime.getRuntime().exec("/bin/sh -c \"grep -c 'encoding' transcode.log\"");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        assertNull(in.readLine());
        in.close();

        assertTrue(convert.m_Error);
    }

    @Test
    public void testMplexCommand() throws IOException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File validDir = new File("valid_directory");
        convert.video_files = validDir.list();
        convert.Transcode();

        Process p = Runtime.getRuntime().exec("/bin/sh -c \"grep -c 'Input file' mplex.log\"");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        int count = 0;
        while (in.ready()) {
            String line = in.readLine();
            if (line.contains("Input file")) {
                count++;
            }
        }
        in.close();

        assertEquals(validDir.list().length, count);
    }

    @Test
    public void testErrorHandling() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File invalidDir = new File("invalid_directory");
        convert.video_files = invalidDir.list();
        convert.Transcode();

        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck("transcode.log") || convert.m_GUI.ErrorCheck("mplex.log"));
    }
}