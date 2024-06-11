import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        assertEquals("optionLong", optionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.required);
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        assertEquals(5, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.optionalArg);
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertTrue(optionBuilder.optionalArg);
        assertEquals(3, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", optionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder
                .withLongOpt("optionLong")
                .withDescription("description")
                .hasArg()
                .withArgName("argName")
                .isRequired();

        Option option = optionBuilder.create('o');

        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals("argName", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.create();
        throw new AssertionError("IllegalArgumentException not thrown");
    }
}