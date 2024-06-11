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
 JFrame testFrame = new JFrame();
 testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 testFrame.add(gui, BorderLayout.CENTER);
 testFrame.setSize(600, 400);
 testFrame.setVisible(true);
 }

 @After
 public void tearDown() {
 if (gui != null) {
 gui.dispose();
 }
 }

 @Test
 public void testInitComponents() {
 // Test for menu components
 assertNotNull(gui.menuBar);
 assertNotNull(gui.menuFile);
 assertNotNull(gui.menuFileExit);
 assertNotNull(gui.menuFileDemo);
 assertNotNull(gui.menuHelp);
 assertNotNull(gui.menuHelpContents);
 assertNotNull(gui.menuHelpAbout);

 // Test for paneOpts components
 assertNotNull(gui.paneOpts);
 assertNotNull(gui.panelAcquire);
 assertNotNull(gui.panelDocs);
 assertNotNull(gui.labelKnown);
 assertNotNull(gui.scrollPane1);
 assertNotNull(gui.listKnown);
 assertNotNull(gui.labelUnknown);
 assertNotNull(gui.scrollPane2);
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
 assertNotNull(gui.panelEventSet);
 assertNotNull(gui.panelEOpts);
 assertNotNull(gui.panelEvProgress);
 }

 @Test
 public void testActionPerformedKnownAuthor() {
 // Simulate the knownAuthor button click event
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.currentAuthor.dispatchEvent(new ActionEvent(gui.currentAuthor, ActionEvent.ACTION_PERFORMED, "knownauthor"));
 // Check if the currentAuthor field is emptied and enabled
 assertTrue(gui.currentAuthor.getText().isEmpty());
 assertTrue(gui.currentAuthor.isEnabled());
 }

 @Test
 public void testActionPerformedUnknownAuthor() {
 // Simulate the unknownAuthor button click event
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.currentAuthor.dispatchEvent(new ActionEvent(gui.currentAuthor, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
 // Check if the currentAuthor field is emptied and disabled
 assertTrue(gui.currentAuthor.getText().isEmpty());
 assertFalse(gui.currentAuthor.isEnabled());
 }

 @Test
 public void testActionPerformedImport() throws IOException {
 // Simulate the Import button click event
 File demoFile = new File("testFile.txt");
 gui.chooseFile.setSelectedFile(demoFile);
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.buttonLocal.dispatchEvent(new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import"));
 // Check if the file is added to the known list
 assertEquals("testFile.txt", gui.listKnown.getText().split("\n")[gui.listKnown.getText().split("\n").length - 1]);
 }

 @Test
 public void testActionPerformedCanonicize() {
 // Simulate the Canonicize button click event
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.buttonCanonicize.dispatchEvent(new ActionEvent(gui.buttonCanonicize, ActionEvent.ACTION_PERFORMED, "Canonicize"));
 // Check if the progressCanonicize is incremented
 assertTrue(gui.progressCanonicize.getValue() > 0);
 }

 @Test
 public void testActionPerformedCreateEventSet() {
 // Simulate the CreateEventSet button click event
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.buttonEventSet.dispatchEvent(new ActionEvent(gui.buttonEventSet, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
 // Check if the progressEventSet is incremented
 assertTrue(gui.progressEventSet.getValue() > 0);
 }

 @Test
 public void testActionPerformedAnalyze() {
 // Simulate the Analyze button click event
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.buttonAnalyze.dispatchEvent(new ActionEvent(gui.buttonAnalyze, ActionEvent.ACTION_PERFORMED, "Analyze"));
 // Check if the progressAnalysis is incremented and non-zero
 assertTrue(gui.progressAnalysis.getValue() > 0);
 }

 @Test
 public void testActionPerformedDemo() {
 // Simulate the Demo button click event
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, new Point(10, 10));
 gui.menuFileDemo.dispatchEvent(new ActionEvent(gui.menuFileDemo, ActionEvent.ACTION_PERFORMED, "demo"));
 // Check if the loadDemo method is called
 assertTrue(gui.loadDemoCalled);
 }
}