import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;

class jgaapGUITest {

 private static JFrame frame;
 private static jgaapGUI gui;

 @BeforeAll
 public static void setUp() {
 gui = new jgaapGUI();
 JFrame testFrame = new JFrame();
 testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 testFrame.setContentPane(gui);
 testFrame.pack();
 testFrame.setSize(gui.getPreferredSize().width, gui.getPreferredSize().height);
 testFrame.setVisible(true);
 frame = testFrame;
 }

 @AfterAll
 public static void tearDown() {
 frame.dispose();
 }

 @Test
 void testInitComponents() {
 // Testing if the components are initialized correctly
 assertEquals(1, gui.menuBar.getComponentCount());
 assertEquals(2, gui.menuBar.getComponentAt(0).getComponentCount());
 assertEquals(2, gui.menuFile.getComponentCount());
 assertTrue(gui.menuFileExit instanceof JMenuItem);
 assertTrue(gui.menuFileDemo instanceof JMenuItem);
 assertTrue(gui.paneOpts instanceof JTabbedPane);
 assertNotNull(gui.panelAcquire);
 assertNotNull(gui.panelDocs);
 assertNotNull(gui.labelKnown);
 assertNotNull(gui.listKnown);
 assertNotNull(gui.labelUnknown);
 assertNotNull(gui.listUnknown);
 assertNotNull(gui.panelOpts);
 assertNotNull(gui.radioKnown);
 assertNotNull(gui.radioUnknown);
 assertNotNull(gui.buttonLocal);
 assertNotNull(gui.panelCanonicize);
 assertNotNull(gui.panelCOpts);
 assertNotNull(gui.canonicizers);
 assertNotNull(gui.eventifiers);
 assertNotNull(gui.esButtonGroup);
 assertNotNull(gui.panelProgress);
 assertNotNull(gui.buttonCanonicize);
 assertNotNull(gui.label1);
 assertNotNull(gui.progressCanonicize);
 assertNotNull(gui.labelCanon);
 assertNotNull(gui.panelAnalyze);
 assertNotNull(gui.labelStatInf);
 assertNotNull(gui.panelMethods);
 assertNotNull(gui.radioCrossEnt);
 assertNotNull(gui.radioLZW);
 assertNotNull(gui.panelAnalProg);
 assertNotNull(gui.buttonAnalyze);
 assertNotNull(gui.labelAnalProg);
 assertNotNull(gui.progressAnalysis);
 assertNotNull(gui.scrollPane3);
 assertNotNull(gui.listResults);
 assertNotNull(gui.buttonEventSet);
 assertNotNull(gui.paneEvProgress);
 assertNotNull(gui.labelevSet);
 assertNotNull(gui.progressEventSet);
 assertNotNull(gui.panelEventSet);
 assertNotNull(gui.panelEOpts);
 }

 @Test
 void testActionPerformedWithValidFile() {
 // Testing if the "Import" button works correctly with a valid file
 ActionEvent actionEvent = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
 gui.actionPerformed(actionEvent);
 JFileChooser fileChooser = gui.chooseFile;
 fileChooser.setSelectedFile(new File("test.txt"));
 int returnValue = fileChooser.showOpenDialog(gui);
 assertEquals(0, returnValue);
 ActionEvent actionEvent2 = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
 gui.actionPerformed(actionEvent2);
 assertTrue(gui.listKnown.getText().contains("test.txt"));
 }

 @Test
 void testActionPerformedWithEmptyAuthor() {
 // Testing if the "Import" button works correctly with an empty author
 ActionEvent actionEvent = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
 gui.currentAuthor.setText("");
 gui.actionPerformed(actionEvent);
 JFileChooser fileChooser = gui.chooseFile;
 fileChooser.setSelectedFile(new File("test.txt"));
 int returnValue = fileChooser.showOpenDialog(gui);
 assertEquals(0, returnValue);
 ActionEvent actionEvent2 = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
 gui.actionPerformed(actionEvent2);
 assertTrue(gui.listUnknown.getText().contains("test.txt"));
 }

 @Test
 void testActionPerformedWithNonEmptyAuthor() {
 // Testing if the "Import" button works correctly with a non-empty author
 gui.currentAuthor.setText("testAuthor");
 ActionEvent actionEvent = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
 gui.actionPerformed(actionEvent);
 JFileChooser fileChooser = gui.chooseFile;
 fileChooser.setSelectedFile(new File("test.txt"));
 int returnValue = fileChooser.showOpenDialog(gui);
 assertEquals(0, returnValue);
 ActionEvent actionEvent2 = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
 gui.actionPerformed(actionEvent2);
 assertTrue(gui.listKnown.getText().contains("testAuthor - test.txt"));
 }

 @Test
 void testCanonicizeButton() {
 // Testing if the "Canonicize" button works correctly
 ActionEvent actionEvent = new ActionEvent(gui.buttonCanonicize, ActionEvent.ACTION_PERFORMED, "Canonicize");
 gui.actionPerformed(actionEvent);
 assertTrue(gui.progressCanonicize.isIndeterminate());
 }

 @Test
 void testCreateEventSetButton() {
 // Testing if the "Create Event Set" button works correctly
 ActionEvent actionEvent = new ActionEvent(gui.buttonEventSet, ActionEvent.ACTION_PERFORMED, "CreateEventSet");
 gui.actionPerformed(actionEvent);
 assertTrue(gui.progressEventSet.isIndeterminate());
 }

 @Test
 void testAnalyzeButton() {
 // Testing if the "Analyze" button works correctly
 ActionEvent actionEvent = new ActionEvent(gui.buttonAnalyze, ActionEvent.ACTION_PERFORMED, "Analyze");
 gui.actionPerformed(actionEvent);
 assertTrue(gui.progressAnalysis.isIndeterminate());
 }
}