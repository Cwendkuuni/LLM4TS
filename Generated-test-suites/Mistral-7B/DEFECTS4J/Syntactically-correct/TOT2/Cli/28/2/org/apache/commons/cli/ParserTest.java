import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, arguments);

        // Then
        Assertions.assertEquals(2, commandLine.getArgs().size());
        Assertions.assertEquals("1", commandLine.getOptionValue("a"));
        Assertions.assertEquals("2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--optionA", "argA", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, arguments);

        // Then
        Assertions.assertEquals(0, commandLine.getArgs().size());
        Assertions.assertTrue(commandLine.hasOption("a"));
        Assertions.assertEquals("argA", commandLine.getOptionValue("a"));
        Assertions.assertTrue(commandLine.hasOption("b"));
    }

    @Test
    public void testParseWithMissingRequiredOptions() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--optionA", "argA"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When & Then
        Assertions.assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithInvalidLongOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--invalidOption"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When & Then
        Assertions.assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithUnrecognizedShortOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"-x", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When & Then
        Assertions.assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"-a", "1", "--optionB", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B", true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, arguments);

        // Then
        Assertions.assertEquals(0, commandLine.getArgs().size());
        Assertions.assertEquals(2, commandLine.getOptionValues("b").length);
        Assertions.assertEquals("value1", commandLine.getOptionValue("b")[0]);
        Assertions.assertEquals("value2", commandLine.getOptionValue("b")[1]);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--propertyA=valueA", "--propertyB=valueB"};
        Properties properties = new Properties();
        properties.setProperty("propertyA", "valueA");
        properties.setProperty("propertyB", "valueB");

        Options options = new Options();
        options.addOptions(Arrays.asList(
                new Option("propertyA", true, "Property A"),
                new Option("propertyB", true, "Property B")
        ));

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, arguments, properties, false);

        // Then
        Assertions.assertEquals(0, commandLine.getArgs().size());
        Assertions.assertEquals("valueA", commandLine.getOptionValue("propertyA"));
        Assertions.assertEquals("valueB", commandLine.getOptionValue("propertyB"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--optionA", "argA", "invalid", "--optionB", "valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, arguments, null, true);

        // Then
        Assertions.assertEquals(2, commandLine.getArgs().size());
        Assertions.assertEquals("argA", commandLine.getOptionValue("a"));
        Assertions.assertEquals("", commandLine.getOptionValue("b"));
        Assertions.assertEquals("invalid", commandLine.getArg(0));
        Assertions.assertEquals("valueB", commandLine.getArg(1));
    }

    // Add more test cases for edge cases and exceptions as needed
}