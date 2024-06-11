package de.devcity.parseargs;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--long-a", "--no-b"});
        SwitchArgument switchArgA = parser.parseSwitchArgument("a");
        assertTrue(switchArgA.isPresent());
        assertEquals("a", switchArgA.getKey());
        assertEquals(0, switchArgA.getIndex());

        SwitchArgument switchArgB = parser.parseSwitchArgument("b");
        assertFalse(switchArgB.isPresent());
        assertEquals(-1, switchArgB.getIndex());

        SwitchArgument switchArgLongA = parser.parseSwitchArgument("long-a");
        assertTrue(switchArgLongA.isPresent());
        assertEquals("long-a", switchArgLongA.getKey());
        assertEquals(1, switchArgLongA.getIndex());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--foo=bar", "-b", "--baz=qux"});
        ParameterArgument paramArgFoo = parser.parseParameterArgument("foo");
        assertTrue(paramArgFoo.isPresent());
        assertEquals("foo", paramArgFoo.getKey());
        assertEquals(1, paramArgFoo.getIndex());
        assertEquals("bar", paramArgFoo.getValue());

        ParameterArgument paramArgBaz = parser.parseParameterArgument("baz");
        assertTrue(paramArgBaz.isPresent());
        assertEquals("baz", paramArgBaz.getKey());
        assertEquals(3, paramArgBaz.getIndex());
        assertEquals("qux", paramArgBaz.getValue());

        ParameterArgument paramArgUnknown = parser.parseParameterArgument("unknown");
        assertFalse(paramArgUnknown.isPresent());
        assertEquals(-1, paramArgUnknown.getIndex());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--foo=bar", "-b", "--baz=qux", "-c", "argC", "--d", "argD"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(4, stringArgs.size());
        assertEquals("argC", stringArgs.get(2).getValue());
        assertEquals("argD", stringArgs.get(3).getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testParseArgumentsWithNullArgs() {
        new ArgsParser(null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseArgumentsWithNullInnerArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"-a"});
        parser.setArgs(null);
    }
}