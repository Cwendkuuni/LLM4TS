package de.devcity.parseargs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParserCreation() {
        ArgsParser argsParser = new ArgsParser();
        ArgsParser argsParserWithArgs = new ArgsParser(new String[] {});
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserNullConstructor() {
        new ArgsParser(null);
    }

    @Test
    public void testArgsParserGetters() {
        String[] args = new String[] { "-a", "-b", "--c", "d" };
        ArgsParser argsParser = new ArgsParser(args);

        assertEquals(4, argsParser.getArgsCount());
        assertArrayEquals(args, argsParser.getArgs());

        argsParser.setArgs(new String[] { "e", "f" });
        assertEquals(2, argsParser.getArgsCount());
        assertArrayEquals(new String[] { "e", "f" }, argsParser.getArgs());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = new String[] { "-a", "--b", "-c", "-d" };

        ArgsParser argsParser = new ArgsParser(args);

        SwitchArgument switchArgA = argsParser.parseSwitchArgument("a");
        SwitchArgument switchArgB = argsParser.parseSwitchArgument("b");
        SwitchArgument switchArgC = argsParser.parseSwitchArgument("c");
        SwitchArgument switchArgD = argsParser.parseSwitchArgument("d");
        SwitchArgument switchArgE = argsParser.parseSwitchArgument("e");

        assertEquals(0, switchArgA.getIndex());
        assertTrue(switchArgA.isLongKey());
        assertFalse(switchArgA.hasValue());

        assertEquals(1, switchArgB.getIndex());
        assertFalse(switchArgB.isLongKey());
        assertFalse(switchArgB.hasValue());

        assertEquals(2, switchArgC.getIndex());
        assertTrue(switchArgC.isLongKey());
        assertFalse(switchArgC.hasValue());

        assertEquals(-1, switchArgD.getIndex());
        assertFalse(switchArgD.isLongKey());
        assertFalse(switchArgD.hasValue());

        assertEquals(-1, switchArgE.getIndex());
        assertFalse(switchArgE.isLongKey());
        assertFalse(switchArgE.hasValue());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = new String[] { "-a=1", "--b=2", "-c=3", "-d=4" };

        ArgsParser argsParser = new ArgsParser(args);

        ParameterArgument paramArgA = argsParser.parseParameterArgument("a");
        ParameterArgument paramArgB = argsParser.parseParameterArgument("b");
        ParameterArgument paramArgC = argsParser.parseParameterArgument("c");
        ParameterArgument paramArgD = argsParser.parseParameterArgument("d");

        assertEquals(0, paramArgA.getIndex());
        assertFalse(paramArgA.isLongKey());
        assertEquals("1", paramArgA.getValue());

        assertEquals(2, paramArgB.getIndex());
        assertFalse(paramArgB.isLongKey());
        assertEquals("2", paramArgB.getValue());

        assertEquals(1, paramArgC.getIndex());
        assertFalse(paramArgC.isLongKey());
        assertEquals("3", paramArgC.getValue());

        assertEquals(-1, paramArgD.getIndex());
        assertFalse(paramArgD.isLongKey());
        assertNull(paramArgD.getValue());
    }

    @Test
    public void testParseStringArguments() {
        String[] args = new String[] { "-a=1", "--b=2", "-c=\"foo bar\"", "-d=\"baz qux\"" };

        ArgsParser argsParser = new ArgsParser(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();

        assertEquals(2, stringArguments.size());
        assertEquals(1, stringArguments.get(0).getIndex());
        assertEquals("foo bar", stringArguments.get(0).getValue());
        assertEquals(3, stringArguments.get(1).getIndex());
        assertEquals("baz qux", stringArguments.get(1).getValue());
    }

    @Test
    public void testParseEmptyArgs() {
        ArgsParser argsParser = new ArgsParser();
        assertNull(argsParser.parseSwitchArgument("a"));
        assertNull(argsParser.parseParameterArgument("a"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseSwitchArgumentOutOfBounds() {
        String[] args = new String[] { "-a" };
        ArgsParser argsParser = new ArgsParser(args);
        argsParser.parseSwitchArgument("b");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseParameterArgumentOutOfBounds() {
        String[] args = new String[] { "-a=1" };
        ArgsParser argsParser = new ArgsParser(args);
        argsParser.parseParameterArgument("b");
    }
}