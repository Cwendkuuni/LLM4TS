import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class jgaapGUITest {

 private jgaapGUI gui;

 @Before
 public void setUp() {
 gui = new jgaapGUI();
 }

 @Test
 public void testInitComponents() {
 // Testing if components are initialized correctly
 assertEquals(1, gui.getComponentCount());
 // Testing if menu items are added to the menu
 assertEquals(3, gui.menuFile.getMenuComponentCount());
 // Testing if menu items have correct label
 assertEquals("File", gui.menuFile.getItem(0).getText());
 // Testing if JFileChooser is initialized
 assertNotNull(gui.chooseFile);
 }

 @Test
 public void testActionPerformedKnownAuthor() {
 // Testing if "knownauthor" action command is handled correctly
 ActionEvent event = new ActionEvent(new Object(), 0, "knownauthor");
 gui.actionPerformed(event);
 assertTrue(gui.currentAuthor.isEnabled());
 assertFalse(gui.currentAuthor.getText().isEmpty());
 }

 @Test
 public void testActionPerformedUnknownAuthor() {
 // Testing if "unknownauthor" action command is handled correctly
 ActionEvent event = new ActionEvent(new Object(), 0, "unknownauthor");
 gui.actionPerformed(event);
 assertFalse(gui.currentAuthor.isEnabled());
 assertEmpty(gui.currentAuthor.getText());
 }

 @Test
 public void testActionPerformedImport() throws FileNotFoundException {
 // Testing if "Import" action command is handled correctly
 GuiDriver driver = new GuiDriver();
 JFileChooser chooser = new JFileChooser();
 chooser.setDialogTitle("Select a file to open");
 File file = new File("testFile.txt");
 chooser.setSelectedFile(file);
 ActionEvent importEvent = new ActionEvent(file, 0, "Import");
 gui.chooseFile = chooser;
 gui.currentAuthor.setText("Author Name");
 gui.actionPerformed(importEvent);
 // Checking if document is added to list
 assertEquals("Author Name - testFile.txt\n", gui.listKnown.getText());
 // Checking if document is added to driver
 assertEquals("Author Name - testFile.txt", driver.getDocumentName(0));
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformedCanonicizeNullEngine() {
 // Testing if null engine causes exception
 gui.driver = null;
 ActionEvent event = new ActionEvent(new Object(), 0, "Canonicize");
 gui.actionPerformed(event);
 }

 @Test
 public void testActionPerformedAnalyzeNullMethod() {
 // Testing if null method causes exception
 ActionEvent event = new ActionEvent(new Object(), 0, "Analyze");
 gui.buttonGroup2 = null;
 gui.actionPerformed(event);
 }

}