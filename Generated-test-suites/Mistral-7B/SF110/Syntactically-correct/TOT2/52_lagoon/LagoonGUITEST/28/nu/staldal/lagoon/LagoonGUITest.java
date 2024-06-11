import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {
    @Test
    public void testConstructor() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNotNull(lagoonGUI);
    }

    @Test
    public void testLoadButtonActionPerformed() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "loadProperties");
        lagoonGUI.loadButton.actionPerformed(actionEvent);
        assertTrue(lagoonGUI.loadPropertyFileWasCalled); // Assuming loadPropertyFileWasCalled is a flag set in loadPropertyFile method
    }

    @Test
    public void testSaveButtonActionPerformed() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "saveProperties");
        lagoonGUI.saveButton.actionPerformed(actionEvent);
        assertTrue(lagoonGUI.savePropertyFileWasCalled); // Assuming savePropertyFileWasCalled is a flag set in savePropertyFile method
    }

    @Test
    public void testBuildButtonActionPerformedWithNoForce() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "build");
        lagoonGUI.buildButton.actionPerformed(actionEvent);
        // Add assertions related to build method's functionality and edge cases
    }

    @Test
    public void testBuildButtonActionPerformedWithForce() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "forceBuild");
        lagoonGUI.forceButton.actionPerformed(actionEvent);
        // Add assertions related to build method's functionality and edge cases with force flag
    }

    // Add test cases for other methods and edge cases as needed
}