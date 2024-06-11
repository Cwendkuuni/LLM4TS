import org.junit.Assert;
import org.junit.Test;
import java.util.StringJoiner;
import java.io.File;
import java.io.IOException;

public class MenuTest {

 private Menu menu;

 @Test
 public void testMenuConstructionWithTitleAndTextFile() {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 Assert.assertEquals("Test Menu", menu.getStrTitle());
 }

 @Test
 public void testMenuConstructionWithTitlePicPathAndAudioPath() {
 menu = new Menu("Test Menu", "testPicPath.jpg", "testAudioPath.mp3", "testTextFile.txt", false, null);
 Assert.assertEquals("Test Menu", menu.getStrTitle());
 Assert.assertEquals("testPicPath.jpg", menu.getStrPicPath());
 Assert.assertEquals("testAudioPath.mp3", menu.getStrAudioPath());
 }

 @Test
 public void testMenuConstructionWithTitlePicPathAndTextFile() {
 menu = new Menu("Test Menu", "testPicPath.jpg", "", "testTextFile.txt", false, null);
 Assert.assertEquals("Test Menu", menu.getStrTitle());
 Assert.assertEquals("testPicPath.jpg", menu.getStrPicPath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMenuConstructionWithNullTitle() {
 menu = new Menu(null, "testTextFile.txt", false, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMenuConstructionWithEmptyTitle() {
 menu = new Menu("", "testTextFile.txt", false, null);
 }

 @Test
 public void testInit() {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 menu.init();
 Assert.assertFalse(menu.isError());
 }

 @Test(expected = InterruptedException.class)
 public void testInitInterrupted() throws InterruptedException {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 Thread.currentThread().interrupt();
 menu.init();
 }

 @Test
 public void testDVDMainMenu() {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 menu.DVDMainMenu();
 }

 @Test(expected = NullPointerException.class)
 public void testDVDMainMenuWithNullGUI() {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 menu.m_GUI = null;
 menu.DVDMainMenu();
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 Assert.assertTrue(xmlFile.exists());
 }

 @Test(expected = IOException.class)
 public void testCreateXMLIOError() throws IOException {
 menu = new Menu("Test Menu", "testTextFile.txt", false, null);
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 final BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile));
 writer.close();
 menu.createXML();
 }
}