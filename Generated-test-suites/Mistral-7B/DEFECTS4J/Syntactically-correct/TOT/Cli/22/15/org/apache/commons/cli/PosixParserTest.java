import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with valid options.
     */
    @Test
    public void testFlattenValidOptions()
    {
        // create command line options
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 2, "option C");

        // create test data
        String[] args = new String[]{"-a", "--b", "1", "--c=2"};

        // create parser
        PosixParser parser = new PosixParser();

        // test method
        String[] result = parser.flatten(options, args, false);

        // check result
        Assert.assertArrayEquals(new String[]{"-a", "--b", "1", "--c", "2"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptions()
    {
        // create command line options
        Options options = new Options();
        options.addOption("a", true, "option A");

        // create test data
        String[] args = new String[]{"-a", "--invalid", "--b"};

        // create parser
        PosixParser parser = new PosixParser();

        // test method
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with non-option arguments.
     */
    @Test
    public void testFlattenNonOptionArguments()
    {
        // create command line options
        Options options = new Options();
        options.addOption("a", true, "option A");

        // create test data
        String[] args = new String[]{"-a", "arg1", "--b", "arg2"};

        // create parser
        PosixParser parser = new PosixParser();

        // test method
        String[] result = parser.flatten(options, args, false);

        // check result
        Assert.assertArrayEquals(new String[]{"-a", "--b", "arg1", "arg2"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption option.
     */
    @Test
    public void testFlattenStopAtNonOption()
    {
        // create command line options
        Options options = new Options();
        options.addOption("a", true, "option A");

        // create test data
        String[] args = new String[]{"-a", "--b", "arg1", "--", "arg3", "arg4"};

        // create parser
        PosixParser parser = new PosixParser();

        // test method
        String[] result = parser.flatten(options, args, true);

        // check result
        Assert.assertArrayEquals(new String[]{"-a", "--b", "arg1", "--", "arg3", "arg4"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options.
     */
    @Test
    public void testFlattenLongOptions()
    {
        // create command line options
        Options options = new Options();
        options.addOption("longOptionA", true, "long option A");
        options.addOption("longOptionB", 1, "long option B");

        // create test data
        String[] args = new String[]{"--longOptionA", "--longOptionB=1", "--longOptionC=2"};

        // create parser
        PosixParser parser = new PosixParser();

        // test method
        String[] result = parser.flatten(options, args, false);

        // check result
        Assert.assertArrayEquals(new String[]{"--longOptionA", "--longOptionB", "2"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions()
    {
        // create command line options
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");

        // create test data
        String[] args = new String[]{"-a", "--b=1", "-a", "--b=2"};

        // create parser
        PosixParser parser = new PosixParser();

        // test method
        String[] result = parser.flatten(options, args, false);

        // check result
        Assert.assertArrayEquals(new String[]{"-a", "--b", "-a", "--b"}, result);
    }
}