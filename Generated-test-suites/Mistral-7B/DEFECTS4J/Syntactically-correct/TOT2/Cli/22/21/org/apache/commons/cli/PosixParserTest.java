import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 2, "option B");
        options.addOption("c", 3, "option C with argument");
        options.addOption("d", false, "option D");
    }

    @Test
    public void testFlatten_EmptyArguments()
    {
        String[] arguments = new String[0];
        String[] expected = new String[0];

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument()
    {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption()
    {
        String[] arguments = {"--optionA"};
        String[] expected = {"--optionA"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument()
    {
        String[] arguments = {"--optionB", "valueB"};
        String[] expected = {"--optionB", "valueB"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOption()
    {
        String[] arguments = {"-a", "-b", "-c", "--d"};
        String[] expected = {"-a", "-b", "-c", "--d"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArgument()
    {
        String[] arguments = {"-c", "valueC"};
        String[] expected = {"-c", "valueC"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NonOption()
    {
        String[] arguments = {"nonOption"};
        String[] expected = {"--", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidShortOption()
    {
        String[] arguments = {"-z"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidLongOption()
    {
        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}