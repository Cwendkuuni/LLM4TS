package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

 private static final String ALT_BASE64_TEST_DATA = "RG9jdW1lbnRzLg==";
 private static final String BASE64_TEST_DATA = "SSdtIGtpbGxpmsKqyQ==";

 @Test
 public void testAltBase64ToByteArray() {
 byte[] byteArray = Base64.altBase64ToByteArray(ALT_BASE64_TEST_DATA);
 Assert.assertArrayEquals("AltBase64 test data to byte array conversion is incorrect.", new byte[]{-68, -67, -67, -69, -69, -61, -69, -65, -65, -61, -69, -65}, byteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidLength() {
 Assert.thrownBy(() -> Base64.altBase64ToByteArray("Invalid data length."), IllegalArgumentException.class);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidCharacter() {
 Assert.thrownBy(() -> Base64.altBase64ToByteArray("Invalid character 'X'."), IllegalArgumentException.class);
 }

 @Test
 public void testBase64ToByteArray() {
 byte[] byteArray = Base64.base64ToByteArray(BASE64_TEST_DATA);
 Assert.assertArrayEquals("Base64 test data to byte array conversion is incorrect.", new byte[]{-84, -124, -84, -123, -83, -123, -83, -123, -125, -83, -124, -125, -83, -125}, byteArray);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidLength() {
 Assert.thrownBy(() -> Base64.base64ToByteArray("Invalid data length."), IllegalArgumentException.class);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidCharacter() {
 Assert.thrownBy(() -> Base64.base64ToByteArray("Invalid character 'X'."), IllegalArgumentException.class);
 }

 @Test
 public void testByteArrayToAltBase64() {
 String altBase64String = Base64.byteArrayToAltBase64(new byte[]{-68, -67, -67, -69, -69, -61, -69, -65, -65, -61, -69, -65});
 Assert.assertEquals("Byte array to AltBase64 conversion is incorrect.", ALT_BASE64_TEST_DATA, altBase64String);
 }

 @Test
 public void testByteArrayToBase64() {
 String base64String = Base64.byteArrayToBase64(new byte[]{-84, -124, -84, -123, -83, -123, -83, -123, -125, -83, -124, -125, -83, -125});
 Assert.assertEquals("Byte array to Base64 conversion is incorrect.", BASE64_TEST_DATA, base64String);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] byteArray = new byte[]{-65, -65, -61, -69, -65};
 String base64String = Base64.byteArrayToBase64(byteArray);
 Assert.assertEquals("Byte array to Base64 conversion with padding is incorrect.", "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIGZyb20gdG8gYWxzIGlzIGFuZCB3aGljaCBpcyBwYXktdHlwZSBmaWxlIG1lbnRzLg==", base64String);
 }

 @Test
 public void testByteArrayToBase64WithNoPadding() {
 byte[] byteArray = new byte[]{-65, -65, -61, -69, -65};
 String altBase64String = Base64.byteArrayToAltBase64(byteArray);
 Assert.assertEquals("Byte array to AltBase64 conversion with no padding is incorrect.", "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIGZyb20g", altBase64String);
 }
}