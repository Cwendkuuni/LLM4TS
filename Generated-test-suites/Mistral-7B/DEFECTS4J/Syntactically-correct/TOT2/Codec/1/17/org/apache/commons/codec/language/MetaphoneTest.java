import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneBasic() {
        Metaphone encoder = new Metaphone();

        String input = "apple";
        String output = encoder.metaphone(input);
        String expected = "APEL";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialTwoCharExceptions() {
        Metaphone encoder = new Metaphone();

        String input = "canon";
        String output = encoder.metaphone(input);
        String expected = "KAN";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialAEXception() {
        Metaphone encoder = new Metaphone();

        String input = "appleae";
        String output = encoder.metaphone(input);
        String expected = "APELAE";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialWRException() {
        Metaphone encoder = new Metaphone();

        String input = "water";
        String output = encoder.metaphone(input);
        String expected = "WATR";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialXException() {
        Metaphone encoder = new Metaphone();

        String input = "xylophonix";
        String output = encoder.metaphone(input);
        String expected = "KSXL";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialKGException() {
        Metaphone encoder = new Metaphone();

        String input = "king";
        String output = encoder.metaphone(input);
        String expected = "KIN";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialAEException() {
        Metaphone encoder = new Metaphone();

        String input = "baker";
        String output = encoder.metaphone(input);
        String expected = "BAKR";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialWHException() {
        Metaphone encoder = new Metaphone();

        String input = "white";
        String output = encoder.metaphone(input);
        String expected = "WHT";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneInitialSCException() {
        Metaphone encoder = new Metaphone();

        String input = "scissors";
        String output = encoder.metaphone(input);
        String expected = "SISR";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleTwoCharExceptions() {
        Metaphone encoder = new Metaphone();

        String input = "banana";
        String output = encoder.metaphone(input);
        String expected = "BANA";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleCIAException() {
        Metaphone encoder = new Metaphone();

        String input = "ciao";
        String output = encoder.metaphone(input);
        String expected = "SXO";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleCSEException() {
        Metaphone encoder = new Metaphone();

        String input = "ceiling";
        String output = encoder.metaphone(input);
        String expected = "SIL";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleSCYException() {
        Metaphone encoder = new Metaphone();

        String input = "sky";
        String output = encoder.metaphone(input);
        String expected = "S";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleCH() {
        Metaphone encoder = new Metaphone();

        String input = "church";
        String output = encoder.metaphone(input);
        String expected = "KURCH";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleDGE() {
        Metaphone encoder = new Metaphone();

        String input = "edge";
        String output = encoder.metaphone(input);
        String expected = "JED";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleDGI() {
        Metaphone encoder = new Metaphone();

        String input = "digit";
        String output = encoder.metaphone(input);
        String expected = "JET";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleDGY() {
        Metaphone encoder = new Metaphone();

        String input = "digital";
        String output = encoder.metaphone(input);
        String expected = "JETL";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleG() {
        Metaphone encoder = new Metaphone();

        String input = "gopher";
        String output = encoder.metaphone(input);
        String expected = "GF";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleGH() {
        Metaphone encoder = new Metaphone();

        String input = "ghost";
        String output = encoder.metaphone(input);
        String expected = "F";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleGHsilent() {
        Metaphone encoder = new Metaphone();

        String input = "ghostly";
        String output = encoder.metaphone(input);
        String expected = "F";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleQ() {
        Metaphone encoder = new Metaphone();

        String input = "quiet";
        String output = encoder.metaphone(input);
        String expected = "K";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleS() {
        Metaphone encoder = new Metaphone();

        String input = "session";
        String output = encoder.metaphone(input);
        String expected = "S";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleSH() {
        Metaphone encoder = new Metaphone();

        String input = "shoes";
        String output = encoder.metaphone(input);
        String expected = "S";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleSIO() {
        Metaphone encoder = new Metaphone();

        String input = "ios";
        String output = encoder.metaphone(input);
        String expected = "I";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleSIA() {
        Metaphone encoder = new Metaphone();

        String input = "sea";
        String output = encoder.metaphone(input);
        String expected = "S";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneMiddleTH() {
        Metaphone encoder = new Metaphone();

        String input = "theatre";
        String output = encoder.metaphone(input);
        String expected = "T";

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneEdgeCaseLongString() {
        Metaphone encoder = new Metaphone();

        String input = "thisisatesttocheckmetaphonefunctionalitywithalongstringofcharacters";
        String output = encoder.metaphone(input);
        String expected = "TISSTISCHKMETFALONGSTRNGAFALITYTWMETHAF";

        Assert.assertEquals(expected, output);
    }

    @Test(expected = DecoderException.class)
    public void testMetaphoneInvalidInput() {
        Metaphone encoder = new Metaphone();

        String input = "12345";
        encoder.metaphone(input);
    }

    @Test(expected = DecoderException.class)
    public void testMetaphoneNullInput() {
        Metaphone encoder = new Metaphone();

        encoder.metaphone(null);
    }

    @Test
    public void testMetaphoneMaxCodeLenException() {
        Metaphone encoder = new Metaphone();

        String input = "metaphoneencodertest";
        encoder.setMaxCodeLen(5);
        String output = encoder.metaphone(input);
        String expected = "METAPHONETEST";

        Assert.assertEquals(expected, output);
    }
}