package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option-a", true, "this is option a"));
        options.add(new Option("b", "option-b", false, "this is option b"));
        options.add(new Option("c", "option-c", false, "this is option c"));
        options.add(new Option("d", "option-d", true, "this is option d"));
        options.add(new Option("e", "option-e", false, "this is option e"));
        options.add(new Option("f", "option-f", false, "this is option f"));

        List anonymous = new ArrayList<>();
        anonymous.add(new Argument(0, "arg1", true, "argument 1"));
        anonymous.add(new Argument(1, "arg2", true, "argument 2"));

        Group group = new GroupImpl(options, "group", "This is a test group", 1, 5, anonymous, null, null);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        // test canProcess method
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertTrue(group.canProcess(commandLine, "d"));
        assertTrue(group.canProcess(commandLine, "e"));
        assertTrue(group.canProcess(commandLine, "f"));

        // test getPrefixes method
        Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);

        // test getTriggers method
        Set triggers = group.getTriggers();
        assertEquals(options, triggers);

        // test process method
        commandLine.parse("--a --b --c --d --e --f arg1 arg2");
        group.process(commandLine, commandLine.getArgList().listIterator());

        assertEquals(5, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArg(0));
        assertEquals("arg2", commandLine.getArg(1));
        assertEquals("arg1", commandLine.getArg(4));
        assertEquals("arg2", commandLine.getArg(5));

        // test validate method
        commandLine.clear();
        commandLine.parse("--a --b --c --d");
        group.validate(commandLine);
        assertEquals(5, commandLine.getArgs().size());

        commandLine.clear();
        commandLine.parse("--a --d");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option: b, c, e, or f", e.getMessage());
        }

        commandLine.clear();
        commandLine.parse("--a --b --c --d --e --f arg1 arg2 arg3");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many arguments: 3", e.getMessage());
        }

        // test getPreferredName method
        assertEquals("group", group.getPreferredName());

        // test getDescription method
        assertEquals("This is a test group", group.getDescription());

        // test appendUsage method
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals("group (options):\n\n" +
                "  option-a: this is option a [boolean]\n" +
                "  option-b: this is option b [no value]\n" +
                "  option-c: this is option c [no value]\n" +
                "  option-d: this is option d [boolean]\n" +
                "  option-e: this is option e [no value]\n" +
                "  option-f: this is option f [no value]\n", buffer.toString());

        // test helpLines method
        List helpLines = group.helpLines(0, null, null);
        assertEquals(6, helpLines.size());
        assertEquals("group (options):\n\n" +
                "  option-a: this is option a [boolean]\n" +
                "  option-b: this is option b [no value]\n" +
                "  option-c: this is option c [no value]\n" +
                "  option-d: this is option d [boolean]\n" +
                "  option-e: this is option e [no value]\n" +
                "  option-f: this is option f [no value]\n", helpLines.get(0).getHelp());

        // test getOptions method
        List optionsList = group.getOptions();
        assertEquals(options, optionsList);

        // test getAnonymous method
        List anonymousList = group.getAnonymous();
        assertEquals(anonymous, anonymousList);

        // test findOption method
        Option optionA = group.findOption("a");
        assertEquals(optionA, optionA);

        // test getMinimum and isRequired methods
        assertEquals(1, group.getMinimum());
        assertTrue(group.isRequired());

        // test getMaximum method
        assertEquals(5, group.getMaximum());

        // test defaults method
        commandLine.clear();
        group.defaults(commandLine);
        assertFalse(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
        assertFalse(commandLine.hasOption("d"));
        assertFalse(commandLine.hasOption("e"));
        assertFalse(commandLine.hasOption("f"));

        commandLine.parse("--a --b --c --d --e --f arg1 arg2 arg3");
        group.defaults(commandLine);
        assertTrue(commandLine.hasOption("a", true));
        assertTrue(commandLine.hasOption("b", false));
        assertTrue(commandLine.hasOption("c", false));
        assertTrue(commandLine.hasOption("d", true));
        assertTrue(commandLine.hasOption("e", false));
        assertTrue(commandLine.hasOption("f", false));
        assertEquals("arg1", commandLine.getArg(0));
        assertEquals("arg2", commandLine.getArg(1));
        assertEquals("arg3", commandLine.getArg(5));
    }
}