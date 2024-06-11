package de.devcity.parseargs;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testParseArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        assertEquals(0, parser.getArgsCount());

        args = new String[]{"arg1"};
        parser = new ArgsParser(args);
        assertEquals(1, parser.getArgsCount());

        args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testParseArgs() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        String[] result = parser.getArgs();
        assertArrayEquals(new String[0], result);

        args = new String[]{"arg1"};
        parser = new ArgsParser(args);
        result = parser.getArgs();
        assertArrayEquals(new String[]{"arg1"}, result);

        args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        result = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2"};
        parser.setArgs(args);
        String[] result = parser.getArgs();
        assertArrayEquals(args, result);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        int result = parser.getArgsLeftCount();
        assertEquals(0, result);

        String[] args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        result = parser.getArgsLeftCount();
        assertEquals(2, result);

        parser.parseSwitchArgument("--help");
        parser.parseSwitchArgument("--verbose");
        result = parser.getArgsLeftCount();
        assertEquals(0, result);
    }

    @Test
    public void testParseSwitchArgumentShort() {
        String[] args = new String[]{"-a"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("-a");
        assertTrue(result.isSet());
        assertEquals("-a", result.getKey());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentLong() {
        String[] args = new String[]{"--optionA"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("--optionA");
        assertTrue(result.isSet());
        assertEquals("optionA", result.getKey());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentMissing() {
        String[] args = new String[]{"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("--nonexistent");
        assertFalse(result.isSet());
        assertEquals("-1", result.getKey());
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseParameterArgumentShort() {
        String[] args = new String[]{"-a=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("a");
        assertNotNull(result);
        assertEquals("value", result.getValue());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseParameterArgumentLong() {
        String[] args = new String[]{"--optionA=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("optionA");
        assertNotNull(result);
        assertEquals("value", result.getValue());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseParameterArgumentMissingShort() {
        String[] args = new String[]{"-a"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("a");
        assertNull(result);
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseParameterArgumentMissingLong() {
        String[] args = new String[]{"--optionA"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("optionB");
        assertNull(result);
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseStringArguments() {
        String[] args = new String[]{"--input=file.txt", "-o=output.txt", "-i=input.txt"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> result = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<StringArgument>(3);
        expected.add(new StringArgument(0, "file.txt"));
        expected.add(new StringArgument(1, "output.txt"));
        expected.add(new StringArgument(2, "input.txt"));
        assertEquals(expected, result);
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseStringArgumentsEmpty() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(0, result.size());
        assertEquals(0, parser.getArgsLeftCount());
    }
}