import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class jgaapGUITest {
    // Test instance of jgaapGUI
    private jgaapGUI jgaapGUI;

    @Before
    public void setUp() {
        jgaapGUI = new jgaapGUI();
    }

    // Test initial size of the JFrame
    @Test
    public void testInitialSize() {
        assertTrue(jgaapGUI.getPreferredSize().height > 0 && jgaapGUI.getPreferredSize().width > 0);
    }

    // Test creation of JMenuBar, JMenu and JMenuItem
    @Test
    public void testCreateMenuComponents() {
        assertNotNull(jgaapGUI.menuBar);
        assertNotNull(jgaapGUI.menuFile);
        assertNotNull(jgaapGUI.menuFileExit);
        assertNotNull(jgaapGUI.menuFileDemo);
        assertNotNull(jgaapGUI.menuHelp);
        assertNotNull(jgaapGUI.menuHelpContents);
        assertNotNull(jgaapGUI.menuHelpAbout);
    }

    // Test JTextFields initialization
    @Test
    public void testInitJTextField() {
        assertNotNull(jgaapGUI.currentAuthor);
        assertTrue(jgaapGUI.currentAuthor.isEditable());
    }

    // Test JFileChooser initialization
    @Test
    public void testInitJFileChooser() {
        assertNotNull(jgaapGUI.chooseFile);
    }

    // Test JButton initialization
    @Test
    public void testInitJButton() {
        assertNotNull(jgaapGUI.buttonEvSet);
        assertNotNull(jgaapGUI.buttonCanonicize);
        assertNotNull(jgaapGUI.buttonAnalyze);
    }

    // Test JPanel initialization
    @Test
    public void testInitJPanel() {
        assertNotNull(jgaapGUI.paneEvProgress);
        assertNotNull(jgaapGUI.panelCanonicize);
        assertNotNull(jgaapGUI.panelAnalyze);
    }

    // Test JProgressBar initialization
    @Test
    public void testInitJProgressBar() {
        assertNotNull(jgaapGUI.progressEventSet);
        assertNotNull(jgaapGUI.progressCanonicize);
        assertNotNull(jgaapGUI.progressAnalysis);
    }

    // Test JLabel initialization
    @Test
    public void testInitJLabel() {
        assertNotNull(jgaapGUI.labelevSet);
        assertNotNull(jgaapGUI.labelCanon);
        assertNotNull(jgaapGUI.labelStatInf);
        assertNotNull(jgaapGUI.label1);
        assertNotNull(jgaapGUI.labelAnalProg);
    }

    // Test JTextArea initialization
    @Test
    public void testInitJTextArea() {
        assertNotNull(jgaapGUI.listKnown);
        assertNotNull(jgaapGUI.listUnknown);
        assertNotNull(jgaapGUI.listResults);
    }

    // Test JTabbedPane initialization
    @Test
    public void testInitJTabbedPane() {
        assertNotNull(jgaapGUI.paneOpts);
    }

    // Test JRadioButton initialization
    @Test
    public void testInitJRadioButton() {
        assertNotNull(jgaapGUI.radioKnown);
        assertNotNull(jgaapGUI.radioUnknown);
        assertNotNull(jgaapGUI.radioCrossEnt);
        assertNotNull(jgaapGUI.radioLZW);
    }

    // Test JCheckBox initialization
    @Test
    public void testInitJCheckBox() {
        assertNotNull(jgaapGUI.canonicizers);
        assertNotNull(jgaapGUI.eventifiers);
    }

    // Test ActionListener registration for buttons
    @Test
    public void testRegisterActionListener() {
        assertTrue(jgaapGUI.buttonEvSet.hasFocusableComponent(0));
        assertTrue(jgaapGUI.buttonCanonicize.hasFocusableComponent(0));
        assertTrue(jgaapGUI.buttonAnalyze.hasFocusableComponent(0));
        assertTrue(jgaapGUI.menuFileExit.getActionCommands().length > 0);
        assertTrue(jgaapGUI.menuFileDemo.getActionCommands().length > 0);
        assertTrue(jgaapGUI.menuHelpContents.getActionCommands().length > 0);
        assertTrue(jgaapGUI.menuHelpAbout.getActionCommands().length > 0);
    }

    // Test JFileChooser showOpenDialog method
    @Test
    public void testJFileChooserShowOpenDialog() {
        JFrame frame = new JFrame();
        JFileChooser chooser = new JFileChooser();
        chooser.setApproveButtonText("Open");
        int returnVal = chooser.showOpenDialog(frame);
        assertFalse(returnVal == JFileChooser.APPROVE_OPTION);
    }

    // Test adding a document using JFileChooser
    @Test
    public void testAddDocumentUsingJFileChooser() {
        final String filePath = "test.txt";
        JFrame frame = new JFrame();
        final String author = "Test Author";
        jgaapGUI.currentAuthor.setText(author);
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = jgaapGUI.chooseFile;
            chooser.setApproveButtonText("Import");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == 0) {
                File file = chooser.getSelectedFile();
                jgaapGUI.driver.addDocument(file.getAbsolutePath(), author);
                String expected = author + " - " + file.getName();
                if (!author.isEmpty()) {
                    assertEquals(expected, jgaapGUI.listKnown.getText().split("\n")[jgaapGUI.listKnown.getText().split("\n").length - 1]);
                } else {
                    assertEquals(file.getName(), jgaapGUI.listUnknown.getText().split("\n")[jgaapGUI.listUnknown.getText().split("\n").length - 1]);
                }
            }
        });
    }

    // Test exception handling in actionPerformed when selecting unknown author
    @Test(expected = NullPointerException.class)
    public void testExceptionHandlingUnknownAuthor() {
        JFrame frame = new JFrame();
        jgaapGUI.currentAuthor.setText("");
        SwingUtilities.invokeLater(() -> jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI, 0, "Import")));
    }

    // Test exception handling in actionPerformed when no document is selected
    @Test(expected = NullPointerException.class)
    public void testExceptionHandlingNoDocumentSelected() {
        JFrame frame = new JFrame();
        String author = "Test Author";
        jgaapGUI.currentAuthor.setText(author);
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = jgaapGUI.chooseFile;
            chooser.setApproveButtonText("Import");
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal != 0) {
                jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI, 0, "Import"));
            }
        });
    }

    // Test exception handling in actionPerformed when document cannot be added
    @Test(expected = RuntimeException.class)
    public void testExceptionHandlingCannotAddDocument() {
        JFrame frame = new JFrame();
        String author = "Test Author";
        jgaapGUI.currentAuthor.setText(author);
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = jgaapGUI.chooseFile;
            chooser.setApproveButtonText("Import");
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == 0) {
                File file = chooser.getSelectedFile();
                jgaapGUI.driver.addDocument(file.getAbsolutePath(), author);
                jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI, 0, "Import"));
            }
        });
    }

    // Test createEventSet method with individual character eventifier
    @Test
    public void testCreateEventSetIndividualCharacter() {
        SwingUtilities.invokeLater(() -> jgaapGUI.eventifiers[0].doClick());
        jgaapGUI.buttonEventSet.doClick();
        assertTrue(jgaapGUI.progressEventSet.isIndeterminate());
        jgaapGUI.buttonEventSet.doClick();
        assertTrue(jgaapGUI.progressEventSet.getValue() >= 0 && jgaapGUI.progressEventSet.getValue() <= 100);
    }

    // Test createEventSet method with entire word eventifier
    @Test
    public void testCreateEventSetEntireWord() {
        SwingUtilities.invokeLater(() -> jgaapGUI.eventifiers[1].doClick());
        jgaapGUI.buttonEventSet.doClick();
        assertTrue(jgaapGUI.progressEventSet.isIndeterminate());
        jgaapGUI.buttonEventSet.doClick();
        assertTrue(jgaapGUI.progressEventSet.getValue() >= 0 && jgaapGUI.progressEventSet.getValue() <= 100);
    }

    // Test runStatisticalAnalysis method with cross entropy analyzer
    @Test
    public void testRunStatisticalAnalysisCrossEntropy() {
        SwingUtilities.invokeLater(() -> jgaapGUI.radioCrossEnt.doClick());
        jgaapGUI.buttonCanonicize.doClick();
        jgaapGUI.buttonAnalyze.doClick();
        assertTrue(jgaapGUI.progressAnalysis.isIndeterminate());
        String results = jgaapGUI.listResults.getText();
        int start = results.indexOf("Cross Entropy Results:");
        int end = results.indexOf("---------------------");
        String crossEntropyResults = results.substring(start, end);
        assertTrue(crossEntropyResults.length() > 0);
    }

    // Test runStatisticalAnalysis method with LZW analyzer
    @Test
    public void testRunStatisticalAnalysisLZW() {
        SwingUtilities.invokeLater(() -> jgaapGUI.radioLZW.doClick());
        jgaapGUI.buttonCanonicize.doClick();
        jgaapGUI.buttonAnalyze.doClick();
        assertTrue(jgaapGUI.progressAnalysis.isIndeterminate());
        String results = jgaapGUI.listResults.getText();
        int start = results.indexOf("LZW Results:");
        int end = results.indexOf("---------------------");
        String lzwResults = results.substring(start, end);
        assertTrue(lzwResults.length() > 0);
    }
}