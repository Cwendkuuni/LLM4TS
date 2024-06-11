import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("option1").create();
        assertNotNull(option);
        assertEquals("option1", option.getLongOpt());
    }
}