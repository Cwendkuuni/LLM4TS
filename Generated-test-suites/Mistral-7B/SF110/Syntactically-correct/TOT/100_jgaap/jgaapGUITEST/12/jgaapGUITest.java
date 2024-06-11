import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jgaap.gui.guiDriver;
import jgaap.gui.jgaapGUI;

public class jgaapGUITest {

 private JFrame frame;
 private JgaapGUI gui;

 @Before
 public void setUp() {
 gui = new jgaapGUI();
 frame = new JFrame("JGAAP GUI Test");
 frame.getContentPane().add(gui);
 frame.pack();
 frame.setSize(new Dimension(600, 400));
 frame.setVisible(true);
 }

 @Test
 public void testInitComponents() {
 // Testing if all components have been initialized
 assertEquals("Unexpected number of components", 41, gui.getComponentCount());
 // Testing if specific components exist
 assertTrue("Button 'Load Demo' should exist", gui.menuFileDemoExists());
 }

 @Test
 public void testActionPerformed_ImportButton() {
 // Setting up a mock file chooser to simulate a file selection
 JFileChooser mockFileChooser = new JFileChooser();
 mockFileChooser.setSelectedFile(new File("sample.txt"));
 mockFileChooser.setApproveButtonText("Select");

 // Simulating a file selection event
 ActionEvent mockFileChooserEvent = new ActionEvent(mockFileChooser, 0, "SELECTED");

 // Calling the actionPerformed method with the mock event
 SwingUtilities.invokeLater(() -> gui.actionPerformed(mockFileChooserEvent));

 // Testing if the listKnown text area received the correct text
 String expectedText = "sample.txt";
 String actualText = gui.listKnown.getText();
 assertTrue("listKnown text area should contain: " + expectedText, actualText.contains(expectedText));
 }

 @Test
 public void testActionPerformed_ExitButton() {
 // Simulating an exit event
 ActionEvent exitEvent = new ActionEvent(new Object(), 0, "EXIT");

 // Calling the actionPerformed method with the mock event
 SwingUtilities.invokeLater(() -> gui.actionPerformed(exitEvent));

 // Testing if the frame has been closed
 assertTrue("Frame should be closed", frame.isClosed());
 }

 @Test
 public void testActionPerformed_CanonicizeButton() {
 // Testing if the canonicize button's action command is correct
 assertEquals("Unexpected canonicize button's action command", "Canonicize", gui.buttonCanonicize.getActionCommand());

 // Simulating a button click event
 ActionEvent buttonClickEvent = new ActionEvent(gui.buttonCanonicize, 0, "CLICKED");

 // Calling the actionPerformed method with the mock event
 SwingUtilities.invokeLater(() -> gui.actionPerformed(buttonClickEvent));

 // Testing if the progressCanonicize bar is visible
 assertTrue("progressCanonicize bar should be visible", gui.progressCanonicize.isVisible());
 }

 @Test
 public void testActionPerformed_AnalyzeButton() {
 // Testing if the analyze button's action command is correct
 assertEquals("Unexpected analyze button's action command", "Analyze", gui.buttonAnalyze.getActionCommand());

 // Simulating a button click event
 ActionEvent buttonClickEvent = new ActionEvent(gui.buttonAnalyze, 0, "CLICKED");

 // Calling the actionPerformed method with the mock event
 SwingUtilities.invokeLater(() -> gui.actionPerformed(buttonClickEvent));

 // Testing if the progressAnalysis bar is visible
 assertTrue("progressAnalysis bar should be visible", gui.progressAnalysis.isVisible());
 }

 @Test
 public void testActionPerformed_EventSetButton() {
 // Testing if the event set button's action command is correct
 assertEquals("Unexpected event set button's action command", "CreateEventSet", gui.buttonEventSet.getActionCommand());

 // Simulating a button click event
 ActionEvent buttonClickEvent = new ActionEvent(gui.buttonEventSet, 0, "CLICKED");

 // Calling the actionPerformed method with the mock event
 SwingUtilities.invokeLater(() -> gui.actionPerformed(buttonClickEvent));

 // Testing if the progressEventSet bar is visible
 assertTrue("progressEventSet bar should be visible", gui.progressEventSet.isVisible());
 }

 @Test
 public void testMenuFileDemoExists() {
 // Testing if the 'Load Demo' menu item exists
 JMenuItem menuFileDemo = (JMenuItem) gui.menuFile.getComponent(1);
 assertEquals("Unexpected menu item label", "Load Demo", menuFileDemo.getText());
 }
}