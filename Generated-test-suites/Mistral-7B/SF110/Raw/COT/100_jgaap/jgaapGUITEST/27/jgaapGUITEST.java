import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {

    private jgaapGUI jgaapGUI;
    private JFrame frame;

    @Before
    public void setUp() {
        jgaapGUI = new jgaapGUI();
        frame = new JFrame("JGAAP GUI Test");
        frame.setContentPane(jgaapGUI);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Test
    public void testConstructor() {
        assertNotNull(jgaapGUI);
    }

    @Test
    public void testInitComponents() {
        // Check if the components are initialized correctly
        assertEquals(2, jgaapGUI.menuBar.getMenuCount());
        assertEquals(3, jgaapGUI.menuFile.getMenuComponentCount());
        assertEquals(2, jgaapGUI.paneOpts.getTabCount());
        assertNotNull(jgaapGUI.currentAuthor);
        assertNotNull(jgaapGUI.chooseFile);
        assertNotNull(jgaapGUI.menuFileExit);
        assertNotNull(jgaapGUI.menuFileDemo);
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
        assertNotNull(jgaapGUI
            .panelEOpts);
        assertNotNull(jgaapGUI.panelEvProgress);

        // Check if the size and layout are set correctly
        assertEquals(new Dimension(600, 400), jgaapGUI.getPreferredSize());
        assertEquals(new BorderLayout(), jgaapGUI.getLayout());
    }

    @Test
    public void testFileExitAction() {
        // Simulate file exit action
        SwingUtilities.invokeLater(() -> jgaapGUI.menuFileExit.doClick());

        // Check if the system exits correctly
        assertEquals(-1, System.exit(0));
    }

    @Test
    public void testImportAction() {
        // Set up a file chooser with a valid file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        File file = new File("test.txt");

        // Simulate import action with a valid file
        SwingUtilities.invokeLater(() -> {
            jgaapGUI.chooseFile = fileChooser;
            jgaapGUI.buttonLocal.doClick();
            jgaapGUI.chooseFile.setSelectedFile(file);
            jgaapGUI.chooseFile.showOpenDialog(jgaapGUI);
        });

        // Check if the document is added to the list correctly
        SwingUtilities.invokeLater(() -> {
            String expected = "test.txt";
            assertEquals(expected, jgaapGUI.listUnknown.getText().split("\n")[jgaapGUI.listUnknown.getText().split("\n").length - 1]);
        });
    }

    @Test
    public void testCanonicizeAction() {
        // Set up the canonicizers and eventifiers
        JCheckBox[] canonicizers = jgaapGUI.canonicizers;
        JRadioButton[] eventifiers = jgaapGUI.eventifiers;
        ButtonGroup esButtonGroup = jgaapGUI.esButtonGroup;

        // Simulate check all canonicizers and select individual characters eventifier
        SwingUtilities.invokeLater(() -> {
            for (JCheckBox checkBox : canonicizers) {
                checkBox.doClick();
            }
            eventifiers[0].doClick();
        });

        // Simulate canonicize action
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonCanonicize.doClick());

        // Check if the progress bar is updated correctly
        SwingUtilities.invokeLater(() -> {
            JProgressBar progressBar = jgaapGUI.progressCanonicize;
            int progress = progressBar.getValue();
            assertTrue(progress >= 0 && progress < 100);
        });
    }

    @Test(expected = NullPointerException.class)
    public void testNullEventSetterAction() {
        // Simulate event set action with null eventifiers
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonEventSet.doClick());

        // Check if a NullPointerException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyEventSetterAction() {
        // Set up the eventifiers with no selection
        JRadioButton[] eventifiers = jgaapGUI.eventifiers;

        // Simulate event set action with no selection
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonEventSet.doClick());

        // Check if an IllegalArgumentException is thrown
    }

    @Test
    public void testAnalyzeAction() {
        // Set up the eventifiers with a selection
        JRadioButton[] eventifiers = jgaapGUI.eventifiers;
        eventifiers[0].doClick();

        // Simulate analyze action
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonAnalyze.doClick());

        // Check if the progress bar is updated correctly
        SwingUtilities.invokeLater(() -> {
            JProgressBar progressBar = jgaapGUI.progressAnalysis;
            int progress = progressBar.getValue();
            assertTrue(progress >= 0 && progress < 100);
        });

        // Check if the results are displayed correctly
        SwingUtilities.invokeLater(() -> {
            String expected = "Analysis results:\n";
            String actual = jgaapGUI.listResults.getText();
            assertEquals(expected, actual);
        });
    }

    @Test
    public void testLoadDemoAction() {
        // Simulate load demo action
        SwingUtilities.invokeLater(() -> jgaapGUI.menuFileDemo.doClick());

        // Check if the loadDemo method is called correctly
        SwingUtilities.invokeLater(() -> {
            verify(jgaapGUI, times(1)).loadDemo();
        });
    }

    @Test(expected = IllegalStateException.class)
    public void testAnalyzeWithNullEventSetAction() {
        // Simulate analyze action with null event set
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonAnalyze.doClick());

        // Check if an IllegalStateException is thrown
    }

    @Test
    public void testCreateEventSetActionWithMultipleSelections() {
        // Set up the eventifiers with multiple selections
        JRadioButton[] eventifiers = jgaapGUI.eventifiers;
        eventifiers[0].doClick();
        eventifiers[1].doClick();

        // Simulate create event set action
        SwingUtilities.invokeLater(() -> jgaapGUI.buttonEventSet.doClick());

        // Check if an IllegalArgumentException is thrown
        SwingUtilities.invokeLater(() -> {
            verify(jgaapGUI, times(1)).createEventSet(eq(Arrays.asList(eventifiers[0], eventifiers[1])));
        });
    }
}