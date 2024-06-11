import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import jgaap.*;
import jgaap.gui.*;

public class jgaapGUITest {

 private jgaapGUI gui;

 @Before
 public void setup() {
 gui = new jgaapGUI();
 }

 @Test
 public void testInitComponents() {
 // Test if components are initialized correctly
 assertEquals("Expected number of components", 46, gui.getComponentCount());
 }

 @Test
 public void testActionPerformedKnownAuthor() {
 // Test if text field is enabled when "Known Author" radio button is selected
 gui.radioKnown.doClick();
 assertTrue("Text field should be enabled", gui.currentAuthor.isEnabled());
 }

 @Test
 public void testActionPerformedUnknownAuthor() {
 // Test if text field is disabled when "Unknown Author" radio button is selected
 gui.radioUnknown.doClick();
 assertFalse("Text field should be disabled", gui.currentAuthor.isEnabled());
 }

 @Test
 public void testActionPerformedImport() throws IOException {
 // Test if document is added to the list and JFileChooser dialog is shown when "Import" button is clicked
 File file = new File("test.txt");
 gui.buttonLocal.doClick();
 gui.chooseFile.setSelectedFile(file);
 gui.buttonLocal.doClick();
 assertEquals("Expected number of items in known list", 1, gui.listKnown.getLineCount());
 assertEquals("Expected text in known list", file.getName(), gui.listKnown.getText().split("\n")[0]);
 }

 @Test
 public void testActionPerformedCanonicize() {
 // Test if canonicize button is disabled when no document is selected
 assertFalse("Canonicize button should be disabled", gui.buttonCanonicize.isEnabled());
 // Test if canonicize button is enabled when a document is selected
 gui.listKnown.setText("test.txt");
 assertTrue("Canonicize button should be enabled", gui.buttonCanonicize.isEnabled());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformedCanonicizeInvalidDocument() {
 // Test if an IllegalArgumentException is thrown when an invalid document is selected
 gui.listUnknown.setText("invaliddocument.txt");
 gui.buttonCanonicize.doClick();
 }

 @Test
 public void testActionPerformedAnalyze() {
 // Test if analyze button is disabled when no document is selected
 assertFalse("Analyze button should be disabled", gui.buttonAnalyze.isEnabled());
 // Test if analyze button is enabled when a document is selected
 gui.listKnown.setText("test.txt");
 assertTrue("Analyze button should be enabled", gui.buttonAnalyze.isEnabled());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformedAnalyzeInvalidDocument() {
 // Test if an IllegalArgumentException is thrown when an invalid document is selected
 gui.listUnknown.setText("invaliddocument.txt");
 gui.buttonAnalyze.doClick();
 }
}