import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Options class.
 */
public class OptionsTest {

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)}.
     */
    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("Test Option Group", "tog");
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        Assert.assertEquals(option, options.getOption("tog.test"));
        Assert.assertEquals(Collections.singletonList(option), options.getOptions());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @Test
    public void testAddOption() {
        Options options = new Options();

        options.addOption("t", "test", false, "Test option");
        Option option = options.getOption("t");

        Assert.assertEquals("t", option.getOpt());
        Assert.assertEquals("test", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("Test option", option.getDescription());
    }

    /**
     * Test method for {@link Options#getOptions()}.
     */
    @Test
    public void testGetOptions() {
        Options options = new Options();
        options.addOption("t", "test", false, "Test option");

        List<Option> optionsList = options.getOptions();

        Assert.assertEquals(Collections.singletonList(options.getOption("t")), optionsList);
    }

    /**
     * Test method for {@link Options#hasOption(String)}.
     */
    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("t", "test", false, "Test option");

        Assert.assertTrue(options.hasOption("t"));
        Assert.assertTrue(options.hasOption("test"));
        Assert.assertFalse(options.hasOption("f"));
    }

    /**
     * Test method for {@link Options#parse(String[], String[])}.
     */
    @Test
    public void testParse() {
        Options options = new Options();

        options.addOption("t", "test", false, "Test option");

        String[] args = {"test"};
        CommandLine parsed = null;
        try {
            parsed = options.parse(args, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(parsed);
        Assert.assertFalse(parsed.hasOption("help"));
        Assert.assertTrue(parsed.hasOption("test"));
    }

    /**
     * Test method for {@link Options#parse(String[], String[])}.
     */
    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() {
        Options options = new Options();

        options.addOption("t", "test", false, "Test option");

        String[] args = {"invalid"};

        options.parse(args, null);
    }
}