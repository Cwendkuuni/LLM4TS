import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flatten method with no options and no arguments.
     */
    @Test
    public void testEmpty() {
        String[] args = new String[0];
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test flatten method with options and no arguments.
     */
    @Test
    public void testOptions() {
        String[] args = new String[]{"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", false, "Option B");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "--optionB"}, result);
    }

    /**
     * Test flatten method with options and arguments.
     */
    @Test
    public void testOptionsWithArguments() {
        String[] args = new String[]{"-a", "argA", "--optionB=valueB", "argB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "argA", "--optionB", "valueB", "argB"}, result);
    }

    /**
     * Test flatten method with long option.
     */
    @Test
    public void testLongOption() {
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    /**
     * Test flatten method with long option and value.
     */
    @Test
    public void testLongOptionWithValue() {
        String[] args = new String[]{"--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    /**
     * Test flatten method with multiple long options.
     */
    @Test
    public void testLongOptions() {
        String[] args = new String[]{"--longOption1", "--longOption2=value2", "--longOption3=value3"};
        Options options = new Options();
        options.addOption("longOption1", false, "Long option 1");
        options.addOption("longOption2", true, "Long option 2");
        options.addOption("longOption3", true, "Long option 3");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2", "value2", "--longOption3", "value3"}, result);
    }

    /**
     * Test flatten method with multiple options and arguments.
     */
    @Test
    public void testMixedOptions() {
        String[] args = new String[]{"-a", "argA", "--longOption=value", "-b", "argB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", true, "Long option");
        options.addOption("b", false, "Option B");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "argA", "--longOption", "value", "-b", "argB"}, result);
    }

    /**
     * Test flatten method with single hyphen.
     */
    @Test
    public void testSingleHyphen() {
        String[] args = new String[]{"-", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Test flatten method with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        String[] args = new String[]{"--", "nonOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }
        List tokens = parser.getTokens();
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals("--", tokens.get(0));
        Assert.assertEquals("nonOption", tokens.get(1));
    }
}