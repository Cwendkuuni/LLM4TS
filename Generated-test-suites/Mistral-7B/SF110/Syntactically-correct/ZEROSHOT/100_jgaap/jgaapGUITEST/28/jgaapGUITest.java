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
 // Initialize the GUI
 gui = new jgaapGUI();
 JFrame testFrame = new JFrame();
 testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 testFrame.getContentPane().add(gui);
 testFrame.pack();
 testFrame.setSize(new Dimension(600, 400));
 testFrame.setVisible(true);
 }

 @After
 public void tearDown() {
 // Clean up the GUI
 if (frame != null) {
 frame.dispose();
 }
 }

 @Test
 public void testInitComponents() {
 // Test the initialization of components
 assertEquals("Unexpected number of components initialized", 49, gui.getComponentCount());
 }

 @Test
 public void testActionPerformedExit() {
 // Test the Exit action
 ActionEvent exitEvent = new ActionEvent(new Object(), 0, "Exit");
 gui.actionPerformed(exitEvent);
 // Verify that the system exits
 assertTrue("System did not exit", System.exitCallCount > 0);
 }

 @Test
 public void testActionPerformedKnownAuthor() {
 // Test the Known Author action
 ActionEvent knownEvent = new ActionEvent(new Object(), 0, "knownauthor");
 gui.actionPerformed(knownEvent);
 // Verify that the current author text field is empty and disabled
 assertTrue("Current author text field should be empty and disabled", gui.currentAuthor.getText().isEmpty() && !gui.currentAuthor.isEnabled());
 }

 @Test
 public void testActionPerformedUnknownAuthor() {
 // Test the Unknown Author action
 ActionEvent unknownEvent = new ActionEvent(new Object(), 0, "unknownauthor");
 gui.actionPerformed(unknownEvent);
 // Verify that the current author text field is empty and enabled
 assertTrue("Current author text field should be empty and enabled", gui.currentAuthor.getText().isEmpty() && gui.currentAuthor.isEnabled());
 }

 @Test
 public void testActionPerformedImport() throws IOException {
 // Test the Import action
 File file = new File("test.txt");
 ActionEvent importEvent = new ActionEvent(new Object(), 0, "Import");
 gui.chooseFile.setSelectedFile(file);
 gui.actionPerformed(importEvent);
 // Verify that the document was added to the known list
 String knownText = gui.listKnown.getText();
 assertTrue("Known list should contain the imported document", knownText.contains(file.getName()));
 }

 @Test
 public void testActionPerformedCanonicize() {
 // Test the Canonicize action
 ActionEvent canonicizeEvent = new ActionEvent(new Object(), 0, "Canonicize");
 gui.buttonCanonicize.doClick();
 // Verify that the canonicize progress bar is displayed and updates
 assertTrue("Canonicize progress bar should be displayed", gui.panelCanonicize.isVisible());
 assertTrue("Canonicize progress bar should update", gui.progressCanonicize.isVisible());
 }

 @Test
 public void testActionPerformedCreateEventSet() {
 // Test the Create Event Set action
 ActionEvent createEventSetEvent = new ActionEvent(new Object(), 0, "CreateEventSet");
 gui.buttonEventSet.doClick();
 // Verify that the create event set progress bar is displayed and updates
 assertTrue("Create event set progress bar should be displayed", gui.panelEvProgress.isVisible());
 assertTrue("Create event set progress bar should update", gui.progressEventSet.isVisible());
 }

 @Test
 public void testActionPerformedAnalyze() {
 // Test the Analyze action
 ActionEvent analyzeEvent = new ActionEvent(new Object(), 0, "Analyze");
 gui.buttonAnalyze.doClick();
 // Verify that the analyze progress bar is displayed and updates
 assertTrue("Analyze progress bar should be displayed", gui.panelAnalyze.isVisible());
 assertTrue("Analyze progress bar should update", gui.progressAnalysis.isVisible());
 }

 @Test
 public void testActionPerformedDemo() {
 // Test the Demo action
 ActionEvent demoEvent = new ActionEvent(new Object(), 0, "demo");
 gui.menuFileDemo.doClick();
 // Verify that the demo method is called
 assertTrue("Demo method was not called", gui.loadDemoCalled);
 }
}