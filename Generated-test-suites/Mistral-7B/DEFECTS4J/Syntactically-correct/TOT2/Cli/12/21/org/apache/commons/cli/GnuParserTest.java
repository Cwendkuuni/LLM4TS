import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"-Dproperty=value", "--foo", "-bar=value", "arg1", "arg2"};
        Options options = new Options();
        boolean stopAtNonOption = false;

        String[] expected = {"-D", "property", "value", "--", "foo", "-bar", "value", "arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }
}