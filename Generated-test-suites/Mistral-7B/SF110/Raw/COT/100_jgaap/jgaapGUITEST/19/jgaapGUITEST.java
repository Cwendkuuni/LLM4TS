import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

public class jgaapGUITest {

    private jgaapGUI jgaapGUI;

    @Before
    public void setUp() {
        jgaapGUI = new jgaapGUI();
    }

    @Test
    public void testConstructor() {
        assertNotNull(jgaapGUI);
    }

    @Test
    public void testInitComponents() {
        jgaapGUI.initComponents();
        assertNotNull(jgaapGUI.menuBar);
        assertNotNull(jgaapGUI.menuFile);
        assertNotNull(jgaapGUI.menuFileExit);
        assertNotNull(jgaapGUI.menuFileDemo);
        assertNotNull(jgaapGUI.menuHelp);
        assertNotNull(jgaapGUI.menuHelpContents);
        assertNotNull(jgaapGUI.menuHelpAbout);
        assertNotNull(jgaapGUI.paneOpts);
        assertNotNull(jgaapGUI.panelAcquire);
        assertNotNull(jgaapGUI.panelDocs);
        assertNotNull(jgaapGUI.labelKnown);
        assertNotNull(jgaapGUI.scrollPane1);
        assertNotNull(jgaapGUI.listKnown);
        assertNotNull(jgaapGUI.labelUnknown);
        assertNotNull(jgaapGUI.scrollPane2);
        assertNotNull(jgaapGUI.listUnknown);
        assertNotNull(jgaapGUI.panelOpts);
        assertNotNull(jgaapGUI.radioKnown);
        assertNotNull(jgaapGUI.radioUnknown);
        assertNotNull(jgaapGUI.buttonLocal);
        assertNotNull(jgaapGUI.panelCanonicize);
        assertNotNull(jgaapGUI.panelCOpts);
        assertNotNull(jgaapGUI.canonicizers);
        assertNotNull(jgaapGUI.eventifiers);
        assertNotNull(jgaapGUI.esButtonGroup);
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
        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.menuFileExit, 0, "Exit"));
        assertEquals(-1, System.exit(0));
    }

    @Test
    public void testActionPerformed_Import() {
        JFileChooser chooseFile = mock(JFileChooser.class);
        when(chooseFile.showOpenDialog(jgaapGUI)).thenReturn(JFileChooser.APPROVE_OPTION);
        jgaapGUI.chooseFile = chooseFile;
        jgaapGUI.currentAuthor.setText("Author1");
        File file = new File("testFile.txt");
        when(chooseFile.getSelectedFile()).thenReturn(file);

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.buttonLocal, 0, "Import"));
        assertEquals("Author1", jgaapGUI.currentAuthor.getText());
        assertTrue(jgaapGUI.listKnown.getText().contains("Author1 - testFile.txt"));

        jgaapGUI.currentAuthor.setText("");
        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.buttonLocal, 0, "Import"));
        assertTrue(jgaapGUI.listUnknown.getText().contains("testFile.txt"));
    }

    @Test
    public void testActionPerformed_Canonicize() {
        jgaapGUI.canonicizers[0].setSelected(true);
        jgaapGUI.canonicizers[1].setSelected(true);
        jgaapGUI.canonicizers[2].setSelected(true);
        JProgressBar progressCanonicize = mock(JProgressBar.class);
        jgaapGUI.progressCanonicize = progressCanonicize;

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.buttonCanonicize, 0, "Canonicize"));
        verify(progressCanonicize).setProgress(100);
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformed_CreateEventSet_NullEventifiers() {
        JCheckBox[] canonicizers = jgaapGUI.canonicizers;
        JRadioButton[] eventifiers = null;
        jgaapGUI.eventifiers = eventifiers;

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.buttonEventSet, 0, "CreateEventSet"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActionPerformed_CreateEventSet_InvalidEventifier() {
        JCheckBox[] canonicizers = jgaapGUI.canonicizers;
        JRadioButton[] eventifiers = jgaapGUI.eventifiers;
        jgaapGUI.esButtonGroup.add(eventifiers[1]);
        String invalidEventifier = "InvalidEventifier";

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.buttonEventSet, 0, invalidEventifier));
    }

    @Test
    public void testActionPerformed_Analyze() {
        JProgressBar progressAnalysis = mock(JProgressBar.class);
        jgaapGUI.progressAnalysis = progressAnalysis;

        jgaapGUI.actionPerformed(new ActionEvent(jgaapGUI.buttonAnalyze, 0, "Analyze"));
        verify(progressAnalysis).setProgress(100);

        String expectedResults = "Results of analysis: \n1. Statistic 1: Value 1\n2. Statistic 2: Value 2";
        when(progressAnalysis.getModel().getValue()).thenReturn(100);
        assertEquals(expectedResults, jgaapGUI.listResults.getText());
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI.loadDemo();
        assertTrue(jgaapGUI.listResults.getText().contains("Need to Demo"));
    }
}