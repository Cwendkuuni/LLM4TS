import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testOptionBuilderHasArgWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        optionBuilder.create();
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create();
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("Description", option.getDescription());
    }
}