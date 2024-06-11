import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {
    @Test
    public void testFlatten() throws ParseException {
        // given
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c", "-d=value2", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, args, false);

        // then
        assertEquals(Arrays.asList("--foo", "--bar", "value", "-a", "-b", "-c", "-d", "value2").toArray(), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        // given
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c", "-d=value2", "--", "-e"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, args, true);

        // then
        assertEquals(Arrays.asList("--foo", "--bar=value", "-a", "-b", "-c", "-d=value2", "-e").toArray(), result);
    }

    @Test
    public void testProcessOptionToken() {
        // given
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "An option with flag");
        options.addOption("b", 1, "An option with argument");
        options.addOption("c", null, "An option without argument");

        // when
        parser.processOptionToken("a", false);
        parser.processOptionToken("b", false);
        parser.processOptionToken("c", false);
        parser.processOptionToken("d", false);

        // then
        assertTrue(parser.currentOption == null);
        assertFalse(parser.eatTheRest);
        assertEquals(Arrays.asList("a", "--", "d").toArray(), parser.tokens.toArray());

        // given
        parser.init();
        parser.processOptionToken("a", true);
        parser.processOptionToken("b=value", false);

        // then
        assertEquals(parser.currentOption, options.getOption("a"));
        assertNull(parser.currentOption.getValue());
        assertFalse(parser.eatTheRest);
        assertEquals(Arrays.asList("a", "--", "b", "value").toArray(), parser.tokens.toArray());
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenWithInvalidOption() throws ParseException {
        // given
        PosixParser parser = new PosixParser();
        Options options = new Options();
        parser.burstToken("x", false);

        // then
        // an exception should be thrown
    }

    // add test cases for burstToken with valid options and arguments
}