import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Given
        final List options = new ArrayList();
        options.add(createOption("opt1", "o1", false));
        options.add(createOption("opt2", "o2", false));
        options.add(createOption("opt3", "o3", false));
        options.add(createArgument("arg1", "a1", true));
        options.add(createArgument("arg2", "a2", true));

        final Group group = new GroupImpl(options, null, null, 1, 1);

        // When

        // Can process
        WriteableCommandLine commandLine = new WriteableCommandLine("");
        Assert.assertTrue(group.canProcess(commandLine, "opt1"));
        Assert.assertTrue(group.canProcess(commandLine, "opt2"));
        Assert.assertTrue(group.canProcess(commandLine, "opt3"));
        Assert.assertTrue(group.canProcess(commandLine, "arg1"));
        Assert.assertTrue(group.canProcess(commandLine, "arg2"));

        // GetPrefixes
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());

        // GetTriggers
        Assert.assertEquals(Collections.singleton("opt1"), group.getTriggers());
        Assert.assertEquals(Collections.singleton("opt2"), group.getTriggers());
        Assert.assertEquals(Collections.singleton("opt3"), group.getTriggers());

        // process
        commandLine = new WriteableCommandLine("");
        ListIterator arguments = commandLine.getParsed();
        group.process(commandLine, arguments);
        Assert.assertEquals(1, arguments.nextInt()); // opt1 index

        // Validate
        commandLine = new WriteableCommandLine("");
        group.validate(commandLine);

        // GetPreferredName
        Assert.assertNull(group.getPreferredName());

        // GetDescription
        Assert.assertNull(group.getDescription());

        // appendUsage
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        Assert.assertEquals("+ opt1 (o1)\n"+
                "+ opt2 (o2)\n"+
                "+ opt3 (o3)\n"+
                "+ arg1 (a1)\n"+
                "+ arg2 (a2)", buffer.toString());

        // helpLines
        List helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(6, helpLines.size());

        // GetOptions
        List optionsList = group.getOptions();
        Assert.assertEquals(3, optionsList.size());
        Assert.assertEquals("opt1", optionsList.get(0).getPreferredName());
        Assert.assertEquals("opt2", optionsList.get(1).getPreferredName());
        Assert.assertEquals("opt3", optionsList.get(2).getPreferredName());

        // GetAnonymous
        List anonymous = group.getAnonymous();
        Assert.assertEquals(2, anonymous.size());
        Assert.assertEquals("arg1", anonymous.get(0).getPreferredName());
        Assert.assertEquals("arg2", anonymous.get(1).getPreferredName());

        // FindOption
        Option option = group.findOption("opt1");
        Assert.assertNotNull(option);

        // GetMinimum
        Assert.assertEquals(1, group.getMinimum());

        // GetMaximum
        Assert.assertEquals(1, group.getMaximum());

        // IsRequired
        Assert.assertFalse(group.isRequired());

        // Defaults
        commandLine = new WriteableCommandLine("");
        group.defaults(commandLine);
        Assert.assertFalse(commandLine.hasOption("opt1"));
        Assert.assertFalse(commandLine.hasOption("opt2"));
        Assert.assertFalse(commandLine.hasOption("opt3"));
        Assert.assertFalse(commandLine.hasOption("arg1"));
        Assert.assertFalse(commandLine.hasOption("arg2"));
    }

    private Option createOption(final String name, final String description, final boolean required) {
        return new OptionBuilder()
                .withLongOpt(name)
                .withDescription(description)
                .isRequired(required)
                .build();
    }

    private Argument createArgument(final String name, final String description, final boolean required) {
        return new ArgumentBuilder()
                .withLongOpt(name)
                .withDescription(description)
                .isRequired(required)
                .build();
    }
}