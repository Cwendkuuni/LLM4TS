import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class jgaapGUITest {

 private JFrame frame;
 private jgaapGUI gui;

 @Before
 public void setUp() {
 gui = new jgaapGUI();
 JFrame tempFrame = new JFrame();
 tempFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 tempFrame.setContentPane(gui);
 tempFrame.setSize(600, 400);
 tempFrame.setVisible(true);
 frame = tempFrame;
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testInitComponents() {
 // Check if components are initialized correctly
 assertEquals("Expected 14 components, found: " + gui.getComponentCount(), 14, gui.getComponentCount());
 }

 @Test
 public void testActionPerformed_knownauthor() {
 gui.actionPerformed(new ActionEvent("knownauthor", ActionEvent.ACTION_PERFORMED, null));
 // Check if text field is empty and disabled
 assertFalse("Current author text field should be empty", gui.currentAuthor.getText().length() > 0);
 assertTrue("Current author text field should be disabled", gui.currentAuthor.isEnabled() == false);
 }

 @Test
 public void testActionPerformed_unknownauthor() {
 gui.actionPerformed(new ActionEvent("unknownauthor", ActionEvent.ACTION_PERFORMED, null));
 // Check if text field is empty and enabled
 assertTrue("Current author text field should be empty", gui.currentAuthor.getText().length() == 0);
 assertTrue("Current author text field should be enabled", gui.currentAuthor.isEnabled() == true);
 }

 @Test
 public void testActionPerformed_Import() {
 File testFile = new File("testFile.txt");
 gui.menuFileDemo_actionPerformed(new ActionEvent("Load Demo", ActionEvent.ACTION_PERFORMED, null)); // Load demo file to populate listKnown and listUnknown
 gui.buttonLocal_actionPerformed(new ActionEvent("Import", ActionEvent.ACTION_PERFORMED, null)); // Simulate clicking the "Import" button
 JFileChooser fileChooser = spy(gui.chooseFile);
 doReturn(testFile).when(fileChooser).getSelectedFile();
 gui.actionPerformed(new ActionEvent("Import", ActionEvent.ACTION_PERFORMED, null)); // Simulate action event for "Import" button
 // Check if document is added to the driver
 assertEquals("Expected 1 document, found: " + gui.driver.getDocumentCount(), 1, gui.driver.getDocumentCount());
 // Check if document name is added to the listKnown
 String expectedListKnownOutput = "Test Author - testFile.txt\n";
 assertEquals("Expected listKnown to contain: " + expectedListKnownOutput, expectedListKnownOutput, gui.listKnown.getText());
 }

 @Test
 public void testActionPerformed_Canonicize() {
 // Simulate selecting "Canonicize" tab and clicking "Canonicize" button
 gui.paneOpts.setSelectedIndex(1);
 gui.buttonCanonicize.doClick();
 // Check if progressBar is visible
 assertTrue("ProgressBar should be visible", gui.panelCanonicize.isVisible());
 }

 @Test
 public void testActionPerformed_CreateEventSet() {
 // Simulate selecting "Event Set" tab and clicking "Create Event Set" button
 gui.paneOpts.setSelectedIndex(2);
 gui.buttonEventSet.doClick();
 // Check if progressBar is visible
 assertTrue("ProgressBar should be visible", gui.panelEventSet.isVisible());
 }

 @Test
 public void testActionPerformed_Analyze() {
 // Simulate selecting "Analyze" tab and clicking "Analyze" button
 gui.paneOpts.setSelectedIndex(3);
 gui.buttonAnalyze.doClick();
 // Check if progressBar is visible
 assertTrue("ProgressBar should be visible", gui.panelAnalyze.isVisible());
 }
}