package com.hf.sfm.crypt;
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "RnJvbSBhbmQgaXMgZW1i";
 byte[] expectedOutput = {52, 50, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80};
 byte[] output = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayWithInvalidLength() {
 String input = "RnJvbSBhbmQg";
 Base64.altBase64ToByteArray(input);
 }

 @Test
 public void testAltBase64ToByteArrayWithPadding() {
 String input = "RnJvbSBhbmQgZW1i";
 byte[] expectedOutput = {52, 50, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 80, 80};
 byte[] output = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedOutput = {56, 53, 53, 53, 48, 51, 51, 49, 57, 48, 55, 50, 55, 53, 54, 48, 54, 53, 51, 55, 54, 51, 50, 54, 51, 49, 51, 54, 54, 52, 51, 52, 54, 55, 55, 53, 52, 52, 51, 48, 51, 53, 54, 49, 51, 54, 54, 51, 53, 55, 54, 55, 53, 53};
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testBase64ToByteArrayWithPadding() {
 String input = "SGVsbG8sIFdvcmxkIQ===";
 byte[] expectedOutput = {56, 53, 53, 53, 48, 51, 51, 49, 57, 48, 55, 50, 55, 53, 54, 48, 54, 53, 51, 55, 54, 51, 50, 54, 51, 49, 51, 54, 54, 52, 51, 52, 54, 55, 55, 53, 52, 52, 51, 48, 51, 53, 54, 49, 51, 54, 54, 51, 53, 55, 54, 55, 53, 53, 80, 80, 80};
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = {52, 50, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80};
 String expectedOutput = "RnJvbSBhbmQgaXMgZW1i";
 String output = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testByteArrayToAltBase64WithInvalidLength() {
 byte[] input = {52, 50, 53, 54, 55, 56};
 Base64.byteArrayToAltBase64(input);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = {56, 53, 53, 53, 48, 51, 51, 49, 57, 48, 55, 50, 55, 53, 54, 48, 54, 53, 51, 55, 54, 51, 50, 54, 51, 49, 51, 54, 54, 52, 51, 52, 54, 55, 55, 53, 52, 52, 51, 48, 51, 53, 54, 49, 51, 54, 54, 51, 53, 55, 54, 55, 53, 53};
 String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] input = {56, 53, 53, 53, 48, 51, 51, 49, 57, 48, 55, 50, 55, 53, 54, 48, 54, 53, 51, 55, 54, 51, 50, 54, 51, 49, 51, 54, 54, 52, 51, 52, 54, 55, 55, 53, 52, 52, 51, 48, 51, 53, 54, 49, 51, 54, 54, 51, 53, 55, 54, 55, 53, 53, 80, 80, 80};
 String expectedOutput = "SGVsbG8sIFdvcmxkIQ===";
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

}