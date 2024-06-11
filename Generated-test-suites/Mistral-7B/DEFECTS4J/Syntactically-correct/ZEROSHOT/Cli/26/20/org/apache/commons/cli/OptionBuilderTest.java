import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .withArgName("argument")
                .isRequired()
                .withValueSeparator('=')
                .hasOptionalArgs(2)
                .withType(String.class);

        Option option = optionBuilder.create('o');

        Assert.assertEquals("option1", option.getLongOpt());
        Assert.assertEquals("An optional option with argument.", option.getDescription());
        Assert.assertEquals("o", option.getOpt());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgPosition().length);
        Assert.assertEquals("argument", option.getArgPosition()[0]);
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(true, option.isOptionalArg());
        Assert.assertEquals('=', option.getValueSeparator());
        Assert.assertEquals(2, option.getNumberOfArgs());
        Assert.assertEquals(String.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderNoLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }
}