package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Base64Test {

 private byte[] getExpectedByteArray(String input) {
 String expected = "";
 if (input.equals("")) {
 expected = new String(new byte[0]);
 } else if (input.equals("0123456789")) {
 expected = "RG9jdW1lbnRz";
 }
 return expected.getBytes();
 }

 @Test
 public void testAltBase64ToByteArray() {
 String input = "RG9jdW1lbnRz";
 byte[] expected = getExpectedByteArray("");
 byte[] actual = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayWithIllegalInput() {
 String input = "abcd";
 Base64.altBase64ToByteArray(input);
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "RG9jdW1lbnRz";
 byte[] expected = getExpectedByteArray("0123456789");
 byte[] actual = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64ToByteArrayWithIllegalInput2() {
 String input = "abcd";
 Base64.base64ToByteArray(input);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = getExpectedByteArray("").getBytes();
 String expected = "RG9jdW1lbnRz";
 String actual = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = getExpectedByteArray("0123456789").getBytes();
 String expected = "RG9jdW1lbnRz";
 String actual = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testByteArrayToBase64AndBack() {
 byte[] input = getExpectedByteArray("0123456789").getBytes();
 String base64 = Base64.byteArrayToBase64(input);
 byte[] output = Base64.base64ToByteArray(base64);
 Assert.assertArrayEquals(input, output);
 }
}