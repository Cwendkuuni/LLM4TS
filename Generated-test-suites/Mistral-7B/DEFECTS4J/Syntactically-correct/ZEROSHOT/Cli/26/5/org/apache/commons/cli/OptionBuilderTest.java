import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithDefaultValues() {
        Option option = OptionBuilder.create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\u0000', OptionBuilder.valuesep);

        assertEquals(Option.builder(), option);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option");
        Option option = optionBuilder.create();

        assertEquals("--option", option.getLongOpt());
        assertNull(OptionBuilder.longopt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .withDescription("This is an option");
        Option option = optionBuilder.create();

        assertEquals("This is an option", option.getDescription());
        assertEquals("--option", option.getLongOpt());
        assertNull(OptionBuilder.description);
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .withArgName("name");
        Option option = optionBuilder.create();

        assertEquals("name", option.getArgName());
        assertEquals("--option", option.getLongOpt());
        assertEquals("name", OptionBuilder.argName);
    }

    @Test
    public void testOptionBuilderWithHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .hasArg();
        Option option = optionBuilder.create();

        assertEquals(Option.UNINITIALIZED + 1, option.getArgs());
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .isRequired();
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .withValueSeparator(':');
        Option option = optionBuilder.create();

        assertEquals(':', option.getValueSeparator());
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .hasArgs();
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithHasArgsAndValue() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .hasArgs(2);
        Option option = optionBuilder.create();

        assertEquals(2, option.getArgs().length);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .hasOptionalArgs();
        Option option = optionBuilder.create();

        assertTrue(option.isOptional());
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderWithHasOptionalArgsAndValue() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .hasOptionalArgs(2);
        Option option = optionBuilder.create();

        assertTrue(option.isOptional());
        assertEquals(2, option.getArgs().length);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option")
                .withType(Boolean.class);
        Option option = optionBuilder.create();

        assertEquals(Boolean.class, option.getType());
        assertEquals(Boolean.class, OptionBuilder.type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingArgType() {
        OptionBuilder.create();
    }
}