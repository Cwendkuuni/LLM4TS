package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "Robert is a great programmer!";
 byte[] expectedByteArray = {72, 101, 108, 108, 111, 111, 32, 103, 111, 116, 104, 101, 114, 101, 108, 100, 33, 32, 68, 101, 112, 111, 117, 116, 107, 97, 108, 111, 114, 108, 100, 33};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayInvalidLength() {
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("This string is not a multiple of four."));
 }

 @Test
 public void testAltBase64ToByteArrayWithPadding() {
 String testString = "This is a test with padding.";
 byte[] expectedByteArray = {84, 104, 111, 116, 32, 84, 111, 117, 108, 100, 32, 83, 111, 119, 103, 111, 114, 116, 104, 32, 84, 111, 114, 114, 101, 116, 101, 116, 108, 101, 115, 101, 115, 46};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithNoPadding() {
 String testString = "This is a test.";
 byte[] expectedByteArray = {83, 115, 104, 115, 116, 104, 111, 119, 110, 32, 83, 111, 115, 116, 101, 114, 117, 115, 104, 101, 32, 84, 111, 114, 114, 111, 114, 108, 100};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8gV29ybGQh";
 byte[] expectedByteArray = {71, 73, 76, 67, 84, 83, 79, 71, 65, 72, 65, 61, 74, 61, 77, 73, 83, 69, 69, 82, 84};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArrayInvalidLength() {
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray("This string is not a multiple of four."));
 }

 @Test
 public void testBase64ToByteArrayWithPadding() {
 String testString = "SGVsbG8gV1+DQ==";
 byte[] expectedByteArray = {71, 73, 76, 67, 84, 83, 79, 71, 65, 72, 65, 61, 74, 61, 77, 73, 83, 69, 69, 82, 84, 65, 65};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArrayWithNoPadding() {
 String testString = "SGVsbG8gV19hMw==";
 byte[] expectedByteArray = {71, 73, 76, 67, 84, 83, 79, 71, 65, 72, 65, 61, 74, 61, 77, 73, 83, 69, 69, 82, 84};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = {72, 101, 108, 108, 111, 111, 32, 103, 111, 116, 104, 101, 114, 101, 108, 100, 33, 32, 68, 101, 112, 111, 117, 116, 107, 97, 108, 111, 114, 108, 100, 33};
 String expectedTestString = "Robert is a great programmer!";
 String resultTestString = Base64.byteArrayToAltBase64(testByteArray);
 Assert.assertEquals(expectedTestString, resultTestString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = {71, 73, 76, 67, 84, 83, 79, 71, 65, 72, 65, 61, 74, 61, 77, 73, 83, 69, 69, 82, 84};
 String expectedTestString = "SGVsbG8gV29ybGQh";
 String resultTestString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedTestString, resultTestString);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] testByteArray = {71, 73, 76, 67, 84, 83, 79, 71, 65, 72, 65, 61, 74, 61, 77, 73, 83, 69, 69, 82, 84, 65, 65};
 String expectedTestString = "SGVsbG8gV1+DQ==";
 String resultTestString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedTestString, resultTestString);
 }

 @Test
 public void testByteArrayToBase64WithNoPadding() {
 byte[] testByteArray = {71, 73, 76, 67, 84, 83, 79, 71, 65, 72, 65, 61, 74, 61, 77, 73, 83, 69, 69, 82, 84, 83, 83};
 String expectedTestString = "SGVsbG8gV19hMw==";
 String resultTestString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedTestString, resultTestString);
 }
}