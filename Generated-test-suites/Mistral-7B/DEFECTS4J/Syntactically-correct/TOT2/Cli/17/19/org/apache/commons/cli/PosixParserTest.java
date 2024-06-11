import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments() throws Exception {
        // Given
        String[] arguments = new String[0];
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArgument() throws Exception {
        // Given
        String[] arguments = new String[]{"argument1"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"argument1"}, result);
    }

    @Test
    public void testFlatten_SingleOption() throws Exception {
        // Given
        String[] arguments = new String[]{"-option1"};
        Options options = new Options();
        options.addOption("option1", false, false, "Option 1");
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"-option1"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgument() throws Exception {
        // Given
        String[] arguments = new String[]{"-option1=value1"};
        Options options = new Options();
        options.addOption("option1", true, false, "Option 1");
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"-option1", "value1"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() throws Exception {
        // Given
        String[] arguments = new String[]{"-option1", "-option2"};
        Options options = new Options();
        options.addOption("option1", false, false, "Option 1");
        options.addOption("option2", false, false, "Option 2");
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"-option1", "-option2"}, result);
    }

    @Test
    public void testFlatten_LongOption() throws Exception {
        // Given
        String[] arguments = new String[]{"--long-option"};
        Options options = new Options();
        options.addOption("longOption", false, false, "Long Option");
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() throws Exception {
        // Given
        String[] arguments = new String[]{"-invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // When & Then
        parser.flatten(options, arguments, false);
    }
}