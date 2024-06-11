package org.apache.commons.cli.menu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class MenuTest {

 Menu menu = new Menu();
 Menu menuWithTitle = new Menu("Title", null, null, null, false, null);
 Menu menuWithTitlePicAndAudio = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
 Menu menuWithPal = new Menu("Title", null, null, null, true, null);

 @Test
 public void testMenuConstruction() {
 Menu menuWithNoArguments = new Menu();
 assertEquals(50, menuWithNoArguments.titles.length);
 }

 @Test
 public void testMenuWithTitleConstruction() {
 Menu menuWithTitleInstance = menuWithTitle;
 assertEquals("Title", menuWithTitleInstance.strTitle);
 }

 @Test
 public void testMenuWithTitlePicAndAudioConstruction() {
 Menu menuWithTitlePicAndAudioInstance = menuWithTitlePicAndAudio;
 assertEquals("Title", menuWithTitlePicAndAudioInstance.strTitle);
 assertEquals("pic.jpg", menuWithTitlePicAndAudioInstance.strPicPath);
 assertEquals("audio.mp3", menuWithTitlePicAndAudioInstance.strAudioPath);
 }

 @Test
 public void testMenuWithTextFilePathConstruction() {
 Menu menuWithTextFilePathInstance = new Menu("Title", null, null, "text.txt", false, null);
 assertFalse(menuWithTextFilePathInstance.video_files == null);
 }

 @Test
 public void testMenuWithPalConstruction() {
 Menu menuWithPalInstance = menuWithPal;
 assertTrue(menuWithPalInstance.pal_menu);
 }

 @Test
 public void testInitSuccess() {
 Menu menuInstance = new Menu();
 assertTrue(menuInstance.init());
 }

 @Test
 public void testInitFailure() {
 Menu menuInstance = new Menu();
 menuInstance.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 return "outputDir";
 }

 @Override
 public String strErrorDir() {
 return "errorDir";
 }

 @Override
 public void MessageBox(String message, int type) {
 // Empty implementation
 }
 };
 menuInstance.m_Thread = new Thread(menuInstance) {
 @Override
 public void run() {
 throw new RuntimeException("Thread Exception");
 }
 };
 assertFalse(menuInstance.init());
 }

 @Test
 public void testDVDMainMenu() throws IOException, InterruptedException {
 Menu menuInstance = new Menu();
 File listDir = new File("outputDir");
 listDir.mkdirs();
 File[] files = new File("outputDir").listFiles(new FilenameFilter() {
 boolean accept(File dir, String name) {
 return name.endsWith(".vob");
 }
 });
 File menuVob = new File("outputDir", "menu.vob");
 menuVob.createNewFile();
 menuInstance.strTextFilePath = "text.txt";
 File textFile = new File("outputDir", "text.txt");
 textFile.createNewFile();
 BufferedReader reader = new BufferedReader(new FileReader("text.txt"));
 String line;
 int i = 0;
 while ((line = reader.readLine()) != null) {
 menuInstance.titles[i++] = line;
 }
 reader.close();
 menuInstance.pal_menu = false;
 menuInstance.dvd_menu = "dvd-menu";
 menuInstance.init();
 menuInstance.DVDMainMenu();
 File dvdMenuLogFile = new File("outputDir/log/dvd-menu.log");
 assertTrue(dvdMenuLogFile.exists());
 }

 @Test
 public void testCreateXML() throws IOException {
 Menu menuInstance = new Menu();
 menuInstance.init();
 File xmlFile = new File("outputDir/vmgm.xml");
 assertTrue(xmlFile.createNewFile());
 menuInstance.createXML();
 File xmlFileContent = new File("outputDir/vmgm.xml");
 assertEquals("<dvdauthor dest=\"outputDir/DVD/\" jumppad=\"0\">\n" +
 "\t<vmgm>\n" +
 "\t\t<menus>\n" +
 "\t\t\t<pgc entry=\"title\" >\n" +
 "\t\t\t\t<vob file=\"outputDir/menu.vob\" pause=\"inf\" />\n" +
 "\t\t\t\t<post> jump vmgm menu 1; </post>\n" +
 "\t\t\t</pgc>\n" +
 "\t\t\t<pgc entry=\"title 2\" >\n" +
 "\t\t\t\t<vob file=\"outputDir/video1.vob\" />\n" +
 "\t\t\t\t<post> jump title 2; </post>\n" +
 "\t\t\t</pgc>\n" +
 "\t\t\t<pgc entry=\"title 3\" >\n" +
 "\t\t\t\t<vob file=\"outputDir/video2.vob\" />\n" +
 "\t\t\t\t<post> jump title 3; </post>\n" +
 "\t\t\t</pgc>\n" +
 "\t\t\t<pgc entry=\"title 4\" >\n" +
 "\t\t\t\t<vob file=\"outputDir/video3.vob\" />\n" +
 "\t\t\t\t<post> jump title 4; </post>\n" +
 "\t\t\t</pgc>\n" +
 "\t\t</menus>\n" +
 "\t</vmgm>\n" +
 "\t<titleset>\n" +
 "\t<titles>\n" +
 "\t\t<pgc>\n" +
 "\t\t\t<vob file=\"outputDir/video1.vob\" />\n" +
 "\t\t\t<post>jump title 2;</post>\n" +
 "\t\t</pgc>\n" +
 "\t\t<pgc>\n" +
 "\t\t\t<vob file=\"outputDir/video2.vob\" />\n" +
 "\t\t\t<post>jump title 3;</post>\n" +
 "\t\t</pgc>\n" +
 "\t\t<pgc>\n" +
 "\t\t\t<vob file=\"outputDir/video3.vob\" />\n" +
 "\t\t\t<post>call vmgm menu 1;</post>\n" +
 "\t\t</pgc>\n" +
 "\t</titles>\n" +
 "\t</titleset>\n" +
 "</dvdauthor>", new String(Files.readAllBytes(xmlFileContent.toPath())));
 }
}