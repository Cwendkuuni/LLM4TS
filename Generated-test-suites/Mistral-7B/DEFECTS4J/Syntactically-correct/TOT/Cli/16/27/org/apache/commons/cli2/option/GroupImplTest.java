package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
        Group group = new GroupImpl( Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("This is a test group")
                        .createOption(),
                OptionBuilder.withLongOpt("group1.option1")
                        .withDescription("This is a test group option")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Another test group")
                        .withMinValues(1)
                        .withMaxValues(3)
                        .createOption(),
                OptionBuilder.withLongOpt("group2.option1")
                        .withDescription("Another test group option")
                        .createOption(),
                OptionBuilder.withLongOpt("group2.option2")
                        .withDescription("Another test group option")
                        .createOption(),
                OptionBuilder.withLongOpt("arg")
                        .withDescription("A test argument")
                        .createOption()
        ), "group", "A test group");
        group.setPrefixes(new HashSet<String>() {{
            add("--");
            add("-g");
        }});
    }

    @Test
    public void testProcessWithValidOptions() throws Exception {
        String[] args = {"--group", "--group1", "--group1.option1"};
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parseOptions(options, args, false);
        assertFalse(commandLine.hasOption("help"));
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("group1"));
        assertTrue(commandLine.hasOption("group1.option1"));
        assertFalse(commandLine.hasOption("group2"));
        assertFalse(commandLine.hasOption("arg"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidNumberOfOptions() throws Exception {
        String[] args = {"--group", "--group1", "--group1.option1", "--group1.option1"};
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parseOptions(options, args, false);
        fail();
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() throws Exception {
        String[] args = {"--group", "--group1.invalidOption"};
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parseOptions(options, args, false);
        fail();
    }

    @Test
    public void testValidate() throws Exception {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        options.validate(commandLine);
        assertTrue(commandLine.hasOption("help"));
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("group1"));
        assertTrue(commandLine.hasOption("group1.option1"));
        assertFalse(commandLine.hasOption("group2"));
        assertFalse(commandLine.hasOption("arg"));
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);

        List<String> lines = new ArrayList<>(Arrays.asList(formatter.getHelp().split("\\r\\n")));
        assertTrue(lines.contains("A test group"));
        assertTrue(lines.contains("--group1"));
        assertTrue(lines.contains("An option for group1"));
        assertTrue(lines.contains("--group2"));
        assertTrue(lines.contains("This is another test group"));
        assertTrue(lines.contains("--arg"));
        assertTrue(lines.contains("A test argument"));
    }
}