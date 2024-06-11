package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class ExtendedBufferedReaderTest {

 private ExtendedBufferedReader reader;
 private StringReader input;

 @BeforeEach
 public void setUp() throws IOException {
 input = new StringReader("line1\rline2\rline3\rline4\rline5\n");
 reader = new ExtendedBufferedReader(input);
 }

 @Test
 void testRead() throws IOException {
 int result;
 for (int i = 0; i < 5; i++) {
 result = reader.read();
 assertEquals(i, result, "Incorrect character read");
 }
 Assertions.assertEquals(-1, reader.read(), "End of stream not reached");
 }

 @Test
 void testReadAgain() throws IOException {
 int result;
 for (int i = 0; i < 5; i++) {
 result = reader.read();
 reader.readAgain();
 assertEquals(i, result, "Incorrect character read again");
 }
 Assertions.assertEquals(-1, reader.readAgain(), "End of stream not reached");
 }

 @Test
 void testReadLine() throws IOException {
 String line;
 for (int i = 0; i < 5; i++) {
 line = reader.readLine();
 assertEquals("line" + i, line, "Incorrect line read");
 }
 Assertions.assertNull(reader.readLine(), "End of stream not reached");
 }

 @Test
 void testLookAhead() throws IOException {
 int lookAhead;
 for (int i = 0; i < 4; i++) {
 lookAhead = reader.lookAhead();
 assertEquals((char) ('r' + i), lookAhead, "Incorrect lookahead character");
 reader.read();
 }
 Assertions.assertEquals(9, reader.lookAhead(), "End of stream not reached");
 }

 @Test
 void testGetLineNumber() throws IOException {
 int lineNumber = 0;
 for (int i = 0; i < 5; i++) {
 String line = reader.readLine();
 lineNumber++;
 assertEquals(lineNumber, reader.getLineNumber(), "Incorrect line number");
 }
 Assertions.assertEquals(-1, reader.getLineNumber(), "End of stream not reached");
 }

 @Test
 void testNegativeLineNumber() {
 int lineNumber = reader.getLineNumber();
 Assertions.assertEquals(-1, lineNumber, "Initial line number should be -1");
 }
}