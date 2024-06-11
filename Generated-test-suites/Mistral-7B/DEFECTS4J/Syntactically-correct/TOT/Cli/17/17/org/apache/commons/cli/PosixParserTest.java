import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        String[] args = {"-a", "--optionA=valueA"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "an option with value A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"--optionA", "valueA"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }
    }

    @Test
    public void testMultipleOptions() {
        String[] args = {"--optionA", "--optionB=valueB", "-c"};
        Options options = new Options();
        options.addOption("a", "optionA", false, "an option without value");
        options.addOption("b", "optionB", true, "an option with value B");
        options.addOption("c", "optionC", false, "an optional option C");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"--optionA", "--optionB", "valueB", "-c"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }
    }

    @Test
    public void testNoOptions() {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.fail("Parsing should have failed.");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() {
        String[] args = {"--optionA"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "an option with value A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.fail("Parsing should have failed.");
        } catch (ParseException e) {
            Assert.assertEquals("The following errors occurred:\n" +
                    " Missing required option '-valueA' for option 'optionA'.\n", e.getMessage());
        }
    }

    @Test
    public void testLongOption() {
        String[] args = {"--longOptionWithValue=valueA"};
        Options options = new Options();
        options.addOption("lo", "longOption", true, "a long option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"--longOptionWithValue"}, parsedArgs);
            Assert.assertEquals("valueA", parsedArgs[0]);
        } catch (ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }
    }

    @Test
    public void testBursting() {
        String[] args = {"--optionC=valueC", "-o", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("c", "optionC", true, "option C with value C");
        options.addOption("d", "optionD", true, "option D with value D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"--optionC", "valueC", "--optionD"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        String[] args = {"--optionA", "--noOption", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", "optionA", false, "option A");
        options.addOption("b", "optionB", true, "option B with value B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true); // stop at non-option
            Assert.assertArrayEquals(new String[]{"--optionA", "--noOption"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }
    }

    @Test
    public void testInvalidBurstableToken() {
        String[] args = {"--invalidToken", "--optionA"};
        Options options = new Options();
        options.addOption("a", "optionA", false, "option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.fail("Parsing should have failed.");
        } catch (ParseException e) {
            Assert.assertEquals("The following errors occurred:\n" +
                    " Unrecognized option: invalidToken\n", e.getMessage());
        }
    }
}