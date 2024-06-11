import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.Default;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.cli2.CommandLineParser.DEFAULT;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final String groupName = "testGroup";
        final String groupDescription = "Test group description.";
        final int groupMinimum = 1;
        final int groupMaximum = 3;

        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.newOption("option1", "Option 1")
                .required(false)
                .build());
        groupOptions.add(OptionBuilder.newOption("option2", "Option 2")
                .required(false)
                .build());

        final Option option3 = OptionBuilder.newOption("option3", "Option 3")
                .required(false)
                .build();
        groupOptions.add(option3);

        final Group group = new GroupImpl(groupOptions, groupName, groupDescription, groupMinimum, groupMaximum);

        // Test canProcess
        final CommandLine commandLine = new CommandLine();
        Assert.assertTrue(group.canProcess(commandLine, "option1"));
        Assert.assertTrue(group.canProcess(commandLine, "option2"));
        Assert.assertTrue(group.canProcess(commandLine, "option3"));
        Assert.assertFalse(group.canProcess(commandLine, "nonOption"));

        // Test getPrefixes
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());

        // Test getTriggers
        Assert.assertEquals(groupOptions, group.getTriggers());

        // Test process
        final List<String> arguments = new ArrayList<>();
        arguments.add("testCommand");
        commandLine.parse(DEFAULT, group, arguments, true);

        Assert.assertFalse(commandLine.hasOption("testCommand"));
        Assert.assertEquals(group.getMinimum(), commandLine.numOptions());

        Assert.assertEquals("option1", commandLine.getOptionValues("option1").get(0));
        Assert.assertEquals("option2", commandLine.getOptionValues("option2").get(0));
        Assert.assertEquals("option3", commandLine.getOptionValues("option3").get(0));

        // Test validate
        commandLine.clearParsed();
        commandLine.parse(DEFAULT, group, arguments, true);

        Assert.assertEquals(group.getMinimum(), commandLine.numOptions());

        // Test getPreferredName
        Assert.assertEquals(groupName, group.getPreferredName());

        // Test getDescription
        Assert.assertEquals(groupDescription, group.getDescription());

        // Test appendUsage
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        Assert.assertEquals("testGroup:\n" +
                "Options:\n" +
                "========\n" +
                "option1\n" +
                "option2\n" +
                "option3...\n", buffer.toString());

        // Test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(4, helpLines.size());
        Assert.assertEquals("testGroup", helpLines.get(0).getHeadText());
        Assert.assertEquals("Options:", helpLines.get(1).getHeadText());
        Assert.assertEquals("option1", helpLines.get(2).getOptionSpec().getLongOpt());
        Assert.assertEquals("Option 1", helpLines.get(2).getOptionSpec().getOpt());
        Assert.assertEquals("", helpLines.get(2).getDescription());
        Assert.assertEquals("option2", helpLines.get(3).getOptionSpec().getLongOpt());
        Assert.assertEquals("Option 2", helpLines.get(3).getOptionSpec().getOpt());
        Assert.assertEquals("", helpLines.get(3).getDescription());

        // Test getOptions
        Assert.assertEquals(groupOptions, group.getOptions());

        // Test getAnonymous
        Assert.assertEquals(Collections.emptyList(), group.getAnonymous());

        // Test findOption
        Assert.assertNull(group.findOption("nonOption"));
        Assert.assertEquals(option3, group.findOption("option3"));

        // Test isRequired
        Assert.assertTrue(group.isRequired());

        // Test defaults
        commandLine.clearParsed();
        group.defaults(commandLine);
        Assert.assertFalse(commandLine.hasOption("option1"));
        Assert.assertFalse(commandLine.hasOption("option2"));
        Assert.assertFalse(commandLine.hasOption("option3"));
    }
}