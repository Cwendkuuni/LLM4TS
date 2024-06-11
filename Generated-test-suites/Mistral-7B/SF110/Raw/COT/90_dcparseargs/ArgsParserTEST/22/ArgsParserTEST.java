import org.junit.Test;
import org.junit.Assert.*;
import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @Test
    public void testConstructor() {
        String[] args = new String[]{"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "--key2=value1", "--key3=value2"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentShort() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "-b", "-c"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals(0, switchArgument.getIndex());
        Assert.assertTrue(switchArgument.isLong());

        switchArgument = parser.parseSwitchArgument("c");
        assertEquals(-1, switchArgument.getIndex());
        Assert.assertFalse(switchArgument.isLong());
    }

    @Test
    public void testParseSwitchArgumentLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2", "--arg3"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("arg1");
        assertEquals(0, switchArgument.getIndex());
        Assert.assertTrue(switchArgument.isLong());

        switchArgument = parser.parseSwitchArgument("arg3");
        assertEquals(-1, switchArgument.getIndex());
        Assert.assertFalse(switchArgument.isLong());
    }

    @Test
    public void testParseParameterArgumentShort() {
        ArgsParser parser = new ArgsParser(new String[]{"-a=value1", "-b=value2", "-c"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("a");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("c");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2", "--arg3"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("arg1");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("arg3");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        StringArgument stringArgument = stringArguments.get(0);
        assertEquals(0, stringArgument.getIndex());
        assertEquals("value1", stringArgument.getValue());

        stringArgument = stringArguments.get(1);
        assertEquals(1, stringArgument.getIndex());
        assertEquals("value2", stringArgument.getValue());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseSwitchArgumentNotFoundShort() {
        ArgsParser parser = new ArgsParser(new String[]{"-a"});
        parser.parseSwitchArgument("b");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseParameterArgumentNotFoundShort() {
        ArgsParser parser = new ArgsParser(new String[]{"-a=value"});
        parser.parseParameterArgument("b");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseSwitchArgumentNotFoundLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1"});
        parser.parseSwitchArgument("--arg3");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseParameterArgumentNotFoundLongWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value"});
        parser.parseParameterArgument("--arg3");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseStringArgumentEmptyList() {
        ArgsParser parser = new ArgsParser(new String[0]);
        parser.parseStringArgument();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentEmptyString() {
        ArgsParser parser = new ArgsParser(new String[]{"--", "--arg="});
        parser.parseSwitchArgument("arg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseParameterArgumentEmptyString() {
        ArgsParser parser = new ArgsParser(new String[]{"-", "-a="});
        parser.parseParameterArgument("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseParameterArgumentEmptyStringWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-a=", "-a=value"});
        parser.parseParameterArgument("a");
    }
}