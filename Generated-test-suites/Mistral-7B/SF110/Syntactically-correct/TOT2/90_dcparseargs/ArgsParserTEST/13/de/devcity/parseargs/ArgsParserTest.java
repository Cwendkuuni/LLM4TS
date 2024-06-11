import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    void testGetArgsCountEmptyArray() {
        ArgsParser argsParser = new ArgsParser(new String[] {});
        int result = argsParser.getArgsCount();
        assertEquals(0, result);
    }

    @Test
    void testGetArgsEmptyArray() {
        ArgsParser argsParser = new ArgsParser(new String[] {});
        String[] args = argsParser.getArgs();
        assertEquals(0, args.length);
    }

    @Test
    void testSetArgsValidInput() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    void testGetArgsLeftCountEmptyArgs() {
        ArgsParser argsParser = new ArgsParser(new String[] {});
        int result = argsParser.getArgsLeftCount();
        assertEquals(0, result);
    }

    @Test
    void testParseSwitchArgumentShort() {
        ArgsParser argsParser = new ArgsParser(new String[] {"-a"});
        SwitchArgument result = argsParser.parseSwitchArgument("-a");
        assertEquals(0, result.getIndex());
        assertTrue(result.isValid());
    }

    @Test
    void testParseSwitchArgumentLong() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--arg"});
        SwitchArgument result = argsParser.parseSwitchArgument("--arg");
        assertEquals(0, result.getIndex());
        assertTrue(result.isValid());
    }

    @Test
    void testParseSwitchArgumentInvalidShort() {
        ArgsParser argsParser = new ArgsParser(new String[] {"-a"});
        SwitchArgument result = argsParser.parseSwitchArgument("-a");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isValid());
    }

    @Test
    void testParseSwitchArgumentInvalidLong() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--a"});
        SwitchArgument result = argsParser.parseSwitchArgument("--arg");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isValid());
    }

    @Test
    void testParseParameterArgumentValidShort() {
        ArgsParser argsParser = new ArgsParser(new String[] {"-a=value"});
        ParameterArgument result = argsParser.parseParameterArgument("a");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    void testParseParameterArgumentValidLong() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--arg=value"});
        ParameterArgument result = argsParser.parseParameterArgument("arg");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    void testParseParameterArgumentInvalidShort() {
        ArgsParser argsParser = new ArgsParser(new String[] {"-a"});
        ParameterArgument result = argsParser.parseParameterArgument("a");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    void testParseParameterArgumentInvalidLong() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--a"});
        ParameterArgument result = argsParser.parseParameterArgument("arg");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    void testParseStringArgumentEmpty() {
        ArgsParser argsParser = new ArgsParser(new String[] {});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(0, result.size());
    }

    @Test
    void testParseStringArgumentSingle() {
        ArgsParser argsParser = new ArgsParser(new String[] {"arg"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(1, result.size());
        StringArgument stringArgument = result.get(0);
        assertEquals(0, stringArgument.getIndex());
        assertEquals("arg", stringArgument.getValue());
    }

    @Test
    void testParseStringArgumentMultiple() {
        ArgsParser argsParser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(3, result.size());
        for (int i = 0; i < result.size(); i++) {
            StringArgument stringArgument = result.get(i);
            assertEquals(i, stringArgument.getIndex());
            assertEquals("arg" + (i + 1), stringArgument.getValue());
        }
    }

    @Test
    void testParseSwitchArgumentEmptyString() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--"});
        SwitchArgument result = argsParser.parseSwitchArgument("--");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isValid());
    }

    @Test
    void testParseParameterArgumentEmptyString() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--"});
        ParameterArgument result = argsParser.parseParameterArgument("arg");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    void testParseStringArgumentEmptyString() {
        ArgsParser argsParser = new ArgsParser(new String[] {"arg="});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(1, result.size());
        StringArgument stringArgument = result.get(0);
        assertEquals(0, stringArgument.getIndex());
        assertEquals("", stringArgument.getValue());
    }

    @Test(expected = NullPointerException.class)
    void testParseSwitchArgumentNullArgs() {
        ArgsParser argsParser = new ArgsParser(null);
        argsParser.parseSwitchArgument("--arg");
        fail("Test should have thrown NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    void testParseParameterArgumentNullArgs() {
        ArgsParser argsParser = new ArgsParser(null);
        argsParser.parseParameterArgument("arg");
        fail("Test should have thrown NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    void testParseStringArgumentNullArgs() {
        ArgsParser argsParser = new ArgsParser(null);
        argsParser.parseStringArgument();
        fail("Test should have thrown NullPointerException");
    }

    @Test
    void testParseSwitchArgumentDuplicate() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--arg", "--arg"});
        SwitchArgument result = argsParser.parseSwitchArgument("--arg");
        assertEquals(0, result.getIndex());
        assertTrue(result.isValid());
    }

    @Test
    void testParseParameterArgumentDuplicate() {
        ArgsParser argsParser = new ArgsParser(new String[] {"--arg=value", "--arg=value"});
        ParameterArgument result = argsParser.parseParameterArgument("arg");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    void testParseStringArgumentDuplicate() {
        ArgsParser argsParser = new ArgsParser(new String[] {"arg=value", "arg=value"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(2, result.size());
        for (int i = 0; i < result.size(); i++) {
            StringArgument stringArgument = result.get(i);
            assertEquals(i, stringArgument.getIndex());
            assertEquals("value", stringArgument.getValue());
        }
    }
}