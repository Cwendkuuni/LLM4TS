import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testConstructor() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNotNull(lagoonGUI.getInputPanel());
        assertNotNull(lagoonGUI.getCenterPanel());
        assertNotNull(lagoonGUI.getButtonPanel());
        assertNotNull(lagoonGUI.getLoadButton());
        assertNotNull(lagoonGUI.getSaveButton());
        assertNotNull(lagoonGUI.getBuildButton());
        assertNotNull(lagoonGUI.getForceButton());
        assertNotNull(lagoonGUI.getExitButton());
    }
}