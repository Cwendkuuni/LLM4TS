import org.junit.Test;
import static org.junit.Assert.*;
import java.util.StringTokenizer;

public class MenuTest {

    @Test
    public void testMenuConstructorWithNoArguments() {
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
    public void testMenuConstructorWithTitleArgument() {
        String title = "Sample Menu";
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
    public void testMenuConstructorWithTitlePicPathAudioPathTextFilePathArgument() {
        String title = "Sample Menu";
        String picPath = "sample.jpg";
        String audioPath = "sample.mp3";
        String textFilePath = "sample.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        assertEquals(title, menu.getStrTitle());
        assertEquals(picPath, menu.getStrPicPath());
        assertEquals(audioPath, menu.getStrAudioPath());
        assertEquals(textFilePath, menu.getStrTextFilePath());
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
    public void testInitMethod() throws InterruptedException {
        String title = "Sample Menu";
        Menu menu = new Menu(title, "", "", "", false, null);
        menu.init();
        assertTrue(menu.isInit());
        assertTrue(menu.ism_Error() || menu.getM_GUI().ErrorCheck(menu.getM_GUI().strOutputDir + "/log/dvd-menu.log"));
    }

    @Test(expected = NullPointerException.class)
    public void testInitMethodWithNullM_GUI() throws InterruptedException {
        String title = "Sample Menu";
        Menu menu = new Menu(title, "", "", "", false, null);
        menu.m_GUI = null;
        menu.init();
    }

    @Test
    public void testDVDMainMenuMethodWithNoVobFiles() {
        String title = "Sample Menu";
        Menu menu = new Menu(title, "", "", "", false, null);
        menu.DVDMainMenu();
        assertNull(menu.getDvd_menu());
        assertFalse(menu.ism_Error());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDVDMainMenuMethodWithInvalidTextFile() {
        String title = "Sample Menu";
        String textFilePath = "invalid_text_file.txt";
        Menu menu = new Menu(title, "", "", textFilePath, false, null);
        menu.DVDMainMenu();
    }

    @Test
    public void testDVDMainMenuMethodWithValidTextFile() {
        String title = "Sample Menu";
        String textFilePath = "sample_text_file.txt";
        Menu menu = new Menu(title, "", "", textFilePath, false, null);
        menu.DVDMainMenu();
        String menu_options = menu.getDvd_menu();
        assertTrue(menu_options.contains("dvd-menu"));
        assertTrue(menu_options.contains("c"));
        assertTrue(menu_options.contains("D"));
        assertTrue(menu_options.contains("n"));
        assertTrue(menu_options.contains(title));
        assertTrue(menu_options.contains("-o"));
        assertTrue(menu_options.contains(menu.getM_GUI().strOutputDir));
        if (menu.getStrPicPath() != null) {
            assertTrue(menu_options.contains("-b"));
            assertTrue(menu_options.contains(menu.getStrPicPath()));
        }
        if (menu.getStrAudioPath() != null) {
            assertTrue(menu_options.contains("-a"));
            assertTrue(menu_options.contains(menu.getStrAudioPath()));
        }
        StringTokenizer st = new StringTokenizer(menu_options, " ");
        int num_lines = 0;
        while (st.hasMoreTokens()) {
            ++num_lines;
            String line = st.nextToken();
            if (line.contains("title")) {
                if (menu.getTitles() != null) {
                    for (int i = 0; i < menu.getTitles().length; ++i) {
                        assertEquals(menu.getTitles()[i], line.substring(line.indexOf("title") + 6, line.length()));
                    }
                }
                break;
            }
        }
        assertEquals(num_lines, menu.getTitles().length + 1);
        assertFalse(menu.ism_Error());
    }

    @Test
    public void testCreateXMLMethod() {
        String title = "Sample Menu";
        String textFilePath = "sample_text_file.txt";
        Menu menu = new Menu(title, "", "", textFilePath, false, null);
        menu.DVDMainMenu();
        assertTrue(menu.createXML());
        File xmlFile = new File(menu.getM_GUI().strOutputDir + "/vmgm.xml");
        assertTrue(xmlFile.exists());
        xmlFile.delete();
        assertFalse(menu.ism_Error());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateXMLMethodWithInvalidTextFile() {
        String title = "Sample Menu";
        String textFilePath = "invalid_text_file.txt";
        Menu menu = new Menu(title, "", "", textFilePath, false, null);
        menu.DVDMainMenu();
        assertTrue(menu.createXML());
    }

    @Test(expected = IOException.class)
    public void testCreateXMLMethodWithIOException() {
        String title = "Sample Menu";
        String textFilePath = "sample_text_file.txt";
        Menu menu = new Menu(title, "", "", textFilePath, false, null);
        menu.DVDMainMenu();
        SaveStackTrace.printTrace(menu.getM_GUI().strOutputDir, new IOException("Test IOException"));
        assertTrue(menu.ism_Error());
    }

}