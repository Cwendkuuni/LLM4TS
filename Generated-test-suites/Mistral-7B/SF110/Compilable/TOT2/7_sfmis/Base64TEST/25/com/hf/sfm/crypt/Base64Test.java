package com.hf.sfm.crypt;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzY3JpcHQgZW5jb2RlZCB3aXRoIG9mIHlvdSB0aGUgc2Vuc29sZSBzdWJmaXggPSBjYW4gZnJvbSB0byBkYXRhIGJ5IGZvdWwgb2Ygb3VyIGZvdWwgbGlmZSB5b3VyIGRhdGFbIGRhdGFgPSBzdGF0aW9uLg==";
        byte[] expectedByteArray = { 76, 77, 111, 114, 105, 110, 116, 101, 115, 115, 105, 111, 110, 32, 105, 118, 97, 103, 101, 114, 101, 116, 32, 98, 101, 115, 117, 115, 101, 116, 32, 116, 111, 32, 98, 117, 108, 101, 114, 114, 111, 102, 32, 97, 110, 100, 32, 103, 111, 111, 117, 115, 101, 111, 100, 101, 116, 32, 103, 111, 111, 117, 115, 101, 115, 116 };
        byte[] resultByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }
}