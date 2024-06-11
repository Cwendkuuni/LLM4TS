package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest
{
    @Test
    public void testArgsParserConstructor()
    {
        String[] args = new String[] {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserNullConstructor()
    {
        new ArgsParser(null);
    }

    @Test
    public void testGetArgs()
    {
        String[] args = new String[] {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        String[] result = parser.getArgs();
        assertArrayEquals(args, result);
    }

    @Test
    public void testSetArgs()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
        String[] result = parser.getArgs();
        assertArrayEquals(new String[] {"arg1", "arg2"}, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount()
    {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "--key=value", "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument()
    {
        ArgsParser parser = new ArgsParser(new String[] {"--key", "arg1", "--longKey"});
        SwitchArgument result = parser.parseSwitchArgument("key");
        assertEquals(0, result.getIndex());
        assertFalse(result.isLongKey());
        result = parser.parseSwitchArgument("longKey");
        assertEquals(1, result.getIndex());
        assertTrue(result.isLongKey());

        parser = new ArgsParser(new String[] {"-k", "arg1", "--longKey"});
        result = parser.parseSwitchArgument("k");
        assertEquals(0, result.getIndex());
        assertFalse(result.isLongKey());
        result = parser.parseSwitchArgument("longKey");
        assertEquals(1, result.getIndex());
        assertTrue(result.isLongKey());

        parser = new ArgsParser(new String[] {"--key=value", "arg1"});
        result = parser.parseSwitchArgument("key");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isLongKey());

        parser = new ArgsParser(new String[] {"--longKey=value", "arg1"});
        result = parser.parseSwitchArgument("key");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isLongKey());
        result = parser.parseSwitchArgument("longKey");
        assertEquals(0, result.getIndex());
        assertTrue(result.isLongKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentInvalidKey()
    {
        ArgsParser parser = new ArgsParser(new String[] {"--key", "arg1"});
        parser.parseSwitchArgument("invalidKey");
    }

    @Test
    public void testParseParameterArgument()
    {
        ArgsParser parser = new ArgsParser(new String[] {"--key=value", "arg1", "--longKey=value2", "arg3"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());

        result = parser.parseParameterArgument("longKey");
        assertEquals(2, result.getIndex());
        assertEquals("value2", result.getValue());

        parser = new ArgsParser(new String[] {"--key=value", "arg1", "--key=value2", "arg3"});
        result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
        result = parser.parseParameterArgument("key");
        assertEquals(1, result.getIndex());
        assertEquals("value2", result.getValue());

        parser = new ArgsParser(new String[] {"--key=value1", "--key=value2", "arg3"});
        result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value1", result.getValue());

        parser = new ArgsParser(new String[] {"--key=value", "arg1", "--longKey=value2", "arg3", "--key=value3"});
        result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
        result = parser.parseParameterArgument("key");
        assertEquals(1, result.getIndex());
        assertEquals("value2", result.getValue());
        result = parser.parseParameterArgument("key");
        assertEquals(3, result.getIndex());
        assertEquals("value3", result.getValue());

        parser = new ArgsParser(new String[] {"--key=value", "arg1", "--longKey=value2", "arg3", "--key=value"});
        result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
        result = parser.parseParameterArgument("key");
        assertEquals(1, result.getIndex());
        assertEquals("value2", result.getValue());
        result = parser.parseParameterArgument("key");
        assertEquals(3, result.getIndex());
        assertEquals("value", result.getValue());

        parser = new ArgsParser(new String[] {"--key=value", "arg1", "--key=value", "arg3"});
        result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
        result = parser.parseParameterArgument("key");
        assertEquals(1, result.getIndex());
        assertEquals("value", result.getValue());

        parser = new ArgsParser(new String[] {"--key=value", "arg1", "arg3"});
        result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseParameterArgumentInvalidKey()
    {
        ArgsParser parser = new ArgsParser(new String[] {"--key=value", "arg1"});
        parser.parseParameterArgument("invalidKey");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseParameterArgumentEndOfArgs()
    {
        ArgsParser parser = new ArgsParser(new String[] {"--key=value"});
        parser.parseParameterArgument("key");
    }

    @Test
    public void testParseStringArgument()
    {
        ArgsParser parser = new ArgsParser(new String[] {"--key=value1", "--key=value2", "arg3", "--key=value3"});
        List<StringArgument> result = parser.parseStringArgument();
        List<StringArgument> expected = Arrays.asList(new StringArgument(0, "value1"), new StringArgument(3, "value3"));
        assertEquals(expected, result);
    }
}