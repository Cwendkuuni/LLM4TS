package org.apache.commons.cli2.test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testValidArgument() {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, '.', ' ', null, null, Collections.singletonList("value"), 0);
        options.addArgument(arg);

        String[] args = {"--arg", "value"};

        try {
            parser.parse(args, options);
            Assert.assertEquals("value", options.getArgumentValue("arg"));
        } catch (ParseException e) {
            Assert.fail("Unexpected error parsing command-line arguments: " + e.getMessage());
        }
    }

    @Test
    public void testValidArgumentMultipleValues() {
        Argument arg = new ArgumentImpl("arg", "description", 1, 3, '.', ' ', null, null, Collections.emptyList(), 0);
        options.addArgument(arg);

        String[] args = {"--arg", "value1", "value2", "value3"};

        try {
            parser.parse(args, options);
            Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, options.getValues("arg").toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail("Unexpected error parsing command-line arguments: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgumentMinimum() {
        Argument arg = new ArgumentImpl("arg", "description", 2, 1, '.', ' ', null, null, Collections.emptyList(), 0);
        options.addArgument(arg);

        String[] args = {"--arg"};
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgumentMaximum() {
        Argument arg = new ArgumentImpl("arg", "description", 1, 0, '.', ' ', null, null, Collections.emptyList(), 0);
        options.addArgument(arg);

        String[] args = {"--arg", "value"};
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgumentSeparator() {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, ':', ' ', null, null, Collections.emptyList(), 0);
        options.addArgument(arg);

        String[] args = {"--arg:value"};
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgumentConsumeRemaining() {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, '.', ' ', null, "--", Collections.emptyList(), 0);
        options.addArgument(arg);

        String[] args = {"--arg", "--consume"};
    }

    @Test
    public void testHelp() {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, '.', ' ', null, null, Collections.emptyList(), 0);
        options.addArgument(arg);

        String help = new HelpFormatter().printHelp("test", options);
        Assert.assertNotNull(help);
    }
}