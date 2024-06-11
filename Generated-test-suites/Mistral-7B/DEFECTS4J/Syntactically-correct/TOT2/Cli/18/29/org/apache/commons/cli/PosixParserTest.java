import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithValidArguments() {
        String[] arguments = {"--option1", "--option2=value", "--option3", "-o", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", false, "o3");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o1", "--option1", "-o3", "--option3", "-o", "value"}, result);
    }

    @Test
    public void testFlattenWithInvalidArguments() {
        String[] arguments = {"--option1", "--option2=value", "--option3=value", "--", "--option", "--option4=value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", false, "o3");
        try {
            parser.flatten(options, arguments, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"--option1", "--option2=value", "--", "somearg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--option1", "--", "somearg"}, result);
    }
}