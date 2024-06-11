import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setup() {
 this.gui = new GUI();
 this.menu = new Menu("Test Menu", "", "", "", true, this.gui);
 }

 @Test
 public void testMenuConstructorNoArgs() {
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
 assertFalse(menu.ismError());
 }

 @Test
 public void testMenuConstructorTitleTextFilePathIsPAL() {
 Constructor<Menu> constructor = null;
 try {
 constructor = Menu.class.getConstructor(String.class, String.class, boolean.class, GUI.class);
 }
 catch (NoSuchMethodException e) {
 e.printStackTrace();
 }
 Menu menu = null;
 try {
 menu = constructor.newInstance(new Object[]{"Test Menu", "", true, this.gui});
 }
 catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
 assertTrue(menu.getStrTitle().equals("Test Menu"));
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertTrue(menu.isPal_menu());
 assertSame(menu.getM_GUI(), this.gui);
 assertNull(menu.getM_Thread());
 assertFalse(menu.ismError());
 }

 @Test
 public void testMenuConstructorTitlePicPathAudioPathTextFilePathIsPAL() {
 Constructor<Menu> constructor = null;
 try {
 constructor = Menu.class.getConstructor(String.class, String.class, String.class, String.class, boolean.class, GUI.class);
 }
 catch (NoSuchMethodException e) {
 e.printStackTrace();
 }
 Menu menu = null;
 try {
 menu = constructor.newInstance(new Object[]{"Test Menu", "/path/to/pic.jpg", "/path/to/audio.mp3", "/path/to/text.txt", true, this.gui});
 }
 catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
 assertTrue(menu.getStrTitle().equals("Test Menu"));
 assertTrue(menu.getStrPicPath().equals("/path/to/pic.jpg"));
 assertTrue(menu.getStrAudioPath().equals("/path/to/audio.mp3"));
 assertTrue(menu.getStrTextFilePath().equals("/path/to/text.txt"));
 assertTrue(menu.isPal_menu());
 assertSame(menu.getM_GUI(), this.gui);
 assertNull(menu.getM_Thread());
 assertFalse(menu.ismError());
 }

 @Test
 public void testInit() {
 this.menu.init();
 assertTrue(this.menu.ismError() || this.gui.ErrorCheck("/log/dvd-menu.log"));
 }

 @Test
 public void testDVDMainMenu() {
 File outputDir = new File("/output/path");
 this.menu.setM_GUI(this.gui);
 this.menu.setStrOutputDir(outputDir.getAbsolutePath());
 this.menu.DVDMainMenu();
 File listDir = new File(outputDir, "list");
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 if (!name.equals("menu.vob")) {
 return true;
 }
 else {
 return false;
 }
 }
 else {
 return false;
 }
 }
 };
 String[] videoFiles = listDir.listFiles(filter);
 assertEquals(1, videoFiles.length);
 assertEquals("menu.vob", videoFiles[0].getName());
 }

 @Test(expected = NullPointerException.class)
 public void testDVDMainMenuWhenTextFilePathIsNull() {
 File outputDir = new File("/output/path");
 this.menu.setM_GUI(this.gui);
 this.menu.setStrOutputDir(outputDir.getAbsolutePath());
 this.menu.setStrTextFilePath(null);
 this.menu.DVDMainMenu();
 }

 @Test
 public void testCreateXML() {
 File outputDir = new File("/output/path");
 this.menu.setStrOutputDir(outputDir.getAbsolutePath());
 this.menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateXMLWhenTextFileDoesNotExist() {
 File outputDir = new File("/output/path");
 this.menu.setStrOutputDir(outputDir.getAbsolutePath());
 this.menu.setTxtTextFile(new File(""));
 this.menu.createXML();
 }

 @Test(expected = IOException.class)
 public void testCreateXMLWhenTextFileCannotBeRead() {
 File outputDir = new File("/output/path");
 this.menu.setStrOutputDir(outputDir.getAbsolutePath());
 this.menu.setTxtTextFile(new File("invalid/path/to/text.txt"));
 this.menu.createXML();
 }

}