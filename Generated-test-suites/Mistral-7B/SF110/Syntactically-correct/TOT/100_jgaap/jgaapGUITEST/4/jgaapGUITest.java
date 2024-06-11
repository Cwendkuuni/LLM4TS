import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import jgaap.*;
import jgaap.gui.guiDriver;
import jgaap.gui.jgaapGUI;

public class jgaapGUITest {

 private jgaapGUI testGUI;
 private Frame frame;

 @Before
 public void setUp() throws HeadlessException {
 testGUI = new jgaapGUI();
 frame = new JFrame();
 frame.getContentPane().add(testGUI);
 frame.pack();
 frame.setVisible(true);
 }

 @Test
 public void testInitComponents() {
 // Test if components are initialized correctly
 final JTextField currentAuthor = testGUI.currentAuthor;
 final JFileChooser chooseFile = testGUI.chooseFile;
 final JButton buttonEvSet = testGUI.buttonEvSet;
 final JPanel paneEvProgress = testGUI.paneEvProgress;
 final JLabel labelevSet = testGUI.labelevSet;
 final JProgressBar progressEventSet = testGUI.progressEventSet;
 final JTextArea listResults = testGUI.listResults;

 // Test JTextField
 assertEquals(20, currentAuthor.getRows());
 assertEquals(30, currentAuthor.getColumns());
 assertNull(currentAuthor.getText());

 // Test JFileChooser
 assertEquals(JFileChooser.OPEN_DIALOG, chooseFile.getDialogType());
 assertTrue(chooseFile.isFileSelectionEnabled());
 assertNull(chooseFile.getFileFilter());

 // Test JButton
 assertEquals("Create Event Set", buttonEvSet.getText());
 buttonEvSet.doClick(); // Trigger actionPerformed()

 // Test JPanel
 assertEquals(BorderLayout.PAGE_END, testGUI.getLayout());

 // Test JProgressBar
 assertEquals(0, progressEventSet.getValue());
 assertEquals(100, progressEventSet.getMaximum());

 // Test JTextArea
 assertEquals(20, listResults.getRows());
 assertEquals(30, listResults.getColumns());
 assertNull(listResults.getText());
 }

 @Test
 public void testActionPerformed_LoadDemo() {
 // Test if loadDemo() method is called correctly
 testGUI.menuFileDemo.doClick();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertEquals("Need to Demo", testGUI.listResults.getText());
 }
 });
 }

 @Test
 public void testActionPerformed_ImportDocument() {
 // Test if importDocument() method is called correctly
 testGUI.chooseFile.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File file) {
 return file.isDirectory() || file.getName().endsWith(".txt");
 }

 @Override
 public String getDescription() {
 return "Text files only";
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.buttonLocal.doClick();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 chooseFile.showOpenDialog(frame);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 File file = chooseFile.getSelectedFile();
 if (file != null) {
 testGUI.currentAuthor.setText("Test Author");
 testGUI.driver.addDocument(file.getAbsolutePath(), testGUI.currentAuthor.getText());
 testGUI.listKnown.append("Test Author - " + file.getName() + "\n");
 }
 }
 });
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertEquals("Test Author - " + testGUI.chooseFile.getSelectedFile().getName(), testGUI.listKnown.getText());
 }
 });
 }

 @Test
 public void testActionPerformed_Canonicize() {
 // Test if canonicize() method is called correctly
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.buttonCanonicize.doClick();
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 JDialog progressDialog = testGUI.progressDialog;
 assertEquals("Document Canonicization", progressDialog.getTitle());
 assertTrue(progressDialog.isVisible());
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.progressCanonicize.setValue(50);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.progressCanonicize.setValue(100);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertTrue(testGUI.progressCanonicize.isVisible());
 }
 });
 }

 @Test
 public void testActionPerformed_CreateEventSet() {
 // Test if createEventSet() method is called correctly
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.buttonEventSet.doClick();
 testGUI.esButtonGroup.clearSelection();
 testGUI.eventifiers[0].doClick();
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 JDialog progressDialog = testGUI.progressDialog;
 assertEquals("Creating event set...", progressDialog.getTitle());
 assertTrue(progressDialog.isVisible());
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.progressAnalysis.setValue(50);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.progressAnalysis.setValue(100);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertTrue(testGUI.progressAnalysis.isVisible());
 }
 });
 }

 @Test
 public void testActionPerformed_Analyze() {
 // Test if analyze() method is called correctly
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.buttonAnalyze.doClick();
 testGUI.radioCrossEnt.doClick();
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 JDialog progressDialog = testGUI.progressDialog;
 assertEquals("Analyzing...", progressDialog.getTitle());
 assertTrue(progressDialog.isVisible());
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.progressAnalysis.setValue(50);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 testGUI.progressAnalysis.setValue(100);
 }
 });
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertNotNull(testGUI.listResults.getText());
 }
 });
 }

 @Test
 public void testActionPerformed_Exit() {
 // Test if exit() method is called correctly
 testGUI.menuFileExit.doClick();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertEquals(-1, System.exit(0));
 }
 });
 }
}