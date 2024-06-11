import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {
    private jgaapGUI jgaapGUI;
    private JFrame frame;

    @Before
    public void setUp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        jgaapGUI = new jgaapGUI();
        frame = new JFrame("jgaapGUI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(jgaapGUI);
        frame.pack();
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = jgaapGUI.chooseFile;
            try {
                fileChooser.setSelectedFile(new File("test.txt"));
            } catch (HeadlessException e) {
                e.printStackTrace();
            }
        });
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            frame.dispose();
        });
    }

    @Test
    public void testConstructor() {
        assertNotNull(jgaapGUI);
    }

    @Test
    public void testInitComponents() {
        assertNotNull(jgaapGUI.currentAuthor);
        assertNotNull(jgaapGUI.buttonGroup1);
        assertNotNull(jgaapGUI.buttonGroup2);
        assertNotNull(jgaapGUI.chooseFile);
        assertNotNull(jgaapGUI.paneOpts);
        assertNotNull(jgaapGUI.panelAcquire);
        assertNotNull(jgaapGUI.labelKnown);
        assertNotNull(jgaapGUI.labelUnknown);
        assertNotNull(jgaapGUI.buttonLocal);
        assertNotNull(jgaapGUI.panelCanonicize);
        assertNotNull(jgaapGUI.panelProgress);
        assertNotNull(jgaapGUI.buttonCanonicize);
        assertNotNull(jgaapGUI.label1);
        assertNotNull(jgaapGUI.progressCanonicize);
        assertNotNull(jgaapGUI.labelCanon);
        assertNotNull(jgaapGUI.panelAnalyze);
        assertNotNull(jgaapGUI.labelStatInf);
        assertNotNull(jgaapGUI.panelMethods);
        assertNotNull(jgaapGUI.radioCrossEnt);
        assertNotNull(jgaapGUI.radioLZW);
        assertNotNull(jgaapGUI.panelAnalProg);
        assertNotNull(jgaapGUI.buttonAnalyze);
        assertNotNull(jgaapGUI.labelAnalProg);
        assertNotNull(jgaapGUI.progressAnalysis);
        assertNotNull(jgaapGUI.scrollPane3);
        assertNotNull(jgaapGUI.listResults);
        assertNotNull(jgaapGUI.buttonEventSet);
        assertNotNull(jgaapGUI.paneEvProgress);
        assertNotNull(jgaapGUI.labelevSet);
        assertNotNull(jgaapGUI.progressEventSet);
        assertNotNull(jgaapGUI.panelEventSet);
        assertNotNull(jgaapGUI.panelEOpts);
        assertNotNull(jgaapGUI.panelEvProgress);
    }

    @Test
    public void testActionPerformed_Exit() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.menuFileExit.doClick();
        });
        assertEquals(0, frame.getSize().getWidth());
        assertEquals(0, frame.getSize().getHeight());
    }

    @Test
    public void testActionPerformed_KnownAuthor() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.radioKnown.doClick();
            jgaapGUI.currentAuthor.setText("TestAuthor");
            jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI, 0, "knownauthor"));
        });
        assertTrue(jgaapGUI.currentAuthor.getText().equals("TestAuthor"));
        assertTrue(jgaapGUI.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_UnknownAuthor() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.radioUnknown.doClick();
            jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI, 0, "unknownauthor"));
        });
        assertNull(jgaapGUI.currentAuthor.getText());
        assertFalse(jgaapGUI.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_Import() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.buttonLocal.doClick();
        });
        assertEquals("test.txt", jgaapGUI.chooseFile.getSelectedFile().getName());
        assertEquals("TestAuthor", jgaapGUI.currentAuthor.getText());
        assertTrue(jgaapGUI.listKnown.getText().contains("TestAuthor - test.txt"));
        assertTrue(jgaapGUI.listUnknown.getText().isEmpty());
    }

    @Test
    public void testActionPerformed_Canonicize() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.buttonCanonicize.doClick();
        });
        assertTrue(jgaapGUI.progressCanonicize.isIndeterminate());
        assertTrue(jgaapGUI.progressCanonicize.isVisible());
        // Add more assertions here based on the expected behavior of the canonicization process
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.esButtonGroup.getSelection().setActionCommand("words");
            jgaapGUI.buttonEventSet.doClick();
        });
        assertTrue(jgaapGUI.progressEventSet.isIndeterminate());
        assertTrue(jgaapGUI.progressEventSet.isVisible());
        // Add more assertions here based on the expected behavior of creating an event set
    }

    @Test
    public void testActionPerformed_Analyze() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.buttonGroup2.getSelection().setActionCommand("crossentropy");
            jgaapGUI.buttonAnalyze.doClick();
        });
        assertTrue(jgaapGUI.progressAnalysis.isIndeterminate());
        assertTrue(jgaapGUI.progressAnalysis.isVisible());
        // Add more assertions here based on the expected behavior of the statistical analysis
    }

    @Test(expected = HeadlessException.class)
    public void testChooseFile_NoFrame() {
        new jgaapGUI().chooseFile.showOpenDialog(null);
    }

    @Test
    public void testLoadDemo() {
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.menuFileDemo.doClick();
        });
        // Add assertions here based on the expected behavior of the loadDemo method
    }
}