package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

 private static String testData = "Line1\rLine2\rLine3\rLine4\rLine5\r";

 @Test
 public void testExtendedBufferedReader() throws IOException {
  Reader reader = new InputStreamReader(new ByteArrayInputStream(testData.getBytes()));
  ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

  // test read
  assertEquals((int) 'L', ebr.read());
  assertEquals((int) 'i', ebr.read());
  assertEquals((int) ' ', ebr.read());
  assertEquals((int) 'e', ebr.read());
  assertEquals((int) '\r', ebr.read()); // read line terminator
  assertEquals((int) 'L', ebr.read());
  assertEquals((int) 'i', ebr.read());
  assertEquals((int) 'n', ebr.read());
  assertEquals((int) 'e', ebr.read());
  assertEquals((int) '2', ebr.read());
  assertEquals((int) '\r', ebr.read()); // read line terminator
  assertEquals((int) 'L', ebr.read());
  assertEquals((int) 'i', ebr.read());
  assertEquals((int) 'n', ebr.read());
  assertEquals((int) 'e', ebr.read());
  assertEquals((int) '3', ebr.read());
  assertEquals((int) '\r', ebr.read()); // read line terminator
  assertEquals((int) 'L', ebr.read());
  assertEquals((int) 'i', ebr.read());
  assertEquals((int) 'n', ebr.read());
  assertEquals((int) 'e', ebr.read());
  assertEquals((int) '4', ebr.read());
  assertEquals((int) '\r', ebr.read()); // read line terminator
  assertEquals((int) 'L', ebr.read());
  assertEquals((int) 'i', ebr.read());
  assertEquals((int) 'n', ebr.read());
  assertEquals((int) 'e', ebr.read());
  assertEquals((int) '5', ebr.read());
  assertEquals((int) '\r', ebr.read()); // read line terminator
  assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());

  // test readAgain
  assertEquals((int) 'L', ebr.read());
  assertEquals((int) 'i', ebr.read());
  assertEquals((int) ' ', ebr.read());
  assertEquals((int) 'e', ebr.read());
  assertEquals((int) '\r', ebr.read()); // read line terminator
  assertEquals((int) 'L', ebr.readAgain());
  assertEquals((int) 'i', ebr.readAgain());

  // test readLine
  assertEquals("Line1\r", ebr.readLine());
  assertEquals("Line2\r", ebr.readLine());
  assertEquals("Line3\r", ebr.readLine());
  assertEquals("Line4\r", ebr.readLine());
  assertEquals("Line5\r", ebr.readLine());
  assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readLine());

  // test lookAhead
  assertEquals((int) 'L', ebr.lookAhead());
  assertEquals((int) 'i', ebr.lookAhead());
  assertEquals((int) ' ', ebr.lookAhead());
  assertEquals((int) 'e', ebr.lookAhead());
  assertEquals((int) '\r', ebr.lookAhead());
  assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());

  // test getLineNumber
  assertEquals(5, ebr.getLineNumber());
 }

 @Test
 public void testExtendedBufferedReaderWithEmptyInput() throws IOException {
  Reader reader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
  ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
  assertEquals(0, ebr.getLineNumber());
  assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
 }
}