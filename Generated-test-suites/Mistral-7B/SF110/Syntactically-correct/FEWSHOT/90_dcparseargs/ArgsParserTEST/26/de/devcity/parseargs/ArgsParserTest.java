package de.devcity.parseargs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParser_emptyArgs() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsCount());
        assertNull(parser.getArgs());
    }

    @Test
    public void testArgsParser_argsWithValues() {
        String[] args = {"arg1", "arg2=value1", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParser_argsCannotBeNull() {
        new ArgsParser(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2=value1", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument_found() {
        String[] args = {"--key"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("key");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isSet());
    }

    @Test
    public void testParseSwitchArgument_notFound_shortKey() {
        String[] args = {"-k"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("key");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isSet());
    }

    @Test
    public void testParseSwitchArgument_notFound_longKey() {
        String[] args = {"--arg"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("arg");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isSet());
    }

    @Test
    public void testParseSwitchArgument_multiple_shortKeys() {
        String[] args = {"-a", "-b"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgumentA = parser.parseSwitchArgument("a");
        SwitchArgument switchArgumentB = parser.parseSwitchArgument("b");
        assertEquals(0, switchArgumentA.getIndex());
        assertTrue(switchArgumentA.isSet());
        assertEquals(1, switchArgumentB.getIndex());
        assertTrue(switchArgumentB.isSet());
    }

    @Test
    public void testParseSwitchArgument_multiple_longKeys() {
        String[] args = {"--longKey1", "--longKey2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument1 = parser.parseSwitchArgument("longKey1");
        SwitchArgument switchArgument2 = parser.parseSwitchArgument("longKey2");
        assertEquals(0, switchArgument1.getIndex());
        assertTrue(switchArgument1.isSet());
        assertEquals(1, switchArgument2.getIndex());
        assertTrue(switchArgument2.isSet());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--key=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_valueMissing() {
        String[] args = {"--key"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_multiple() {
        String[] args = {"--key1=value1", "--key2=value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument1 = parser.parseParameterArgument("key1");
        ParameterArgument parameterArgument2 = parser.parseParameterArgument("key2");
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("value1", parameterArgument1.getValue());
        assertEquals(1, parameterArgument2.getIndex());
        assertEquals("value2", parameterArgument2.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"--arg1=value1", "--arg2=value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "value1"));
        expected.add(new StringArgument(1, "value2"));
        assertArrayEquals(expected.toArray(), stringArguments.toArray());
    }
}