import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
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
    public void testButtonFileExitActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Exit");
        gui.actionPerformed(actionEvent);
        assertTrue(System.exit(-1) == 0);
    }

    @Test
    public void testRadioButtonKnownAuthorActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "knownauthor");
        gui.actionPerformed(actionEvent);
        assertTrue(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testRadioButtonUnknownAuthorActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "unknownauthor");
        gui.actionPerformed(actionEvent);
        assertFalse(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testButtonImportActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Import");
        File file = new File("testFile.txt");
        gui.chooseFile.setSelectedFile(file);
        gui.currentAuthor.setText("Test Author");
        gui.actionPerformed(actionEvent);
        assertTrue(gui.listKnown.getText().contains("Test Author - testFile.txt"));
    }

    @Test(expected = NullPointerException.class)
    public void testButtonCanonicizeActionPerformedNullFile() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Canonicize");
        gui.driver = null;
        gui.actionPerformed(actionEvent);
    }

    @Test
    public void testButtonEventSetActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "CreateEventSet");
        gui.actionPerformed(actionEvent);
    }

    @Test
    public void testButtonAnalyzeActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Analyze");
        gui.actionPerformed(actionEvent);
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        fail("loadDemo method should be implemented");
    }
}