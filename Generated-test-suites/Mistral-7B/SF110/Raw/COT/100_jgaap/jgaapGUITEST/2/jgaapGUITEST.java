import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jgaap.*;
import jgaap.gui.guiDriver;
import jgaap.gui.jgaapGUI;

public class jgaapGUITest {

    private jgaapGUI gui;
    private JFrame frame;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gui);
        frame.pack();
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testConstructor() {
        assertNotNull(gui);
        assertNotNull(gui.driver);
    }

    @Test
    public void testInitComponents() {
        // Check if components are initialized
        assertNotNull(gui.currentAuthor);
        assertNotNull(gui.chooseFile);
        assertNotNull(gui.menuBar);
        assertNotNull(gui.menuFile);
        assertNotNull(gui.menuFileExit);
        assertNotNull(gui.menuFileDemo);
        assertNotNull(gui.menuHelp);
        assertNotNull(gui.menuHelpContents);
        assertNotNull(gui.menuHelpAbout);
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
        assertNotNull(gui.paneEvProgress);
        assertNotNull(gui.labelevSet);
        assertNotNull(gui.progressEventSet);
        assertNotNull(gui.panelEventSet);
        assertNotNull(gui.panelEOpts);
        assertNotNull(gui.panelEvProgress);
    }

    @Test
    public void testMenuFileExit() {
        gui.menuFileExit.doClick();
        assertTrue(frame.isVisible() == false);
    }

    @Test
    public void testMenuFileDemo() {
        gui.menuFileDemo.doClick();
        assertEquals(gui.loadDemo(), "Need to Demo");
    }

    @Test
    public void testInitComponents_CheckEnabled() {
        assertTrue(gui.currentAuthor.isEnabled() == false);
        assertTrue(gui.buttonLocal.isEnabled() == true);
        assertTrue(gui.buttonCanonicize.isEnabled() == false);
        assertTrue(gui.buttonEventSet.isEnabled() == false);
        assertTrue(gui.buttonAnalyze.isEnabled() == false);
    }

    @Test(expected = NullPointerException.class)
    public void testFileChooserNullFile() {
        JFileChooser chooser = new JFileChooser(null);
        File file = chooser.getSelectedFile();
    }

    @Test
    public void testFileChooserValidFile() {
        gui.chooseFile.setCurrentDirectory(new File("."));
        gui.chooseFile.showOpenDialog(gui);
        File file = gui.chooseFile.getSelectedFile();
        assertNotNull(file);
    }

    @Test
    public void testActionPerformed_Import() {
        gui.chooseFile.setCurrentDirectory(new File("."));
        final int returnVal = gui.chooseFile.showOpenDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = gui.chooseFile.getSelectedFile();
            assertEquals(gui.driver.getDocumentCount(), 0);
            gui.buttonLocal.doClick();
            gui.currentAuthor.setText("Test Author");
            gui.chooseFile.setSelectedFile(file);
            gui.chooseFile.doClick();
            assertEquals(gui.driver.getDocumentCount(), 1);
            assertEquals(gui.listKnown.getText(), "Test Author - " + file.getName() + "\n");
        }
    }

    @Test
    public void testActionPerformed_Canonicize() {
        gui.buttonCanonicize.doClick();
        assertTrue(gui.progressCanonicize.isIndeterminate());
        // Add assertions for edge cases and exception handling
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        gui.buttonEventSet.doClick();
        assertTrue(gui.progressEventSet.isIndeterminate());
        // Add assertions for edge cases and exception handling
    }

    @Test
    public void testActionPerformed_Analyze() {
        gui.buttonAnalyze.doClick();
        assertTrue(gui.progressAnalysis.isIndeterminate());
        // Add assertions for edge cases and exception handling
    }
}