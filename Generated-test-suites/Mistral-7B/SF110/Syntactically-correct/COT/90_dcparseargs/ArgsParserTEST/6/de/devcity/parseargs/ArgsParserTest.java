import static org.junit.Assert.*;
import de.devcity.parseargs.*;
import org.junit.Test;

public class ArgsParserTest
{
    @Test
    public void testGetArgsCount()
    {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        int result = parser.getArgsCount();
        assertEquals(0, result);

        args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser2 = new ArgsParser(args);
        result = parser2.getArgsCount();
        assertEquals(3, result);
    }

    @Test
    public void testGetArgs()
    {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        String[] result = parser.getArgs();
        assertArrayEquals(new String[0], result);

        args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser2 = new ArgsParser(args);
        result = parser2.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount()
    {
        String[] args = new String[0].clone();
        ArgsParser parser = new ArgsParser(args);
        int result = parser.getArgsLeftCount();
        assertEquals(0, result);

        args = new String[]{"--arg1", "arg2", "arg3", "--arg2", "arg4"};
        ArgsParser parser2 = new ArgsParser(args);
        result = parser2.getArgsLeftCount();
        assertEquals(2, result);
    }

    @Test
    public void testParseSwitchArgumentShortKey()
    {
        String[] args = {"-a"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertEquals(0, result.getIndex());
        assertTrue(result.isLongKey());

        args = new String[]{"-a", "arg1", "arg2"};
        ArgsParser parser2 = new ArgsParser(args);
        result = parser2.parseSwitchArgument("a");
        assertEquals(0, result.getIndex());
        assertFalse(result.isLongKey());
    }

    @Test
    public void testParseSwitchArgumentLongKey()
    {
        String[] args = {"--arg1"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("arg1");
        assertEquals(0, result.getIndex());
        assertTrue(result.isLongKey());

        args = new String[]{"--arg1", "arg2", "arg3", "--arg2"};
        ArgsParser parser2 = new ArgsParser(args);
        result = parser2.parseSwitchArgument("arg2");
        assertEquals(2, result.getIndex());
        assertTrue(result.isLongKey());
    }

    @Test
    public void testParseSwitchArgumentMissing()
    {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("arg4");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isLongKey());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseSwitchArgumentOutOfBounds()
    {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseSwitchArgument("arg4");
        parser.parseSwitchArgument("arg4");
    }

    @Test
    public void testParseParameterArgumentShortKey()
    {
        String[] args = {"-a=value", "arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("a");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());

        args = new String[]{"--arg1=value", "arg2", "arg3"};
        ArgsParser parser2 = new ArgsParser(args);
        result = parser2.parseParameterArgument("arg1");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgumentLongKey()
    {
        String[] args = {"--arg1=value", "arg2", "arg3", "--arg2=value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("arg1");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());

        result = parser.parseParameterArgument("arg2");
        assertEquals(2, result.getIndex());
        assertEquals("value2", result.getValue());
    }

    @Test
    public void testParseParameterArgumentMissingShortKey()
    {
        String[] args = {"-a", "arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("b");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    public void testParseParameterArgumentMissingLongKey()
    {
        String[] args = {"--arg1", "arg1", "arg2", "--arg2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("arg3");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseParameterArgumentOutOfBoundsShortKey()
    {
        String[] args = {"-a", "arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("a");
        parser.parseParameterArgument("a");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseParameterArgumentOutOfBoundsLongKey()
    {
        String[] args = {"--arg1", "arg1", "arg2", "--arg2"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("arg1");
        parser.parseParameterArgument("arg1");
    }

    @Test
    public void testParseStringArgument()
    {
        String[] args = {"arg1=value1", "arg2=value2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(2, result.size());
        StringArgument firstElement = result.get(0);
        assertEquals(0, firstElement.getIndex());
        assertEquals("value1", firstElement.getValue());

        StringArgument secondElement = result.get(1);
        assertEquals(1, secondElement.getIndex());
        assertEquals("value2", secondElement.getValue());
    }
}