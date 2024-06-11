package de.devcity.parseargs;

import static org.junit.Assert.*;
import de.devcity.parseargs.arguments.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-a", "1", "-b", "2", "--test2"});
        assertEquals(6, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-a", "1", "-b", "2", "--test2"});
        String[] args = parser.getArgs();
        assertEquals("--test", args[0]);
        assertEquals("-a", args[1]);
        assertEquals("1", args[2]);
        assertEquals("-b", args[3]);
        assertEquals("2", args[4]);
        assertEquals("--test2", args[5]);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test", "-a", "1", "-b", "2", "--test2"};
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], parser.getArgs()[i]);
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-a", "1", "-b", "2", "--test2"});
        assertEquals(4, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-a", "1", "-b", "--switch", "2", "--test2"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("--switch");
        assertEquals(3, switchArgument.getIndex());
        assertTrue(switchArgument.isPresent());

        switchArgument = parser.parseSwitchArgument("--unknown");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isPresent());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-a", "1", "--param=value1", "-b", "--param=value2", "--test2"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("--param");
        assertEquals(3, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("--unknown");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-a", "--string=value1", "--string=value2", "--test2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        assertEquals("value1", stringArguments.get(0).getValue());
        assertEquals("value2", stringArguments.get(1).getValue());

        stringArguments = parser.parseStringArgument();
        assertEquals(0, stringArguments.size());
    }
}