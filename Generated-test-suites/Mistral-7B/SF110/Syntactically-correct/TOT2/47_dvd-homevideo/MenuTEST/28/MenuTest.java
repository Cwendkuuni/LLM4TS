import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.StringTokenizer;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "test.txt", false, null);
 }

 @Test
 public void testConstructor() {
 Menu menus[] = new Menu[3];
 menus[0] = new Menu();
 menus[1] = new Menu("Test Title", null, null, null, false, null);
 menus[2] = new Menu("Test Title", "test.png", "test.wav", "test.txt", false, null);
 assertNotNull(menus[0]);
 assertNotNull(menus[1]);
 assertNotNull(menus[2]);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test(expected = InterruptedException.class)
 public void testInitInterrupted() {
 menu.init();
 Thread.currentThread().interrupt();
 menu.init();
 }

 @Test
 public void testDVDMainMenu() {
 menu.DVDMainMenu();
 assertTrue(menu.m_GUI.lblAuthor.isEnabled());
 assertTrue(menu.m_GUI.lblAuthorProg.isEnabled());
 assertTrue(menu.m_GUI.prgAuthor.isEnabled());
 }

 @Test
 public void testCreateXML() {
 File xmlFile = new File("test.xml");
 boolean result = menu.createXML();
 assertTrue(result);
 assertEquals(true, xmlFile.exists());
 }

 @Test(expected = IOException.class)
 public void testCreateXMLIOException() {
 File xmlFile = new File("test.xml");
 menu.createXML();
 xmlFile.delete();
 menu.createXML();
 }

 @Test
 public void testVideoFilesFiltering() {
 File ListDir = new File("test");
 menus[0] = new Menu(null, null, null, null, false, null);
 menus[0].video_files = ListDir.listFiles(new FilenameFilter() {
 @Override
 public boolean accept(File dir, String name) {
 return name.endsWith(".vob") || name.equals("menu.vob");
 }
 });
 assertEquals(2, menus[0].video_files.length);
 }

 @Test
 public void testBufferedReader() throws IOException {
 BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
 String strLine;
 StringTokenizer tokenizer;
 String title;
 int i = 0;
 while ((strLine = reader.readLine()) != null) {
 title = strLine;
 assertEquals(title, menus[1].titles[i]);
 ++i;
 }
 reader.close();
 }

 @Test(expected = FileNotFoundException.class)
 public void testBufferedReaderFileNotFound() throws IOException {
 BufferedReader reader = new BufferedReader(new FileReader("nonExistentFile.txt"));
 }
}