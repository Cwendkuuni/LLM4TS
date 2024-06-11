package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";

 @Test
 public void testGetEncoder_ValidEncoding_ReturnsEncoderInstance() {
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     assertEquals(UTF8_ENCODING, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoder_NullEncoding_ThrowsIllegalArgumentException() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoder_UnsupportedEncoding_ThrowsUnsupportedEncodingException() {
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("Invalid_Encoding"));
 }

 @Test
 public void testDeclaration_EmptyOutput_WritesCorrectXMLDeclaration() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     encoder.declaration(output);
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + UTF8_ENCODING + "\"?>";
     assertEquals(expectedDeclaration, output.toString());
 }

 @Test
 public void testText_ASCII_WritesCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, "ABC", false);
     assertEquals("ABC", output.toString());
 }

 @Test
 public void testText_NonASCII_EscapesCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, "é", false);
     assertEquals("&Atilde;", output.toString());
 }

 @Test(expected = InvalidXMLException.class)
 public void testText_InvalidCharacter_ThrowsInvalidXMLException() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, '\u0001', false);
     assertThrows(InvalidXMLException.class, () -> encoder.text(output, '\u0001', false));
 }

 @Test
 public void testText_EscapedAmpersand_WritesCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, "&", true);
     assertEquals("&amp;", output.toString());
 }

 @Test
 public void testText_NonEscapedAmpersand_WritesCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, "&amp;", false);
     assertEquals("&amp;", output.toString());
 }

 @Test
 public void testText_InvalidXMLCharacter_EscapesCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, '\u0001', false);
     assertEquals("&x0001;", output.toString());
 }

 @Test
 public void testText_MixedASCIIAndNonASCII_EscapesNonASCIICharacters() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.text(output, "ABC&é", false);
     assertEquals("ABC&Atilde;", output.toString());
 }

 @Test
 public void testWhitespace_EmptyString_WritesEmptyStringToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.whitespace(output, "");
     assertEquals("", output.toString());
 }

 @Test
 public void testWhitespace_StringWithWhitespace_WritesStringToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.whitespace(output, "\t abc \n def");
     assertEquals("\t abc \n def", output.toString());
 }

 @Test
 public void testAttribute_EmptyString_WritesEmptyAttributeToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "", "", '\"', false);
     assertEquals("", output.toString());
 }

 @Test
 public void testAttribute_StringWithQuotationMark_WritesEscapedQuotationMarkToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "name", "\"value\"", '\"', false);
     assertEquals(" name=\"&quot;value&quot;\">", output.toString());
 }

 @Test
 public void testAttribute_StringWithoutQuotationMark_WritesAttributeToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "name", "value", '\u0000', false);
     assertEquals(" name=value", output.toString());
 }

 @Test
 public void testAttribute_StringWithEscapedCharacter_WritesEscapedCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "name", "value&amp;", '\u0000', false);
     assertEquals(" name=value&amp;", output.toString());
 }

 @Test
 public void testAttribute_StringWithQuotationMarkAndEscapedCharacter_WritesEscapedCharactersToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "name", "\"value&amp;\"", '\"', false);
     assertEquals(" name=\"&quot;value&amp;amp;\">", output.toString());
 }

 @Test
 public void testAttribute_StringWithAmpersand_WritesEscapedAmpersandToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "name", "value&", '\u0000', false);
     assertEquals(" name=value&amp;", output.toString());
 }

 @Test
 public void testAttribute_StringWithNonASCII_WritesEscapedCharacterToOutput() throws IOException {
     Writer output = new ByteArrayOutputStream(1024).writer();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     encoder.attribute(output, "name", "\u00E9value", '\u0000', false);
     assertEquals(" name=value&Atilde;", output.toString());
 }
}