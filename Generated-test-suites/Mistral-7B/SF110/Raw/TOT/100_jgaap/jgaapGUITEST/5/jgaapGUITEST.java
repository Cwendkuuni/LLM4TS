import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class jgaapGUITest {

 private jgaapGUI gui;

 @Before
 public void setUp() {
 gui = new jgaapGUI();
 }

 @Test
 public void testInitComponents() {
 //Test if components are initialized correctly
 assertNotNull(gui.menuBar);
 assertNotNull(gui.menuFile);
 assertNotNull(gui.menuFileExit);
 assertNotNull(gui.menuFileDemo);
 assertNotNull(gui.menuHelp);
 assertNotNull(gui.menuHelpContents);
 assertNotNull(gui.menuHelpAbout);
 assertNotNull(gui.paneOpts);
 // Test other components initialization here
 }

 @Test
 public void testFileExitActionPerformed() {
 // Test if Exit action performs correctly
 ActionEvent exitEvent = new ActionEvent(new Object(), 0, "Exit");
 gui.actionPerformed(exitEvent);
 assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testImportActionPerformedWithValidFile() {
 // Test if import action performs correctly with a valid file
 ActionEvent importEvent = new ActionEvent(new Object(), 0, "Import");
 gui.chooseFile.setSelectedFile(new File("testFile.txt"));
 gui.actionPerformed(importEvent);
 assertTrue(gui.listKnown.getText().contains("testFile.txt"));
 }

 @Test
 public void testImportActionPerformedWithInvalidFile() {
 // Test if import action throws an error with an invalid file
 ActionEvent importEvent = new ActionEvent(new Object(), 0, "Import");
 gui.chooseFile.setSelectedFile(null);
 try {
 gui.actionPerformed(importEvent);
 fail("Expected FileNotFoundException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 // Add other test methods here for other methods and edge cases
}