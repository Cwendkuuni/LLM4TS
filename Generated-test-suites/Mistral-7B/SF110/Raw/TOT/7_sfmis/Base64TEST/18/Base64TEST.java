import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expected = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
        byte[] result = altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        expected = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, 62, 62};
        result = altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        input = "";
        try {
            result = altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expected = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
        byte[] result = base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        expected = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, 62, 62};
        result = base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        input = "";
        try {
            result = base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);

        input = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, 62, 62};
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        result = byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);

        input = new byte[0];
        String emptyResult = byteArrayToAltBase64(input);
        Assert.assertEquals("", emptyResult);

        input = new byte[]{32, 33, 52, 53, 54, -1, 56, 57, 58, 59, 60, 61, 62, -1, 62, 62};
        try {
            result = byteArrayToAltBase64(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = byteArrayToBase64(input);
        Assert.assertEquals(expected, result);

        input = new byte[]{32, 33, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, -1, 62, 62};
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        result = byteArrayToBase64(input);
        Assert.assertEquals(expected, result);

        input = new byte[0];
        String emptyResult = byteArrayToBase64(input);
        Assert.assertEquals("", emptyResult);

        input = new byte[]{32, 33, 52, 53, 54, -1, 56, 57, 58, 59, 60, 61, 62, -1, 62, 62};
        try {
            result = byteArrayToBase64(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}