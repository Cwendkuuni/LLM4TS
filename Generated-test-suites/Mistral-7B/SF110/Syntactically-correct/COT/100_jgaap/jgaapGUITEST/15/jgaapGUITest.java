import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class jgaapGUITest {
    // Create a new instance of jgaapGUI
    private jgaapGUI gui;

    @Before
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
           gui = new jgaapGUI();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(gui);
            frame.pack();
            frame.setVisible(true);
        });
    }

    

    // Test constructor
    @Test
    public void testConstructor() {
        assertNotNull(gui);
    }

    // Test initComponents method
    @Test
    public void testInitComponents() {
        assertNotNull(gui.currentAuthor);
        assertNotNull(gui.chooseFile);
        // Add more assertions for other components
    }

    // Test actionPerformed method with "Exit" command
    @Test
    public void testActionPerformedExit() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Exit");
        gui.actionPerformed(actionEvent);
        assertEquals(-1, System.exit(0));
    }

    // Test actionPerformed method with "knownauthor" command
    @Test
    public void testActionPerformedKnownAuthor() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "knownauthor");
        gui.actionPerformed(actionEvent);
        assertTrue(gui.currentAuthor.isEnabled());
        assertEquals("", gui.currentAuthor.getText());
    }

    // Test actionPerformed method with "unknownauthor" command
    @Test
    public void testActionPerformedUnknownAuthor() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "unknownauthor");
        gui.actionPerformed(actionEvent);
        assertFalse(gui.currentAuthor.isEnabled());
        assertEquals("", gui.currentAuthor.getText());
    }

    // Test actionPerformed method with "Import" command
    @Test
    public void testActionPerformedImport() throws IOException {
        final String filePath = "path/to/your/file.txt";
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Import");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showOpenDialog( eq(gui) )).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(filePath));
        gui.chooseFile = fileChooser;
        gui.actionPerformed(actionEvent);
        assertTrue(gui.listKnown.getText().contains(filePath));
        verify(fileChooser).showOpenDialog(eq(gui));
    }

    // Test actionPerformed method with "Canonicize" command
    @Test
    public void testActionPerformedCanonicize() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Canonicize");
        gui.actionPerformed(actionEvent);
        // Add assertions to check the progress and other components' states
    }

    // Test actionPerformed method with "CreateEventSet" command
    @Test
    public void testActionPerformedCreateEventSet() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "CreateEventSet");
        gui.actionPerformed(actionEvent);
        // Add assertions to check the progress and other components' states
    }

    // Test actionPerformed method with "Analyze" command
    @Test
    public void testActionPerformedAnalyze() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Analyze");
        gui.actionPerformed(actionEvent);
        // Add assertions to check the progress and other components' states
    }

    // Test actionPerformed method with "demo" command
    @Test
    public void testActionPerformedDemo() {
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "demo");
        gui.actionPerformed(actionEvent);
        // Add assertions based on the expected behavior of loadDemo() method
    }

    
}