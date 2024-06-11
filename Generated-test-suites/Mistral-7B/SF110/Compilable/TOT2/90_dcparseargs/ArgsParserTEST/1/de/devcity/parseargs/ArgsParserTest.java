package de.devcity.parseargs;
import static org.junit.Assert.assertEquals;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {

    @org.junit.Test
    public void testGetArgsCount() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        int expected = 3;
        int actual = parser.getArgsCount();
        assertEquals(expected, actual);
    }
}