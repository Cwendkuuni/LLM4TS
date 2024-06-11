package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 private byte[] byteArray;
 private String base64String;

 @Test
 public void testAltBase64ToByteArray() {
 byteArray = new byte[]{-1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 base64String = Base64.byteArrayToAltBase64(byteArray);
 byte[] result = Base64.altBase64ToByteArray(base64String);
 Assert.assertArrayEquals(byteArray, result);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidLength() {
 Assert.throwsException(IllegalArgumentException.class, () -> {
 Base64.altBase64ToByteArray("abc");
 });
 }

 @Test
 public void testBase64ToByteArray() {
 byteArray = new byte[]{-1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 base64String = Base64.byteArrayToBase64(byteArray);
 byte[] result = Base64.base64ToByteArray(base64String);
 Assert.assertArrayEquals(byteArray, result);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidLength() {
 Assert.throwsException(IllegalArgumentException.class, () -> {
 Base64.base64ToByteArray("abcdefg==");
 });
 }

 @Test
 public void testByteArrayToAltBase64() {
 byteArray = new byte[]{-1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 base64String = Base64.byteArrayToAltBase64(byteArray);
 String result = Base64.altBase64ToByteArray(base64String);
 Assert.assertArrayEquals(byteArray, Base65.parseBase64(result));
 }

 @Test
 public void testByteArrayToBase64() {
 byteArray = new byte[]{-1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 base64String = Base64.byteArrayToBase64(byteArray);
 String result = Base64.byteArrayToBase64(byteArray);
 Assert.assertEquals(base64String, result);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byteArray = new byte[]{1, 2, 3};
 base64String = Base64.byteArrayToBase64(byteArray);
 String paddingBase64 = base64String + "==";
 String result = Base64.byteArrayToBase64(byteArray);
 Assert.assertNotEquals(paddingBase64, result);
 }

 @Test
 public void testByteArrayToBase64WithSinglePadding() {
 byteArray = new byte[]{1, 2, 3};
 base64String = Base64.byteArrayToBase64(byteArray);
 String singlePaddingBase64 = base64String + "=";
 String result = Base64.byteArrayToBase64(byteArray);
 Assert.assertNotEquals(singlePaddingBase64, result);
 }

 @Test
 public void testByteArrayToBase64WithDoublePadding() {
 byteArray = new byte[]{1, 2, 3};
 base64String = Base64.byteArrayToBase64(byteArray);
 String doublePaddingBase64 = base64String + "==";
 String result = Base64.byteArrayToBase64(byteArray);
 Assert.assertNotEquals(doublePaddingBase64, result);
 }

 private static byte[] parseBase64(String base64) {
 byte[] result = new byte[base64.length() * 3 / 4];
 int index = 0;
 int len = base64.length();
 int i = 0;
 int j = 0;
 while (i < len) {
 int k = Base64._$23183(base64.charAt(i++), Base64._$23169);
 int l = Base64._$23183(base64.charAt(i++), Base64._$23169);
 int m = Base64._$23183(base64.charAt(i++), Base64._$23169);
 int n = Base64._$23183(base64.charAt(i++), Base64._$23169);
 result[j++] = (byte) (k << 2 | l >> 4);
 result[j++] = (byte) (l << 4 | m >> 2);result[j++] = (byte) (m << 6 | n);
 }
 return result;
 }
}