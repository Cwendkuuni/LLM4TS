package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest
{
    @Test
    public void testArgsParserConstructionWithArgs()
    {
        String[] testArgs = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(testArgs);
        assertEquals(3, parser.getArgsCount());
        assertArrayEquals(testArgs, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserConstructionWithNullArgs()
    {
        String[] testArgs = null;
        ArgsParser parser = new ArgsParser(testArgs);
    }

    @Test
    public void testGetArgsCount()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgs()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testSetArgs()
    {
        ArgsParser parser = new ArgsParser();
        String[] testArgs = new String[]{"arg1", "arg2"};
        parser.setArgs(testArgs);
        assertEquals(2, parser.getArgsCount());
        assertArrayEquals(testArgs, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs()
    {
        ArgsParser parser = new ArgsParser();
        String[] testArgs = null;
        parser.setArgs(testArgs);
    }

    @Test
    public void testGetArgsLeftCount()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "--key", "value", "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentShortKey()
    {
        ArgsParser parser = new ArgsParser(new String[]{"-a"});
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertEquals(0, result.getIndex());
        assertTrue(result.isPresent());
    }

    @Test
    public void testParseSwitchArgumentLongKey()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--longKey"});
        SwitchArgument result = parser.parseSwitchArgument("longKey");
        assertEquals(0, result.getIndex());
        assertTrue(result.isPresent());
    }

    @Test
    public void testParseSwitchArgumentMissing()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1"});
        SwitchArgument result = parser.parseSwitchArgument("key");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isPresent());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseSwitchArgumentOutOfBounds()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1"});
        parser.parseSwitchArgument("key");
        parser.parseSwitchArgument("key");
    }

    @Test
    public void testParseParameterArgumentShortKey()
    {
        ArgsParser parser = new ArgsParser(new String[]{"-a=value", "arg1"});
        ParameterArgument result = parser.parseParameterArgument("a");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
        assertTrue(result.isPresent());
    }

    @Test
    public void testParseParameterArgumentLongKey()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--longKey=value", "arg1"});
        ParameterArgument result = parser.parseParameterArgument("longKey");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
        assertTrue(result.isPresent());
    }

    @Test
    public void testParseParameterArgumentMissing()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertEquals(-1, result.getIndex());
        assertNull(result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseParameterArgumentOutOfBounds()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1"});
        parser.parseParameterArgument("key");
    }

    @Test
    public void testParseStringArgument()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--key=value1", "--key2=value2", "arg1"});
        List<StringArgument> result = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<StringArgument>();
        expected.add(new StringArgument(1, "value1"));
        expected.add(new StringArgument(2, "value2"));
        assertEquals(expected, result);
    }
}