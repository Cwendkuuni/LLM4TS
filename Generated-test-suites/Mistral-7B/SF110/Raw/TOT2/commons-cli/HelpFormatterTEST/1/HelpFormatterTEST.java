import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterConstructors() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
    }
}