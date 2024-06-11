package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Prepare test data
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));
        options.add(new Option("c", "optionC", false, "Description for option C"));
        options.add(new Option("d", "optionD", true, "Description for option D"));
        options.add(new Option("e", "optionE", false, "Description for option E"));
        options.add(new Option("f", "optionF", false, "Description for option F"));

        List anonymous = new ArrayList<>();
        anonymous.add(new Option("g", "argumentG", true, "Description for argument G"));

        GroupImpl group = new GroupImpl(options, "group", "Description for group", 1, 3, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test canProcess method
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "a"));
        assertFalse(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "ab"));
        assertFalse(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "cd"));
        assertFalse(group.canProcess(commandLine, "def"));
        assertFalse(group.canProcess(commandLine, "efg"));
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "ab"));
        assertTrue(group.canProcess(commandLine, "aab"));
        assertTrue(group.canProcess(commandLine, "abc"));
        assertTrue(group.canProcess(commandLine, "abcd"));
        assertTrue(group.canProcess(commandLine, "abcde"));
        assertTrue(group.canProcess(commandLine, "abcdefg"));
        assertFalse(group.canProcess(commandLine, "abcdef"));
        assertFalse(group.canProcess(commandLine, "abcdefgx"));
        assertTrue(group.canProcess(commandLine, "ag"));
        assertTrue(group.canProcess(commandLine, "ag"));
        assertFalse(group.canProcess(commandLine, "h"));

        // Test getPrefixes method
        Set prefixes = group.getPrefixes();
        assertEquals(0, prefixes.size());

        // Test getTriggers method
        Set triggers = group.getTriggers();
        assertEquals(6, triggers.size());
        assertTrue(triggers.contains("a"));
        assertTrue(triggers.contains("b"));
        assertTrue(triggers.contains("c"));
        assertTrue(triggers.contains("d"));
        assertTrue(triggers.contains("e"));
        assertTrue(triggers.contains("f"));

        // Test process method
        group.process(commandLine, commandLine.listIterator());
        assertEquals(0, commandLine.getValuesForOption("a").size());
        assertEquals(0, commandLine.getValuesForOption("b").size());
        assertEquals(0, commandLine.getValuesForOption("c").size());
        assertEquals(0, commandLine.getValuesForOption("d").size());
        assertEquals(0, commandLine.getValuesForOption("e").size());
        assertEquals(0, commandLine.getValuesForOption("f").size());
        assertTrue(commandLine.hasOption("g"));
        assertTrue(commandLine.getValuesForOption("g").size() > 0);

        // Test validate method
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }

        // Test getPreferredName method
        assertEquals("group", group.getPreferredName());

        // Test getDescription method
        assertEquals("Description for group", group.getDescription());

        // Test appendUsage method
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        String usage = sb.toString();
        assertTrue(usage.contains("group"));
        assertTrue(usage.contains("Description for group"));
        assertTrue(usage.contains("optionA"));
        assertTrue(usage.contains("optionA: true"));
        assertTrue(usage.contains("optionB"));
        assertTrue(usage.contains("optionB: false"));
        assertTrue(usage.contains("optionC"));
        assertTrue(usage.contains("optionC: false"));
        assertTrue(usage.contains("optionD"));
        assertTrue(usage.contains("optionD: true"));
        assertTrue(usage.contains("optionE"));
        assertTrue(usage.contains("optionE: false"));
        assertTrue(usage.contains("optionF"));
        assertTrue(usage.contains("optionF: false"));
        assertTrue(usage.contains("argumentG"));
        assertTrue(usage.contains("argumentG: true"));

        // Test helpLines method
        List<String> helpLines = group.helpLines(0, null, null);
        assertEquals(11, helpLines.size());
        String firstHelpLine = (String) helpLines.get(0);
        assertTrue(firstHelpLine.contains("group"));
        assertTrue(firstHelpLine.contains("Description for group"));
        String thirdHelpLine = (String) helpLines.get(2);
        assertTrue(thirdHelpLine.contains("optionA"));
        assertTrue(thirdHelpLine.contains("optionA: true"));
        String sixthHelpLine = (String) helpLines.get(5);
        assertTrue(sixthHelpLine.contains("optionC"));
        assertTrue(sixthHelpLine.contains("optionC: false"));
        String seventhHelpLine = (String) helpLines.get(6);
        assertTrue(seventhHelpLine.contains("optionD"));
        assertTrue(seventhHelpLine.contains("optionD: true"));
        String tenthHelpLine = (String) helpLines.get(9);
        assertTrue(tenthHelpLine.contains("argumentG"));
        assertTrue(tenthHelpLine.contains("argumentG: true"));

        // Test getOptions method
        List optionsList = group.getOptions();
        assertEquals(options.size(), optionsList.size());
        for (int i = 0; i < options.size(); i++) {
            Option option = (Option) options.get(i);
            Option testOption = (Option) optionsList.get(i);
            assertEquals(option.getLongOpt(), testOption.getLongOpt());
            assertEquals(option.getName(), testOption.getName());
            assertEquals(option.getShortOpt(), testOption.getShortOpt());
            assertEquals(option.getDescription(), testOption.getDescription());
            assertEquals(option.isRequired(), testOption.isRequired());
        }

        // Test getAnonymous method
        List anonymousList = group.getAnonymous();
        assertEquals(anonymous.size(), anonymousList.size());
        for (int i = 0; i < anonymous.size(); i++) {
            Option option = (Option) anonymous.get(i);
            Option testOption = (Option) anonymousList.get(i);
            assertEquals(option.getLongOpt(), testOption.getLongOpt());
            assertEquals(option.getName(), testOption.getName());
            assertEquals(option.getShortOpt(), testOption.getShortOpt());
            assertEquals(option.getDescription(), testOption.getDescription());
            assertEquals(option.isRequired(), testOption.isRequired());
        }

        // Test findOption method
        Option foundOption = group.findOption("a");
        assertEquals(options.get(0), foundOption);

        // Test getMinimum method
        assertEquals(1, group.getMinimum());

        // Test getMaximum method
        assertEquals(3, group.getMaximum());

        // Test isRequired method
        assertFalse(group.isRequired());
    }
}