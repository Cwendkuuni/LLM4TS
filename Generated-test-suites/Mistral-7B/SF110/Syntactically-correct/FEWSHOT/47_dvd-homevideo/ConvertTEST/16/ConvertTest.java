package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testMatrix() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {});
        converter.matrix();

        File file = new File(converter.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                ++count;
                if (count <= 14) {
                    String[] tokens = line.split(",");
                    for (String token : tokens) {
                        assertTrue(Integer.valueOf(token) > 0);
                    }
                } else {
                    String[] headers = line.split("#");
                    assertEquals(1, headers.length);
                    String header = headers[0];
                    assertEquals("# High resolution INTRA table\n", header);
                }
            }
        } catch (IOException e) {
            fail("Failed to read matrix.txt file.");
        }

        file.delete();
    }

    @Test
    public void testTranscodeInitNoThread() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {});
        assertFalse(converter.init());
        assertTrue(converter.m_Error);
    }

    @Test
    public void testTranscodeInitWithThread() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {});
        converter.m_Thread = new Thread(converter);
        converter.m_Thread.start();
        try {
            converter.m_Thread.join();
        } catch (InterruptedException e) {
            fail("Thread was interrupted.");
        }
        assertFalse(converter.init());
        assertTrue(converter.m_Error);
    }

    @Test
    public void testTranscodeInitSuccess() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {});
        assertFalse(converter.init());
        assertFalse(converter.m_GUI.ErrorCheck(converter.m_GUI.strOutputDir + "/log/transcode.log"));
        assertFalse(converter.m_GUI.ErrorCheck(converter.m_GUI.strOutputDir + "/log/mplex.log"));
        assertTrue(converter.init());
        assertFalse(converter.m_Error);
    }

    @Test
    public void testTranscodeRun() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {});
        converter.init();
        converter.run();
        // Add more assertions to check the state of the program after transcode run.
    }
}