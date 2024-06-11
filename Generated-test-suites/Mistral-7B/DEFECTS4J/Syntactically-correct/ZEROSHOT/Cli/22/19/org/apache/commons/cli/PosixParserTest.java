import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setup()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C with an argument");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithEmptyArguments()
    {
        String[] arguments = {};
        String[] expected = {};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithSingleArgument()
    {
        String[] arguments = {"arg"};
        String[] expected = {"arg"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithSingleOption()
    {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithSingleLongOption()
    {
        String[] arguments = {"--option-a"};
        String[] expected = {"--option-a"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithSingleLongOptionValue()
    {
        String[] arguments = {"--option-a", "value"};
        String[] expected = {"--option-a", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithOptionAndArgument()
    {
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionAndArgument()
    {
        String[] arguments = {"--option-a", "value"};
        String[] expected = {"--option-a", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndAnotherOption()
    {
        String[] arguments = {"--option-a", "value1", "-b"};
        String[] expected = {"--option-a", "value1", "-b"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndAnotherOptionWithValue()
    {
        String[] arguments = {"--option-a", "value1", "--option-b", "value2"};
        String[] expected = {"--option-a", "value1", "--option-b", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndAnotherOptionWithMultipleValues()
    {
        options.addOption("option-b", 1, "Option B with multiple values");
        String[] arguments = {"--option-a", "value1", "--option-b", "value1", "value2"};
        String[] expected = {"--option-a", "value1", "--option-b", "value1", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndMultipleOptions()
    {
        String[] arguments = {"--option-a", "value1", "-b", "-c"};
        String[] expected = {"--option-a", "value1", "-b", "-c"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndMultipleOptionsWithValues()
    {
        options.addOption("option-b", 1, "Option B with multiple values");
        String[] arguments = {"--option-a", "value1", "--option-b", "value1", "value2"};
        String[] expected = {"--option-a", "value1", "--option-b", "value1", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithTwoLongOptionsWithValues()
    {
        String[] arguments = {"--option-a", "value1", "--option-b", "value2"};
        String[] expected = {"--option-a", "value1", "--option-b", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithOptionAndLongOption()
    {
        String[] arguments = {"-a", "--option-b", "value"};
        String[] expected = {"-a", "--option-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionAndOption()
    {
        String[] arguments = {"--option-a", "-b"};
        String[] expected = {"--option-a", "-b"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndOption()
    {
        String[] arguments = {"--option-a", "value", "-b"};
        String[] expected = {"--option-a", "value", "-b"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndMultipleOptions()
    {
        String[] arguments = {"--option-a", "value", "-b", "-c"};
        String[] expected = {"--option-a", "value", "-b", "-c"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndMultipleOptionsWithValues()
    {
        options.addOption("option-b", 1, "Option B with multiple values");
        String[] arguments = {"--option-a", "value", "--option-b", "value1", "value2"};
        String[] expected = {"--option-a", "value", "--option-b", "value1", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndMultipleOptionsWithMultipleValues()
    {
        options.addOption("option-b", 1, "Option B with multiple values");
        String[] arguments = {"--option-a", "value", "--option-b", "value1", "value2", "value3"};
        String[] expected = {"--option-a", "value", "--option-b", "value1", "value2", "value3"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithShortOptionAndLongOption()
    {
        String[] arguments = {"-a", "--option-b", "value"};
        String[] expected = {"-a", "--option-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithShortOptionAndLongOptionWithValue()
    {
        String[] arguments = {"-a", "--option-b", "value"};
        String[] expected = {"-a", "--option-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithUnknownOption()
    {
        String[] arguments = {"--unknown-option"};
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption()
    {
        String[] arguments = {"--option-a", "value", "--unknown-option", "--option-b", "value"};
        String[] expected = {"--option-a", "value", "--unknown-option", "--option-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndUnknownOption()
    {
        String[] arguments = {"--option-a", "value", "--unknown-option", "--option-b", "value"};
        String[] expected = {"--option-a", "value", "--unknown-option", "--option-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
}