import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .create();

        Options options = new Options();
        options.addOption(option);

        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals( Option.REQUIRED_ARGUMENT, option.getArgPosition().length);
        assertEquals("The long option requires an argument.", option.getDescription());

        Option optionWithValueSeparator = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .create();

        assertEquals(Option.VALUE_SEPARATOR_COLON, optionWithValueSeparator.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.withArgName("argName");
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasOptionalArg()
                .create();

        assertEquals(Option.NO_ARGUMENTS, option.getArgPosition().length);
        assertEquals("The long option can have an optional argument.", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithMultipleOptionalArgs()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasOptionalArgs(2)
                .create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertEquals("The long option can have multiple optional arguments.", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .withType(Integer.class)
                .create();

        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .withDescription("Description for the long option")
                .hasArg()
                .withArgName("argName")
                .create();

        assertEquals("Description for the long option", option.getDescription());
    }
}