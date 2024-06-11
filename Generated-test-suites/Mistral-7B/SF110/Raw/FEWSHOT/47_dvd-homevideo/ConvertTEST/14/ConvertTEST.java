package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.junit.Test;

public class ConvertTest {

 @Test
 public void testConvertConstructor1() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
 assertEquals("8500", convert.m_bitrate);
 assertNull(convert.m_Quality);
 }

 @Test
 public void testConvertConstructor2() {
 Convert convert = new Convert(1, "dvd-pal", "16:9", new GUI());
 assertEquals(1, convert.m_Quality);
 assertEquals("dvd-pal", convert.m_Format);
 assertEquals("16:9", convert.m_AspectRatio);
 assertNull(convert.m_bitrate);
 }

 @Test
 public void testInitNoThread() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(convert.init());
 }

 @Test
 public void testInitWithThread() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 assertTrue(convert.m_Thread != null);
 }

 @Test
 public void testTranscodeCommandStringManipulation() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 String format = "dvd-ntsc";
 String aspectRatio = "4:3";
 String flags = "8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2";
 String expectedTranscodeCommand = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 convert.m_transcode = convert.m_transcode.replaceAll("format", format);
 convert.m_transcode = convert.m_transcode.replaceAll("aspectRatio", aspectRatio);
 convert.m_flags = flags;
 convert.m_transcode = convert.m_transcode.replaceAll("flags", this.flags);
 assertEquals(expectedTranscodeCommand, convert.m_transcode);
 }

 @Test
 public void testMplexCommandStringManipulation() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 String expectedMplexCommand = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";
 convert.mplex = convert.mplex.replaceAll("format", "dvd-ntsc");
 assertEquals(expectedMplexCommand, convert.mplex);
 }

 @Test
 public void testInitErrorOnInvalidFormat() {
 Convert convert = new Convert("invalid-format", "16:9", new GUI());
 assertTrue(convert.init());
 assertTrue(convert.m_Error);
 }

 @Test
 public void testInitErrorOnNullGUI() {
 Convert convert = new Convert("dvd-ntsc", "4:3", null);
 assertTrue(convert.init());
 assertTrue(convert.m_Error);
 }

 @Test
 public void testInitSuccess() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertTrue(convert.init());
 assertFalse(convert.m_Error);
 }

 @Test
 public void testRunTranscodeSuccessfully() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.Transcode();
 assertFalse(convert.m_Error);
 }

 @Test
 public void testRunTranscodeFailsWithIOException() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.m_Thread = new Thread(() -> {throw new IOException();});
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testRunTranscodeFailsWithNullPointerException() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.m_Thread = new Thread(() -> {throw new NullPointerException();});
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testRunTranscodeFailsWithIllegalArgumentException() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.m_Thread = new Thread(() -> {throw new IllegalArgumentException();});
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testRunTranscodeFailsWithUnknownException() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.m_Thread = new Thread(() -> {throw new RuntimeException();});
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testRunTranscodeMatchesExpectedLogOutput() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.Transcode();
 String[] expectedLogOutputLines = {"encoding: VC1_Q2 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q4 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)",
     "encoding: VC1_Q6 [dv:dv] 25.000 fps (29.970 video)"};
 String[] actualLogOutputLines = convert.m_GUI.txtAreaOutput.getText().split("\n");
 assertTrue(Arrays.equals(expectedLogOutputLines, actualLogOutputLines));
 }

 @Test
 public void testRunMplexSuccessfully() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 convert.Transcode();
 assertTrue(convert.m_Error == false);
 }

}