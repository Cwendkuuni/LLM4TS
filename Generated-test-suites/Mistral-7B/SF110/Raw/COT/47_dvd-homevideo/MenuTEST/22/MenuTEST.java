import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;

public class MenuTest {

    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertNull(menu.getStrTitle());
        assertNull(menu.getStrPicPath());
        assertNull(menu.getStrAudioPath());
        assertNull(menu.getStrTextFilePath());
        assertEquals("dvd-menu", menu.getDvd_menu());
        assertArrayEquals(new String[0], menu.getTitles());
        assertFalse(menu.isPal_menu());
        assertNull(menu.getBaseErr());
        assertNull(menu.getVideo_files());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.ism_Error());

        Menu menu1 = new Menu("Title1", "", "", "", false, null);
        assertEquals("Title1", menu1.getStrTitle());
        assertNull(menu1.getStrPicPath());
        assertNull(menu1.getStrAudioPath());
        assertNull(menu1.getStrTextFilePath());
        assertEquals("dvd-menu", menu1.getDvd_menu());
        assertArrayEquals(new String[0], menu1.getTitles());
        assertFalse(menu1.isPal_menu());
        assertNull(menu1.getBaseErr());
        assertNull(menu1.getVideo_files());
        assertNotNull(menu1.getM_GUI());
        assertNull(menu1.getM_Thread());
        assertFalse(menu1.ism_Error());

        Menu menu2 = new Menu("Title1", "picPath1", "audioPath1", "TextFilePath1", false, new GUI());
        assertEquals("Title1", menu2.getStrTitle());
        assertEquals("picPath1", menu2.getStrPicPath());
        assertEquals("audioPath1", menu2.getStrAudioPath());
        assertEquals("TextFilePath1", menu2.getStrTextFilePath());
        assertEquals("dvd-menu", menu2.getDvd_menu());
        assertArrayEquals(new String[0], menu2.getTitles());
        assertFalse(menu2.isPal_menu());
        assertNull(menu2.getBaseErr());
        assertNull(menu2.getVideo_files());
        assertNotNull(menu2.getM_GUI());
        assertNotNull(menu2.getM_Thread());
        assertFalse(menu2.ism_Error());
    }

    @Test
    public void testInit_NoError() {
        Menu menu = new Menu();
        assertTrue(menu.init());
        assertFalse(menu.isErrorCheckFailed());
    }

    @Test(expected = InterruptedException.class)
    public void testInit_InterruptedException() {
        Menu menu = new Menu();
        Thread.currentThread().interrupt();
        menu.init();
    }

    @Test(expected = RuntimeException.class)
    public void testInit_RuntimeException() {
        Menu menu = new Menu() {
            @Override
            public void DVDMainMenu() {
                throw new RuntimeException("Test Exception");
            }
        };
        menu.init();
    }

    @Test
    public void testCreateXML_Success() {
        Menu menu = new Menu();
        File xmlFile = menu.createXML();
        assertTrue(xmlFile.exists());
        xmlFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCreateXML_IOException() {
        Menu menu = new Menu() {
            @Override
            public BufferedWriter newBufferedWriter(File file) throws IOException {
                throw new IOException("Test Exception");
            }
        };
        menu.createXML();
    }

    @Test
    public void testDVDMainMenu_NoError() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
    }

    @Test(expected = NullPointerException.class)
    public void testDVDMainMenu_NullTextFile() {
        Menu menu = new Menu();
        menu.setM_GUI(new GUI());
        menu.setStrTextFilePath(null);
        menu.DVDMainMenu();
    }

    @Test(expected = FileNotFoundException.class)
    public void testDVDMainMenu_FileNotFoundException() {
        Menu menu = new Menu() {
            @Override
            GUI getM_GUI() {
                return new GUI() {
                    @Override
                    FileInputStream getInputStream() throws FileNotFoundException {
                        throw new FileNotFoundException("Test FileNotFoundException");
                    }
                };
            }
        };
        menu.setM_GUI(menu.getM_GUI());
        menu.setStrTextFilePath("TextFilePath");
        menu.DVDMainMenu();
    }

    @Test(expected = IOException.class)
    public void testDVDMainMenu_IOException() {
        Menu menu = new Menu() {
            @Override
            BufferedReader newBufferedReader(InputStream inputStream) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        Menu mockMenu = mock(Menu.class);
        when(mockMenu.getM_GUI()).thenReturn(mock(GUI.class));
        when(mockMenu.getM_Thread()).thenReturn(mock(Thread.class));
        doNothing().when(mockMenu).MessageBox(anyString(), anyInt());
        doThrow(new UndeclaredThrowableException(new RuntimeException("Test Exception"))).when(mockMenu).DVDMainMenu();
        menu.setM_GUI(mockMenu);
        menu.init();
    }

    @Test
    public void testDVDMainMenu_InvalidTextFileFormat() {
        Menu menu = new Menu();
        menu.setM_GUI(new GUI());
        String textFileContent = "Title1\nTitle2\nBLANK\nDONE\nTitle3\nTitle4\nTitle5\nTitle6\nTitle7\nTitle8\nTitle9\nTitle10\nTitle11\nTitle12\nDONE\n";
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn(textFileContent);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn("13thTitle");
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn("13thTitle");
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(reader.readLine()).thenReturn(null);
        when(menu.getM_GUI()).thenReturn(mock(GUI.class));
        when(menu.getM_Thread()).thenReturn(mock(Thread.class));
        when(menu.getTextFileContent()).thenReturn(textFileContent);
        doNothing().when(menu).MessageBox(anyString(), anyInt());
        menu.init();
        assertEquals(11, menu.getVideo_files().length);
    }
}