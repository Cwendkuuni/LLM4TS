package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

 Convert testConvert;
 String[] testVideoFiles;

 @Test
 public void testInit() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 assertFalse(testConvert.m_Error);

 testConvert = new Convert(0, "format", "aspectRatio", new GUI());
 assertFalse(testConvert.m_Error);

 testConvert = new Convert(1, "format", "aspectRatio", new GUI());
 assertFalse(testConvert.m_Error);

 testConvert = new Convert(6, "format", "aspectRatio", new GUI());
 assertFalse(testConvert.m_Error);
 }

 @Test
 public void testTranscode() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 assertFalse(testConvert.m_Error);
 }

 @Test
 public void testMatrix() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.matrix();
}

 @Test
 public void testRun() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.run();
}

 @Test
 public void testReplaceAll() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 String testTranscode = testConvert.m_transcode;
 String testFormat = "testFormat";
 String testAspectRatio = "testAspectRatio";
 testConvert.m_Format = testFormat;
 testConvert.m_AspectRatio = testAspectRatio;
 testTranscode = testTranscode.replaceAll("format", testFormat);
 testTranscode = testTranscode.replaceAll("aspectRatio", testAspectRatio);
 assertEquals(testTranscode, testConvert.m_transcode);
}

 @Test
 public void testThreadExecution() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 testConvert.thread_track = 0;
 testConvert.run();
 testConvert.thread_track++;
 testConvert.run();
 testConvert.thread_track++;
 testConvert.run();
}

 @Test
 public void testErrorHandling() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.m_GUI.menuChkThread.setSelected(false);
 assertFalse(testConvert.m_Error);
 testConvert.Transcode();
 assertTrue(testConvert.m_Error);
}

 @Test
 public void testPathNotFound() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.ac3", "test3.dv"};
 testConvert.thread_track = 0;
 testConvert.run();
 testConvert.thread_track++;
 testConvert.run();
 assertThrows(StringIndexOutOfBoundsException.class, () -> testConvert.run());
}

 @Test
 public void testFileExecutionWithError() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 testConvert.thread_track = 0;
 String testError = "Command not found";
 testConvert.m_GUI.strOutputDir = "testOutputDir";
 when(testConvert.m_GUI.MessageBox(anyString(), intThat(isA(Integer.class))))
 .thenAnswer(i -> {
 throw new RuntimeException(testError);
 });
 testConvert.run();
 assertTrue(testConvert.m_Error);
}

 @Test
 public void testFileExecutionWithoutError() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 testConvert.thread_track = 0;
 testConvert.run();
 testConvert.thread_track++;
 testConvert.run();
 testConvert.thread_track++;
 testConvert.run();
 assertFalse(testConvert.m_Error);
}

 @Test
 public void testNullPointerException() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = null;
 assertThrows(NullPointerException.class, () -> testConvert.run());
}

 @Test
 public void testIOException() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 testConvert.thread_track = 0;
 when(testConvert.m_GUI.MessageBox(anyString(), intThat(isA(Integer.class))))
 .thenAnswer(i -> {});
 when(System.in.read())
 .thenThrow(new IOException());
 testConvert.run();
 assertTrue(testConvert.m_Error);
}

 @Test
 public void testIllegalArgumentException() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 testConvert.thread_track = 0;
 when(testConvert.m_GUI.MessageBox(anyString(), intThat(isA(Integer.class))))
 .thenAnswer(i -> {});
 when(Runtime.getRuntime().exec(anyArray(), (File[])eq(null), any(File.class)))
 .thenThrow(new IllegalArgumentException());
 testConvert.run();
 assertTrue(testConvert.m_Error);
}

 @Test
 public void testUnknownException() {
 testConvert = new Convert("format", "aspectRatio", new GUI());
 testConvert.init();
 testConvert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 testConvert.thread_track = 0;
 when(testConvert.m_GUI.MessageBox(anyString(), intThat(isA(Integer.class))))
 .thenAnswer(i -> {});
 when(Runtime.getRuntime().exec(anyArray(), (File[])eq(null), any(File.class)))
 .thenThrow(new RuntimeException());
 testConvert.run();
 assertTrue(testConvert.m_Error);
}
}