import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

    @Test
    public void testCreateOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.create('A').create('a');
        assertEquals("a", option.getLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgs());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
    }
}