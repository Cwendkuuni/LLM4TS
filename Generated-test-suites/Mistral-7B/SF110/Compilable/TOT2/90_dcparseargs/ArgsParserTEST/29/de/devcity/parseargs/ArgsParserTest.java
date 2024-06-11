package de.devcity.parseargs;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        assertEquals(0, parser.getArgsCount());

        args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsCount());
    }
}