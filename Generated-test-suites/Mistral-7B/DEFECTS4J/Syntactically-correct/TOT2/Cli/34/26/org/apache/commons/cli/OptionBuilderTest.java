import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testOptionWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .withDescription("This is an option with long opt");
        Option option = optionBuilder.create();

        Assert.assertEquals("optionLong", option.getLongOpt());
        Assert.assertNull(option.getOpt());
        Assert.assertEquals("This is an option with long opt", option.getDescription());
    }

    @Test
    public void testOptionWithShortOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create('o')
                .withDescription("This is an option with short opt");
        Option option = optionBuilder.create();

        Assert.assertEquals('o', option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is an option with short opt", option.getDescription());
    }

    @Test
    public void testOptionWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();

        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName")
                .hasArg();
        Option option = optionBuilder.create();

        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithArgValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                .withValueSeparator('=');
        Option option = optionBuilder.create();

        option.setValue("value");

        Assert.assertEquals(Option.VALUE_PRESENT, option.getArgs());
        Assert.assertEquals("value", option.getArg());
    }

    @Test
    public void testOptionWithArgValues() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2)
                .withValueSeparator(':');
        Option option = optionBuilder.create();

        option.setValue("value1:value2");

        Assert.assertEquals(Option.VALUE_PRESENT, option.getArgs());
        Assert.assertEquals("value1", option.getArgs()[0]);
        Assert.assertEquals("value2", option.getArgs()[1]);
    }

    @Test
    public void testOptionWithRequiredArg() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();

        Assert.assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        Option option = optionBuilder.create();

        Assert.fail("IllegalArgumentException expected");
    }

    
}