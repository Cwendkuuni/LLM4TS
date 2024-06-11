import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option-long");
        Option option = optionBuilder.create();
        Assert.assertEquals("option-long", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        optionBuilder = OptionBuilder.withArgName("arg-name");
        option = optionBuilder.create();
        Assert.assertEquals("arg-name", option.getArgName());
        optionBuilder = OptionBuilder.hasArg(true);
        option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasArgWithoutArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertFalse(option.isRequired());
        optionBuilder = OptionBuilder.isRequired(true);
        option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertFalse(option.optionalArg);
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        optionBuilder = OptionBuilder.hasOptionalArgs(2);
        option = optionBuilder.create();
        Assert.assertTrue(option.optionalArg);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        Assert.assertNull(option.getType());
        optionBuilder = OptionBuilder.withType(Integer.TYPE);
        option = optionBuilder.create();
        Assert.assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        Option option = optionBuilder.create();
        Assert.assertNull(option.getDescription());
        optionBuilder = OptionBuilder.withDescription("Option description");
        option = optionBuilder.create();
        Assert.assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testCreateWithDefaultValues() {
        Option option = OptionBuilder.create();
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertNull(option.getType());
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testCreateWithValues() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();
        option.setValue("value1");
        option.setValue("value2");
        Assert.assertEquals(new String[]{"value1", "value2"}, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidChar() {
        OptionBuilder optionBuilder = OptionBuilder.create('%');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("invalid-longopt");
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("\0");
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(new Object());
        optionBuilder.create();
    }

    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create();
        option.setValue("value1,value2");
        Assert.assertEquals(new String[]{"value1", "value2"}, option.getArgs());
    }

    @Test
    public void testCreateWithValueSeparatorAndValues() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',')
                .hasArgs(3);
        Option option = optionBuilder.create();
        option.setValue("value1,value2,value3");
        Assert.assertEquals(new String[]{"value1", "value2", "value3"}, option.getArgs());
    }

    @Test
    public void testCreateWithValueSeparatorAndInvalidValue() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',')
                .hasArgs(2);
        Option option = optionBuilder.create();
        try {
            option.setValue("value1,value2,value3");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid value: value3", e.getMessage());
        }
    }

    @Test
    public void testCreateWithValueSeparatorAndEmptyValue() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',')
                .hasArgs(1);
        Option option = optionBuilder.create();
        option.setValue("");
        Assert.assertEquals(new String[0], option.getArgs());
    }
}