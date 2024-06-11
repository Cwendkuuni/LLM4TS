import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenMethod() throws ParseException {
        String[] arguments = {"-Dproperty=value", "-option1", "--option2", "argument"};
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] expected = {"-D", "property", "value", "-option1", "--option2", "argument"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }
}