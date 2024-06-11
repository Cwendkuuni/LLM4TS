package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("testOption", "Test Option", true);
        commandLine.addOption(option);
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    @Test
    public void testAddOptionWithEmptyArgumentList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("testOption", "Test Option", true);
        commandLine.addOption(option);
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithExistingOptionTrigger() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testExisting"), new ArrayList<>());
        Option option = new Option("testExisting", "Test Existing Option", false);
        commandLine.addOption(option);
    }

    @Test
    public void testAddOptionWithMultipleTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("testOption", "Test Option 1", true);
        option.addTrigger("testOption2");
        commandLine.addOption(option);
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        commandLine.addOption(null);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("testOption", "Test Option", true);
        commandLine.addOption(option);
        String expected = "testOption";
        String actual = commandLine.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option1 = new Option("testOption1", "Test Option 1", true);
        Option option2 = new Option("testOption2", "Test Option 2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> expected = Arrays.asList(option1, option2);
        List<Option> actual = commandLine.getOptions();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("testOption", "Test Option", true);
        commandLine.addOption(option);
        Set<String> expected = Collections.singleton("testOption");
        Set<String> actual = commandLine.getOptionTriggers();
        assertEquals(expected, actual);
    }
}