import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    private jgaapGUI jgaapGUI;

    @Test
    public void testInitComponents() {
        jgaapGUI = new jgaapGUI();
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
        assertNotNull(jgaapGUI.panelEventSet);
        assertNotNull(jgaapGUI.panelEOpts);
        assertNotNull(jgaapGUI.panelEvProgress);
        assertNotNull(jgaapGUI.buttonEventSet);
        assertNotNull(jgaapGUI.labelevSet);
        assertNotNull(jgaapGUI.progressEventSet);
    }

    @Test
    public void testActionPerformedWithFileImport() {
        jgaapGUI = new jgaapGUI();
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(jgaapGUI.chooseFile).thenReturn(fileChooser);
        when(fileChooser.showOpenDialog(jgaapGUI)).thenReturn(JFileChooser.APPROVE_OPTION);
        String fileName = "testFile.txt";
        when(fileChooser.getSelectedFile()).thenReturn(new File(fileName));
        jgaapGUI.actionPerformed(new ActionEvent("Import", 0, "Import"));
        assertTrue(jgaapGUI.driver.getDocuments().containsKey(fileName));
        assertEquals("", jgaapGUI.currentAuthor.getText());
        assertTrue(jgaapGUI.listKnown.getText().contains(fileName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActionPerformedWithEmptyFileImport() {
        jgaapGUI = new jgaapGUI();
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(jgaapGUI.chooseFile).thenReturn(fileChooser);
        when(fileChooser.showOpenDialog(jgaapGUI)).thenReturn(JFileChooser.APPROVE_OPTION);
        String fileName = "";
        when(fileChooser.getSelectedFile()).thenReturn(new File(fileName));
        jgaapGUI.actionPerformed(new ActionEvent("Import", 0, "Import"));
    }

    @Test
    public void testActionPerformedWithCanonicize() {
        jgaapGUI = new jgaapGUI();
        jgaapGUI.driver.preprocessEngineDriver = mock(guiDriver.preprocessEngineDriver.class);
        jgaapGUI.canonicizers = new JCheckBox[3];
        jgaapGUI.canonicizers[0].setSelected(true);
        jgaapGUI.canonicizers[1].setSelected(true);
        jgaapGUI.canonicizers[2].setSelected(true);
        jgaapGUI.progressCanonicize = mock(JProgressBar.class);
        jgaapGUI.actionPerformed(new ActionEvent("Canonicize", 0, "Canonicize"));
        verify(jgaapGUI.driver.preprocessEngineDriver).setCanonicizers(jgaapGUI.canonicizers);
        verify(jgaapGUI.driver.preprocessEngineDriver).start();
        verify(jgaapGUI.progressCanonicize).setIndeterminate(true);
        verify(jgaapGUI.progressCanonicize).setMaximum(100);
        verify(jgaapGUI.progressCanonicize).setValue(0);
    }

    @Test(expected = IllegalStateException.class)
    public void testActionPerformedWithCanonicizeNoEngine() {
        jgaapGUI = new jgaapGUI();
        jgaapGUI.driver.preprocessEngineDriver = null;
        jgaapGUI.canonicizers = new JCheckBox[3];
        jgaapGUI.canonicizers[0].setSelected(true);
        jgaapGUI.canonicizers[1].setSelected(true);
        jgaapGUI.canonicizers[2].setSelected(true);
        jgaapGUI.progressCanonicize = mock(JProgressBar.class);
        jgaapGUI.actionPerformed(new ActionEvent("Canonicize", 0, "Canonicize"));
    }
}