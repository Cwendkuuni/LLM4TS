import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private final StringEncoder caverphoneEncoder = new Caverphone();

    @Test
    public void testEmptyString() {
        String input = "";
        String expectedOutput = "1111111111";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of empty string is incorrect", expectedOutput, output);
    }

    @Test
    public void testLowerCaseString() {
        String input = "hello world";
        String expectedOutput = "h3l2o2w2l2d2";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of lowercase string is incorrect", expectedOutput, output);
    }

    @Test
    public void testStringWithDigits() {
        String input = "hello123world";
        String expectedOutput = "h3l2o2w2l2d2111";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of string with digits is incorrect", expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        String input = null;
        caverphoneEncoder.encode(input);
    }

    @Test
    public void testStringWithSpecialCharacters() {
        String input = "!@#$%^&*()_+-=[]{}|;':\",.<>/\\? ";
        String expectedOutput = "2222222222";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of string with special characters is incorrect", expectedOutput, output);
    }

    @Test
    public void testStringWithStartOptions() {
        String input = "enough cough";
        String expectedOutput = "enou2f cou2f";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of string with start options is incorrect", expectedOutput, output);
    }

    @Test
    public void testStringWithReplacements() {
        String input = "action tchapter";
        String expectedOutput = "a2cion t2chapter";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of string with replacements is incorrect", expectedOutput, output);
    }

    @Test
    public void testStringWithRemovals() {
        String input = "two threes and two";
        String expectedOutput = "2222";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of string with removals is incorrect", expectedOutput, output);
    }

    @Test
    public void testStringWithMixedCases() {
        String input = "Hello World!";
        String expectedOutput = "h3l2o2w2l2d2";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of string with mixed cases is incorrect", expectedOutput, output);
    }

    @Test
    public void testStringWithLongLength() {
        String input = StringUtils.repeat("a", 100);
        String expectedOutput = StringUtils.repeat("2", 100);
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("Caverphone encoding of extremely long string is incorrect", expectedOutput, output);
    }

    @Test
    public void testIsCaverphoneEqual() {
        String str1 = "hello world";
        String str2 = "hello world";
        boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
        Assert.assertTrue("Caverphone strings that are the same should be considered equal", result);

        String str3 = "hello world";
        String str4 = "hello world!";
        result = caverphoneEncoder.isCaverphoneEqual(str3, str4);
        Assert.assertFalse("Caverphone strings that are not the same should not be considered equal", result);
    }

    @Test
    public void testIsCaverphoneEqualWithDifferentStrings() {
        String str1 = "hello world";
        String str2 = "hello world!";
        String str3 = "hello123 world";
        String str4 = "hello world123";

        boolean result1 = caverphoneEncoder.isCaverphoneEqual(str1, str2);
        Assert.assertFalse("Caverphone strings that are not the same should not be considered equal", result1);

        boolean result2 = caverphoneEncoder.isCaverphoneEqual(str1, str3);
        Assert.assertFalse("Caverphone strings with different digits should not be considered equal", result2);

        boolean result3 = caverphoneEncoder.isCaverphoneEqual(str1, str4);
        Assert.assertFalse("Caverphone strings with different positions of digits should not be considered equal", result3);
    }

    @Test
    public void testIsCaverphoneEqualWithNullStrings() {
        String str1 = null;
        String str2 = null;
        boolean result = caverphoneEncoder.isCaverphoneEqual(str1, str2);
        Assert.assertTrue("Caverphone null strings should be considered equal", result);

        String str3 = null;
        String str4 = "hello world";
        result = caverphoneEncoder.isCaverphoneEqual(str3, str4);
        Assert.assertFalse("Caverphone null strings should not be considered equal to non-null strings", result);
    }

    @Test(expected = NullPointerException.class)
    public void testIsCaverphoneEqualWithNullFirstString() {
        String str1 = null;
        String str2 = "hello world";
        caverphoneEncoder.isCaverphoneEqual(str1, str2);
    }

    @Test(expected = NullPointerException.class)
    public void testIsCaverphoneEqualWithNullSecondString() {
        String str1 = "hello world";
        String str2 = null;
        caverphoneEncoder.isCaverphoneEqual(str1, str2);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidCaverphone() {
        String caverphone = "11111111111111111111111111111111";
        caverphoneEncoder.decode(caverphone);
    }
}