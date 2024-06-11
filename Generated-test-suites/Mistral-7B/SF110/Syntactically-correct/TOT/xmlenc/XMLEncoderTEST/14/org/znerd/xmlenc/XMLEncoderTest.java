package org.znerd.xmlenc;

 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.Writer;
 import java.nio.charset.StandardCharsets;

 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.MockitoAnnotations;

 /**
 * Tests for the XMLEncoder class.
 */
 public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer writerMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 this.writerMock = mock(Writer.class);
 this.encoder = new XMLEncoder("UTF-8");
 }

 @Test
 public void testDeclaration() throws IOException {
 // Given
 final ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
 when(this.writerMock.write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH))
 .thenWriteTo(expectedOutput);
 when(this.writerMock.write(this.encoder._encodingCharArray))
 .thenWriteTo(expectedOutput);
 when(this.writerMock.write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH))
 .thenWriteTo(expectedOutput);

 // When
 this.encoder.declaration(this.writerMock);

 // Then
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", new String(expectedOutput.toByteArray(), StandardCharsets.UTF_8));
 verify(this.writerMock, times(1)).write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH);
 verify(this.writerMock, times(1)).write(this.encoder._encodingCharArray);
 verify(this.writerMock, times(1)).write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH);
 }

 @Test
 public void testTextWithValidCharacters() throws IOException {
 // Given
 final ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
 when(this.writerMock.write(this.encoder._encodingCharArray))
 .thenWriteTo(expectedOutput);

 // When
 this.encoder.text(this.writerMock, "Hello, World!");

 // Then
 assertEquals("Hello, World!", new String(expectedOutput.toByteArray(), StandardCharsets.UTF_8));
 }

 @Test
 public void testTextWithInvalidCharacters() throws IOException {
 // Given
 final ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
 final String invalidCharacter = "\u00A0"; // Non-breaking space
 when(this.writerMock.write(this.encoder._encodingCharArray))
 .thenWriteTo(expectedOutput);

 // When
 this.encoder.text(this.writerMock, invalidCharacter);

 // Then
 // The XMLEncoder should have escaped the invalid character
 assertEquals("&#160;", new String(expectedOutput.toByteArray(), StandardCharsets.UTF_8));

 // Check for other invalid characters
 final String invalidChars = "&><\"'";
 for (int i = 0; i < invalidChars.length(); ++i) {
 final char c = invalidChars.charAt(i);
 this.encoder.text(this.writerMock, String.valueOf(c));
 assertEquals("&" + XMLEncoder.AMPERSAND_HASH[0] + (char) Integer.parseInt(XMLEncoder.AMPERSAND_HASH[2] + String.valueOf(c.codePointAt(0))) + XMLEncoder.AMPERSAND_HASH[1], new String(expectedOutput.toByteArray(), StandardCharsets.UTF_8));
 }
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidXmlCharacters() throws IOException {
 // Given
 final ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
 when(this.writerMock.write(this.encoder._encodingCharArray))
 .thenWriteTo(expectedOutput);

 // When
 this.encoder.text(this.writerMock, "\u0085"); // Control character XON

 // Then
 // The XMLEncoder should have thrown an InvalidXMLException
 this.encoder.text(this.writerMock, "\u0085");
 fail("An InvalidXMLException was expected but not thrown.");
 }

 @Test
 public void testTextWithSevenBitEncoding() throws IOException {
 // Given
 final ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
 this.encoder = new XMLEncoder("ASCII");

 // When
 this.encoder.text(this.writerMock, "Hélló Mundó");

 // Then
 assertEquals("H&e9ll&o Mund&o", new String(expectedOutput.toByteArray(), StandardCharsets.UTF_8));
 }
 }