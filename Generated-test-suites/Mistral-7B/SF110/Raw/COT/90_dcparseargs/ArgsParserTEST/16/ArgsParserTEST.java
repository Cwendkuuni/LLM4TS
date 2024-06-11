import org.junit.Test;
import org.junit.Assert.*;
import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int argsCount = parser.getArgsCount();
        assertEquals(argsCount, 2);
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "--key", "arg3", "--key", "arg4"});
        int argsLeftCount = parser.getArgsLeftCount();
        assertEquals(argsLeftCount, 3);
    }

    @Test
    public void testParseSwitchArgumentWithShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a"});
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertEquals(switchArg.getIndex(), 0);
        assertTrue(switchArg.isLong());
    }

    @Test
    public void testParseSwitchArgumentWithLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key"});
        SwitchArgument switchArg = parser.parseSwitchArgument("key");
        assertEquals(switchArg.getIndex(), 0);
        assertTrue(switchArg.isLong());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "-ab"});
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertEquals(switchArg.getIndex(), -1);
        assertFalse(switchArg.isLong());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "--invalidKey"});
        SwitchArgument switchArg = parser.parseSwitchArgument("invalidKey");
        assertEquals(switchArg.getIndex(), -1);
        assertFalse(switchArg.isLong());
    }

    @Test
    public void testParseSwitchArgumentWithMultipleOccurrences() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "--key", "arg3"});
        SwitchArgument switchArg = parser.parseSwitchArgument("key");
        assertEquals(switchArg.getIndex(), 0);
        assertTrue(switchArg.isLong());
    }

    @Test
    public void testParseParameterArgumentWithShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("a");
        assertEquals(paramArg.getIndex(), 0);
        assertEquals(paramArg.getKey(), "a");
        assertEquals(paramArg.getValue(), "value");
    }

    @Test
    public void testParseParameterArgumentWithLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("key");
        assertEquals(paramArg.getIndex(), 0);
        assertEquals(paramArg.getKey(), "key");
        assertEquals(paramArg.getValue(), "value");
    }

    @Test
    public void testParseParameterArgumentWithMultipleArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value1", "--key2", "value2"});
        ParameterArgument paramArg1 = parser.parseParameterArgument("key1");
        assertEquals(paramArg1.getIndex(), 0);
        assertEquals(paramArg1.getKey(), "key1");
        assertEquals(paramArg1.getValue(), "value1");
        ParameterArgument paramArg2 = parser.parseParameterArgument("key2");
        assertEquals(paramArg2.getIndex(), 2);
        assertEquals(paramArg2.getKey(), "key2");
        assertEquals(paramArg2.getValue(), "value2");
    }

    @Test
    public void testParseParameterArgumentWithInvalidKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "arg1", "--invalidKey"});
        ParameterArgument paramArg = parser.parseParameterArgument("invalidKey");
        assertEquals(paramArg.getIndex(), -1);
        assertNull(paramArg.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "value1", "--arg2", "value2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(stringArgs.size(), 2);
        StringArgument stringArg1 = stringArgs.get(0);
        assertEquals(stringArg1.getIndex(), 0);
        assertEquals(stringArg1.getKey(), "arg1");
        assertEquals(stringArg1.getValue(), "value1");
        StringArgument stringArg2 = stringArgs.get(1);
        assertEquals(stringArg2.getIndex(), 2);
        assertEquals(stringArg2.getKey(), "arg2");
        assertEquals(stringArg2.getValue(), "value2");
    }

    @Test
    public void testParseStringArgumentWithEmptyArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2", ""});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(stringArgs.size(), 2);
        StringArgument stringArg1 = stringArgs.get(0);
        assertEquals(stringArg1.getIndex(), 0);
        assertEquals(stringArg1.getKey(), "arg1");
        assertNull(stringArg1.getValue());
        StringArgument stringArg2 = stringArgs.get(1);
        assertEquals(stringArg2.getIndex(), 2);
        assertEquals(stringArg2.getKey(), "arg2");
        assertNull(stringArg2.getValue());
    }
}