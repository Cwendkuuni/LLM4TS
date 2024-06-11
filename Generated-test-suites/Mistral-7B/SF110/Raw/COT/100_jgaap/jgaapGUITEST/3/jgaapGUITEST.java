import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @Test
    public void testInitComponents() {
        // Check if components have been initialized
        assertNotNull(gui.menuBar);
        assertNotNull(gui.menuFile);
        assertNotNull(gui.menuFileExit);
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
        // Check if System.exit() has been called
        verify(gui, times(1)).exit();
    }

    @Test
    public void testButtonLocalImportFile() {
        // Given
        final File testFile = new File("test.txt");

        // When
        gui.buttonLocal.doClick();
        JFileChooser fileChooser = spy(gui.chooseFile);
        doReturn(testFile).when(fileChooser).getSelectedFile();

        // Then
        gui.actionPerformed(new ActionEvent("Import"));

        // Check if document has been added
        assertTrue(gui.driver.getDocuments().contains(testFile.getAbsolutePath()));

        // Check if list has been updated
        assertEquals("test.txt\n", gui.listKnown.getText());
    }

    @Test
    public void testButtonCanonicize() {
        // Given
        gui.canonicizers[0].setSelected(true);
        gui.canonicizers[1].setSelected(true);
        gui.canonicizers[2].setSelected(true);

        // When
        gui.buttonCanonicize.doClick();

        // Then
        // Check if preprocessEngineDriver has been started
        verify(gui.driver, times(1)).preprocessEngineDriver(gui.canonicizers, gui.paneOpts, gui.progressCanonicize);
    }

    @Test
    public void testButtonAnalyze() {
        // Given
        gui.radioCrossEnt.setSelected(true);

        // When
        gui.buttonAnalyze.doClick();

        // Then
        // Check if runStatisticalAnalysisDriver has been started
        verify(gui.driver, times(1)).runStatisticalAnalysisDriver("crossentropy", gui.paneOpts, gui.progressAnalysis);
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformedWithNullEvent() {
        // When
        gui.actionPerformed(null);

        // Then
        fail("ActionPerformed should throw NullPointerException with null event argument");
    }

    @Test
    public void testLoadDemo() {
        // Given

        // When
        gui.loadDemo();

        // Then
        // Add assertions for expected actions during demo loading
    }
}