import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
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

import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {

    private JgaapGUI jgaapGUI;

    @Before
    public void setup() {
        jgaapGUI = new JgaapGUI();
    }

    @Test
    public void testConstructor() {
        assertNotNull(jgaapGUI);
    }

    @Test
    public void testInitComponents() {
        // Check the number of components added to the JPanel
        assertEquals(52, jgaapGUI.getComponentCount());

        // Check the type of each component
        List<Class<?>> componentTypes = Arrays.asList(JTextField.class, JFileChooser.class, JMenuBar.class, JMenu.class, JMenuItem.class,
                JTabbedPane.class, JPanel.class, JLabel.class, JScrollPane.class, JTextArea.class, JRadioButton.class, JButton.class,
                ButtonGroup.class, JProgressBar.class, JPanel.class, JPanel.class, JCheckBox.class, JCheckBox.class, JCheckBox.class,
                ButtonGroup.class, JPanel.class, JPanel.class, JPanel.class, JPanel.class, JPanel.class, JPanel.class, JPanel.class,
                JPanel.class, JPanel.class, JPanel.class, JPanel.class, JRadioButton.class, JRadioButton.class, JButton.class,
                JLabel.class, JProgressBar.class, JLabel.class, JScrollPane.class, JTextArea.class, JPanel.class, JButton.class);

        for (int i = 0; i < jgaapGUI.getComponentCount(); i++) {
            assertEquals(componentTypes.get(i), jgaapGUI.getComponent(i).getClass());
        }
    }

    @Test
    public void testActionPerformed_Exit() {
        ActionEvent exitEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Exit");
        jgaapGUI.actionPerformed(exitEvent);
        assertEquals(0, JOptionPane.showConfirmDialog(jgaapGUI, "Do you want to save your work before exiting?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE));
        assertEquals(-1, System.exit(0));
    }

    @Test
    public void testActionPerformed_UnknownAuthor() {
        JTextField currentAuthor = jgaapGUI.getCurrentAuthor();
        jgaapGUI.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        assertFalse(currentAuthor.isEnabled());
        currentAuthor.setText("Test Author");
        jgaapGUI.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "knownauthor"));
        assertTrue(currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_Import() {
        JFileChooser chooseFile = jgaapGUI.getChooseFile();
        chooseFile.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        chooseFile.setCurrentDirectory(new File("."));
        File testFile = new File("test.txt");
        chooseFile.setSelectedFile(testFile);

        JTextField currentAuthor = jgaapGUI.getCurrentAuthor();
        JTextArea listKnown = jgaapGUI.getListKnown();

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonLocal(), ActionEvent.ACTION_PERFORMED, "Import"));
        assertEquals(0, chooseFile.showSaveDialog(jgaapGUI));
        assertNull(currentAuthor.getText());
        assertEquals("", listKnown.getText());

        currentAuthor.setText("Test Author");
        jgaapGUI.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Import"));
        assertEquals(1, chooseFile.getSelectedFiles().length);
        assertEquals("test.txt", chooseFile.getSelectedFiles()[0].getName());
        assertEquals("Test Author - test.txt", listKnown.getText());
    }

    @Test
    public void testActionPerformed_Canonicize() {
        JProgressBar progressCanonicize = jgaapGUI.getProgressCanonicize();
        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonCanonicize(), ActionEvent.ACTION_PERFORMED, "Canonicize"));
        assertTrue(progressCanonicize.isIndeterminate());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonCanonicize(), ActionEvent.ACTION_PERFORMED, "Canonicize"));
        assertFalse(progressCanonicize.isIndeterminate());
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        JProgressBar progressEventSet = jgaapGUI.getProgressEventSet();
        ButtonGroup esButtonGroup = jgaapGUI.getEsButtonGroup();

        JRadioButton wordsButton = new JRadioButton("Entire Words");
        esButtonGroup.add(wordsButton);
        jgaapGUI.getPanelEOpts().add(wordsButton);
        wordsButton.setSelected(true);

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonEventSet(), ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
        assertFalse(progressEventSet.isIndeterminate());

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonEventSet(), ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
        assertTrue(progressEventSet.isIndeterminate());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonEventSet(), ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
        assertFalse(progressEventSet.isIndeterminate());
    }

    @Test
    public void testActionPerformed_Analyze() {
        JProgressBar progressAnalysis = jgaapGUI.getProgressAnalysis();
        JRadioButton crossEntropyButton = jgaapGUI.getRadioCrossEnt();
        JTextArea listResults = jgaapGUI.getListResults();

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonAnalyze(), ActionEvent.ACTION_PERFORMED, "Analyze"));
        assertTrue(progressAnalysis.isIndeterminate());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonAnalyze(), ActionEvent.ACTION_PERFORMED, "Analyze"));
        assertFalse(progressAnalysis.isIndeterminate());
        assertEquals("Results:\n", listResults.getText());

        crossEntropyButton.setSelected(true);
        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonAnalyze(), ActionEvent.ACTION_PERFORMED, "Analyze"));
        assertFalse(progressAnalysis.isIndeterminate());
        String expectedResults = "Cross Entropy Results:\n...";
        assertTrue(listResults.getText().startsWith(expectedResults));
    }

    @Test(expected = HeadlessException.class)
    public void testActionPerformed_LoadDemo_Headless() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        JFrame frame = new JFrame();
        frame.setVisible(true);
        jgaapGUI.setGraphicsConfiguration(frame.getGraphicsConfiguration());
        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.getButtonEventSet(), ActionEvent.ACTION_PERFORMED, "demo"));
    }

    @Test
    public void testActionPerformed_LoadDemo() {
        try {
            jgaapGUI.loadDemo();
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetFont() {
        Font expectedFont = new Font("Garamond", 0, 14);
        assertEquals(expectedFont, jgaapGUI.getFont());
    }
}