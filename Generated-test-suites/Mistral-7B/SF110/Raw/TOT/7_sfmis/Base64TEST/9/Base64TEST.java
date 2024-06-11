import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = {26, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};

        byte[] actualByteArray = altBase64ToByteArray(input);

        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_illegalArgumentException() {
        String input = "ABCDEF"; // String length must be a multiple of four.
        byte[] actualByteArray = altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggKHJlc3MgYW5kIGZvdWwgdG8gcGxhbnQgYW5k";
        byte[] expectedByteArray = {56, 53, 48, 49, 44, 51, 55, 50, 51, 45, 53, 49, 48, 48, 45, 52, 48, 51, 49, 49, 50, 49, 49, 51, 46, 48, 49, 50, 53, 50, 53, 52, 51, 51, 51, 49, 53, 53, 54, 51, 51, 50, 48, 50, 49, 52};

        byte[] actualByteArray = base64ToByteArray(input);

        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_illegalArgumentException() {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggKHJlc3MgYW5kIGZvdWwgdG8gcGxhbnQgYW5k"; // Input is already base64 encoded and does not need to be decoded again
        byte[] actualByteArray = base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] inputByteArray = {26, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        String actualOutput = byteArrayToAltBase64(inputByteArray);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] inputByteArray = {56, 53, 48, 49, 44, 51, 55, 50, 51, 45, 53, 49, 48, 48, 45, 52, 48, 51, 49, 49, 50, 49, 49, 51, 46, 48, 49, 50, 53, 50, 53, 52, 51, 51, 51, 49, 53, 53, 54, 51, 51, 50, 48, 50, 49, 52};
        String expectedOutput = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggKHJlc3MgYW5kIGZvdWwgdG8gcGxhbnQgYW5k";

        String actualOutput = byteArrayToBase64(inputByteArray);

        Assert.assertEquals(expectedOutput, actualOutput);
    }
}