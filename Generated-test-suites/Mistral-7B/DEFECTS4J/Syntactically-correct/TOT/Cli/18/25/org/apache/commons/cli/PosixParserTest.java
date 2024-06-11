import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = new String[]{"--app", "test1.jar", "-o", "out", "-d", "debug", "-P", "testProfile"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs;

        try {
            flattenedArgs = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--app", "test1.jar", "-o", "out", "-d", "debug", "-P", "testProfile"}, flattenedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception:" + e.getMessage());
        }
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        String[] args = new String[]{"--long-option-with-value=SomeValue"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs;

        try {
            flattenedArgs = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--long-option-with-value", "SomeValue"}, flattenedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception:" + e.getMessage());
        }
    }

    @Test
    public void testFlattenNoOption() {
        Options options = new Options();
        String[] args = new String[]{"--notAnOption", "value1", "--notAnOption", "value2"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs;

        try {
            flattenedArgs = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--notAnOption", "value1", "--notAnOption", "value2"}, flattenedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception:" + e.getMessage());
        }

        try {
            flattenedArgs = parser.flatten(options, args, true);
            Assert.assertArrayEquals(new String[]{"--notAnOption", "value1", "--notAnOption", "value2"}, flattenedArgs);
        } catch (ParseException e) {
            // expected, since "stopAtNonOption" is set to true
        }

        try {
            flattenedArgs = parser.flatten(options, new String[0], false);
            Assert.assertArrayEquals(new String[0], flattenedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception:" + e.getMessage());
        }

        try {
            flattenedArgs = parser.flatten(options, new String[0], true);
            Assert.assertEquals(0, flattenedArgs.length);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception:" + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingOption() {
        Options options = new Options();
        String[] args = new String[]{"--missingOption"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = new String[]{"--invalidOption"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithValue() {
        Options options = new Options();
        String[] args = new String[]{"--invalidOption=value"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}