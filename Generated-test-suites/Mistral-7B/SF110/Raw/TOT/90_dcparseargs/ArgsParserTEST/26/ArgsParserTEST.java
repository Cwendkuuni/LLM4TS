import static org.junit.Assert.*;
import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @org.junit.Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetArgsCountNullArgs() {
        ArgsParser parser = new ArgsParser(null);
    }

    @org.junit.Test
    public void testParseSwitchArgumentShortKey() {
        String[] args = {"-a"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals(-1, switchArgument.getIndex());
        assertNull(switchArgument.getKey());
        assertFalse(switchArgument.isLongKey());
    }

    @org.junit.Test
    public void testParseSwitchArgumentLongKey() {
        String[] args = {"--longKey"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("longKey");
        assertEquals(0, switchArgument.getIndex());
        assertEquals("longKey", switchArgument.getKey());
        assertTrue(switchArgument.isLongKey());
    }

    @org.junit.Test(expected = SwitchArgument.NoSuchArgumentException.class)
    public void testParseSwitchArgumentNoArgument() {
        String[] args = {};
        ArgsParser parser = new ArgsParser(args);
        parser.parseSwitchArgument("anyKey");
    }

    @org.junit.Test(expected = SwitchArgument.NoSuchArgumentException.class)
    public void testParseSwitchArgumentWrongArgument() {
        String[] args = {"arg1", "--longKey", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseSwitchArgument("arg2");
    }

    @org.junit.Test
    public void testParseParameterArgumentShortKey() {
        String[] args = {"-a=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("a");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @org.junit.Test
    public void testParseParameterArgumentLongKey() {
        String[] args = {"--longKey=value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("longKey", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @org.junit.Test(expected = ParameterArgument.NoSuchArgumentException.class)
    public void testParseParameterArgumentNoArgument() {
        String[] args = {};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("anyKey");
    }

    @org.junit.Test(expected = ParameterArgument.NoSuchArgumentException.class)
    public void testParseParameterArgumentWrongArgument() {
        String[] args = {"arg1", "--longKey=value", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("arg2");
    }

    @org.junit.Test(expected = ParameterArgument.ValueMissingException.class)
    public void testParseParameterArgumentMissingValue() {
        String[] args = {"-a"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("a");
    }

    @org.junit.Test
    public void testParseStringArguments() {
        String[] args = {"--stringArg1=value1", "--stringArg2=value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        assertEquals("value1", stringArguments.get(0).getValue());
        assertEquals("value2", stringArguments.get(1).getValue());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseStringArgumentsMissingValue() {
        String[] args = {"--stringArg1"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseStringArgument();
    }
}