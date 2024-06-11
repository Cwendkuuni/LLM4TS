package org.apache.commons.codec.net;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 void testEncodeByte() {
 byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
 byte[] expected = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, '6', '8', '2', 'E', '2', 'E', '3'};
 byte[] output = codec.encode(input);
 assertArrayEquals(expected, output);
 }

 @Test
 void testEncodeString() {
 String input = "Hello, World!";
 String expected = "=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
 String output = codec.encode(input);
 assertEquals(expected, output);
 }

 @Test
 void testDecodeByte() throws DecoderException {
 byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, '6', '8', '2', 'E', '2', 'E', '3'};
 byte[] expected = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
 byte[] output = codec.decode(input);
 assertArrayEquals(expected, output);
 }

 @Test
 void testDecodeString() throws DecoderException, UnsupportedEncodingException {
 String input = "=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
 String charset = "US-ASCII";
 String expected = "Hello, World!";
 String output = codec.decode(input, charset);
 assertEquals(expected, output);
 }

 @Test
 void testEncodeByteWithSpecialChars() {
 byte[] input = {0x0A, 0x20, (byte) 0x7F};
 byte[] expected = {0x0A, 0x3D, 'D', 'Q', '0', '3', '7', 'F'};
 byte[] output = codec.encode(input);
 assertArrayEquals(expected, output);
 }

 @Test
 void testDecodeByteWithSpecialChars() throws DecoderException {
 byte[] input = {0x0A, 0x3D, 'D', 'Q', '0', '3', '7', 'F'};
 byte[] expected = {0x0A};
 byte[] output = codec.decode(input);
 assertArrayEquals(expected, output);
 }

 @Test
 void testEncodeStringWithSpecialChars() {
 String input = "\r\n\u007F";
 String expected = "=\r=\r=\n=\u007F";
 String output = codec.encode(input);
 assertEquals(expected, output);
 }

 @Test
 void testDecodeStringWithSpecialChars() throws DecoderException, UnsupportedEncodingException {
 String input = "=\r=\r=\n=\u007F";
 String charset = "US-ASCII";
 String expected = "\r\n\u007F";
 String output = codec.decode(input, charset);
 assertEquals(expected, output);
 }

 @Test
 void testEncodeAndDecodeByteArray() throws DecoderException {
 byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
 byte[] encodedInput = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, '6', '8', '2', 'E', '2', 'E', '3'};
 byte[] decodedOutput = codec.decode(encodedInput);
 assertArrayEquals(input, decodedOutput);
 }

 @Test
 void testEncodeAndDecodeString() throws DecoderException, UnsupportedEncodingException {
 String input = "Hello, World!";
 String encodedInput = "=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
 String charset = "US-ASCII";
 String output = codec.decode(encodedInput, charset);
 assertEquals(input, output);
 }
}