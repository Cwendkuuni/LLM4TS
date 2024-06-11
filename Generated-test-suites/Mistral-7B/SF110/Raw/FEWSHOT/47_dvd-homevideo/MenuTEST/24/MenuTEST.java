package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertLineContains;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MenuTest {

 Menu menu;
 GUI gui;

 @Before
 public void setUp() {
 this.gui = mock(GUI.class);
 this.menu = new Menu();
 }

 @Test
 public void testMenuDefaultConstructor() {
 assertEquals("dvd-menu", this.menu.dvd_menu);
 assertNull(this.menu.strTitle);
 assertNull(this.menu.strPicPath);
 assertNull(this.menu.strAudioPath);
 assertNull(this.menu.strTextFilePath);
 assertNull(this.menu.titles);
 assertFalse(this.menu.init());
 }

 @Test
 public void testMenuConstructorWithTitle() {
 String title = "Test Title";
 this.menu = new Menu(title);
 assertEquals(title, this.menu.strTitle);
 assertEquals("dvd-menu", this.menu.dvd_menu);
 assertNull(this.menu.strPicPath);
 assertNull(this.menu.strAudioPath);
 assertNull(this.menu.strTextFilePath);
 assertNull(this.menu.titles);
 assertFalse(this.menu.init());
 }

 @Test
 public void testMenuConstructorWithTitlePicPathAndAudioPath() {
 String title = "Test Title";
 String picPath = "test-pic.jpg";
 String audioPath = "test-audio.mp3";
 this.menu = new Menu(title, picPath, audioPath, null, null, this.gui);
 assertEquals(title, this.menu.strTitle);
 assertEquals(picPath, this.menu.strPicPath);
 assertEquals(audioPath, this.menu.strAudioPath);
 assertEquals("dvd-menu", this.menu.dvd_menu);
 assertNull(this.menu.strTextFilePath);
 assertNull(this.menu.titles);
 assertFalse(this.menu.init());
 }

 @Test
 public void testMenuConstructorWithTitleTextFilePath() {
 String title = "Test Title";
 String textFilePath = "test-text.txt";
 this.menu = new Menu(title, null, null, textFilePath, null, this.gui);
 assertEquals(title, this.menu.strTitle);
 assertEquals("dvd-menu", this.menu.dvd_menu);
 assertNull(this.menu.strPicPath);
 assertNull(this.menu.strAudioPath);
 assertArrayEquals(new String[0], this.menu.titles);
 assertFalse(this.menu.init());
 }

 @Test
 public void testMenuConstructorWithTitlePicPathAudioPathAndTextFilePath() {
 String title = "Test Title";
 String picPath = "test-pic.jpg";
 String audioPath = "test-audio.mp3";
 String textFilePath = "test-text.txt";
 this.menu = new Menu(title, picPath, audioPath, textFilePath, null, this.gui);
 assertEquals(title, this.menu.strTitle);
 assertEquals(picPath, this.menu.strPicPath);
 assertEquals(audioPath, this.menu.strAudioPath);
 assertEquals(textFilePath, this.menu.strTextFilePath);
 assertEquals("dvd-menu", this.menu.dvd_menu);
 assertNull(this.menu.titles);
 assertFalse(this.menu.init());
 }

 @Test
 public void testInitMethodWhenThreadRunsSuccessfully() throws InterruptedException {
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 when(this.gui.ErrorCheck(String.valueOf(new File("log/dvd-menu.log")))).thenReturn(false);
 when(this.gui.MessageBox(String.valueOf(String.class), int.class)).thenReturn(true);
 this.menu.init();
 assertTrue(this.menu.init());
 }

 @Test
 public void testInitMethodWhenThreadThrowsError() throws InterruptedException, IOException {
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 when(this.gui.ErrorCheck(String.valueOf(new File("log/dvd-menu.log")))).thenThrow(IOException.class);
 when(this.gui.MessageBox(String.valueOf(String.class), int.class)).thenReturn(true);
 this.menu.init();
 assertFalse(this.menu.init());
 }

 @Test
 public void testInitMethodWhenThreadThrowsInterruptedException() throws InterruptedException {
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 when(this.gui.ErrorCheck(String.valueOf(new File("log/dvd-menu.log")))).thenReturn(false);
 when(this.gui.MessageBox(String.valueOf(String.class), int.class)).thenReturn(true);
 this.menu.m_Thread = new Thread(this.menu);
 this.menu.m_Thread.interrupt();
 this.menu.init();
 assertFalse(this.menu.init());
 }

 @Test
 public void testCreateXMLMethodWhenFileExists() throws IOException {
 File xmlFile = new File("dvd-menu.xml");
 FileOutputStream output = new FileOutputStream(xmlFile);
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 this.menu.createXML();
 output.close();
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXMLMethodWhenFileDoesNotExist() throws IOException {
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 this.menu.createXML();
 assertFalse(new File("dvd-menu.xml").exists());
 }

 @Test
 @Ignore("File system test on Windows may not work properly.")
 public void testCreateXMLMethodWhenFileExistsAndListDirReturnsFiles() throws IOException {
 File xmlFile = new File("dvd-menu.xml");
 FileListing mockListing = mock(FileListing.class);
 when(this.gui.getFileListing()).thenReturn(mockListing);
 when(mockListing.listFiles((FileFilter) anyObject())).thenReturn(new File[]{"menu.vob", "test-file.vob", "another-file.vob"});
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 this.menu.createXML();
 assertLineContains("test-file.vob", this.menu.m_GUI.txtAreaOutput.getText());
 }

 @Test
 public void testCreateXMLMethodWhenFileExistsAndListDirReturnsEmptyArray() throws IOException {
 File xmlFile = new File("dvd-menu.xml");
 FileListing mockListing = mock(FileListing.class);
 when(this.gui.getFileListing()).thenReturn(mockListing);
 when(mockListing.listFiles((FileFilter) anyObject())).thenReturn(new File[0]);
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 this.menu.createXML();
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXMLMethodWhenTextFileIsEmpty() throws IOException, InterruptedException {
 HashMap<String, Integer> expectedTextFileLines = new HashMap<>();
 expectedTextFileLines.put("Title 1", 1);
 expectedTextFileLines.put("Title 2", 2);
 expectedTextFileLines.put("Title 3", 3);
 expectedTextFileLines.put("Title 4", 4);
 expectedTextFileLines.put("Title 5", 5);
 expectedTextFileLines.put("DONE", 6);
 File textFile = new File("test-text.txt");
 FileWriter textFileWriter = new FileWriter(textFile);
 textFileWriter.write(String.join("\n", expectedTextFileLines.keySet()));
 textFileWriter.close();
 this.menu = new Menu("Test Title", null, null, "test-text.txt", null, this.gui);
 this.menu.createXML();
 textFile.delete();
 }

 @Test
 public void testCreateXMLMethodWhenTextFileIsNotEmptyButHasIncorrectNumberOfLines() throws IOException, InterruptedException {
 HashMap<String, Integer> expectedTextFileLines = new HashMap<>();
 expectedTextFileLines.put("Title 1", 1);
 expectedTextFileLines.put("Title 2", 2);
 expectedTextFileLines.put("Title 3", 3);
 expectedTextFileLines.put("Title 4", 4);
 expectedTextFileLines.put("Title 5", 5);
 expectedTextFileLines.put("Title 6", 6); // Extra line
 expectedTextFileLines.put("DONE", 7);
 File textFile = new File("test-text.txt");
 FileWriter textFileWriter = new FileWriter(textFile);
 textFileWriter.write(String.join("\n", expectedTextFileLines.keySet()));
 textFileWriter.close();
 this.menu = new Menu("Test Title", null, null, "test-text.txt", null, this.gui);
 assertThrows(RuntimeException.class, () -> this.menu.createXML());
 textFile.delete();
 }

 @Test
 public void testCreateXMLMethodWhenTextFileIsNotEmptyButHasMissingTitle() throws IOException, InterruptedException {
 HashMap<String, Integer> expectedTextFileLines = new HashMap<>();
 expectedTextFileLines.put("Title 1", 1);
 expectedTextFileLines.put("Title 2", 2);
 expectedTextFileLines.put("Title 3", 3);
 expectedTextFileLines.put("Title 4", 4);
 expectedTextFileLines.put("Title 5", 5);
 expectedTextFileLines.put("BLANK", 6); // Missing Title 6
 expectedTextFileLines.put("DONE", 7);
 File textFile = new File("test-text.txt");
 FileWriter textFileWriter = new FileWriter(textFile);
 textFileWriter.write(String.join("\n", expectedTextFileLines.keySet()));
 textFileWriter.close();
 this.menu = new Menu("Test Title", null, null, "test-text.txt", null, this.gui);
 assertThrows(RuntimeException.class, () -> this.menu.createXML());
 textFile.delete();
 }

 @Test
 public void testCreateXMLMethodWhenTextFileIsNotEmptyButHasMissingTitleAndExtraTitle() throws IOException, InterruptedException {
 HashMap<String, Integer> expectedTextFileLines = new HashMap<>();
 expectedTextFileLines.put("Title 1", 1);
 expectedTextFileLines.put("Title 2", 2);
 expectedTextFileLines.put("Title 3", 3);
 expectedTextFileLines.put("Title 4", 4);
 expectedTextFileLines.put("Title 5", 5);
 expectedTextFileLines.put("BLANK", 6); // Missing Title 6
 expectedTextFileLines.put("Extra Title", 7); // Extra title
 expectedTextFileLines.put("DONE", 8);
 File textFile = new File("test-text.txt");
 FileWriter textFileWriter = new FileWriter(textFile);
 textFileWriter.write(String.join("\n", expectedTextFileLines.keySet()));
 textFileWriter.close();
 this.menu = new Menu("Test Title", null, null, "test-text.txt", null, this.gui);
 assertThrows(RuntimeException.class, () -> this.menu.createXML());
 textFile.delete();
 }

 @Test
 public void testInitMethodWhenDvdMenuCommandNotFound() throws IOException, InterruptedException {
 this.menu = new Menu("Test Title", null, null, null, null, this.gui);
 when(this.gui.MessageBox(String.valueOf(String.class), int.class)).thenReturn(true);
 String errorMessage = "Could not locate dvd-menu in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.";
 this.menu.init();
 assertLineContains(errorMessage, this.menu.m_GUI.txtAreaOutput.getText());
 }
}

class FileListing {
 File[] listFiles(FileFilter filter) {
 return new File[0];
 }
}