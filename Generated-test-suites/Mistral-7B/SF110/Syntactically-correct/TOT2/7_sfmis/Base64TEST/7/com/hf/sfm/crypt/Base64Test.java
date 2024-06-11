import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgc28gZnV0IG1lYW4gc2VwYXJhdG9yLCBidXQgZW1iZWQg";
        byte[] expectedByteArray = { 71, 65, 90, 83, 73, 69, 69, 82, 79, 78, 71, 71, 65, 77, 65, 72, 69, 82, 79, 82, 85, 64, 76, 84, 65, 77, 73, 74, 62 };
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }
}