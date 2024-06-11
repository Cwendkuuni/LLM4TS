import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructor() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
 assertNull(convert.m_Error);
 }

 @Test
 public void testConvertConstructorWithQuality() {
 convert = new Convert(1, "dvd-ntsc", "4:3", new GUI());
 assertEquals(1, convert.m_Quality);
 }

 @Test
 public void testInitFalseWhenThreadNotSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(convert.init());
 }

 @Test
 public void testInitTrueWhenThreadSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 assertTrue(convert.init());
 }

 @Test
 public void testInitWithErrorWhenThreadJoinFails() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 convert.m_Thread = new Thread(convert);
 convert.m_Thread.start();
 try {
 Thread.sleep(100);
 }
 catch (InterruptedException e) {
 e.printStackTrace();
 }
 convert.m_Thread.interrupt();
 assertFalse(convert.init());
 }

 @Test
 public void testInitWithErrorWhenFileNotFound() {
 File listDir = new File("dvd");
 listDir.mkdir();
 File testFile = new File(listDir, "test.dv");
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 convert.m_Thread = new Thread(convert);
 convert.m_Thread.start();
 try {
 Thread.sleep(100);
 }
 catch (InterruptedException e) {
 e.printStackTrace();
 }
 File[] files = listDir.listFiles();
 assertFalse(Arrays.asList(files).contains(testFile));
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithSuccess() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 convert.m_Thread = new Thread(convert);
 convert.m_Thread.start();
 try {
 Thread.sleep(5000);
 }
 catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertTrue(convert.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithNonStandardFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 convert.m_Thread = new Thread(convert);
 convert.m_Thread.start();
 File listDir = new File("dvd");
 listDir.mkdir();
 File testFile1 = new File(listDir, "test1.avi");
 File testFile2 = new File(listDir, "test2.mp4");
 try {
 Thread.sleep(100);
 }
 catch (InterruptedException e) {
 e.printStackTrace();
 }
 File[] files = listDir.listFiles();
 assertTrue(Arrays.asList(files).contains(testFile1));
 assertTrue(Arrays.asList(files).contains(testFile2));
 convert.video_files = files;
 assertArrayEquals(files, convert.video_files);
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithInvalidCommand() {
 String transcodeCmd = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = transcodeCmd;
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithNotFoundCommand() {
 String transcodeCmd = "/bin/sh: transcode: command not found";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = transcodeCmd;
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithIOException() {
 String transcodeCmd = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30 > error.log 2>&1";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = transcodeCmd;
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithNullPointerException() {
 String transcodeCmd = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = transcodeCmd;
 convert.m_GUI = null;
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithIllegalArgumentException() {
 String transcodeCmd = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = transcodeCmd;
 convert.m_fps = -1.0;
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithException() {
 String transcodeCmd = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = transcodeCmd;
 try {
 File listDir = new File("dvd");
 listDir.mkdir();
 File testFile1 = new File(listDir, "test1.dv");
 File testFile2 = new File(listDir, "test2.dv");
 convert.video_files = new String[]{"test1.dv", "test2.dv"};
 convert.thread_track = 0;
 Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", transcodeCmd});
 assertFalse(convert.init());
 p.destroy();
 }
 catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testMatrixFileCreation() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File matrixFile = new File(convert.m_GUI.strOutputDir, "matrix.txt");
 assertTrue(matrixFile.exists());
 }

 @Test
 public void testMatrixFileNotCreated() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File matrixFile = new File(convert.m_GUI.strOutputDir, "matrix.txt");
 assertFalse(matrixFile.exists());
 }
}