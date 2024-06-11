import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertNotNull(menu.titles);
        assertEquals(0, menu.titles.length);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);

        Menu menu1 = new Menu("Test Title", "", "", "", false, null);
        assertEquals("Test Title", menu1.strTitle);
        assertNull(menu1.strPicPath);
        assertNull(menu1.strAudioPath);
        assertNull(menu1.strTextFilePath);
        assertNull(menu1.dvd_menu);
        assertNotNull(menu1.titles);
        assertEquals(0, menu1.titles.length);
        assertNull(menu1.baseErr);
        assertNull(menu1.video_files);
        assertNotNull(menu1.m_GUI);
        assertNull(menu1.m_Thread);
        assertFalse(menu1.m_Error);

        Menu menu2 = new Menu("Test Title", "test_pic.jpg", "test_audio.mp3", "test.txt", true, null);
        assertEquals("Test Title", menu2.strTitle);
        assertEquals("test_pic.jpg", menu2.strPicPath);
        assertEquals("test_audio.mp3", menu2.strAudioPath);
        assertEquals("test.txt", menu2.strTextFilePath);
        assertNull(menu2.dvd_menu);
        assertNotNull(menu2.titles);
        assertEquals(0, menu2.titles.length);
        assertEquals("Menu Error - ", menu2.baseErr);
        assertNull(menu2.video_files);
        assertNotNull(menu2.m_GUI);
        assertNull(menu2.m_Thread);
        assertFalse(menu2.m_Error);
    }

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
        assertTrue(menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log").equals("Error found in log file."));
        assertTrue(menu.m_Error);

        Menu menu1 = new Menu("Test Title", "");
        assertFalse(menu1.init());
        assertTrue(menu1.m_GUI.ErrorCheck(menu1.m_GUI.strOutputDir + "/log/dvd-menu.log").equals("Error found in log file."));
        assertTrue(menu1.m_Error);

        Menu menu2 = new Menu("Test Title", "", "", "", false, null);
        assertFalse(menu2.init());
        assertTrue(menu2.m_GUI.ErrorCheck(menu2.m_GUI.strOutputDir + "/log/dvd-menu.log").equals("Error found in log file."));
        assertTrue(menu2.m_Error);

        Menu menu3 = new Menu("Test Title", "test_pic.jpg", "test_audio.mp3", "test.txt", true, null);
        assertTrue(menu3.init());
        assertFalse(menu3.m_GUI.ErrorCheck(menu3.m_GUI.strOutputDir + "/log/dvd-menu.log").equals("Error found in log file."));
        assertFalse(menu3.m_Error);
    }

    @Test(expected = InterruptedException.class)
    public void testRun() throws InterruptedException {
        Menu menu = new Menu();
        menu.run();
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
        assertNull(menu.dvd_menu);

        Menu menu1 = new Menu("Test Title", "");
        menu1.DVDMainMenu();
        assertNull(menu1.dvd_menu);

        Menu menu2 = new Menu("Test Title", "test_pic.jpg", "test_audio.mp3", "test.txt", true, null);
        menu2.DVDMainMenu();
        assertNotNull(menu2.dvd_menu);
        assertTrue(menu2.dvd_menu.contains("dvd-menu"));
        assertTrue(menu2.dvd_menu.contains("dvdauthor"));
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
        assertFalse(new File(menu.m_GUI.strOutputDir + "/vmgm.xml").exists());

        Menu menu1 = new Menu("Test Title", "");
        assertFalse(menu1.createXML());
        assertFalse(new File(menu1.m_GUI.strOutputDir + "/vmgm.xml").exists());

        Menu menu2 = new Menu("Test Title", "test_pic.jpg", "test_audio.mp3", "test.txt", true, null);
        assertTrue(menu2.createXML());
        assertTrue(new File(menu2.m_GUI.strOutputDir + "/vmgm.xml").exists());
    }
}