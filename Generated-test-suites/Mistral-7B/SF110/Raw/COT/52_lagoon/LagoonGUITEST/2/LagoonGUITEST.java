import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class LagoonGUITest {
    @Test
    public void testConstructor() {
        new LagoonGUI(); // Call the constructor
        assertNotNull(new LagoonGUI().buttonPanel); // Verify the buttonPanel exists
    }
}