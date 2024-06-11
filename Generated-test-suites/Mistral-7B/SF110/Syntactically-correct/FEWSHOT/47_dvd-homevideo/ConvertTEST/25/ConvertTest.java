package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class ConvertTest {

    @Test
    public void testConvertConstructor() {
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        assertNotNull(converter);
        assertEquals("dvd-ntsc", converter.m_Format);
        assertEquals("1:1", converter.m_AspectRatio);
        assertEquals(29.97, converter.m_fps, 0.01);
    }

    @Test
    public void testConvertConstructorWithQuality() {
        Convert converter = new Convert(1, "dvd-ntsc", "1:1", new GUI() {});
        assertNotNull(converter);
        assertEquals(1, converter.m_Quality);
        assertEquals("dvd-ntsc", converter.m_Format);
        assertEquals("1:1", converter.m_AspectRatio);
        assertEquals(29.97, converter.m_fps, 0.01);
    }

    @Test
    public void testInitNoThread() throws InterruptedException, IOException {
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        assertTrue(converter.init());
    }

    @Test
    public void testInitWithThread() throws InterruptedException, IOException {
        File dir = new File("test_output");
        dir.mkdir();
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        converter.init();
        Thread.sleep(1000); // give some time for the thread to start
        assertTrue(converter.m_Thread.isAlive());

        Thread.sleep(5000); // wait for the thread to finish
        assertTrue(converter.m_Thread.isTerminated());
        assertTrue(converter.init());
        dir.delete();
    }

    @Test
    public void testTranscodeCommand() {
        String format = "dvd-ntsc";
        String aspectRatio = "1:1";
        Convert converter = new Convert(format, aspectRatio, new GUI() {});
        String command = converter.getTranscodeCommand();

        String expectedFormat = format.equals("dvd-ntsc") ? "dv" : "mp4";
        String expectedFlags = format.equals("dvd-ntsc") ? "8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2" : "8, -c:a libfdk_aac -b:a 192k -ar 48000 -mkv:language en:EN -f matroska";

        String expectedTranscode = String.format("/bin/sh -c '%s' %s %s %s %s %s %s %s %s %s", command,
                expectedFormat,
                expectedFlags,
                converter.m_Bitrate,
                converter.m_Format,
                converter.m_AspectRatio,
                String.valueOf(converter.m_fps),
                "file=matrix.txt",
                "file=matrix.txt",
                "file=matrix.txt");

        assertEquals(expectedTranscode, command);
    }

    @Test
    public void testMplexCommand() {
        String format = "dvd-ntsc";
        Convert converter = new Convert(format, "1:1", new GUI() {});
        String command = converter.getMplexCommand();

        String expectedMplex = "/bin/sh -c 'mplex -f 8 -V -o inp.vob inp.m2v inp.ac3'";

        assertEquals(expectedMplex, command);
    }

    @Test
    public void testTranscodeLogFileExists() {
        String outputDir = "test_output";
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        converter.init();
        assertTrue(new File(outputDir + "/log/transcode.log").exists());
        converter.init();
    }

    @Test
    public void testTranscodeLogFileContent() throws IOException {
        String outputDir = "test_output";
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        converter.init();
        Thread.sleep(5000); // wait for the thread to finish
        String logContent = new String(Files.readAllBytes(Paths.get(outputDir + "/log/transcode.log")));
        assertTrue(logContent.contains("encoding"));
        assertTrue(logContent.contains("frames/s"));
        assertTrue(logContent.contains("total_time"));
        assertTrue(logContent.contains("fps_sum"));
        assertTrue(logContent.contains("time_remaining"));
        converter.init();
    }

    @Test
    public void testErrorInitialization() {
        Convert converter = new Convert("invalid_format", "1:1", new GUI() {});
        assertFalse(converter.init());
    }

    @Test
    public void testErrorTranscodeCommand() {
        String format = "dvd-ntsc";
        Convert converter = new Convert(format, "1:1", new GUI() {});
        String command = converter.getTranscodeCommand();
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", command);
        pb.redirectErrorStream(true);
        try {
            pb.start();
            fail("Expected IOException when running the transcode command");
        } catch (IOException e) {
            // expected
        }
        converter.init();
    }

    @Test
    public void testErrorMplexCommand() {
        String format = "dvd-ntsc";
        Convert converter = new Convert(format, "1:1", new GUI() {});
        String command = converter.getMplexCommand();
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", command);
        pb.redirectErrorStream(true);
        try {
            pb.start();
            fail("Expected IOException when running the mplex command");
        } catch (IOException e) {
            // expected
        }
        converter.init();
    }

    @Test
    public void testErrorMatrixFile() {
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        try {
            converter.matrix();
            fail("Expected IOException when creating matrix.txt file");
        } catch (IOException e) {
            // expected
        }
        converter.init();
    }

    @Test
    public void testErrorFileAccess() {
        String outputDir = "test_output";
        Convert converter = new Convert("dvd-ntsc", "1:1", new GUI() {});
        converter.init();
        File logFile = new File(outputDir + "/log/transcode.log");
        FileOutputStream os = new FileOutputStream(logFile);
        os.write("invalid data".getBytes());
        os.close();

        try {
            converter.init();
            fail("Expected IOException when reading transcode log file");
        } catch (IOException e) {
            // expected
        }

        converter.init();
        logFile.delete();
    }

    @Test
    public void testErrorInvalidArguments() {
        Convert converter = new Convert("dvd-ntsc", "", new GUI() {});
        assertFalse(converter.init());

        converter = new Convert("", "1:1", new GUI() {});
        assertFalse(converter.init());

        converter = new Convert("dvd-ntsc", "1:1", null);
        assertFalse(converter.init());
    }

    @Test
    public void testThreadExecutionOrder() throws InterruptedException, IOException {
        String outputDir = "test_output";
        File dir = new File(outputDir);
        dir.mkdir();

        Convert[] converters = new Convert[3];
        for (int i = 0; i < converters.length; i++) {
            converters[i] = new Convert("dvd-ntsc", "1:1", new GUI() {});
            converters[i].init();
            Thread.sleep(1000); // give some time for the threads to start
        }

        // check that the threads are executed in the correct order
        for (int i = 0; i < converters.length; i++) {
            String logContent = new String(Files.readAllBytes(Paths.get(outputDir + "/log/transcode.log")));
            String[] lines = logContent.split("\n");
            int threadIndex = Arrays.asList(converters).indexOf(converters[i]);
            for (int j = 0; j < threadIndex; j++) {
                String line = lines[0];
                assertTrue(line.startsWith("Encoding: " + (j + 1)));
                lines = Arrays.copyOfRange(lines, 1, lines.length);
            }
        }

        converters[0].init();
        converters[1].init();
        converters[2].init();
        dir.delete();
    }
}