import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testSimpleOption() {
        Option option = Option.builder()
                .longOpt("input")
                .hasArg(true)
                .build();

        // Process option with valid argument
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("java", "MyClass", "--input", "test.txt").listIterator();
        option.process(commandLine, args);
        Assert.assertEquals("test.txt", commandLine.getOptionValue("input"));

        // Process option with invalid argument
        args = new ArrayList<>(Arrays.asList( "java", "MyClass", "--input")).listIterator();
        try {
            option.process(commandLine, args);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("Required option 'input' is missing", e.getMessage());
        }

        // Defaults
        WriteableCommandLine cmdDefaults = new WriteableCommandLine();
        option.defaults(cmdDefaults);
        Assert.assertNull(cmdDefaults.getOptionValue("input"));

        // Can process
        Assert.assertTrue(option.canProcess(commandLine, "--input"));
        Assert.assertFalse(option.canProcess(commandLine, ""));

        // Triggers
        Set<String> triggers = option.getTriggers();
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("input", triggers.iterator().next());

        // Validate
        commandLine = new WriteableCommandLine();
        commandLine.putString("input", "test.txt");
        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail();
        }

        // HelpLines
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = Collections.naturalOrder();
        List helpLines = option.helpLines(depth, helpSettings, comp);
        Assert.assertEquals(1, helpLines.size());
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java MyClass [options]", "", helpLines.get(0), "", "");
        Assert.assertTrue(formatter.getOutput().contains("--input"));

        // AppendUsage
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, helpSettings, comp);
        Assert.assertEquals("--input <input>\n", buffer.toString());

        // Preferred name
        String preferredName = option.getPreferredName();
        Assert.assertEquals("input", preferredName);

        // Description
        String description = option.getDescription();
        Assert.assertNotNull(description);

        // Id
        int id = option.getId();
        Assert.assertEquals(0, id);

        // Find option
        Option foundOption = option.findOption("input");
        Assert.assertEquals(option, foundOption);

        // Is required
        Assert.assertFalse(option.isRequired());

        // Parent
        Option parentOption = Option.builder()
                .longOpt("group")
                .build();
        option.setParent(parentOption);
        Option foundParentOption = option.findOption("group");
        Assert.assertEquals(parentOption, foundParentOption);
    }
}