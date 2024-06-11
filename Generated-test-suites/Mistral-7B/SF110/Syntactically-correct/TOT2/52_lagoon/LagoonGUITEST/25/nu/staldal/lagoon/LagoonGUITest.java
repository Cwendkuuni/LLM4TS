import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LagoonGUITest {
    @Test
    public void testMain() {
        String[] args = new String[]{"config.properties"};
        LagoonGUI.main(args);
        assertEquals("Lagoon", this.frame.getTitle());
    }
}