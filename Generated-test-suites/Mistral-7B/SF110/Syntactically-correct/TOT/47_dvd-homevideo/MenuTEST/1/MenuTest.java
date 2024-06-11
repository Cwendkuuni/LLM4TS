import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.File;
import java.util.FilenameFilter;
import java.util.StringTokenizer;

public class MenuTest {

 private Menu menu;
 private String outputDir;
 private String txtTextFile;
 private String strTitle;
 private String picPath;
 private String audioPath;
 private boolean isPAL;
 private GUI dvdGUI;

 @Before
 public void setUp() {
 menu = new Menu();
 outputDir = "output";
 txtTextFile = "test.txt";
 strTitle = "Test Menu";
 picPath = null;
 audioPath = null;
 isPAL = false;
 dvdGUI = new GUI();
 }

 @Test
 public void testInit_NoArguments() {
 menu = new Menu();
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_TitleArg() {
 menu = new Menu(strTitle, null, null, null, isPAL, dvdGUI);
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_TitleArgWithTextFilePath() {
 menu = new Menu(strTitle, null, null, txtTextFile, isPAL, dvdGUI);
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_TitleArgWithPicPathAndTextFilePath() {
 menu = new Menu(strTitle, picPath, audioPath, txtTextFile, isPAL, dvdGUI);
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_TitleArgWithPicPathAudioPathAndTextFilePath() {
 menu = new Menu(strTitle, picPath, audioPath, txtTextFile, isPAL, dvdGUI);
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile = new File(outputDir, "menu.vob");
 menuVobFile.createNewFile();
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test(expected = InterruptedException.class)
 public void testInit_ThreadInterrupted() {
 menu = new Menu();
 Thread.currentThread().interrupt();
 menu.init();
 fail("Thread should be interrupted and return an error");
 }

 @Test
 public void testCreateXML_NoVobs() {
 menu = new Menu();
 assertFalse(menu.createXML());
 }

 @Test
 public void testCreateXML_OneVob() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile = new File(outputDir, "menu.vob");
 menuVobFile.createNewFile();
 menu.init();
 menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXML_MultipleVobs() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile1 = new File(outputDir, "menu1.vob");
 menuVobFile1.createNewFile();
 File menuVobFile2 = new File(outputDir, "menu2.vob");
 menuVobFile2.createNewFile();
 menu.init();
 menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXML_InvalidTextFile() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile = new File(outputDir, "menu.vob");
 menuVobFile.createNewFile();
 File txtTextFileInvalid = new File(outputDir, "testTextInvalid.txt");
 txtTextFileInvalid.createNewFile();
 menu.init();
 menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXML_InvalidNumberOfTitles() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile = new File(outputDir, "menu.vob");
 menuVobFile.createNewFile();
 File txtTextFile = new File(outputDir, txtTextFile);
 try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtTextFile))) {writer.write("Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nTitle 11");}catch (IOException e) {e.printStackTrace();}
 menu.init();
 menu.createXML();File xmlFile = new File(outputDir, "vmgm.xml");assertFalse(xmlFile.exists());}

 @Test
 public void testCreateXML_InvalidTitleLineFormat() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile = new File(outputDir, "menu.vob");
 menuVobFile.createNewFile();
 File txtTextFile = new File(outputDir, txtTextFile);
 try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtTextFile))) {writer.write("Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nTitle 10\nBLANK\nTitle 12");}catch (IOException e) {e.printStackTrace();}
 menu.init();
 menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXML_InvalidTitleLineFormat_MissingTitle() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile = new File(outputDir, "menu.vob");
 menuVobFile.createNewFile();
 File txtTextFile = new File(outputDir, txtTextFile);
 try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtTextFile))) {writer.write("Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nDONE");}catch (IOException e) {e.printStackTrace();}
 menu.init();
 menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXML_InvalidTitleLineFormat_MissingVob() {
 menu = new Menu();
 File outputDirFolder = new File(outputDir);
 outputDirFolder.mkdir();
 File menuVobFile1 = new File(outputDir, "menu1.vob");
 menuVobFile1.createNewFile();
 File txtTextFile = new File(outputDir, txtTextFile);
 try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtTextFile))) {writer.write("Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nTitle 10\nTitle 11\nTitle 12");}catch (IOException e) {e.printStackTrace();}
 menu.init();
 menu.createXML();
 File xmlFile = new File(outputDir, "vmgm.xml");
 assertFalse(xmlFile.exists());
 }
}