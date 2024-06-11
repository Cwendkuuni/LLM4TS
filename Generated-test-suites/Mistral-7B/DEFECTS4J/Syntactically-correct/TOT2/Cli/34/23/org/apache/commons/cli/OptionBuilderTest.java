import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder ob = OptionBuilder.withLongOpt("optionLongOpt");
        Option option = ob.create();
        assertEquals("optionLongOpt", option.getLongOpt());
    }

    // Test for hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder ob = OptionBuilder.hasArg();
        Option option = ob.create();
        assertFalse(option.isUnbounded());
        assertEquals(1, option.getArgs().length);
    }

    // Test for hasArg with false
    @Test
    public void testHasArgFalse() {
        OptionBuilder ob = OptionBuilder.hasArg(false);
        Option option = ob.create();
        assertTrue(option.isUnbounded());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    // Test for hasArgName method
    @Test
    public void testHasArgName() {
        OptionBuilder ob = OptionBuilder.withArgName("argName");
        Option option = ob.create();
        assertEquals("argName", option.getArgName());
    }

    // Test for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder ob = OptionBuilder.isRequired();
        Option option = ob.create();
        assertTrue(option.isRequired());
    }

    // Test for hasOptionalArg method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder ob = OptionBuilder.hasOptionalArg();
        Option option = ob.create();
        assertTrue(option.optionalArg);
    }

    // Test for hasArgs method
    @Test
    public void testHasArgs() {
        OptionBuilder ob = OptionBuilder.hasArgs();
        Option option = ob.create();
        assertTrue(option.isUnbounded());
    }

    // Test for hasArgs with specific number
    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder ob = OptionBuilder.hasArgs(2);
        Option option = ob.create();
        assertFalse(option.isUnbounded());
        assertEquals(2, option.getArgs().length);
    }

    // Test for withType method
    @Test
    public void testWithType() {
        OptionBuilder ob = OptionBuilder.withType(String.class);
        Option option = ob.create();
        assertEquals(String.class, option.getType());
    }

    // Test for create method with empty description
    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyDescription() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.create();
    }

    // Test for create method with long opt not set
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.create((char) 1);
    }

    // Test for create method with invalid char
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidChar() {
        OptionBuilder ob = OptionBuilder.withLongOpt("invalidOption");
        Option option = ob.create((char) 0);
    }
}