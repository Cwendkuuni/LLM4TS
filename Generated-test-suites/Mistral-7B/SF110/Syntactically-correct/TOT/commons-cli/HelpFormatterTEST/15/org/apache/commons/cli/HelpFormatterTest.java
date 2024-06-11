import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter.getSyntaxPrefix());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        // Test other default values
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        // Test setters and getters
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
        formatter.setDescPadding(10);
        assertEquals(10, formatter.getDescPadding());
    }
}