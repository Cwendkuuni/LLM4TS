package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImplConstructor() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        assertEquals("groupName", group.getPreferredName());
        assertEquals("groupDescription", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testCanProcess() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // When
        WriteableCommandLine commandLine = new DefaultCommandLine();
        String arg = "option1";

        // Then
        assertTrue(group.canProcess(commandLine, arg));

        // When
        arg = "option3";

        // Then
        assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        Set<String> triggers = group.getTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testProcess() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("option1", true);
        commandLine.addOption("option2");

        // When
        group.process(commandLine, commandLine.iterator());

        // Then
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }

    @Test
    public void testValidate() throws OptionException {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // When
        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Then
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithLessOptionsThanMinimum() throws OptionException {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 3, 3);

        // When
        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Then
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMoreOptionsThanMaximum() throws OptionException {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 1);

        // When
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("option1");
        commandLine.addOption("option1");

        // Then
        group.validate(commandLine);
    }

    @Test
    public void testGetName() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        assertEquals("groupName", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        assertEquals("groupDescription", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        StringBuffer buffer = new StringBuffer();

        // When
        group.appendUsage(buffer, null, null);

        // Then
        String expectedUsage = "groupName:\n" +
                "description1\n" +
                "option1 <value>\n" +
                "\n" +
                "description2\n" +
                "option2 <value>\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // When
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        // Then
        List<String> expectedHelpLines = new ArrayList<>();
        expectedHelpLines.add("groupName:");
        expectedHelpLines.add("");
        expectedHelpLines.add("description1");
        expectedHelpLines.add("option1 <value>");
        expectedHelpLines.add("");
        expectedHelpLines.add("description2");
        expectedHelpLines.add("option2 <value>");
        expectedHelpLines.add("");

        for (HelpLine helpLine : helpLines) {
            expectedHelpLines.remove(helpLine.getHelp());
        }

        assertTrue(expectedHelpLines.isEmpty());
    }

    @Test
    public void testFindOption() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // When
        Option foundOption = group.findOption("option1");

        // Then
        assertNotNull(foundOption);
        assertEquals("description1", foundOption.getDescription());
    }

    @Test
    public void testGetMinimum() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        // Then
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        // Given
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("description1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("description2")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // When
        group.defaults(commandLine);

        // Then
        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
    }
}