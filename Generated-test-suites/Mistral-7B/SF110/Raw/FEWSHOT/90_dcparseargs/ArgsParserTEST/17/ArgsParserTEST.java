package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--foo", "-bar=1", "-baz"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"--foo", "-bar=1", "-baz"});
        String[] args = parser.getArgs();
        Assert.assertArrayEquals(new String[]{"--foo", "-bar=1", "-baz"}, args);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--foo", "-bar=1", "-baz"});
        String[] args = parser.getArgs();
        Assert.assertArrayEquals(new String[]{"--foo", "-bar=1", "-baz"}, args);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--foo=-bar", "--foo=-bar=1"});
        assertEquals(1, parser.getArgsLeftCount());

        parser = new ArgsParser(new String[]{"--foo"});
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--foo", "-bar"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("--foo");
        assertTrue(switchArgument.isPresent());
        assertEquals("--foo", switchArgument.getKey());

        switchArgument = parser.parseSwitchArgument("--bar");
        assertFalse(switchArgument.isPresent());

        parser = new ArgsParser(new String[]{"--foo=--bar"});
        switchArgument = parser.parseSwitchArgument("--foo");
        assertFalse(switchArgument.isPresent());

        parser = new ArgsParser(new String[]{"--foo=--bar", "--foo"});
        switchArgument = parser.parseSwitchArgument("--foo");
        assertTrue(switchArgument.isPresent());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--foo=1", "--bar=2"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("--foo");
        assertTrue(parameterArgument.isPresent());
        assertEquals("1", parameterArgument.getValue());

        parser = new ArgsParser(new String[]{"--foo=1", "--bar=2", "--foo=3"});
        parameterArgument = parser.parseParameterArgument("--foo");
        assertTrue(parameterArgument.isPresent());
        assertEquals("3", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("--bar");
        assertFalse(parameterArgument.isPresent());

        parser = new ArgsParser(new String[]{"--foo=1", "--foo=2"});
        parameterArgument = parser.parseParameterArgument("--foo");
        assertTrue(parameterArgument.isPresent());
        assertEquals("2", parameterArgument.getValue());

        parser = new ArgsParser(new String[]{"--foo=--bar"});
        parameterArgument = parser.parseParameterArgument("--foo");
        assertFalse(parameterArgument.isPresent());

        parser = new ArgsParser(new String[]{"--foo=--bar", "--foo=1"});
        parameterArgument = parser.parseParameterArgument("--foo");
        assertTrue(parameterArgument.isPresent());
        assertEquals("1", parameterArgument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--foo=1", "1"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(1, "1"));
        Assert.assertEquals(expected, stringArguments);

        parser = new ArgsParser(new String[]{"--foo=1", "1", "2"});
        stringArguments = parser.parseStringArgument();
        expected.add(new StringArgument(2, "2"));
        Assert.assertEquals(expected, stringArguments);
    }
}