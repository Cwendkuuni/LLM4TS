import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.StringJoiner;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 gui = new GUI();
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithError() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 menu.m_Error = true;
 assertTrue(menu.init());
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", null, null, null, false, gui);
 assertFalse(menu.createXML());
 }

 @Test
 public void testCreateXMLWithError() {
 menu = new Menu("Test Menu", null, null, null, false, gui);
 menu.m_Error = true;
 assertFalse(menu.createXML());
 }

 @Test
 public void testDVDMainMenuWithValidFiles() {
 menu = new Menu("Test Menu", null, null, null, false, gui);
 final StringJoiner joiner = new StringJoiner(", ", "", " ");
 final File ListDir = new File("testOutputDir");
 final File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; ++i) {
 testFiles[i] = new File(ListDir, "testFile" + i + ".vob");
 testFiles[i].createNewFile();
 joiner.add(testFiles[i].getName());
 }
 menu.video_files = ListDir.listFiles(new FilenameFilter() {
 @Override
 public boolean accept(File dir, String name) {
 return name.endsWith(".vob");
 }
 });
 menu.DVDMainMenu();
 for (int i = 0; i < testFiles.length; ++i) {
 assertTrue(new File(ListDir, "testFile" + i + ".xml").exists());
 }
 for (int i = 0; i < testFiles.length; ++i) {
 testFiles[i].delete();
 }
 }

 @Test
 public void testDVDMainMenuWithMissingFile() {
 menu = new Menu("Test Menu", null, null, null, false, gui);
 final StringJoiner joiner = new StringJoiner(", ", "", " ");
 final File ListDir = new File("testOutputDir");
 final File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; ++i) {
 testFiles[i] = new File(ListDir, "testFile" + i + ".vob");
 testFiles[i].createNewFile();
 }
 menu.video_files = ListDir.listFiles(new FilenameFilter() {
 @Override
 public boolean accept(File dir, String name) {
 return name.endsWith(".vob");
 }
 });
 File missingFile = new File(ListDir, "missingFile.vob");
 menu.video_files = new File[menu.video_files.length - 1];
 System.arraycopy(menu.video_files, 0, menu.video_files = new File[menu.video_files.length], 0);
 menu.DVDMainMenu();
 assertFalse(missingFile.exists());
 for (int i = 0; i < testFiles.length; ++i) {
 testFiles[i].delete();
 }
 }
}