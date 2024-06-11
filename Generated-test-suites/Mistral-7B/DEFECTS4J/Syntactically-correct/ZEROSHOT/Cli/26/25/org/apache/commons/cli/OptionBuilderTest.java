import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("optionName")
                .create();
        assertEquals("optionName", OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertEquals("--optionName", option.getLongOpt());
    }

    @Test
    public void testOptionWithDescription() {
        Option option = OptionBuilder.withDescription("Description for option")
                .create();
        assertNull(OptionBuilder.longopt);
        assertEquals("Description for option", option.getDescription());
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertEquals("argName", option.getArgName());
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionIsRequired() {
        Option option = OptionBuilder.isRequired()
                .create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionHasArg() {
        Option option = OptionBuilder.hasArg()
                .create();
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertEquals(Option.VALUE_OPTIONAL, option.getArgParse());
    }

    @Test
    public void testOptionHasArgWithName() {
        Option option = OptionBuilder.hasArg("argName")
                .create();
        assertTrue(OptionBuilder.required);
        assertEquals("argName", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertEquals(Option.VALUE_OPTIONAL, option.getArgParse());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = OptionBuilder.hasArgs()
                .create();
        assertTrue(OptionBuilder.required);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertEquals(Option.VALUE_OPTIONAL, option.getArgParse());
    }

    @Test
    public void testOptionHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs(2)
                .create();
        assertTrue(OptionBuilder.required);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionWithType() {
        Option option = OptionBuilder.withType(Integer.class)
                .create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.VALUE_OPTIONAL, OptionBuilder.numberOfArgs);
        assertEquals(Integer.class, OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertEquals(Option.VALUE_OPTIONAL, option.getArgParse());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}