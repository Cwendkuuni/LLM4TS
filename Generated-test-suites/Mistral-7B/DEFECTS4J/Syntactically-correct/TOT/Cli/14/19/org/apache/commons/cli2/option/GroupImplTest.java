package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws ParseException {
        // create command line parser
        final HelpFormatter formatter = new HelpFormatter();
        final CommandLineParser parser = new DefaultCommandLineParser();

        // create test options
        final Option optionA = OptionBuilder.withLongOpt("optionA")
                .withDescription("test option A")
                .build();
        final Option optionB = OptionBuilder.withLongOpt("optionB")
                .withDescription("test option B")
                .withArgName("value")
                .isRequired()
                .build();
        final Option optionC = OptionBuilder.withLongOpt("optionC")
                .withDescription("test option C")
                .build();

        // create test group
        final OptionGroup group = new OptionGroup("testGroup", "Test group");
        group.setMinimum(1);
        group.setMaximum(3);
        group.addOption(optionA);
        group.addOption(optionB);
        group.addOption(optionC);

        // create test arguments
        final String[] args = {"-testGroup", "optionA", "value1", "-testGroup", "optionB", "value2", "-testGroup"};

        // parse command line
        final CommandLine line = parser.parse(group.getOptions(), args);

        // validate parsed command line
        assertTrue(line.hasOption("testGroup"));
        assertTrue(line.hasOption("optionA"));
        assertFalse(line.hasOption("optionB", false)); // false means not checking value
        assertFalse(line.hasOption("optionC"));
        assertEquals(2, line.getOptionValues("testGroup").length);
        assertEquals("value1", line.getOptionValues("optionA")[0]);
        assertEquals("value2", line.getOptionValues("optionB")[0]);

        // test help method
        final String help = formatter.printHelp("Test", group.getOptions());
        assertTrue(help.contains("Test group:"));
        assertTrue(help.contains("test option A"));
        assertTrue(help.contains("test option B with an argument"));
        assertTrue(help.contains("test option C"));
        assertTrue(help.contains("1-3"));
        assertTrue(help.contains("The following options are required."));

        // test validate method
        final CommandLine lineInvalid = parser.parse(group.getOptions(), new String[0]);
        try {
            group.validate(lineInvalid);
            fail("validate method should have thrown an exception");
        } catch (OptionException e) {
            assertTrue(e.getMessage().contains("MISSING_OPTION"));
        }

        final CommandLine lineTooMany = parser.parse(group.getOptions(), args);
        lineTooMany.addOption(optionC);
        try {
            group.validate(lineTooMany);
            fail("validate method should have thrown an exception");
        } catch (OptionException e) {
            assertTrue(e.getMessage().contains("UNEXPECTED_TOKEN"));
        }
    }
}