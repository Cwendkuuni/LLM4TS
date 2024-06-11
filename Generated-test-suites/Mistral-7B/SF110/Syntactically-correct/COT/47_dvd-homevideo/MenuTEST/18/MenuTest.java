import org.junit.Test;
import static org.junit.Assert.*;
import java.util.StringJoiner;

public class MenuTest {
    @Test
    public void testConstructor() {
        Menu menu = new Menu();
        assertNotNull(menu);

        Menu menuWithTitle = new Menu("Test Title", "", "", "", true, null);
        assertNotNull(menuWithTitle);
        assertEquals("Test Title", menuWithTitle.strTitle);

        Menu menuWithTitlePicPath = new Menu("Test Title", "testpic.jpg", "", "test.txt", true, null);
        assertNotNull(menuWithTitlePicPath);
        assertEquals("testpic.jpg", menuWithTitlePicPath.strPicPath);

        Menu menuWithTitlePicPathTextFilePath = new Menu("Test Title", "testpic.jpg", "testaudio.mp3", "test.txt", true, null);
        assertNotNull(menuWithTitlePicPathTextFilePath);
        assertEquals("testpic.jpg", menuWithTitlePicPathTextFilePath.strPicPath);
        assertEquals("test.txt", menuWithTitlePicPathTextFilePath.strTextFilePath);
    }

    @Test
    public void testInit_NoError() {
        Menu menu = new Menu();
        assertTrue(menu.init());
    }

    @Test
    public void testInit_Error() {
        Menu menu = new Menu();
        menu.m_Error = true;
        assertFalse(menu.init());
    }

    @Test(expected = InterruptedException.class)
    public void testInit_Interrupted() {
        Menu menu = new Menu();
        Thread.currentThread().interrupt();
        menu.init();
    }

    @Test
    public void testDVDMainMenu_NoTextFile() {
        Menu menu = new Menu("Test Title", "", "", "", true, null);
        menu.init();
        menu.DVDMainMenu();

        // Assert some output from the dvd_menu command
        StringJoiner joiner = new StringJoiner("\n");
        String[] expectedOutput = new String[] {"dvd-menu", "-c", "-D", "-n", "Test Title", "-o", menu.m_GUI.strOutputDir, "-f", "menu.vob"};
        String actualOutput = menu.dvd_menu.substring(0, menu.dvd_menu.indexOf("\n")).split(" ");

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDVDMainMenu_WithTextFile() {
        Menu menu = new Menu("Test Title", "", "", "test.txt", true, null);
        menu.init();
        menu.DVDMainMenu();

        // Assert some output from the dvd_menu command
        StringJoiner joiner = new StringJoiner("\n");
        String[] expectedOutput = new String[] {"dvd-menu", "-c", "-D", "-n", "Test Title", "-o", menu.m_GUI.strOutputDir, "-a", "testaudio.mp3", "-b", "testpic.jpg"};
        String actualOutput = menu.dvd_menu.substring(0, menu.dvd_menu.indexOf("\n")).split(" ");

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = FileNotFoundException.class)
    public void testDVDMainMenu_TextFileNotFound() {
        Menu menu = new Menu("Test Title", "", "", "test.txt", true, null);
        menu.init();
        menu.txtTextFile = new File("non-existent-file.txt");
        menu.DVDMainMenu();
    }

    @Test(expected = IOException.class)
    public void testDVDMainMenu_IOError() {
        Menu menu = new Menu("Test Title", "", "", "test.txt", true, null);
        menu.init();
        try {
            new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu("Test Title", "", "", "test.txt", true, null);
        menu.init();
        assertTrue(menu.createXML());
    }
}