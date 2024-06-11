package com.hf.sfm.crypt;
import org.junit.Assert;import org.junit.Test;import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expected = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
 byte[] result = altBase64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testAltBase64ToByteArray_EmptyString() {
 String input = "";
 byte[] result = altBase64ToByteArray(input);
 Assert.assertArrayEquals(new byte[0], result);
 }

 @Test
 public void testAltBase64ToByteArray_InvalidLength() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 try {
 altBase64ToByteArray(input);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 //Expected
 }
 }

 @Test
 public void testAltBase64ToByteArray_InvalidCharacter() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/!";
 try {
 altBase64ToByteArray(input);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 //Expected
 }
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expected = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
 byte[] result = base64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBase64ToByteArray_EmptyString() {
 String input = "";
 byte[] result = base64ToByteArray(input);
 Assert.assertArrayEquals(new byte[0], result);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
 String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String result = byteArrayToAltBase64(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
 String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String result = byteArrayToBase64(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64_EmptyArray() {
 byte[] input = new byte[0];
 String result = byteArrayToBase64(input);
 Assert.assertEquals("", result);
 }
}