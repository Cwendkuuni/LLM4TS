import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class jgaapGUITest {

    private JFrame frame;
    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
        frame = new JFrame("JGAAP GUI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.pack();
        frame.setVisible(true);
    }

    @Test
    public void testInitComponents() {
        // Check if components are initialized properly
        assertNotNull(gui.currentAuthor);
        assertNotNull(gui.buttonGroup1);
        assertNotNull(gui.buttonGroup2);
        assertNotNull(gui.chooseFile);
        assertNotNull(gui.buttonEvSet);
        assertNotNull(gui.paneEvProgress);
        assertNotNull(gui.labelKnown);
        assertNotNull(gui.scrollPane1);
        assertNotNull(gui.listKnown);
        assertNotNull(gui.labelUnknown);
        assertNotNull(gui.scrollPane2);
        assertNotNull(gui.listUnknown);
        assertNotNull(gui.panelCanonicize);
        assertNotNull(gui.panelProgress);
        assertNotNull(gui.buttonCanonicize);
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
        assertNotNull(gui.panelEventSet);
        assertNotNull(gui.panelEOpts);
        assertNotNull(gui.panelEvProgress);
        assertNotNull(gui.buttonEventSet);
        assertNotNull(gui.labelevSet);
        assertNotNull(gui.progressEventSet);
    }

    @Test
    public void testFileOpenDialog() {
        // Test file open dialog
        gui.chooseFile.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        gui.chooseFile.showOpenDialog(gui);

        // Check if file is added to the list
        JList list = (JList) gui.scrollPane1.getViewport().getView();
        assertEquals(1, list.getModel().getSize());
    }

    @Test
    public void testButtonEventSet() {
        // Test create event set button
        gui.buttonEventSet.doClick();

        // Check if progress bar is visible
        JProgressBar progressBar = (JProgressBar) gui.panelEvProgress.getComponent(1);
        assertTrue(progressBar.isVisible());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullCurrentAuthor() {
        // Test exception when current author is null
        gui.currentAuthor = null;
        gui.driver.addDocument(new File("test.txt").toString(), null);

        // This should throw an exception
        gui.actionPerformed(new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullDocument() {
        // Test exception when document is null
        gui.driver.addDocument(null, "Author");

        // This should throw a null pointer exception
        gui.actionPerformed(new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import"));
    }

    @Test
    public void testCanonicize() {
        // Test canonicize button
        gui.buttonCanonicize.doClick();

        // Check if progress bar is visible
        JProgressBar progressBar = (JProgressBar) gui.panelProgress.getComponent(1);
        assertTrue(progressBar.isVisible());
    }

    @Test
    public void testAnalyze() {
        // Test analyze button
        gui.buttonAnalyze.doClick();

        // Check if progress bar is visible
        JProgressBar progressBar = (JProgressBar) gui.panelAnalProg.getComponent(1);
        assertTrue(progressBar.isVisible());

        // Check if results are displayed in the text area
        JTextArea results = (JTextArea) gui.scrollPane3.getViewport().getView();
        assertNotNull(results.getText());
    }
}