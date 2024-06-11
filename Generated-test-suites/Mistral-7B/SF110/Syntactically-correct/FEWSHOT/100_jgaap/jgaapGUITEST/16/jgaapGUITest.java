package org.apache.commons.cli.jgaapGUI; // makes sure the test class is in the correct package

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.junit.Test;

public class jgaapGUITest {

    private static final String TEST_FILE = "test.txt";
    private jgaapGUI gui;

    @Test
    public void testInitComponents() {
        gui = new jgaapGUI();

        // Test JMenuBar
        assertEquals("File", gui.menuFile.getText());
        assertEquals("Help", gui.menuHelp.getText());

        // Test JFileChooser
        assertNotNull(gui.chooseFile);

        // Test JButton
        assertEquals("Canonicize", gui.buttonCanonicize.getText());

        // Test JLabel
        assertEquals("Document Canonicization", gui.labelCanon.getText());

        // Test JTextArea
        assertTrue(gui.listResults.isEditable());

        // Test JTabbedPane
        assertEquals(3, gui.paneOpts.getTabCount());
    }

    @Test
    public void testActionPerformed_Import() {
        gui = new jgaapGUI();
        gui.chooseFile.setSelectedFile(new File(TEST_FILE));
        gui.actionPerformed(new ActionEvent("Import", ActionEvent.ACTION_PERFORMED, TEST_FILE));

        assertEquals("test.txt", gui.driver.getDocumentFileName());
        assertTrue(gui.listKnown.getText().contains("test.txt"));
    }

    @Test
    public void testActionPerformed_Canonicize() {
        gui = new jgaapGUI();
        gui.buttonCanonicize.doClick();

        assertTrue(gui.progressCanonicize.isIndeterminate());
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        gui = new jgaapGUI();
        gui.esButtonGroup.setSelected(gui.eventifiers[1], true);
        gui.actionPerformed(new ActionEvent("CreateEventSet", ActionEvent.ACTION_PERFORMED, "words"));

        assertTrue(gui.progressEventSet.isIndeterminate());
    }

    @Test
    public void testActionPerformed_Analyze() {
        gui = new jgaapGUI();
        gui.buttonAnalyze.doClick();

        assertTrue(gui.progressAnalysis.isIndeterminate());
    }

    @Test
    public void testActionPerformed_Exit() {
        gui = new jgaapGUI();
        gui.menuFileExit.doClick();

        assertEquals(0, jgaapGUI.instanceCount);
    }
}