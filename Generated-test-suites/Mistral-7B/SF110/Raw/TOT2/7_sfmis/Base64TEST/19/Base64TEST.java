import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_ValidString() {
        String base64EncodedString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGxlIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] expectedByteArray = {-65, -65, 6, 7, 6, 7, 6, 8, 6, 9, 6, 10, 6, 11, 6, 12, 6, 13, 6, 14, 6, 15, 6, 16, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56};
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64EncodedString);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }
}