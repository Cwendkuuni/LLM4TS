package com.hf.sfm.crypt;
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "TestString";
        byte[] expectedOutput = new byte[] { 72, 65, 73, 74, 68, 69, 84, 73, 74, 65, 64, 69, 66, 65, 64, 65, 78, 74, 68, 32, 83, 65, 61, 63, 68, 69, 76, 65, 72, 73, 65, 74, 73, 74, 61, 68, 65, 78, 69, 67, 84, 73, 74, 65, 64, 65, 72, 74, 62, 76, 65, 72 };
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    // ... other test cases for other methods here
}