package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

 private byte[] testByteArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes();

 @Test
 public void testAltBase64ToByteArray() {
 byte[] input = "ZXhhbXBsZSBmaWxlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSE=".getBytes();
 byte[] output = altBase64ToByteArray(new String(input));
 Assert.assertArrayEquals(testByteArray, output);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidInput() {
 Assert.assertThrows(IllegalArgumentException.class, () -> altBase64ToByteArray("this is not a valid base64 string"));
 }

 @Test
 public void testBase64ToByteArray() {
 byte[] input = "VG8gZXhhbXBsZSBmaWxlIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSE=".getBytes();
 byte[] output = base64ToByteArray(new String(input));
 Assert.assertArrayEquals(testByteArray, output);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidInput() {
 Assert.assertThrows(IllegalArgumentException.class, () -> base64ToByteArray("this is not a valid base64 string"));
 }

 @Test
 public void testByteArrayToAltBase64() {
 String input = byteArrayToAltBase64(testByteArray);
 Assert.assertEquals("ZXhhbXBsZSBmaWxlIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSE=", input);
 }

 @Test
 public void testByteArrayToBase64() {
 String input = byteArrayToBase64(testByteArray);
 Assert.assertEquals("VG8gZXhhbXBsZSBmaWxlIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSE=", input);
 }

 @Test
 public void testByteArrayToBase64WithEmptyInput() {
 String input = byteArrayToBase64(new byte[0]);
 Assert.assertEquals("", input);
 }

 @Test
 public void testByteArrayToAltBase64WithEmptyInput() {
 String input = byteArrayToAltBase64(new byte[0]);
 Assert.assertEquals("", input);
 }
}