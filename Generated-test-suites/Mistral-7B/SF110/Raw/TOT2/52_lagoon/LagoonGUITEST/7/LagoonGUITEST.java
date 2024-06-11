import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

    @Test
    public void testConstructor() {
        // Test initializing GUI without arguments
        LagoonGUI appFrame = new LagoonGUI();
        assertNotNull(appFrame);

        // Test initializing GUI with arguments
        String[] args = new String[]{"config.properties"};
        LagoonGUI appFrameWithArgs = new LagoonGUI(args);
        assertNotNull(appFrameWithArgs);
    }

    @Test
    public void testLoadButtonActionListener() {
        // Create a mock GUI instance
        LagoonGUI appFrame = new LagoonGUI();

        // Simulate button click event
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "actionPerformed");
        appFrame.loadButton.addActionListener(e -> appFrame.loadPropertyFile());
        appFrame.loadButton.actionPerformed(actionEvent);

        // Test if loadPropertyFile is called
        verify(appFrame, times(1)).loadPropertyFile();
    }

    // Add more test cases for other methods and edge cases here

    // Test main method
    @Test
    public void testMainMethod() {
        String[] args = new String[]{"config.properties"};
        LagoonGUI.main(args);
        // Add assertions to check if the GUI is created and shown correctly
    }
}