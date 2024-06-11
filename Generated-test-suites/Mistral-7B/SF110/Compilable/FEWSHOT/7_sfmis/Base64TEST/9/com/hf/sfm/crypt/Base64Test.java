package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String s = "RGVjbGllciBxdWVyeSwKCiAgIDxQTCwgZXhwIG1haWxfdGltZSBlYWdlLg==";
 byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
 byte[] result = Base64.altBase64ToByteArray(s);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testAltBase64ToByteArrayIllegalArgumentException() {
 String s = "abc";
 try {
 Base64.altBase64ToByteArray(s);
 } catch (IllegalArgumentException e) {
 assertEquals("String length must be a multiple of four.", e.getMessage());
 }
 }

 @Test
 public void testBase64ToByteArray() {
 String s = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhhdCB5b3VyIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
 byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
 byte[] result = Base64.base64ToByteArray(s);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBase64ToByteArrayIllegalArgumentException() {
 String s = "abc";
 try {
 Base64.base64ToByteArray(s);
 } catch (IllegalArgumentException e) {
 assertEquals("String length must be a multiple of four.", e.getMessage());
 }
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
 String expected = "Rk9SRUxQT0NFRVBTVFVWU1BXVFFdrk5JYW55bGl2ZQ==";
 String result = Base64.byteArrayToAltBase64(bytes);
 assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToAltBase64Null() {
 assertNull(Base64.byteArrayToAltBase64(null));
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
 String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhhdCB5b3VyIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
 String result = Base64.byteArrayToBase64(bytes);
 assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64Null() {
 assertNull(Base64.byteArrayToBase64(null));
 }
}