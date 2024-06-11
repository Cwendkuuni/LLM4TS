import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        // Given
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "--bar=baz", "-c", "arg", "--", "-d", "--help"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar", "baz", "--", "-c", "arg", "--d", "--help"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with long option with value.
     */
    @Test
    public void testFlattenWithValue() {
        // Given
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] arguments = {"--foo", "true"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"--foo", "true"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with unrecognized option.
     */
    @Test(expected = ParseException.class)
    public void testUnrecognizedOption() {
        // Given
        Options options = new Options();
        String[] arguments = {"--unknown"};
        PosixParser parser = new PosixParser();

        // When
        parser.flatten(options, arguments, false);

        // Then - ParseException should be thrown
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        // Given
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "arg", "--", "-c", "arg"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, true);

        // Then
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--", "-c", "arg"}, result);
    }
}