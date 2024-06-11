import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());

        OptionBuilder optionBuilderWithDescription = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Test long opt");
        Option optionWithDescription = optionBuilderWithDescription.create();
        assertNotNull(optionWithDescription);
        assertEquals("Test long opt", optionWithDescription.getDescription());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getArgs().length);

        OptionBuilder optionBuilderWithArgName = OptionBuilder.hasArg().withArgName("argName");
        Option optionWithArgName = optionBuilderWithArgName.create();
        assertNotNull(optionWithArgName);
        assertEquals("argName", optionWithArgName.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());

        OptionBuilder optionBuilderNotRequired = OptionBuilder.isRequired(false);
        Option optionNotRequired = optionBuilderNotRequired.create();
        assertNotNull(optionNotRequired);
        assertFalse(optionNotRequired.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertNotNull(option);
        assertTrue(option.isOptional());

        String[] args = {"--longOpt", "value"};
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(OptionBuilderTest.class.getProtectedMember("args"), new DefaultOptions());
            Option optionValue = cmd.getOption("longOpt");
            assertEquals("value", optionValue.getValue());
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        optionBuilder.create('');
    }

    // Add more test cases for other methods, edge cases and error scenarios here
}