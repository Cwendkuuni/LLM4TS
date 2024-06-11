import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        int actual = parser.getArgsCount();
        int expected = args.length;
        assertEquals(expected, actual);
    }
}