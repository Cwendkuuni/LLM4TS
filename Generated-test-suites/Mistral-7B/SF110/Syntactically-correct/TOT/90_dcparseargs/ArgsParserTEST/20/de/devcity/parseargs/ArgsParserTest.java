package de.devcity.parseargs.ArgsParserTest;

import static org.junit.Assert.*;
import java.util.List;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsCount();
        assertEquals(3, count);
    }

    @Test
    public void testGetArgsCount_emptyArgsArray() {
        ArgsParser parser = new ArgsParser(new String[]{});
        int count = parser.getArgsCount();
        assertEquals(0, count);
    }

    @Test
    public void testGetArgsCount_nullArgsArray() {
        ArgsParser parser = new ArgsParser(null);
        try {
            parser.getArgsCount();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseSwitchArgument_validShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgument_validLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--longKey"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("longKey");
        assertEquals(0, switchArgument.getIndex());
        assertFalse(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgument_invalidKey() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("invalidKey");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.hasValue());
        assertFalse(switchArgument.isLongKey());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, "arg3"});
        int count = parser.getArgsLeftCount();
        assertEquals(2, count);
    }

    @Test
    public void testGetArgsLeftCount_emptyArgsArray() {
        ArgsParser parser = new ArgsParser(new String[]{});
        int count = parser.getArgsLeftCount();
        assertEquals(0, count);
    }

    @Test
    public void testGetArgsLeftCount_allArgsNull() {
        ArgsParser parser = new ArgsParser(new String[]{null, null, null});
        int count = parser.getArgsLeftCount();
        assertEquals(3, count);
    }

    @Test
    public void testParseParameterArgument_validShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a=value"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("a");
        assertEquals(0, parameterArgument.getIndex());
        assertFalse(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_validLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--longKey=value"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(0, parameterArgument.getIndex());
        assertTrue(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_invalidKey() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("invalidKey");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
        assertFalse(parameterArgument.isLongKey());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-arg=value1", "-arg2=value2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(0, stringArgument1.getIndex());
        assertEquals("value1", stringArgument1.getValue());
        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(1, stringArgument2.getIndex());
        assertEquals("value2", stringArgument2.getValue());
    }

    @Test
    public void testParseStringArgument_emptyArgsArray() {
        ArgsParser parser = new ArgsParser(new String[]{});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(0, stringArguments.size());
    }

    @Test
    public void testParseStringArgument_oneArgWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-arg"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(1, stringArguments.size());
        StringArgument stringArgument = stringArguments.get(0);
        assertEquals(0, stringArgument.getIndex());
        assertNull(stringArgument.getValue());
    }
}