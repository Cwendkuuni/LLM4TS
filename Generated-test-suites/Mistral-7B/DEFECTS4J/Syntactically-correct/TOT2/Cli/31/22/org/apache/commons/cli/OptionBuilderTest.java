import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("longOpt");

        assertEquals("longOpt", instance.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg();

        assertEquals(Option.UNINITIALIZED, instance.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testHasArg_withArgName() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg().withArgName("argName");

        assertEquals("argName", instance.argName);
        assertEquals(Option.UNINITIALIZED, instance.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testHasArg_withValueSeparator() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg().withValueSeparator(';');

        assertEquals(Option.UNINITIALIZED, instance.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, instance.optionalArg);
        assertFalse(instance.required);
        assertEquals(';', instance.valuesep);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired();

        assertTrue(instance.required);
        assertEquals(Option.UNINITIALIZED, instance.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, instance.optionalArg);
    }

    @Test(expected = IllegalStateException.class)
    public void testHasArgs_withInitialValue_hasAlreadyBeenSet() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs().hasArg();

        // This should throw an IllegalStateException
        instance.hasArgs(1);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, instance.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testHasArgs_withSpecifiedNumberOfArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs(2);

        assertEquals(2, instance.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArg();

        assertEquals(Option.UNINITIALIZED, instance.numberOfArgs);
        assertEquals(true, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, instance.numberOfArgs);
        assertEquals(true, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testHasOptionalArgs_withSpecifiedNumberOfArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs(3);

        assertEquals(3, instance.numberOfArgs);
        assertEquals(true, instance.optionalArg);
        assertFalse(instance.required);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withType(Integer.class);

        assertSame(Integer.class, instance.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withDescription("A test option");

        assertEquals("A test option", instance.description);
    }

    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArgs(2)
                .withValueSeparator(':');

        Option option = instance.create('L');

        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getOptType());
        assertEquals(2, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg();

        try {
            instance.create((char) 0);
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
            throw e;
        }
    }
}