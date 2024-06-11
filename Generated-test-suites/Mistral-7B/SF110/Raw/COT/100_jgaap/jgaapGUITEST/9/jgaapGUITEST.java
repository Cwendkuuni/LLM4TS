import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    

    @Test
    public void testJgaapGUIConstruction() {
        assertNotNull(gui);
    }

    @Test
    public void testInitComponents() {
        gui.initComponents();
        assertNotNull(gui.currentAuthor);
        assertNotNull(gui.buttonEvSet);
        assertNotNull(gui.menuFileExit);
        // Add more assertions for other components as needed
    }

    @Test
    public void testActionPerformedWithExitCommand() {
        ActionEvent event = new ActionEvent(new Object(), 0, "Exit");
        gui.actionPerformed(event);
        assertEquals(0, JOptionPane.showConfirmDialog(gui, "Are you sure you want to exit?"));
        assertTrue(gui.isVisible()); // Test if JFrame is closed after the exit command
    }

    @Test
    public void testImportDocumentWithEmptyTextFieldAndFileChooser() {
        gui.chooseFile.showOpenDialog(gui);
        JButton button = new JButton("Import");
        ActionEvent event = new ActionEvent(button, 0, "Import");
        gui.actionPerformed(event);
        assertFalse(gui.listKnown.getText().contains(""));
        assertFalse(gui.listUnknown.getText().contains(""));
    }

    @Test(expected = NullPointerException.class)
    public void testCanonicizeWithNullCanonicizersArray() {
        JButton button = new JButton("Canonicize");
        ActionEvent event = new ActionEvent(button, 0, "Canonicize");
        gui.buttonGroup1.clearSelection();
        gui.buttonGroup2.clearSelection();
        gui.canonicizers = null;
        gui.actionPerformed(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventSetWithNullEventifiersArray() {
        JButton button = new JButton("CreateEventSet");
        ActionEvent event = new ActionEvent(button, 0, "CreateEventSet");
        gui.esButtonGroup.clearSelection();
        gui.eventifiers = null;
        gui.actionPerformed(event);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnalyzeWithNullResultsTextArea() {
        JButton button = new JButton("Analyze");
        ActionEvent event = new ActionEvent(button, 0, "Analyze");
        gui.scrollPane3.getViewport().removeAll();
        gui.actionPerformed(event);
    }

    @Test
    public void testLoadDemo() {
        JButton button = new JButton("Load Demo");
        ActionEvent event = new ActionEvent(button, 0, "Load Demo");
        gui.actionPerformed(event);
        // Add assertions to verify the demo data is loaded correctly
    }

    

}