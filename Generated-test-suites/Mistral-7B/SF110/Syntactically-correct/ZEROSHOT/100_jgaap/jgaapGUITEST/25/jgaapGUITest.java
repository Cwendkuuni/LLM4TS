import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import javax.swing.*;
import java.io.*;

public class jgaapGUITest {

 private JFrame frame;
 private jgaapGUI gui;

 @Before
 public void setUp() {
 gui = new jgaapGUI();
 JFrame tempFrame = new JFrame();
 tempFrame.getContentPane().add(gui);
 tempFrame.pack();
 tempFrame.setVisible(true);
 frame = tempFrame;
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testInitComponents() {
 assertNotNull(gui.driver);
 // Testing the various components of the JFrame
 // Add assertions for each component based on your requirements
 assertNotNull(gui.currentAuthor);
 assertNotNull(gui.chooseFile);
 // ... and so on for all other components
 }

 @Test
 public void testActionPerformed() {
 // Create mock events for testing
 ActionEvent mockEvent1 = new ActionEvent(new Object(), 0, "knownauthor");
 ActionEvent mockEvent2 = new ActionEvent(new Object(), 0, "unknownauthor");
 ActionEvent mockEvent3 = new ActionEvent(new Object(), 0, "Import");
 ActionEvent mockEvent4 = new ActionEvent(new Object(), 0, "Canonicize");
 ActionEvent mockEvent5 = new ActionEvent(new Object(), 0, "CreateEventSet");
 ActionEvent mockEvent6 = new ActionEvent(new Object(), 0, "Analyze");
 ActionEvent mockEvent7 = new ActionEvent(new Object(), 0, "Exit");
 ActionEvent mockEvent8 = new ActionEvent(new Object(), 0, "demo");

 // Test each actionPerformed method call with the mock events
 gui.actionPerformed(mockEvent1);
 // Add assertions for each method call based on your requirements
 // ...
 gui.actionPerformed(mockEvent8);
 // Add assertions for the loadDemo() method if necessary
 }
}