package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "QWRvYmFpbiBpbiBzYWdl";
 byte[] expectedByteArray = {-65, -66, -66, -63, -68, -61, -68, -61, -63, -68, -65, -65, -64, -65, -64, -69, -61, -62, -65, -63, -68, -61, -67, -65, -64, -65, -64, -65, -64, -69, -66, -66, -65, -64, -65, -64, -65, -64, -65, -64, -65, -64, 61, -62, -63, -63, -68, -61, -66, -66, -65, -63, -68, -61};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = {-65, -66, -66, -63, -68, -61, -68, -61, -63, -68, -65, -65, -64, -65, -64, -69, -61, -62, -65, -63, -68, -61, -67, -65, -64, -65, -64, -65, -64, -65, -64, -65, -64, -65, -64, 61, -62, -63, -63, -68, -61, -66, -66, -65, -63, -68, -61};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = {-65, -66, -66, -63, -68, -61, -68, -61, -63, -68, -65, -65, -64, -65, -64, -69, -61, -62, -65, -63, -68, -61, -67, -65, -64, -65, -64, -65, -64, -65, -64, -65, -64, 61, -62, -63, -63, -68, -61, -66, -66, -65, -63, -68, -61};
 String expectedTestString = "QWRvYmFpbiBpbiBzYWdl";
 String resultTestString = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedTestString, resultTestString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = {-65, -66, -66, -63, -68, -61, -68, -61, -63, -68, -65, -65, -64, -65, -64, -69, -61, -62, -65, -63, -68, -61, -67, -65, -64, -65, -64, -65, -64, -65, -64, -65, -64, 61, -62, -63, -63, -68, -61, -66, -66, -65, -63, -68, -61};
 String expectedTestString = "SGVsbG8sIFdvcmxkIQ==";
 String resultTestString = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedTestString, resultTestString);
 }
}