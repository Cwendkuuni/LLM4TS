package de.devcity.parseargs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-a", "--arg2"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentShort() {
        ArgsParser parser = new ArgsParser(new String[]{"-s"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("-s");
        assertTrue(switchArgument.isSet());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("-s", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("--switch");
        assertTrue(switchArgument.isSet());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("switch", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentNotPresent() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("--not-present");
        assertFalse(switchArgument.isSet());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("not-present", switchArgument.getKey());
    }

    @Test
    public void testParseParameterArgumentShort() {
        ArgsParser parser = new ArgsParser(new String[]{"-p=value"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("p");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("p", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--param=value"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("param");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("param", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentNotPresent() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("p");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgumentMissingValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-p"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("p");
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> stringArguments = parser.parseStringArguments();
        List<StringArgument> expected = new ArrayList<StringArgument>();
        expected.add(new StringArgument(0, "arg1", "value1"));
        expected.add(new StringArgument(1, "arg2", "value2"));
        assertEquals(expected, stringArguments);
    }

    @Test
    public void testParseStringArgumentsEmpty() {
        ArgsParser parser = new ArgsParser(new String[0]);
        List<StringArgument> stringArguments = parser.parseStringArguments();
        assertEquals(Collections.emptyList(), stringArguments);
    }
}