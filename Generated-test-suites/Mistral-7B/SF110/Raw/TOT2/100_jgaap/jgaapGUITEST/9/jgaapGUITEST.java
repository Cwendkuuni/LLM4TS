import org.junit.Test;
import static org.junit.Assert.*;
import jgaap.*; // Import the entire package

public class jgaapGUITest {

    @Test
    public void testInitComponents() {
        // Test initializing components here
    }

    @Test
    public void testActionPerformed_Exit() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent exitEvent = new ActionEvent(new Object(), 0, "Exit");
        gui.actionPerformed(exitEvent);
        // Assert that System.exit() is called
        // Use a mocking framework like Mockito to test this
    }

    @Test
    public void testActionPerformed_KnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.currentAuthor.setText("TestAuthor");
        gui.buttonLocal.doClick();
        ActionEvent knownEvent = new ActionEvent(new Object(), 0, "knownauthor");
        gui.actionPerformed(knownEvent);
        assertEquals("", gui.currentAuthor.getText());
        assertEquals(false, gui.currentAuthor.isEnabled());
    }

    // ...

    @Test
    public void testActionPerformed_InvalidFileType() {
        jgaapGUI gui = new jgaapGUI();
        final int returnVal = gui.chooseFile.showOpenDialog(gui);
        final File file = gui.chooseFile.getSelectedFile();
        if (returnVal == 0) {
            if (!file.isFile() || !file.getName().toLowerCase().endsWith(".txt")) {
                // Test that an error message is displayed or an exception is thrown
                // Use a mocking framework like PowerMockito to test this
            }
        }
    }

    @Test
    public void testActionPerformed_NullEventSetButton() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent createEventSetEvent = new ActionEvent(new Object(), 0, "CreateEventSet");
        gui.buttonEventSet = null;
        gui.actionPerformed(createEventSetEvent);
        // Test that an error message is displayed or an exception is thrown
        // Use a mocking framework like PowerMockito to test this
    }

    // ...

    

    @Test
    public void testJgaapGUI() {
        jgaapGUI gui = new jgaapGUI();
        // Test the overall functionality of the jgaapGUI class here
        // Use the methods and fields you've tested individually in the previous test cases
    }

    
}