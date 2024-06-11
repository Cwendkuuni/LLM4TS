package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParser_EmptyArgsArray() {
        ArgsParser parser = new ArgsParser();
        int argsCount = parser.getArgsCount();
        assertEquals(0, argsCount);
    }

    @Test
    public void testArgsParser_NullArgsArray() {
        try {
            new ArgsParser(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testArgsParser_ValidArgsArray() {
        String[] args = {"arg1", "arg2", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        int argsCount = parser.getArgsCount();
        assertEquals(args.length, argsCount);
        for (int i = 0; i < args.length; ++i) {
            assertEquals(args[i], parser.getArgs()[i]);
        }
    }

    @Test
    public void testArgsParser_SetArgs() {
        String[] args = {"arg1", "arg2", "--key=value"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        int argsCount = parser.getArgsCount();
        assertEquals(args.length, argsCount);
        for (int i = 0; i < args.length; ++i) {
            assertEquals(args[i], parser.getArgs()[i]);
        }
    }

    @Test
    public void testArgsParser_GetArgsLeftCount_NoArgs() {
        ArgsParser parser = new ArgsParser();
        int argsLeftCount = parser.getArgsLeftCount();
        assertEquals(0, argsLeftCount);
    }

    @Test
    public void testArgsParser_GetArgsLeftCount_ArgsPresent() {
        String[] args = {"arg1", "arg2", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        int argsLeftCount = parser.getArgsLeftCount();
        assertEquals(1, argsLeftCount);
    }

    @Test
    public void testParseSwitchArgument_ShortKey_ArgPresent() {
        String[] args = {"-a", "arg2", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("-a");
        assertTrue(switchArg.isFound());
        assertEquals(-1 + parser.getArgsLeftCount(), switchArg.getIndex());
        assertEquals("-a", switchArg.getKey());
        assertFalse(switchArg.hasValue());
    }

    @Test
    public void testParseSwitchArgument_ShortKey_ArgNotPresent() {
        String[] args = {"arg1", "arg2", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("-a");
        assertFalse(switchArg.isFound());
        assertEquals(-1, switchArg.getIndex());
        assertNull(switchArg.getKey());
        assertFalse(switchArg.hasValue());
    }

    @Test
    public void testParseSwitchArgument_LongKey_ArgPresent() {
        String[] args = {"arg1", "--key=value", "--longKey"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("--longKey");
        assertTrue(switchArg.isFound());
        assertEquals(1 + parser.getArgsLeftCount(), switchArg.getIndex());
        assertEquals("--longKey", switchArg.getKey());
        assertFalse(switchArg.hasValue());
    }

    @Test
    public void testParseSwitchArgument_LongKey_ArgNotPresent() {
        String[] args = {"arg1", "arg2", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("--longKey");
        assertFalse(switchArg.isFound());
        assertEquals(-1, switchArg.getIndex());
        assertNull(switchArg.getKey());
        assertFalse(switchArg.hasValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyValue_ArgPresent() {
        String[] args = {"arg1", "-a=value", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("-a");
        assertTrue(paramArg.isFound());
        assertEquals(-1 + parser.getArgsLeftCount(), paramArg.getIndex());
        assertEquals("-a", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyValue_ArgNotPresent() {
        String[] args = {"arg1", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("-a");
        assertFalse(paramArg.isFound());
        assertEquals(-1, paramArg.getIndex());
        assertNull(paramArg.getKey());
        assertNull(paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyValue_ArgPresent() {
        String[] args = {"arg1", "--key=value", "--longKey=longValue"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("--longKey");
        assertTrue(paramArg.isFound());
        assertEquals(1 + parser.getArgsLeftCount(), paramArg.getIndex());
        assertEquals("--longKey", paramArg.getKey());
        assertEquals("longValue", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyValue_ArgNotPresent() {
        String[] args = {"arg1", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("--longKey");
        assertFalse(paramArg.isFound());
        assertEquals(-1, paramArg.getIndex());
        assertNull(paramArg.getKey());
        assertNull(paramArg.getValue());
    }

    @Test
    public void testParseStringArgument_ArgsPresent() {
        String[] args = {"arg1", "--key=value", "-a=arg", "--longKey=longArg"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        List<StringArgument> expectedArgs = Arrays.asList(new StringArgument(0, "--key=value"), new StringArgument(2, "--longKey=longArg"));
        assertEquals(expectedArgs.size(), stringArgs.size());
        for (int i = 0; i < expectedArgs.size(); ++i) {
            assertEquals(expectedArgs.get(i).getValue(), stringArgs.get(i).getValue());
            assertEquals(expectedArgs.get(i).getIndex(), stringArgs.get(i).getIndex());
        }
    }

    @Test
    public void testParseStringArgument_StringArgPresent() {
        String[] args = {"arg1", "--key=value", "--longKey=longArg", "arg"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        List<StringArgument> expectedArgs = Arrays.asList(new StringArgument(2, "arg"));
        assertEquals(expectedArgs.size(), stringArgs.size());
        for (int i = 0; i < expectedArgs.size(); ++i) {
            assertEquals(expectedArgs.get(i).getValue(), stringArgs.get(i).getValue());
            assertEquals(expectedArgs.get(i).getIndex(), stringArgs.get(i).getIndex());
        }
    }

    @Test
    public void testParseStringArgument_NoStringArgs() {
        String[] args = {"arg1", "--key=value"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertTrue(stringArgs.isEmpty());
    }

    @Test
    public void testParseStringArgument_MixedArgs() {
        String[] args = {"arg1", "--key=value", "arg", "--longKey=longArg", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
        StringArgument arg1 = stringArgs.get(0);
        StringArgument arg2 = stringArgs.get(1);
        assertEquals("arg", arg1.getValue());
        assertEquals(2, arg1.getIndex());
        assertEquals("arg2", arg2.getValue());
        assertEquals(4, arg2.getIndex());
    }
}