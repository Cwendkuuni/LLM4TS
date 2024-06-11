import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        // Given
        String[] arguments = {"-a", "1", "--optionB", "2", "-o", "output.txt"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("o", true, "Output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, arguments);

        // Then
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("o"));
        Assert.assertFalse(cmd.hasOption("unknown"));

        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        // Given
        String[] arguments = {"--optionA", "valueA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, arguments);

        // Then
        // ParseException should be thrown
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() throws ParseException {
        // Given
        String[] arguments = {"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, arguments);

        // Then
        // ParseException should be thrown
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        // Given
        String[] arguments = {"--optionA", "--", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, arguments, true);

        // Then
        List<String> args = cmd.getArgs();
        Assert.assertEquals("nonOption", args.get(0));
    }
}