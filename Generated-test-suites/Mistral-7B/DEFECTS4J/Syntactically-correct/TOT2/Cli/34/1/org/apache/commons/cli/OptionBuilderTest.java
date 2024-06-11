import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("newLongopt");
        assertNotNull(optionBuilder.longopt);
        assertEquals("newLongopt", optionBuilder.longopt);
    }
}