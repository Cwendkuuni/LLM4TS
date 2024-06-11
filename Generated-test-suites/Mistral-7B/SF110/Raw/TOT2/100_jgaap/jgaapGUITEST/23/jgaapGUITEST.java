import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui.menuBar);
        assertNotNull(gui.menuFile);
        // Add more assertions for other components as needed
    }
}