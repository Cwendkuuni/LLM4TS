import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    

    @Test
    public void testOptionWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("A long option test")
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("A long option test", option.getDescription());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = OptionBuilder.isRequired()
                .create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionHasArg() {
        Option option = OptionBuilder.hasArg()
                .create();
        assertEquals(1, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionMissingLongOpt() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create();
        fail("An IllegalArgumentException should be thrown when no long opt is specified.");
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertTrue(option.optionalArg);
    }

    @Test
    public void testOptionHasArgs() {
        Option option = OptionBuilder.hasArgs(3)
                .create();
        assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testOptionWithType() {
        Option option = OptionBuilder.withType(Integer.class)
                .create();
        assertEquals(Integer.class, option.getType());
    }

    
}