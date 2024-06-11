package de.devcity.parseargs;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        int actual = parser.getArgsCount();
        int expected = 0;
        assertEquals(expected, actual);

        args = new String[]{"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        actual = parser.getArgsCount();
        expected = 3;
        assertEquals(expected, actual);
    }
}