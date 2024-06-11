import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String input = "Apple";
        String expected = "APPL";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        try {
            metaphone.metaphone(input);
        } catch (EncoderException e) {
            Assert.assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        String input = "AppleAppleAppleAppleAppleApple";
        String expected = "APPLAPPL";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }
}