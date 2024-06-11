package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

 private byte[] inputData = "This is a test message".getBytes();
 private byte[] base64EncodedData = new byte[]{"/Q==", "Q29tbWl0aG91c2Vk", "QW5kIFRoZSBQYXVs", "IFdpbmRvdyw="}[0];
 private byte[] base64DecodedData = "This is a test message".getBytes();

 @Test
 public void testBase64InputStreamDecoding() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
 byte[] outputData = new byte[inputData.length];
 int lengthRead = base64InputStream.read(outputData);
 assertArrayEquals(inputData, outputData);
 assertFalse(base64InputStream.hasNextLine());
 base64InputStream.close();
 }

 @Test
 public void testBase64InputStreamDecodingWithLineLength() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 48, null);
 byte[] outputData = new byte[inputData.length];
 int lengthRead = base64InputStream.read(outputData);
 assertArrayEquals(inputData, outputData);
 assertTrue(base64InputStream.hasNextLine());
 base64InputStream.close();
 }

 @Test
 public void testBase64InputStreamDecodingWithLineSeparator() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(inputData);
 byte[] lineSeparator = new byte[]{13, 10}; // Carriage Return and Line Feed
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, lineSeparator);
 byte[] outputData = new byte[inputData.length];
 int lengthRead = base64InputStream.read(outputData);
 assertArrayEquals(inputData, outputData);
 assertTrue(base64InputStream.hasNextLine());
 byte[] line = new byte[48]; // Line length
 int lineLength = base64InputStream.read(line);
 assertArrayEquals(new byte[]{"=".getBytes()}, line);
 base64InputStream.close();
 }

 @Test (expected = IOException.class)
 public void testBase64InputStreamDecodingWithInvalidInput() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{1});
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
 byte[] outputData = new byte[1];
 int lengthRead = base64InputStream.read(outputData);
 assertFalse(base64InputStream.hasNextLine());
 base64InputStream.close();
 }

 @Test
 public void testBase64InputStreamEncoding() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(base64DecodedData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 int lengthRead = base64InputStream.read(outputStream.writeToByteArray());
 assertArrayEquals(base64EncodedData, outputStream.toByteArray());
 assertFalse(base64InputStream.hasNextLine());
 base64InputStream.close();
 }

 @Test
 public void testBase64InputStreamEncodingWithLineLength() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(base64DecodedData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, null);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 int lengthRead = base64InputStream.read(outputStream.writeToByteArray());
 assertArrayEquals(base64EncodedData, outputStream.toByteArray());
 assertTrue(base64InputStream.hasNextLine());
 base64InputStream.close();
 }

 @Test
 public void testBase64InputStreamEncodingWithLineSeparator() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(base64DecodedData);
 byte[] lineSeparator = new byte[]{13, 10}; // Carriage Return and Line Feed
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, lineSeparator);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();int lineLength = 0;
 while (base64InputStream.hasNextLine()) {
 byte[] line = new byte[48]; // Line length
 int lengthRead = base64InputStream.read(line, 0, line.length);
 outputStream.write(line, 0, lengthRead);
 lineLength += lengthRead;
 }
 byte[] outputData = outputStream.toByteArray();
 assertArrayEquals(new byte[]{"/Q==", "Q29tbWl0aG91c2Vk", "QW5kIFRoZSBQYXVs", "IFdpbmRvdyw="}[0], outputData, 0, 4);
 assertArrayEquals(new byte[]{"=".getBytes()}, outputData, lineLength - 4, lineLength);
 base64InputStream.close();
 }

 @Test
 public void testBase64InputStreamNoData() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{});
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
 assertNull(base64InputStream.read());
 assertFalse(base64InputStream.hasNextLine());
 base64InputStream.close();
 }

}