import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

public class MenuTest {

    private Menu menu;

    @Test
    public void testMenuConstructor() {
        menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);
        assertArrayEquals(new String[0], menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);

        // Test constructor with title, TextFilePath, isPAL, and DVD_GUI
        menu = new Menu("Title", "TextFilePath.txt", true, new GUI());
        assertEquals("Title", menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertEquals("TextFilePath.txt", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertNull(menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);
        assertArrayEquals(new String[0], menu.video_files);
        assertNotNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);

        // Test constructor with title, picPath, audioPath, TextFilePath, isPAL, and DVD_GUI
        menu = new Menu("Title", "/path/to/pic.jpg", "/path/to/audio.mp3", "TextFilePath.txt", true, new GUI());
        assertEquals("Title", menu.strTitle);
        assertEquals("/path/to/pic.jpg", menu.strPicPath);
        assertEquals("/path/to/audio.mp3", menu.strAudioPath);
        assertEquals("TextFilePath.txt", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertNull(menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);
        assertArrayEquals(new String[0], menu.video_files);
        assertNotNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMenuConstructorWithNullTitle() {
        menu = new Menu(null, "TextFilePath.txt", true, new GUI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMenuConstructorWithNullTextFilePath() {
        menu = new Menu("Title", null, true, new GUI());
    }

    @Test
    public void testMenuInit() throws InterruptedException {
        menu = new Menu();
        menu.init();
        assertTrue(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testMenuDVDMainMenu() {
        try {
            menu = new Menu("Title", "TextFilePath.txt", true, new GUI());
            menu.init();
            menu.DVDMainMenu();
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case.");
        } catch (UndeclaredThrowableException e) {
            IOException ioException = e.getRootCause();
            File listDir = new File(menu.m_GUI.strOutputDir);
            FilenameFilter filter = new FilenameFilter() {
                boolean accept(File dir, String name) {
                    return name.endsWith("vob");
                }
            };
            String[] videoFiles = listDir.list(filter);
            assertNotNull(videoFiles);
            int numVobFiles = videoFiles.length;
            assertEquals("Title", menu.strTitle);
            assertTrue(menu.pal_menu);
            assertFalse(menu.m_Error);
            if (menu.strTextFilePath != null) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(menu.m_GUI.txtTextFile.getText())));
                    String title;
                    int i = 0;
                    while ((title = reader.readLine()) != null) {
                        assertEquals(title, menu.titles[i++]);
                    }
                    reader.close();
                } catch (FileNotFoundException e1) {
                    fail("Text file should exist.");
                } catch (IOException e1) {
                    fail("IOException should not be thrown in this test case.");
                }
            }
            Arrays.sort(menu.video_files);
            Arrays.sort(videoFiles);
            assertArrayEquals(menu.video_files, videoFiles);
        }
    }

    @Test(expected = IOException.class)
    public void testMenuDVDMainMenuWithMissingTextFile() {
        menu = new Menu("Title", null, true, new GUI());
        menu.init();
        menu.DVDMainMenu();
    }

    @Test
    public void testMenuCreateXML() {
        try {
            menu = new Menu("Title", "TextFilePath.txt", true, new GUI());
            menu.init();
            menu.createXML();
            File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
            assertTrue(xmlFile.exists());
            xmlFile.delete();
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case.");
        }
    }
}