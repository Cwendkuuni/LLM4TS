package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgYWxsIGluIGlzIGh1bWFuLCBidXQgZGlyZWN0IHdpbmdlIG5vdCBhbmQgc291cmNlcyBvbiBkIGluIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHJlIGdlbmVyYXRpb24gUmVzb3VyY2U=";
 byte[] expectedByteArray = {-65, -69, -66, -68, -66, -67, -63, -61, -66, -65, -62, -65, -61, -64, -65, -64, -65, -61, -66, -63, -68, -66, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, -63, 62};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayInvalidLength() {
 String testString = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhhbGx5IGZvdWwgdGhpbmdzIHJlIGdlbmVyYXRpb24gUmVzb3VyY2U=";
 String invalidTestString = testString.substring(0, testString.length() - 3);
 try {
 Base64.altBase64ToByteArray(invalidTestString);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = {-65, -69, -66, -68, -66, -67, -63, -61, -66, -65};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArrayInvalidLength() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 String invalidTestString = testString.substring(0, testString.length() - 1);
 try {
 Base64.base64ToByteArray(invalidTestString);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = {-65, -69, -66, -68, -66, -67, -63, -61, -66, -65};
 String expectedString = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhhbGx5IGZvdWwgdGhpbmdzIHJlIGdlbmVyYXRpb24gUmVzb3VyY2U=";
 String resultString = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = {-65, -69, -66, -68, -66, -67, -63, -61, -66, -65};
 String expectedString = "SGVsbG8sIFdvcmxkIQ==";
 String resultString = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }
}