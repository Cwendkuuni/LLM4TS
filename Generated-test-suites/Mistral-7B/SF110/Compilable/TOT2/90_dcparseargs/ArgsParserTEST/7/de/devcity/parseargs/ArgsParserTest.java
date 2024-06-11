package de.devcity.parseargs;
import org.junit.Assert;
import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        int expected = 0;
        int actual = parser.getArgsCount();
        Assert.assertEquals(expected, actual);

        args = new String[]{"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        expected = 3;
        actual = parser.getArgsCount();
        Assert.assertEquals(expected, actual);
    }
}