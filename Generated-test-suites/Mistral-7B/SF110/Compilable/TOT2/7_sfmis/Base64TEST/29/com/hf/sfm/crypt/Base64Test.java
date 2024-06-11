package com.hf.sfm.crypt;
import org.junit.Assert;
import org.junit.Test;
import com.hf.sfm.crypt.Base64;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "RnJvbSBvZiBkZWxldGg=";
        byte[] expectedByteArray = {-65, -65, 6, 101, 120, 105, 98, 108, 101, 116, 116, 108, 111, 118, 101, 32, 32, 50, 51, 52, 53, 54, 55, 56, 57};
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    // Add edge cases and exception handling scenarios here
}