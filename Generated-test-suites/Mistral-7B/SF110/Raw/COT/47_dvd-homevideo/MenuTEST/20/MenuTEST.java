import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.IOException;

public class MenuTest {

 private Menu menu;

 @Test
 public void testMenuConstructionWithNoArgs() {
 Menu menu = new Menu();
 assertNull(menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructionWithTitleArgument() {
 String title = "Sample Title";
 Menu menu = new Menu(title, "", "", "", false, null);
 assertEquals(title, menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructionWithTitleArgPicPathArg() {
 String title = "Sample Title";
 String picPath = "sample.jpg";
 Menu menu = new Menu(title, picPath, "", "", false, null);
 assertEquals(title, menu.getStrTitle());
 assertEquals(picPath, menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructionWithTitleArgPicPathArgAudioPathArg() {
 String title = "Sample Title";
 String picPath = "sample.jpg";
 String audioPath = "sample.mp3";
 Menu menu = new Menu(title, picPath, audioPath, "", false, null);
 assertEquals(title, menu.getStrTitle());
 assertEquals(picPath, menu.getStrPicPath());
 assertEquals(audioPath, menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuInitialization() {
 Menu menu = new Menu();
 menu.init();
 assertTrue(menu.ism_Error() || menu.getM_GUI().ErrorCheck(menu.getM_GUI().strOutputDir + "/log/dvd-menu.log"));
 }

 @Test(expected = InterruptedException.class)
 public void testMenuInitializationInterruptedException() throws InterruptedException {
 Menu menu = new Menu();
 menu.init();
 Thread.currentThread().interrupt();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMenuCreationWithInvalidTextFilePath() {
 String title = "Sample Title";
 Menu menu = new Menu(title, "", "", "invalid", false, null);
 assertFalse(menu.ism_Error());
 assertNotNull(menu.getM_GUI());
 assertNotNull(menu.getM_Thread());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testMenuCreationWithInvalidNumberOfTitles() {
 String title = "Sample Title";
 String textFilePath = "sample.txt";
 Menu menu = new Menu(title, "", "", textFilePath, false, null);
 assertFalse(menu.ism_Error());
 assertNotNull(menu.getM_GUI());
 assertNotNull(menu.getM_Thread());
 assertEquals(0, menu.getTitles().length);
 }

 @Test
 public void testMenuCreateXML() {
 Menu menu = new Menu("Sample Title", "", "", "", false, null);
 menu.DVDMainMenu();
 assertTrue(menu.createXML());
 }

 @Test(expected = IOException.class)
 public void testMenuCreateXMLIOException() throws IOException {
 Menu menu = new Menu("Sample Title", "", "", "", false, null);
 menu.DVDMainMenu();
 MenuXMLParser parser = new MenuXMLParser(new File("sample.xml"));
 parser.parse();
 menu.createXML();
 }

 @Test
 public void testMenuErrorCheckLogFile() {
 Menu menu = new Menu("Sample Title", "", "", "", false, null);
 menu.m_Error = true;
 File logFile = new File("sample.log");
 assertTrue(menu.ErrorCheck(logFile.getAbsolutePath()));
 }

 @Test
 public void testMenuErrorCheckLogFileNoError() {
 Menu menu = new Menu("Sample Title", "", "", "", false, null);
 File logFile = new File("sample.log");
 assertFalse(menu.ErrorCheck(logFile.getAbsolutePath()));
 }

 @Test(expected = FileNotFoundException.class)
 public void testMenuErrorCheckLogFileNotFound() {
 Menu menu = new Menu("Sample Title", "", "", "", false, null);
 assertFalse(menu.ErrorCheck(null));
 }

}