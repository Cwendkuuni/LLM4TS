import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import static org.mockito.Mockito.*;

public class FileTextTest {

   @Test
   public void testFileTextConstructorWithFileAndString() throws IOException {
      File file = new File("test.txt");
      FileText fileText = new FileText(file, "UTF-8");
      assertEquals("Expected file content", "Actual file content", fileText.getFullText());
   }

   @Test
   public void testFileNotFoundException() {
      File file = mock(File.class);
      when(file.exists()).thenReturn(false);
      try {
         new FileText(file, "UTF-8");
         fail("Expected FileNotFoundException");
      } catch (FileNotFoundException e) {
         //Expected
      }
   }

   @Test
   public void testUnsupportedCharsetException() {
      File file = new File("test.txt");
      try {
         new FileText(file, "unknown-charset");
         fail("Expected IllegalStateException with UnsupportedCharsetException");
      } catch (IllegalStateException e) {
         assertEquals("Unsupported charset: unknown-charset", e.getMessage());
      }
   }

   @Test
   public void testMalformedInputException() throws IOException {
      File file = new File("test.txt");
      CharsetDecoder decoder = mock(CharsetDecoder.class);
      when(decoder.onMalformedInput(CodingErrorAction.IGNORE)).thenThrow(CharacterCodingException.class);
      try {
         new FileText(file, "UTF-8", decoder);
         fail("Expected MalformedInputException");
      } catch (MalformedInputException e) {
         //Expected
      }
   }

   @Test
   public void testUnmappableCharacterException() throws IOException {
      File file = new File("test.txt");
      CharsetDecoder decoder = mock(CharsetDecoder.class);
      when(decoder.onUnmappableCharacter(CodingErrorAction.IGNORE)).thenThrow(CharacterCodingException.class);
      try {
         new FileText(file, "UTF-8", decoder);
         fail("Expected UnmappableCharacterException");
      } catch (UnmappableCharacterException e) {
         //Expected
      }
   }

   @Test
   public void testNullFileArgument() {
      try {
         new FileText(null, "UTF-8");
         fail("Expected NullPointerException");
      } catch (NullPointerException e) {
         //Expected
      }
   }

   @Test
   public void testFileSizeLargerThanReadBuffer() throws IOException {
      File file = new File("test.txt");
      CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
      StringBuilder actualFullText = new StringBuilder();
      for (int i = 0; i < FileText.READ_BUFFER_SIZE * 2; i++) {
         actualFullText.append("Test content");
      }
      when(file.length()).thenReturn(actualFullText.length());
      try {
         new FileText(file, decoder);
         fail("Expected IOException");
      } catch (IOException e) {
         //Expected
      }
   }

   @Test
   public void testInvalidLineSeparator() {
      List<String> lines = new ArrayList<>();
      lines.add("Test line 1");
      lines.add("Test line 2");
      lines.add("Test line 3\u0001"); // Invalid line separator
      try {
         new FileText(new File("test.txt"), lines);
         fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         //Expected
      }
   }

   @Test
   public void testGetFile() {
      FileText fileText = new FileText(new File("test.txt"), "UTF-8");
      assertEquals("test.txt", fileText.getFile().getAbsolutePath());
   }

   @Test
   public void testGetCharset() {
      FileText fileText = new FileText(new File("test.txt"), "UTF-8");
      assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
   }

   @Test
   public void testGetFullText() {
      File file = new File("test.txt");
      CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
      FileText fileText = new FileText(file, decoder);
      assertEquals("Expected file content", fileText.getFullText());
   }

   @Test
   public void testToLinesArray() {
      File file = new File("test.txt");
      CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
      FileText fileText = new FileText(file, decoder);
      String[] lines = fileText.toLinesArray();
      assertEquals("Expected number of lines", lines.length, 3);
   }

   @Test
   public void testFindLineBreaks() {
      FileText fileText = new FileText(new File("test.txt"), "UTF-8");
      int[] lineBreaks = fileText.findLineBreaks();
      assertEquals("Expected number of line breaks", lineBreaks.length, 4);
   }

   @Test
   public void testLineColumn() {
      FileText fileText = new FileText(new File("test.txt"), "UTF-8");
      LineColumn lineColumn = fileText.lineColumn(10);
      assertEquals(1, lineColumn.getLineNumber());
      assertEquals(6, lineColumn.getColumnNumber());
   }

   @Test
   public void testGet() {
      FileText fileText = new FileText(new File("test.txt"), "UTF-8");
      String expectedLine = "Expected line content";
      assertEquals(expectedLine, fileText.get(1));
   }

   @Test
   public void testSize() {
      FileText fileText = new FileText(new File("test.txt"), "UTF-8");
      assertEquals(3, fileText.size());
   }

   @Test
   public void testFromLines() {
      File file = new File("test.txt");
      List<String> lines = new ArrayList<>();
      lines.add("Test line 1");
      lines.add("Test line 2");
      lines.add("Test line 3");
      FileText fileText = FileText.fromLines(file, lines);
      assertEquals(file, fileText.getFile());
      assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
   }

}