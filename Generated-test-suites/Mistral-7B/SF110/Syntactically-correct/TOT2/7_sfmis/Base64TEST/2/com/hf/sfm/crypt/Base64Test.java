import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "TestString";
        byte[] expectedOutput = new byte[] {'T', 'e', 's', 't', 'S', 't', 'r', 'i', 'n', 'g'};
        byte[] output = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhhdCBjYXQgY29tcHV0ZXIuCiAgICAtLVBhdGhhdCBjYXQgc2VjYXJpdHkgc2VwYXJhdGlvbnMgc2VwYXJhdGlvbnMgZW1haWxfc2l0ZXMgZXhwbGFpbmVkIGlzIGlmIHRoZSBzYW5jZSwKCiAgICAtLVBhdGhhdCBjYXQgY29tcHV0ZXIuCiAgICAtLVBhdGhhdCBjYXQgc2VjYXJpdHkgc2VwYXJhdGlvbnMgc2VwYXJhdGlvbnMgZW1haWxfc2l0ZXMgZXhwbGFpbmVkIGlzIHNlZW1zIHNlZW1zLg==";
        byte[] expectedOutput = new byte[] { 86, 111, 111, 103, 101, 114, 101, 116, 32, 105, 110, 103, 32, 105, 118, 101, 114, 101, 116, 117, 108, 100, 33, 103, 111, 111, 111, 103, 111, 111, 111, 32, 116, 111, 111, 32, 114, 111, 118, 101, 115, 116, 117, 108, 101, 33, 116, 111, 111, 111, 103, 111, 111, 111, 103, 111, 111, 111, 103 };
        byte[] output = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] {'T', 'e', 's', 't', 'S', 't', 'r', 'i', 'n', 'g'};
        String expectedOutput = "VG8gZW5jIHB1Yw==";
        String output = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        String input = "TestString"; // input string length is not a multiple of four
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = NullPointerException.class)
    public void testAltBase64ToByteArrayNullInput() {
        Base64.altBase64ToByteArray(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        String input = "TestString"; // input string length is not a multiple of four
        Base64.base64ToByteArray(input);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64ToByteArrayNullInput() {
        Base64.base64ToByteArray(null);
    }

    @Test(expected = NullPointerException.class)
    public void testByteArrayToAltBase64NullInput() {
        Base64.byteArrayToAltBase64(null);
    }
}