import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest
{
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", false, "Option D");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithNoOptions() throws ParseException
    {
        String[] args = new String[]{"test"};
        String[] expected = new String[]{"test"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOption() throws ParseException
    {
        String[] args = new String[]{"--a"};
        String[] expected = new String[]{"--a"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleValueOption() throws ParseException
    {
        String[] args = new String[]{"--a", "value"};
        String[] expected = new String[]{"--a", "value"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultiValueOption() throws ParseException
    {
        String[] args = new String[]{"--a", "value1", "value2"};
        String[] expected = new String[]{"--a", "value1", "value2"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithArgument() throws ParseException
    {
        String[] args = new String[]{"--c", "value"};
        String[] expected = new String[]{"--c", "value"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() throws ParseException
    {
        String[] args = new String[]{"--a", "--b", "--d"};
        String[] expected = new String[]{"--a", "--b", "--d"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleDash() throws ParseException
    {
        String[] args = new String[]{"-"};
        String[] expected = new String[]{"--"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() throws ParseException
    {
        String[] args = new String[]{"--c", "value1 value2"};
        String[] expected = new String[]{"--c", "value1 value2"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--x"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOptionValue() throws ParseException
    {
        String[] args = new String[]{"--b", "value"};
        options.setOptionValue("b", null);

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException
    {
        String[] args = new String[]{"--a", "value", "non_option"};
        String[] expected = new String[]{"--a", "value"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }
}