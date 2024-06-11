import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;

 @Test
 public void testMenuConstructor1() {
 menu = new Menu();
 assertNull(menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructor2() {
 menu = new Menu("Title1", "", "", "", false, null);
 assertEquals("Title1", menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructor3() {
 menu = new Menu("Title1", "", "", "TextFilePath1.txt", false, null);
 assertEquals("Title1", menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertEquals("TextFilePath1.txt", menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructor4() {
 menu = new Menu("Title1", "PicPath1.jpg", "AudioPath1.wav", "TextFilePath1.txt", false, null);
 assertEquals("Title1", menu.getStrTitle());
 assertEquals("PicPath1.jpg", menu.getStrPicPath());
 assertEquals("AudioPath1.wav", menu.getStrAudioPath());
 assertEquals("TextFilePath1.txt", menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMenuInitNullTextFilePath() {
 menu = new Menu("Title1", "", "", null, false, null);
 assertNull(menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 menu.init();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testInitSuccess() {
 menu = new Menu("Title1", "", "", "TextFilePath1.txt", false, null);
 assertEquals("Title1", menu.getStrTitle());
 assertEquals("", menu.getStrPicPath());
 assertEquals("", menu.getStrAudioPath());
 assertEquals("TextFilePath1.txt", menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.ism_Error());
 menu.init();
 assertFalse(menu.isError());
 assertTrue(menu.getM_GUI().errorCheck("/log/dvd-menu.log"));
 }

 @Test
 public void testInitFailure() {
 menu = new Menu("Title1", "", "", "TextFilePath1.txt", false, null);
 assertEquals("Title1", menu.getStrTitle());
 assertEquals("", menu.getStrPicPath());
 assertEquals("", menu.getStrAudioPath());
 assertEquals("TextFilePath1.txt", menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNotNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getM_GUI());
 assertNull(menu.getM_Thread());
 assertFalse(menu.isError());
 menu.init();
 menu.setError(true);
 assertTrue(menu.isError());
 }

 @Test
 public void testCreateXMLSuccess() {
 menu = new Menu("Title1", "", "", "TextFilePath1.txt", false, null);
 menu.init();
 assertFalse(menu.isError());
 assertTrue(menu.createXML());
 }

 @Test
 public void testCreateXMLFailure() {
 menu = new Menu("Title1", "", "", "TextFilePath1.txt", false, null);
 menu.setError(true);
 assertFalse(menu.createXML());
 }

}