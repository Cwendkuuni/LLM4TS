import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2. validation.URLValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the PatternBuilder class
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    /**
     * Test pattern for an optional argument with no validator
     */
    @Test
    public void testOptionString() {
        final String pattern = "s:<file>";
        patternBuilder.withPattern(pattern);
        final Option option = patternBuilder.create();

        // typical use case
        final CommandLineParser parser = new CommandLineParser();
        final String[] args = new String[]{"-s", "/path/to/file"};
        assertFalse(parser.parse(option, args).hasErrors());
        assertSame("/path/to/file", option.getValue());

        // edge case: empty argument
        final String[] argsEmpty = new String[0];
        assertFalse(parser.parse(option, argsEmpty).hasErrors());
        assertNull(option.getValue());

        // error case: invalid argument
        final String[] argsInvalid = new String[]{"-s", "not_a_file"};
        assertTrue(parser.parse(option, argsInvalid).hasErrors());
    }

    /**
     * Test pattern for an optional argument with a validator
     */
    @Test
    public void testOptionStringURL() {
        final String pattern = "u:";
        patternBuilder.withPattern(pattern);
        final Option option = patternBuilder.create();

        // typical use case
        final CommandLineParser parser = new CommandLineParser();
        final String[] args = new String[]{"-u", "http://example.com"};
        assertFalse(parser.parse(option, args).hasErrors());
        assertSame("http://example.com", option.getValue());

        // edge case: empty argument
        final String[] argsEmpty = new String[0];
        assertFalse(parser.parse(option, argsEmpty).hasErrors());
        assertNull(option.getValue());

        // error case: invalid argument
        final String[] argsInvalid = new String[]{"-u", "not_a_url"};
        assertTrue(parser.parse(option, argsInvalid).hasErrors());
    }

    /**
     * Test pattern with multiple options
     */
    @Test
    public void testOptions() {
        final String pattern = "s:<file>,u:,i:";
        patternBuilder.withPattern(pattern);
        final CommandLineParser parser = new CommandLineParser();
        final String[] args = new String[]{"-s", "/path/to/file", "--i", "3"};

        // typical use case
        assertFalse(parser.parse(patternBuilder.create(), args).hasErrors());
        assertSame("/path/to/file", patternBuilder.getOptions().get(0).getValue());
        assertSame(3, patternBuilder.getOptions().get(1).getValue());
    }

    /**
     * Test pattern with a group
     */
    @Test
    public void testGroup() {
        final String pattern = "p:<pattern>:{o:<option>}";
        patternBuilder.withPattern(pattern);
        final CommandLineParser parser = new CommandLineParser();
        final String[] args = new String[]{"-p", "test.*", "--o", "i"};

        // typical use case
        assertFalse(parser.parse(patternBuilder.create(), args).hasErrors());
        assertSame("test.*", patternBuilder.getOptions().get(0).getOptions().get(0).getValue());
        assertSame("i", patternBuilder.getOptions().get(0).getOptions().get(1).getValue());
    }
}