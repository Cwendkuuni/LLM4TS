import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setup() {
 outputDir = "test_output";
 File dir = new File(outputDir);
 dir.mkdir();
 convert = new Convert("dvd-ntsc", "4:3", null);
 convert.init();
 }

 @Test
 public void testInitWithValidArguments() {
 assertTrue(convert.init());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithInvalidFormat() {
 convert = new Convert("invalid_format", "4:3", null);
 assertFalse(convert.init());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithInvalidAspectRatio() {
 convert = new Convert("dvd-ntsc", "16:9", null);
 assertFalse(convert.init());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithThreadCheckboxUnchecked() {
 convert = new Convert("dvd-ntsc", "4:3", null);
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithValidArguments() {
 convert = new Convert("dvd-ntsc", "4:3", null);
 convert.init();
 String transcodeCommand = convert.getTranscodeCommand();
 String[] args = transcodeCommand.split(" ");
 String[] expectedArgs = { "/bin/sh", "-c", "transcode", "-i", "dv/inp", "-m", "test1.ac3", "-o", "test1", "-w", "bitr", "-x", "dv,dv", "-F", "flags", "-y", "mpeg2enc,raw", "-N", "0x2000", "-b", "256", "--encode_fields", "b", "--export_prof", "format", "--export_fps", "frames/s", "--export_asr", "aspectRatio", "-j", "0,8,0,8", "--print_status", "30" };
 assertArrayEquals(expectedArgs, args);
 }

 @Test
 public void testTranscodeWithErrorInCommandExecution() {
 convert = new Convert("dvd-ntsc", "4:3", null);
 convert.init();
 String transcodeCommand = convert.getTranscodeCommand();
 String[] args = transcodeCommand.split(" ");
 String[] expectedArgs = { "/bin/sh", "-c", "transcode", "-i", "dv/inp", "-m", "test1.ac3", "-o", "test1", "-w", "bitr", "-x", "dv,dv", "-F", "flags", "-y", "mpeg2enc,raw", "-N", "0x2000", "-b", "256", "--encode_fields", "b", "--export_prof", "format", "--export_fps", "frames/s", "--export_asr", "aspectRatio", "-j", "0,8,0,8", "--print_status", "33" }; // Intentionally incorrect print\_status argument
 convert.Transcode();
 assertFalse(convert.init());
 }

 @Test
 public void testMatrixFileCreation() {
 convert = new Convert("dvd-ntsc", "4:3", null);
 convert.init();
 assertTrue(new File(outputDir + "/matrix.txt").exists());
 }

 @Test
 public void testThreadExecution() {
 convert = new Convert("dvd-ntsc", "4:3", null);
 convert.init();
 convert.run();
 Thread thread = convert.m_Thread;
 assertTrue(thread.isAlive());
 }

 @After
 public void teardown() {
 File dir = new File(outputDir);
 File[] files = dir.listFiles();
 for (File file : files) {
 file.delete();
 }
 dir.delete();
 }

}