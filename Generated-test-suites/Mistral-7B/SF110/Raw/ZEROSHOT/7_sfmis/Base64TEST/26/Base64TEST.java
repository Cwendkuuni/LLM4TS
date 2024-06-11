package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

 private byte[] getExpectedByteArray(String input) {
 byte[] expected = new byte[input.length() / 4 * 3 + (input.length() % 4 == 0 ? 0 : input.length() % 4)];
 return _$23180(input, true, expected);
 }

 @Test
 public void testAltBase64ToByteArray() {
 String input = "Zm9vMDpGb3RoQ29udGVudDpGb3RoQ29udGVudD4=";
 byte[] expected = getExpectedByteArray(input);
 byte[] result = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayWithInvalidStringLength() {
 String input = "Zm9vMDpGb3RoQ29udGVudDpGb3RoQ29udGVudD";
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "Zm9vMDpGb3RoQ29udGVudDpGb3RoQ29udGVudD4=";
 byte[] expected = getExpectedByteArray(input);
 byte[] result = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 String expected = "_ngocmQ==";
 String result = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 String expected = "Mg==ZXhhbXBsZSBmaWxlIHVzIGZvdWwgZm9yIGZvdWwgdG8=";
 String result = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidString() {
 String input = "Zm9vMDpGb3RoQ29udGVudDpGb3RoQ29udGVudD4x";
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidString() {
 String input = "Zm9vMDpGb3RoQ29udGVudDpGb3RoQ29udGVudD4x";
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 private byte[] _$23180(String input, boolean isAltBase64, byte[] expected) {
 if (input == null) {
 throw new IllegalArgumentException("The input string cannot be null.");
 }
 int i = input.length();
 int j = i / 4;
 if (4 * j != i) {
 throw new IllegalArgumentException("The input string length must be a multiple of four.");
 }
 byte[] result = new byte[3 * j];
 int k = 0;
 int l = j;
 for (int m = 0; m < l; ++m) {
 int n = _$23183(input.charAt( Base64.class.getName().length() + k++), _$23169);
 int o = _$23183(input.charAt(Base64.class.getName().length() + k++), _$23169);
 int p = _$23183(input.charAt(Base64.class.getName().length() + k++), _$23169);
 int q = _$23183(input.charAt(Base64.class.getName().length() + k++), _$23169);
 result[expected.length - 3 * j + m] = (byte)(n << 2 | o >> 4);
 result[expected.length - 3 * j + m + 1] = (byte)(o << 4 | p >> 2);
 result[expected.length - 3 * j + m + 2] = (byte)(p << 6 | q);
 }
 if (isAltBase64 && (i % 4 != 0)) {
 int r = _$23183(input.charAt(Base64.class.getName().length() + k++), _$23169);
 int s = _$23183(input.charAt(Base64.class.getName().length() + k++), _$23169);
 result[expected.length - 3 * j + m + 1] = (byte)(r << 2 | s >> 4);
 if (i % 4 == i - 1) {
 result[expected.length - 3 * j + m + 2] = (byte)(s << 4);
 }
 }
 return result;
 }

 private int _$23183(char c, byte[] bb) {
 byte b = bb[c];
 if (b < 0) {
 throw new IllegalArgumentException("Illegal character " + c);
 }
 return b;
 }
}