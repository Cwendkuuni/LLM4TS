import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private Option option;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongOpt("arg")
                .withDescription("An argument test option")
                .withMinValues(1)
                .withMaxValues(3)
                .isRequired()
                .create(false);
        commandLine = new CommandLine();
    }

    @Test
    public void testArgumentWithMinimumAndMaximumValues() {
        Parser parser = new DefaultParser();

        try {
            parser.parse(option, new String[]{"--arg", "value1", "value2"});
            assertEquals(2, commandLine.getValues(option).size());
        } catch (ParseException e) {
            fail("Test should not fail with valid input");
        }

        // Test with minimum value
        try {
            parser.parse(option, new String[]{"--arg"});
            fail("Test should fail as minimum value is not met");
        } catch (ParseException e) {
            // Expected
        }

        // Test with maximum value
        try {
            parser.parse(option, new String[]{"--arg", "value1", "value2", "value3"});
            fail("Test should fail as maximum value is exceeded");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ParseException.class)
    public void testArgumentWithInvalidValue() {
        Parser parser = new DefaultParser();

        try {
            parser.parse(option, new String[]{"--arg", "value1", "--arg", "value3"});
            fail("Test should fail as duplicate argument is not allowed");
        } catch (ParseException e) {
            // Expected
        }
    }
}