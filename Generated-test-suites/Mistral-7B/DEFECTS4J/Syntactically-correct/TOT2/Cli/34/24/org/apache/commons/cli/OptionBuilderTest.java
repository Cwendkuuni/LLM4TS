import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option option = optionBuilder.create('o');

        Assert.assertEquals("optionLongName", option.getLongOpt());
        Assert.assertNull(option.getOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals('\0', option.getValueSeparator());
        Assert.assertNull(option.getArgName());

        Option parsedOption = new Option('o', "optionShortName", false, "Option description");
        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testOptionWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('o');

        Assert.assertEquals("", option.getLongOpt());
        Assert.assertEquals('o', option.getOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertNull(option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('o');

        // This should throw an IllegalArgumentException
    }
}