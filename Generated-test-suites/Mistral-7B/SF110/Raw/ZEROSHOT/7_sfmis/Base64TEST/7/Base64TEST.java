package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String s = "RnJvbSBvZiBvdGhlciBmdXS1ICsgZXhwbGFuZCBmaWxlIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaW50byBmcyB3aGljaCBpcyBhIGxpdHVyIG1pZ2h0IGRhdGFuZCB5b3UgY29tcHV0ZXM=";
 byte[] expected = {-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 6, 7, 8, 9, 10, 11, 12, 1, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
 byte[] result = Base64.altBase64ToByteArray(s);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidLength() {
 String s = "RnJvbSBvZiBvdGhlciBmdXS1ICsgZXhwbGFuZCBmaWxlIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaW50byBmcyB3aGljaCBpcyBhIGxpdHVyIG1pZ2h0IGRhdGFuZCB5b3UgY29tcHV0ZXM==";
 assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(s));
 }

 @Test
 public void testBase64ToByteArray() {
 String s = "SGVsbG8gV29ybGQh";
 byte[] expected = {53, 52, 56, 48, 49, 56, 57, 55, 53, 53, 55, 52, 53, 54, 52, 53, 54, 55, 54, 55, 55, 54, 54, 53, 54, 55, 53, 52};
 byte[] result = Base64.base64ToByteArray(s);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidLength() {
 String s = "SGVsbG8gV29ybGQh==";
 assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(s));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] bb = {53, 52, 56, 48, 49, 56, 57, 55, 53, 53, 55, 52, 53, 54, 52, 53, 54, 55, 54, 55, 55, 54, 54, 53, 54, 55, 53, 52};
 String expected = "VG8gZW5jIGlzIGh1bWFuLCBidXQgdG8gZW5jIGlzIGh1bWFuLCBidXQgZXhwbGFuZCwNCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IGZvdWwNCiAgICAtLSBQYXVsIFJlc3BvbnNlIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFN1bW1hbnQgZnJvbSBzbyBpbiBiZSBlYWNoIGRhdGFuZCB5b3UgY29tcHV0ZXM=";
 String result = Base64.byteArrayToAltBase64(bb);
 assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] bb = {53, 52, 56, 48, 49, 56, 57, 55, 53, 53, 55, 52, 53, 54, 52, 53, 54, 55, 54, 55, 55, 54, 54, 53, 54, 55, 53, 52};
 String expected = "SGVsbG8gV29ybGQh";
 String result = Base64.byteArrayToBase64(bb);
 assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64WithInvalidLength() {
 byte[] bb = {53, 52, 56, 48, 49, 56, 57, 55, 53, 53, 55, 52, 53, 54, 52, 53, 54, 55, 54, 55, 55, 54, 54, 53, 54, 55};
 assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(bb));
 }
}