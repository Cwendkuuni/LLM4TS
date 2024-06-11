import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new GnuParser();
    }

    /**
     * Test flattening single option without argument.
     */
    @Test
    public void testFlattenSingleOptionWithoutArgument() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening single option with argument.
     */
    @Test
    public void testFlattenSingleOptionWithArgument() {
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening multiple options without arguments.
     */
    @Test
    public void testFlattenMultipleOptionsWithoutArguments() {
        String[] arguments = {"-a", "-b", "--"};
        String[] expected = {"-a", "-b", "--"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening multiple options with arguments.
     */
    @Test
    public void testFlatteningMultipleOptionsWithArguments() {
        options.addOption("a", true, "Short option a");
        options.addOption("b", true, "Short option b");
        options.addOption("c", true, "Short option c");

        String[] arguments = {"-a", "value1", "-bc", "value2"};
        String[] expected = {"-a", "value1", "-b", "-c", "value2"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening multiple options with same short name but different long names.
     */
    @Test
    public void testFlatteningMultipleOptionsWithSameShortNameDifferentLongNames() {
        options.addOption("a", true, "First option a");
        options.addOption("a", true, "Second option a");

        String[] arguments = {"-a", "value1", "-a", "value2"};
        String[] expected = {"-a", "value1", "-a", "value2"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening special properties option.
     */
    @Test
    public void testFlatteningSpecialPropertiesOption() {
        String[] arguments = {"-Dproperty=value"};
        String[] expected = {"-D", "property=value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening when a non-option is encountered.
     */
    @Test
    public void testFlatteningWhenANonOptionIsEncountered() {
        String[] arguments = {"-a", "value", "nonOption"};
        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    /**
     * Test parsing null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testParsingNullArguments() {
        parser.flatten(options, null, false);
    }

    /**
     * Test parsing empty arguments.
     */
    @Test
    public void testParsingEmptyArguments() {
        String[] arguments = {};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}