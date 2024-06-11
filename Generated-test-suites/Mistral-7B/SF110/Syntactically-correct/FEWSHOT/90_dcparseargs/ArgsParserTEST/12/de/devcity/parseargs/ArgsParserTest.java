package de.devcity.parseargs;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testParseSwitchArgument() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--test"});
        SwitchArgument switchArgument = ap.parseSwitchArgument("-a");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isSet());

        switchArgument = ap.parseSwitchArgument("--test");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isSet());

        ap = new ArgsParser(new String[]{"--test"});
        switchArgument = ap.parseSwitchArgument("--test");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isSet());

        ap = new ArgsParser(new String[]{"--test", "foo"});
        switchArgument = ap.parseSwitchArgument("--test");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isSet());

        StringArgument stringArgument = ap.parseStringArgument().get(0);
        assertEquals(1, stringArgument.getIndex());
        assertEquals("foo", stringArgument.getValue());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser ap = new ArgsParser(new String[]{"--name=Alice", "--age=30", "--city=New-York"});
        ParameterArgument parameterArgument1 = ap.parseParameterArgument("--name");
        assertEquals(-1, parameterArgument1.getIndex());
        assertNull(parameterArgument1.getValue());

        parameterArgument1 = ap.parseParameterArgument("--age");
        assertEquals(1, parameterArgument1.getIndex());
        assertEquals("30", parameterArgument1.getValue());

        parameterArgument1 = ap.parseParameterArgument("--city");
        assertEquals(2, parameterArgument1.getIndex());
        assertEquals("New-York", parameterArgument1.getValue());

        parameterArgument1 = ap.parseParameterArgument("--test");
        assertEquals(-1, parameterArgument1.getIndex());
        assertNull(parameterArgument1.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser ap = new ArgsParser(new String[]{"--args", "arg1", "arg2"});
        List<StringArgument> stringArguments = ap.parseStringArgument();
        assertEquals(2, stringArguments.size());

        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(0, stringArgument1.getIndex());
        assertEquals("arg1", stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(1, stringArgument2.getIndex());
        assertEquals("arg2", stringArgument2.getValue());
    }

    @Test
    public void testParseEmptyArgs() {
        ArgsParser ap = new ArgsParser(new String[0]);
        List<StringArgument> stringArguments = ap.parseStringArgument();
        assertTrue(stringArguments.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullArgs() {
        new ArgsParser(null);
    }
}