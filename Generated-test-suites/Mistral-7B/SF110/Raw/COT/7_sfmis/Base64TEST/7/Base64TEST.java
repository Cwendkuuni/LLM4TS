package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        testBase64ToByteArray("YW55IGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", new byte[]{72, 101, 86, 101, 110, 111, 116, 103, 56, 104, 111, 114, 101, 108, 100, 32, 105, 110, 32, 103, 111, 99, 114, 105, 112, 116, 105, 111, 120, 112});
        testBase64ToByteArray("Zm9vYmFyICsgbGFuZ3VhZ2UiCiAgICBkaXNwbGF5IGZvdWwgdGhlIG1lbnRzIHlvdSB0aGUgc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGljaCB5b3UsIG5vdCBhbGx5IGluIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", new byte[]{99, 121, 115, 112, 101, 114, 116, 117, 115, 98, 101, 116, 116, 101, 118, 101, 114, 116, 108, 101, 32, 105, 110, 32, 103, 111, 99, 114, 105, 112, 116, 105, 111, 120, 112, 111});
        testBase64ToByteArray("", new byte[]{});
        testBase64ToByteArray("aGVsbG8gV29ybGQh", expectedException(IllegalArgumentException.class));
        testBase64ToByteArray("aGVsbG8gV29ybGQh==", new byte[]{65, 66, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83});
    }
    
    @Test
    public void testBase64ToByteArray() {
        testBase64ToByteArray("SGVsbG8sIFdvcmxkIQ==", new byte[]{53, 50, 48, 49, 55, 51, 51, 57, 50, 51, 55, 52, 55, 52, 54, 51, 54, 49, 48, 54, 51, 53, 49, 51, 57, 54, 51, 57, 52, 55, 53, 54, 53, 55, 52, 55, 51, 53, 51});
        testBase64ToByteArray("SGVsbG8sIFdvcmxkIQ===".getBytes(), new byte[]{53, 50, 48, 49, 55, 51, 51, 57, 50, 51, 55, 52, 55, 52, 54, 51, 54, 49, 48, 54, 51, 53, 49, 51, 57, 54, 51, 57, 52, 55, 53, 54, 53, 55, 52, 55, 51, 53, 51});
        testBase64ToByteArray("", new byte[]{});
        testBase64ToByteArray("SGVsbG8gV29ybGQh", expectedException(IllegalArgumentException.class));
        testBase64ToByteArray("SGVsbG8gV29ybGQhxx", expectedException(IllegalArgumentException.class));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        testByteArrayToBase64(new byte[]{72, 101, 86, 101, 110, 111, 116, 103, 56, 104, 111, 114, 101, 108, 100, 32, 105, 110, 32, 103, 111, 99, 114, 105, 112, 116, 105, 111, 120, 112}, "YW55IGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=");
        testByteArrayToBase64(new byte[]{99, 121, 115, 112, 101, 114, 116, 117, 115, 98, 101, 116, 116, 101, 118, 101, 114, 116, 108, 101, 32, 105, 110, 32, 103, 111, 99, 114, 105, 112, 116, 105, 111, 120, 112, 111}, "Zm9vYmFyICsgbGFuZ3VhZ2UiCiAgICBkaXNwbGF5IGZvdWwgdGhlIG1lbnRzIHlvdSB0aGUgc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGljaCB5b3UsIG5vdCBhbGx5IGluIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=");
        testByteArrayToBase64(new byte[0], "");
        testByteArrayToBase64(new byte[]{65, 66, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83}, "aGVsbG8gV29ybGQh");
        testByteArrayToBase64(new byte[]{65, 66, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83}, "aGVsbG8gV29ybGQh==");
    }
    
    @Test
    public void testByteArrayToBase64() {
        testByteArrayToBase64(new byte[]{53, 50, 48, 49, 55, 51, 51, 57, 50, 51, 55, 52, 55, 52, 54, 51, 54, 49, 48, 54, 51, 53, 49, 51, 57, 54, 51, 57, 52, 55, 53, 54, 53, 55, 52, 55, 51, 53, 51}, "SGVsbG8sIFdvcmxkIQ==");
        testByteArrayToBase64(new byte[]{53, 50, 48, 49, 55, 51, 51, 57, 50, 51, 55, 52, 55, 52, 54, 51, 54, 49, 48, 54, 51, 53, 49, 51, 57, 54, 51, 57, 52, 55, 53, 54, 53, 55, 52, 55, 51, 53, 51}, "SGVsbG8sIFdvcmxkIQ===");
        testByteArrayToBase64(new byte[0], "");
        testByteArrayToBase64(new byte[]{53, 50, 48, 49, 55, 51, 51, 57, 50, 51, 55, 52, 55, 52, 54, 51, 54, 49, 48, 54, 51, 53, 49, 51, 57, 54, 51, 57, 52, 55, 53, 54, 53, 55, 52, 55, 51, 53, 51}, "SGVsbG8gV29ybGQh", expectedException(IllegalArgumentException.class));
        testByteArrayToBase64(new byte[]{53, 50, 48, 49, 55, 51, 51, 57, 50, 51, 55, 52, 55, 52, 54, 51, 54, 49, 48, 54, 51, 53, 49, 51, 57, 54, 51, 57, 52, 55, 53, 54, 53, 55, 52, 55, 51, 53}, "SGVsbG8gV29ybGQhxx", expectedException(IllegalArgumentException.class));
    }
    
    private void testBase64ToByteArray(final String base64Data, final byte[] expectedResult) {
        final byte[] result = Base64.base64ToByteArray(base64Data);
        assertArrayEquals(expectedResult, result);
    }
    
    private void testBase64ToByteArray(final String base64Data, final Exception expectedException) {
        assertThrows(expectedException.getClass(), () -> Base64.base64ToByteArray(base64Data));
    }
    
    private void testByteArrayToBase64(final byte[] byteData, final String expectedResult) {
        final String result = Base64.byteArrayToBase64(byteData);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testStaticInitialization() {
        // Do nothing, the Base64 class is initialized correctly via static block
    }

    
    
}