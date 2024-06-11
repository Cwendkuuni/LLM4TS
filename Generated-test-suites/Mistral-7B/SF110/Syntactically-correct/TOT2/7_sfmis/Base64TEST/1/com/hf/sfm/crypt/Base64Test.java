import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "RG9jdW1lbnQ=";
        byte[] expectedOutput = {48, 65, 78, 69, 79, 71, 75, 74, 68, 65, 84, 83, 69, 70, 75};
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    // Edge case: Empty string
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithEmptyString() {
        Base64.altBase64ToByteArray("");
    }

    // Edge case: String with odd length
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithStringLengthNotMultipleOfFour() {
        String input = "RnBQ==";
        Base64.altBase64ToByteArray(input);
    }
}