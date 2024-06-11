package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpRequest;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("This is option 3")
                .build();
        Option option4 = OptionBuilder.withLongOpt("option4")
                .withDescription("This is option 4")
                .build();

        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        optionGroup = new OptionGroupImpl(options, "group", "This is the group");
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = new CommandLine();
        assertTrue(optionGroup.canProcess(commandLine, "option1"));
        assertTrue(optionGroup.canProcess(commandLine, "option2"));
        assertTrue(optionGroup.canProcess(commandLine, "option3"));
        assertTrue(optionGroup.canProcess(commandLine, "option4"));
        assertFalse(optionGroup.canProcess(commandLine, "nonexistent"));

        commandLine.parse(optionGroup.getArgs());
        assertTrue(optionGroup.canProcess(commandLine, "option1"));
        assertTrue(optionGroup.canProcess(commandLine, "option2"));
        assertTrue(optionGroup.canProcess(commandLine, "option3"));
        assertTrue(optionGroup.canProcess(commandLine, "option4"));
        assertFalse(optionGroup.canProcess(commandLine, "nonexistent"));
    }

    @Test
    public void testProcess() {
        CommandLine commandLine = new CommandLine();

        optionGroup.process(commandLine, commandLine.iterator());

        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("option3"));
        assertNull(commandLine.getOptionValue("option4"));

        // Set values for options
        commandLine.parse(Collections.singletonList("-g"));
        commandLine.parse(Collections.singletonList("-g"));
        commandLine.parse(Collections.singletonList("-g"));
        commandLine.parse(Collections.singletonList("option1"));
        commandLine.parse(Collections.singletonList("option2"));
        commandLine.parse(Collections.singletonList("option3"));

        Option option1 = optionGroup.findOption("option1");
        Option option2 = optionGroup.findOption("option2");
        Option option3 = optionGroup.findOption("option3");

        assertEquals("value1", option1.getValue(commandLine));
        assertEquals("value2", option2.getValue(commandLine));
        assertEquals("value3", option3.getValue(commandLine));
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = new CommandLine();

        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("This is option 1")
                .build());
        OptionGroup optionGroup = new OptionGroupImpl(options, "group", "This is the group");

        try {
            optionGroup.validate(commandLine);
        } catch (OptionException e) {
            // expected
        }

        commandLine.parse(Collections.singletonList("-g"));
        commandLine.parse(Collections.singletonList("-g"));

        optionGroup.validate(commandLine);

        commandLine.parse(Collections.singletonList("-g"));
        commandLine.parse(Collections.singletonList("option1"));

        optionGroup.validate(commandLine);

        optionGroup.validate(commandLine); // should not throw an exception

        commandLine.parse(Collections.singletonList("option1"));
        commandLine.parse(Collections.singletonList("option1"));

        try {
            optionGroup.validate(commandLine);
            fail("OptionException expected");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        final HelpFormatter formatter = new HelpFormatter();

        optionGroup.appendUsage(buffer, null, null);
        final String usage = buffer.toString();

        HelpRequest helpRequest = new HelpRequest(
                new String[]{"Test"},
                new String[0],
                null,
                null,
                HelpFormatter.DEFAULT_WIDTH,
                new String[0]
        );

        formatter.printHelp(System.out, usage, helpRequest);
    }
}