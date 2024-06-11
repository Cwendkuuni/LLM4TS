import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import static org.mockito.Mockito.*;

public class ConvertTest {

 private Convert convert;
 private String format;
 private String aspectRatio;
 private GUI gui;
 private File outputDir;

 @Before
 public void setUp() {
 this.format = "dvd-ntsc";
 this.aspectRatio = "4:3";
 this.outputDir = new File("outputDir");
 this.gui = mock(GUI.class);
 this.convert = new Convert(this.format, this.aspectRatio, this.gui, this.outputDir);
 }

 @After
 public void tearDown() {
 this.convert = null;
 this.gui = null;
 }

 @Test
 public void testInit_ThreadNotSelected() {
 when(this.gui.menuChkThread.isSelected()).thenReturn(false);
 this.convert.init();
 verify(this.gui, times(1)).Transcode();
 }

 @Test
 public void testInit_ThreadSelected() {
 when(this.gui.menuChkThread.isSelected()).thenReturn(true);
 this.convert.init();
 verify(this.gui, times(1)).join();
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscode_InvalidFileInDvDirectory() {
 when(this.gui.menuChkThread.isSelected()).thenReturn(false);
 when(this.gui.strOutputDir.exists()).thenReturn(true);
 doReturn(new String[]{"test1.dv"}).when(this.convert).videoFiles();
 this.convert.Transcode();
 }

 @Test
 public void testTranscode_ValidFilesInDvDirectory() {
 when(this.gui.menuChkThread.isSelected()).thenReturn(false);
 when(this.gui.strOutputDir.exists()).thenReturn(true);
 doReturn(new String[]{"test1.dv", "test2.dv", "test3.dv", "test4.dv"}).when(this.convert).videoFiles();
 this.convert.Transcode();
 verify(this.gui, times(4)).TranscodeFile(anyString());
 }

 @Test
 public void testTranscodeFile_Success() {
 File logFile = new File(this.outputDir, "log/transcode.log");
 String logContent = "Transcode command: /bin/sh -c transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 when(this.gui.strOutputDir.exists()).thenReturn(true);
 doReturn(logFile).when(this.convert).getLogFile();
 this.convert.TranscodeFile("test1.dv");
 verify(this.gui).writeToOutput(eq("Transcode command:\n/bin/sh -c transcode -i dv/test1.dv -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr 4:3 -j 0,8,0,8 --print_status 30\n"), anyString());
 verify(this.gui).writeToLogFile(eq(logContent), anyString());
 }

 @Test
 public void testTranscodeFile_Error() {
 File logFile = new File(this.outputDir, "log/transcode.log");
 String logContent = "Transcode command: /bin/sh -c transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 String errorMessage = "Transcode error: Could not locate transcode in your path.";
 when(this.gui.strOutputDir.exists()).thenReturn(true);
 doReturn(logFile).when(this.convert).getLogFile();
 doThrow(new RuntimeException(errorMessage)).when(this.convert).executeTranscodeCommand(anyString());
 this.convert.TranscodeFile("test1.dv");
 verify(this.gui).writeToOutput(eq("Transcode error:\n" + errorMessage), anyString());
 }
}