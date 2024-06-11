package de.devcity.parseargs;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--long-a"});
        SwitchArgument switchArgA = parser.parseSwitchArgument("a");
        assertEquals(0, switchArgA.getIndex());
        assertTrue(switchArgA.isLong());

        switchArgA = parser.parseSwitchArgument("long-a");
        assertEquals(0, switchArgA.getIndex());
        assertTrue(switchArgA.isLong());

        switchArgA = parser.parseSwitchArgument("b");
        assertEquals(-1, switchArgA.getIndex());
        assertFalse(switchArgA.isLong());

        switchArgA = parser.parseSwitchArgument("long-b");
        assertEquals(-1, switchArgA.getIndex());
        assertFalse(switchArgA.isLong());

        parser = new ArgsParser(new String[]{"--a", "--long-a", "b"});
        switchArgA = parser.parseSwitchArgument("a");
        assertEquals(0, switchArgA.getIndex());
        assertTrue(switchArgA.isLong());

        switchArgA = parser.parseSwitchArgument("long-a");
        assertEquals(0, switchArgA.getIndex());
        assertTrue(switchArgA.isLong());

        switchArgA = parser.parseSwitchArgument("b");
        assertEquals(1, switchArgA.getIndex());
        assertFalse(switchArgA.isLong());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a=value", "--long-a=value"});
        ParameterArgument paramArgA = parser.parseParameterArgument("a");
        assertEquals(0, paramArgA.getIndex());
        assertEquals("value", paramArgA.getValue());

        paramArgA = parser.parseParameterArgument("long-a");
        assertEquals(0, paramArgA.getIndex());
        assertEquals("value", paramArgA.getValue());

        paramArgA = parser.parseParameterArgument("b");
        assertEquals(-1, paramArgA.getIndex());
        assertNull(paramArgA.getValue());

        parser = new ArgsParser(new String[]{"--a=value", "--long-a=value", "b=otherValue"});
        paramArgA = parser.parseParameterArgument("a");
        assertEquals(0, paramArgA.getIndex());
        assertEquals("value", paramArgA.getValue());

        paramArgA = parser.parseParameterArgument("long-a");
        assertEquals(0, paramArgA.getIndex());
        assertEquals("value", paramArgA.getValue());

        paramArgA = parser.parseParameterArgument("b");
        assertEquals(2, paramArgA.getIndex());
        assertEquals("otherValue", paramArgA.getValue());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"--string-list", "value1", "value2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        StringArgument stringArg1 = stringArguments.get(0);
        StringArgument stringArg2 = stringArguments.get(1);
        assertEquals(0, stringArg1.getIndex());
        assertEquals("value1", stringArg1.getValue());
        assertEquals(1, stringArg2.getIndex());
        assertEquals("value2", stringArg2.getValue());

        parser = new ArgsParser(new String[]{"-string-list", "value1", "value2"});
        stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        stringArg1 = stringArguments.get(0);
        stringArg2 = stringArguments.get(1);
        assertEquals(0, stringArg1.getIndex());
        assertEquals("value1", stringArg1.getValue());
        assertEquals(1, stringArg2.getIndex());
        assertEquals("value2", stringArg2.getValue());

        parser = new ArgsParser(new String[]{"--string-list", "--", "value3"});
        stringArguments = parser.parseStringArgument();
        assertEquals(1, stringArguments.size());
        stringArg1 = stringArguments.get(0);
        assertEquals(2, stringArg1.getIndex());
        assertEquals("value3", stringArg1.getValue());
    }

    @Test
    public void testEmptyArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"--empty"});
        assertEquals(1, parser.getArgsCount());
        assertEquals(1, parser.getArgsLeftCount());
        assertNull(parser.parseSwitchArgument("empty"));
        assertNull(parser.parseParameterArgument("empty"));
        assertNull(parser.parseStringArgument().get(0));

        parser = new ArgsParser(new String[]{"--empty", "--long-empty"});
        assertEquals(2, parser.getArgsCount());
        assertEquals(2, parser.getArgsLeftCount());
        assertNull(parser.parseSwitchArgument("empty"));
        assertNull(parser.parseSwitchArgument("long-empty"));
        assertNull(parser.parseParameterArgument("empty"));
        assertNull(parser.parseParameterArgument("long-empty"));
        assertNull(parser.parseStringArgument().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgs() {
        new ArgsParser(null);
    }
}