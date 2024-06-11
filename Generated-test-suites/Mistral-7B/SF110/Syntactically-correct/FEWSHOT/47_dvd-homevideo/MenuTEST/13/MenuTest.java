package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

 Menu menu = new Menu(); // default constructor test
 Menu menuWithTitle = new Menu("Test Menu", ""); // constructor with title and empty text file
 Menu menuWithTitleAndTextFile = new Menu("Test Menu", "", "", "test.txt", false, null); // constructor with title, picPath, audioPath, textFilePath, isPAL, and GUI

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 menuWithTitle.init();
 assertFalse(menuWithTitle.m_Error);
 menuWithTitleAndTextFile.init();
 assertFalse(menuWithTitleAndTextFile.m_Error);
 }

 @Test
 public void testDVDMainMenu() {
 menuWithTitleAndTextFile.init();
 // test for menu_options string creation based on different inputs
 assertEquals("-c -D -n Test Menu -o " + menuWithTitleAndTextFile.m_GUI.strOutputDir, menuWithTitleAndTextFile.menuOptions(false, false, false, false));
 assertEquals("-c -D -n Test Menu -o " + menuWithTitleAndTextFile.m_GUI.strOutputDir + " -a " + menuWithTitleAndTextFile.strAudioPath, menuWithTitleAndTextFile.menuOptions(false, false, false, true));
 assertEquals("-c -D -n Test Menu -o " + menuWithTitleAndTextFile.m_GUI.strOutputDir + " -b " + menuWithTitleAndTextFile.strPicPath, menuWithTitleAndTextFile.menuOptions(false, true, false, false));
 assertEquals("-c -D -n Test Menu -o " + menuWithTitleAndTextFile.m_GUI.strOutputDir + " -a " + menuWithTitleAndTextFile.strAudioPath + " -b " + menuWithTitleAndTextFile.strPicPath, menuWithTitleAndTextFile.menuOptions(true, true, true, true));

 // test for error messages when text file is not found or not formatted correctly
 Menu menuWithInvalidTextFile = new Menu("Test Menu", "", "", "invalidTextFile.txt", false, null);
 menuWithInvalidTextFile.init();
 assertTrue(menuWithInvalidTextFile.m_Error);
 }

 @Test
 public void testCreateXML() {
 Menu menuWithXML = new Menu("Test Menu", "", "", "testXML.xml", false, null);
 menuWithXML.init();
 menuWithXML.createXML();
 File xmlFile = new File(menuWithXML.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 xmlFile.delete();
 }

}