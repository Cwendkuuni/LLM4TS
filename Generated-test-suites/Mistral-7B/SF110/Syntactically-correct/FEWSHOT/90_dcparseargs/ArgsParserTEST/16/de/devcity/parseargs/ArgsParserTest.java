package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParserConstructor() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        assertEquals(0, parser.getArgsCount());
        assertEquals(args, parser.getArgs());
    }

    @Test
    public void testArgsParserSetter() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
        assertEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserSetterNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = new String[]{"arg1", "--key", "arg3", "--key", "arg4"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--key"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("key");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isPresent());

        args = new String[]{"--key1", "--key2"};
        parser = new ArgsParser(args);
        switchArgument = parser.parseSwitchArgument("key");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isPresent());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        String[] args = {"--longKey"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("longKey");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isPresent());

        args = new String[]{"--longKey1", "--longKey2"};
        parser = new ArgsParser(args);
        switchArgument = parser.parseSwitchArgument("longKey");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isPresent());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--key=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value", parameterArgument.getValue());

        args = new String[]{"--key1=value1", "--key2=value2"};
        parser = new ArgsParser(args);
        parameterArgument = parser.parseParameterArgument("key");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());

        args = new String[]{"--key1=value1", "--key=value2"};
        parser = new ArgsParser(args);
        parameterArgument = parser.parseParameterArgument("key");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentLongKey() {
        String[] args = {"--longKey=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value", parameterArgument.getValue());

        args = new String[]{"--longKey1=value1", "--longKey2=value2"};
        parser = new ArgsParser(args);
        parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"--key=value1", "--key2=value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "value1"));
        expected.add(new StringArgument(1, "value2"));
        assertEquals(expected, stringArguments);
    }

    @Test
    public void testParseStringArgumentEmpty() {
        String[] args = {"--key"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(0, stringArguments.size());
    }

    @Test
    public void testParseStringArgumentMixed() {
        String[] args = {"--key1=value1", "arg"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "value1"));
        assertEquals(expected, stringArguments);
    }
}