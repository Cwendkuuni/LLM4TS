import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class jgaapGUITest {

private JgaapGUI gui;
private JFrame frame;

@Before
public void setUp() {
gui = new JgaapGUI();
frame = new JFrame("JgaapGUI Test");
frame.setContentPane(gui);
frame.pack();
frame.setVisible(true);
}

@Test
public void testInitComponents() {
assertNotNull(gui.driver);
assertNotNull(gui.currentAuthor);
// check menuBar components
JMenuBar menuBar = gui.menuBar;
assertNotNull(menuBar);
JMenu menuFile = (JMenu) menuBar.getComponent(0);
assertNotNull(menuFile);
JMenuItem menuFileExit = (JMenuItem) menuFile.getComponent(0);
assertNotNull(menuFileExit);
JMenuItem menuFileDemo = (JMenuItem) menuFile.getComponent(1);
assertNotNull(menuFileDemo);
JMenu menuHelp = (JMenu) menuBar.getComponent(1);
assertNotNull(menuHelp);
JMenuItem menuHelpContents = (JMenuItem) menuHelp.getComponent(0);
assertNotNull(menuHelpContents);
JMenuItem menuHelpAbout = (JMenuItem) menuHelp.getComponent(1);
assertNotNull(menuHelpAbout);
// check paneOpts components
JTabbedPane paneOpts = gui.paneOpts;
assertNotNull(paneOpts);
JPanel panelAcquire = (JPanel) paneOpts.getComponentAt(0);
assertNotNull(panelAcquire);
JPanel panelDocs = (JPanel) panelAcquire.getComponent(0);
assertNotNull(panelDocs);
JLabel labelKnown = (JLabel) panelDocs.getComponent(0);
assertNotNull(labelKnown);
JScrollPane scrollPane1 = (JScrollPane) panelDocs.getComponent(1);
assertNotNull(scrollPane1);
JTextArea listKnown = (JTextArea) scrollPane1.getViewportView();
assertNotNull(listKnown);
JLabel labelUnknown = (JLabel) panelDocs.getComponent(2);
assertNotNull(labelUnknown);
JScrollPane scrollPane2 = (JScrollPane) panelDocs.getComponent(3);
assertNotNull(scrollPane2);
JTextArea listUnknown = (JTextArea) scrollPane2.getViewportView();
assertNotNull(listUnknown);
// check panelOpts components
JPanel panelOpts = (JPanel) paneOpts.getComponentAt(1);
JRadioButton radioKnown = (JRadioButton) panelOpts.getComponent(1);
assertNotNull(radioKnown);
JRadioButton radioUnknown = (JRadioButton) panelOpts.getComponent(2);
assertNotNull(radioUnknown);
JButton buttonLocal = (JButton) panelOpts.getComponent(3);
assertNotNull(buttonLocal);
// other components testing...
}

@Test
public void testEventPerformed_Import() {
final ActionEvent importEvent = new ActionEvent(buttonLocal, InputEvent.MOUSE_DOWN_MASK, "Import");
SwingUtilities.invokeLater(new Runnable() {
public void run() {
gui.actionPerformed(importEvent);
}
});
// check if document was added to listKnown or listUnknown based on the author field state
JTextField currentAuthor = gui.currentAuthor;
if (currentAuthor.isEnabled()) {
// test adding known document
currentAuthor.setText("John Doe");
gui.chooseFile.setSelectedFile(new File("testFile.txt"));
SwingUtilities.invokeLater(new Runnable() {
public void run() {
gui.buttonLocal.doClick();
}
});
assertTrue(listKnown.getText().contains("John Doe - testFile.txt"));
} else {
// test adding unknown document
SwingUtilities.invokeLater(new Runnable() {
public void run() {
gui.buttonLocal.doClick();
}
});
assertTrue(listUnknown.getText().contains("testFile.txt"));
}
}

@Test
public void testEventPerformed_Canonicize() {
final ActionEvent canonicizeEvent = new ActionEvent(gui.buttonCanonicize, InputEvent.MOUSE_DOWN_MASK, "Canonicize");
SwingUtilities.invokeLater(new Runnable() {
public void run() {
gui.actionPerformed(canonicizeEvent);
}
});
// check if canonicize was called and progressBar is updated
JProgressBar progressCanonicize = gui.progressCanonicize;
assertTrue(progressCanonicize.isIndeterminate());
}

@Test
public void testEventPerformed_Analyze() {
final ActionEvent analyzeEvent = new ActionEvent(gui.buttonAnalyze, InputEvent.MOUSE_DOWN_MASK, "Analyze");
SwingUtilities.invokeLater(new Runnable() {
public void run() {
gui.actionPerformed(analyzeEvent);
}
});
// check if analyze was called and progressBar is updated
JProgressBar progressAnalysis = gui.progressAnalysis;
assertTrue(progressAnalysis.isIndeterminate());
// check if results are displayed in listResults
JTextArea listResults = gui.listResults;
assertTrue(listResults.getText().contains("Results of the analysis"));
}

@Test
public void testEventPerformed_Exit() {
final ActionEvent exitEvent = new ActionEvent(gui.menuFileExit, InputEvent.MOUSE_DOWN_MASK, "Exit");
SwingUtilities.invokeLater(new Runnable() {
public void run() {
gui.actionPerformed(exitEvent);
}
});
// check if JFrame is closed
assertTrue(frame.isClosed());
}
}