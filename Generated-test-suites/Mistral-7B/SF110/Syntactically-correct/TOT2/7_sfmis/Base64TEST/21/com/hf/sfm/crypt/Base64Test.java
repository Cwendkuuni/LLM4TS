import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray_ValidString() {
        String base64String = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZmlsZXMgZW5jb2RlIGxpbMggcGxhbnQgZm9yIGlwZXIgZnJvbSBhbmQgc28gc3RyYXN5IHBhcnRuZW1lbnQgc2VwYXJhdGlvbnMgc2VwYXJhdGlvbnMgZ29yZA==";
        byte[] expectedByteArray = new byte[] { 1, 2, 3, 4 }; // Replace this with the expected byte array for the given base64String
        byte[] resultByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }
}