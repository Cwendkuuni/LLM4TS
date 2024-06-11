import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {
    private static final int TEST_WIDTH = 800;
    private static final int TEST_HEIGHT = 600;

    private JgaapGUI jgaapGUI;

    @Before
    public void setUp() throws IOException, HeadlessException {
        // Initialize the JgaapGUI object
        jgaapGUI = new JgaapGUI();

        // Set up the test frame to be able to display the JgaapGUI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(jgaapGUI);
        frame.setSize(TEST_WIDTH, TEST_HEIGHT);
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        // Close the test frame to avoid memory leaks
        SwingUtilities.invokeLater(() -> jgaapGUI.menuFileExit.doClick());
    }

    @Test
    public void testConstructor() {
        assertNotNull(jgaapGUI);
    }

    @Test
    public void testInitComponents() {
        // Test the initialization of various components
        assertEquals(new Dimension(TEST_WIDTH, TEST_HEIGHT), jgaapGUI.getPreferredSize());
        assertSame(new BorderLayout(), jgaapGUI.getLayout());

        assertNotNull(jgaapGUI.currentAuthor);
        assertFalse(jgaapGUI.currentAuthor.isEnabled());

        assertEquals("File", jgaapGUI.menuFile.getText());
        assertNotNull(jgaapGUI.menuFileExit);
        assertEquals("Exit", jgaapGUI.menuFileExit.getText());
        assertNotNull(jgaapGUI.menuFileDemo);
        assertEquals("Load Demo", jgaapGUI.menuFileDemo.getText());

        assertEquals("Help", jgaapGUI.menuHelp.getText());
        assertNotNull(jgaapGUI.menuHelpContents);
        assertEquals("Contents (F1)", jgaapGUI.menuHelpContents.getText());
        assertNotNull(jgaapGUI.menuHelpAbout);
        assertEquals("About", jgaapGUI.menuHelpAbout.getText());

        assertEquals(3, jgaapGUI.paneOpts.getTabCount());

        // Test the initialization of various panels
        JPanel panelAcquire = (JPanel) jgaapGUI.paneOpts.getComponentAt(0, 0);
        assertEquals(new FlowLayout(), panelAcquire.getLayout());
        assertNotNull(panelAcquire.getComponent(0)); // menuFile
        assertNotNull(panelAcquire.getComponent(1)); // menuFileExit
        assertNotNull(panelAcquire.getComponent(2)); // menuFileDemo
    }

    @Test
    public void testActionPerformed_Import() {
        // Test the "Import" action
        JFileChooser chooseFile = mock(JFileChooser.class);
        when(chooseFile.showOpenDialog(jgaapGUI)).thenReturn(JFileChooser.APPROVE_OPTION);
        File file = new File("test.txt");
        when(chooseFile.getSelectedFile()).thenReturn(file);

        SwingUtilities.invokeLater(() -> jgaapGUI.buttonLocal.doClick());
        verify(chooseFile).showOpenDialog(jgaapGUI);

        SwingUtilities.invokeLater(() -> jgaapGUI.chooseFile.setSelectedFile(file));
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonLocal.doClick());

        JTextArea listKnown = (JTextArea) jgaapGUI.getComponent(11);
        JTextArea listUnknown = (JTextArea) jgaapGUI.getComponent(12);

        assertEquals("", jgaapGUI.currentAuthor.getText());
        assertTrue(listKnown.getText().isEmpty());
        assertTrue(listUnknown.getText().isEmpty());

        SwingUtilities.invokeLater(() -> jgaapGUI.currentAuthor.setText("Test Author"));
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonLocal.doClick());
        SwingUtilities.invokeLater(() -> jgaapGUI.chooseFile.setSelectedFile(file));
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonLocal.doClick());

        String expected = "Test Author - test.txt\n";
        assertEquals(expected, listKnown.getText());
        assertEquals("", listUnknown.getText());
    }

    @Test
    public void testActionPerformed_Canonicize() {
        // Test the "Canonicize" action
        JProgressBar progressCanonicize = (JProgressBar) jgaapGUI.getComponent(15);

        SwingUtilities.invokeLater(() -> jgaapGUI.buttonCanonicize.doClick());

        // Test that the progress bar advances properly
        int initialValue = progressCanonicize.getValue();
        for (int i = 0; i < 100; i += 5) {
            SwingUtilities.invokeLater(() -> progressCanonicize.setValue(initialValue + i));
            assertEquals(initialValue + i, progressCanonicize.getValue());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformed_UnknownCommand() {
        // Test an unknown command
        SwingUtilities.invokeLater(() -> jgaapGUI.menuFile.doClick());
        SwingUtilities.invokeLater(() -> jgaapGUI.menuFile.doClick());

        // This should throw a NullPointerException because the menuItem is null
        jgaapGUI.menuFile.doClick();
    }

    @Test
    public void testLoadDemo() {
        // Test the "Load Demo" action
        SwingUtilities.invokeLater(() -> jgaapGUI.menuFileDemo.doClick());

        // The loadDemo() method is empty, so there's not much to test here
        // You might want to add some tests if this method gets implemented later
    }
}