import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Create a list of options
        final List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("a", "a", false, "An option with triggers a and b"));
        options.add(TestUtils.createOption("b", "b", false, "An option with triggers b and c"));

        // Create a group
        final Group group = new GroupImpl(options, "myGroup", "My group", 1, 2);

        // Test name
        assertEquals("myGroup", group.getPreferredName());

        // Test description
        assertNull(group.getDescription());

        // Test options
        assertEquals(2, group.getOptions().size());
        assertEquals(TestUtils.createOption("a", "a", false, "An option with triggers a and b"), group.findOption("a"));
        assertEquals(TestUtils.createOption("b", "b", false, "An option with triggers b and c"), group.findOption("b"));

        // Test anonymous options
        assertNull(group.getAnonymous());

        // Test minimum
        assertEquals(1, group.getMinimum());

        // Test maximum
        assertEquals(2, group.getMaximum());

        // Test canProcess
        final CommandLine commandLine = new CommandLine();
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "d"));

        // Test validate
        commandLine.parse(group.getOptions().toArray(new String[0]), true);
        assertFalse(commandLine.hasUnrecognizedOption("x"));
        assertFalse(commandLine.hasMissingOption("a"));
        assertFalse(commandLine.hasMissingOption("b"));
        commandLine.clear();

        commandLine.parse(group.getOptions().toArray(new String[0]), true);
        commandLine.setOptionValue("a", "valueA");
        assertFalse(commandLine.hasMissingOption("a"));
        assertFalse(commandLine.hasMissingOption("b"));

        commandLine.parse(group.getOptions().toArray(new String[0]), true);
        commandLine.setOptionValue("a", "valueA");
        commandLine.setOptionValue("b", "valueB");
        assertFalse(commandLine.hasMissingOption("a"));
        assertFalse(commandLine.hasMissingOption("b"));

        commandLine.parse(group.getOptions().toArray(new String[0]), true);
        commandLine.setOptionValue("a", "valueA");
        commandLine.setOptionValue("b", "valueB");
        commandLine.setOptionValue("c", "valueC");
        assertTrue(commandLine.hasOptionValue("a"));
        assertTrue(commandLine.hasOptionValue("b"));
        assertTrue(commandLine.hasOptionValue("c"));

        commandLine.clear();
        commandLine.parse(group.getOptions().toArray(new String[0]), true);
        commandLine.setOptionValue("a", "valueA");
        commandLine.setOptionValue("b", "valueB");
        assertFalse(commandLine.hasOptionValue("c"));

        // Test process
        final List<String> args = new ArrayList<>();
        args.add("a");
        args.add("valueA");
        args.add("b");
        args.add("valueB");
        group.process(commandLine, args.listIterator());
        assertTrue(commandLine.hasOptionValue("a"));
        assertTrue(commandLine.hasOptionValue("b"));

        // Test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        assertEquals("myGroup (a [An option with triggers a and b] [b An option with triggers b and c])", sb.toString());

        // Test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        assertEquals(" myGroup:\n" +
                "  (a)An option with triggers a and b\n" +
                "  (b)An option with triggers b and c\n", helpLines.get(0).getHelp());
    }
}