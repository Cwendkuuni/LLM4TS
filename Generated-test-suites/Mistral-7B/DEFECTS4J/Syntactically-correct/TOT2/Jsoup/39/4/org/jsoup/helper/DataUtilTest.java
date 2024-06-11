import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

   // tests for each method
   // ...

   // tests for edge cases and exceptions
   @Test(expected = FileNotFoundException.class)
   public void testLoadFileDocumentNotFound() {
      // ...
   }

   @Test
   public void testLoadFileDocumentEmpty() throws IOException {
      // ...
   }

   @Test(expected = IOException.class)
   public void testLoadInputStreamMalformed() throws IOException {
      // ...
   }

   @Test(expected = IllegalArgumentException.class)
   public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
      // ...
   }

   @Test(expected = IllegalArgumentException.class)
   public void testLoadDocumentWithInvalidBaseUri() throws IOException {
      // ...
   }

   @Test(expected = IllegalArgumentException.class)
   public void testLoadDocumentWithIllegalParser() throws IOException {
      // ...
   }

   @Test
   public void testReadToByteBufferBufferOverflow() throws IOException {
      // ...
   }

   // helper classes for test
   // ...

}