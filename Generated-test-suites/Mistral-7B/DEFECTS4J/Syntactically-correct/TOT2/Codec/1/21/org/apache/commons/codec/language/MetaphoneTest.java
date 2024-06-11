import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone encoder = new Metaphone();

        String original = "Apple";
        String encoded = encoder.metaphone(original);
        String expected = "AAP";
        Assert.assertEquals(expected, encoded);

        original = "Banana";
        encoded = encoder.metaphone(original);
        expected = "BBB";
        Assert.assertEquals(expected, encoded);

        original = "Cat";
        encoded = encoder.metaphone(original);
        expected = "KAT";
        Assert.assertEquals(expected, encoded);

        original = "Dog";
        encoded = encoder.metaphone(original);
        expected = "DOG";
        Assert.assertEquals(expected, encoded);

        original = "Elephant";
        encoded = encoder.metaphone(original);
        expected = "ELP";
        Assert.assertEquals(expected, encoded);
    }

    @Test(expected = DecoderException.class)
    public void testNullInput() {
        Metaphone encoder = new Metaphone();
        String original = null;
        encoder.metaphone(original);
    }

    @Test(expected = DecoderException.class)
    public void testEmptyInput() {
        Metaphone encoder = new Metaphone();
        String original = "";
        encoder.metaphone(original);
    }

    @Test(expected = DecoderException.class)
    public void testInputWithNumbers() {
        Metaphone encoder = new Metaphone();
        String original = "Apple123";
        encoder.metaphone(original);
    }

    @Test(expected = DecoderException.class)
    public void testInputWithSymbols() {
        Metaphone encoder = new Metaphone();
        String original = "Apple@#$%";
        encoder.metaphone(original);
    }
}