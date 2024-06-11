import org.junit.Test;
import org.junit.Assert.*;
import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @Test
    public void testConstructor() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--key=value", "-b"});
        assertEquals(3, ap.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--key=value", "-b"});
        assertArrayEquals(new String[]{"-a", "--key=value", "-b"}, ap.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser ap = new ArgsParser();
        ap.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--key=value", "-b"});
        assertEquals(2, ap.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "-k"});
        SwitchArgument result = ap.parseSwitchArgument("k");
        assertEquals(0, result.getIndex());
        assertFalse(result.isLongKey());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser ap = new ArgsParser(new String[]{"--a", "--key=value", "--switchKey"});
        SwitchArgument result = ap.parseSwitchArgument("switchKey");
        assertEquals(1, result.getIndex());
        assertTrue(result.isLongKey());
    }

    @Test
    public void testParseSwitchArgumentNotPresent() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--key=value", "-b"});
        SwitchArgument result = ap.parseSwitchArgument("notPresent");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isLongKey());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseSwitchArgumentInvalidIndex() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--key=value", "-b"});
        ap.parseSwitchArgument(10);
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        ArgsParser ap = new ArgsParser(new String[]{"-a=value", "-b"});
        ParameterArgument result = ap.parseParameterArgument("a");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgumentLongKey() {
        ArgsParser ap = new ArgsParser(new String[]{"--key=value", "--anotherKey=anotherValue", "-b"});
        ParameterArgument result = ap.parseParameterArgument("anotherKey");
        assertEquals(1, result.getIndex());
        assertEquals("anotherValue", result.getValue());
    }

    @Test
    public void testParseParameterArgumentNotPresent() {
        ArgsParser ap = new ArgsParser(new String[]{"-a=value", "-b"});
        ParameterArgument result = ap.parseParameterArgument("notPresent");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseParameterArgumentInvalidIndex() {
        ArgsParser ap = new ArgsParser(new String[]{"-a=value", "-b"});
        ap.parseParameterArgument(1);
    }

    @Test
    public void testParseParameterArgumentMissingValue() {
        ArgsParser ap = new ArgsParser(new String[]{"--key"});
        ParameterArgument result = ap.parseParameterArgument("key");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser ap = new ArgsParser(new String[]{"-a=value1", "-b=value2", "-c"});
        List<StringArgument> result = ap.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("value1", result.get(0).getValue());
        assertEquals("value2", result.get(1).getValue());
    }
}