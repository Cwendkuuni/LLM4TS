import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArrayWithValidInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expectedOutput = {49, 52, 48, 56, 45, 48, 48, 49, 56, 51, 56, 55, 55, 54, 52, 53, 54, 51, 48, 53, 48, 48, 54, 52, 53, 54, 51, 45, 54, 52, 53, 56, 48, 49, 56, 52, 51, 55, 53, 55, 53, 54, 51, 55, 52, 51, 53, 55, 53, 54, 51};

        byte[] output = altBase64ToByteArray(input);

        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidInputLength() {
        String input = "SGVsbG8sIFdvcmxkIQ===";

        try {
            altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==%";

        try {
            altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArrayWithValidInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expectedOutput = {49, 52, 48, 56, 45, 48, 48, 49, 56, 51, 56, 55, 55, 54, 52, 53, 54, 51, 48, 53, 48, 48, 54, 52, 53, 54, 51, 45, 54, 52, 53, 56, 48, 49, 56, 52, 51, 55, 53, 55, 53, 54, 51, 55, 52, 51, 53, 55, 53, 54, 51};

        byte[] output = base64ToByteArray(input);

        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidInputLength() {
        String input = "SGVsbG8sIFdvcmxkIQ===";

        try {
            base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArrayWithInvalidInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==%";

        try {
            base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64WithValidInput() {
        byte[] input = {49, 52, 48, 56, 45, 48, 48, 49, 56, 51, 56, 55, 55, 54, 52, 53, 54, 51, 48, 53, 48, 48, 54, 52, 53, 54, 51, 45, 54, 52, 53, 56, 48, 49, 56, 52, 51, 55, 53, 55, 53, 54, 51, 55, 52, 51, 53, 55, 53, 54, 51};
        String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";

        String output = byteArrayToAltBase64(input);

        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToAltBase64WithEmptyInput() {
        byte[] input = {};
        String expectedOutput = "";

        String ouput = byteArrayToAltBase64(input);

        Assert.assertEquals(expectedOutput, ouput);
    }

    @Test
    public void testByteArrayToBase64WithValidInput() {
        byte[] input = {49, 52, 48, 56, 45, 48, 48, 49, 56, 51, 56, 55, 55, 54, 52, 53, 54, 51, 48, 53, 48, 48, 54, 52, 53, 54, 51, 45, 54, 52, 53, 56, 48, 49, 56, 52, 51, 55, 53, 55, 53, 54, 51, 55, 52, 51, 53, 55, 53, 54, 51};
        String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";

        String output = byteArrayToBase64(input);

        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64WithEmptyInput() {
        byte[] input = {};
        String expectedOutput = "";

        String output = byteArrayToBase64(input);

        Assert.assertEquals(expectedOutput, output);
    }
}