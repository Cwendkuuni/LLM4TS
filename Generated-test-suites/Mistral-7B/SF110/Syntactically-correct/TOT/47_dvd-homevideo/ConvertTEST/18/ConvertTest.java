import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String format;
 private String aspectRatio;
 private GUI gui;

 @Before
 public void setUp() {
 this.format = "dvd-ntsc";
 this.aspectRatio = "4:3";
 this.gui = mock(GUI.class);
 this.convert = new Convert(this.format, this.aspectRatio, this.gui);
 }

 @Test
 public void testInit_ThreadIsSelected_ThreadIsStartedAndFinished() throws InterruptedException {
 when(this.gui.menuChkThread.isSelected()).thenReturn(true);
 this.convert.init();
 assertTrue(this.convert.m_Thread.isAlive());
 this.convert.m_Thread.join();
 assertFalse(this.convert.m_Error);
 }

 @Test
 public void testInit_ThreadIsNotSelected_TranscodeIsCalled() {
 when(this.gui.menuChkThread.isSelected()).thenReturn(false);
 this.convert.init();
 assertFalse(this.convert.m_Thread.isAlive());
 this.convert.Transcode();
 }

 @Test(expected=StringIndexOutOfBoundsException.class)
 public void testTranscode_InvalidDirectoryContent_ErrorIsThrown() {
 this.convert.thread_track = 5;
 this.convert.Transcode();
 }

 @Test
 public void testTranscode_ValidDirectoryContent_FilesAreProcessed() throws IOException {
 File listDir = new File(this.convert.m_GUI.strOutputDir + "/dv/");
 String[] files = listDir.list();
 this.convert.video_files = files;
 this.convert.thread_track = 0;
 this.convert.Transcode();
 }

 @Test
 public void testMatrix_FileIsCreated() throws IOException {
 File file = new File(this.convert.m_GUI.strOutputDir + "/matrix.txt");
 assertTrue(file.exists());
 }

 @Test
 public void testMatrix_FileIsNotCreated_IOExceptionIsThrown() {
 when(this.gui.strOutputDir).thenReturn(new File("").getAbsolutePath());
 try {
 this.convert.matrix();
 fail();
 }
 catch (IOException e) {
 // expected
 }
 }
}