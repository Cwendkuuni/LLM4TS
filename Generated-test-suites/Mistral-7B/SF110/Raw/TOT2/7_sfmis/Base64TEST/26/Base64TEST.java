import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "RnJvbSBvZiBmaWxlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] expectedByteArray = {48, 51, 49, 49, 56, 49, 56, 45, 50, 51, 45, 56, 56, 52, 53, 55, 57, 51, 50, 51, 53, 54, 51, 55, 51, 51, 56, 54, 50, 53, 55, 56, 54, 50, 51, 51, 52, 56, 53, 54, 55, 55, 53, 55, 55, 56, 53, 56, 56, 52, 51, 53, 53, 55, 53, 56, 52};
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    // ... (other test cases for other methods)
}