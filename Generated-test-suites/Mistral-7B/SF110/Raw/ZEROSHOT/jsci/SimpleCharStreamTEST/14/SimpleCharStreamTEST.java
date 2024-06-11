package jsci.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private int startLine;
 private int startColumn;
 private int bufferSize;
 private Reader inputStream;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("test\r\ntest\r\n12345\r\ntest\n".getBytes());
 charStream = new SimpleCharStream(inputStream, startLine, startColumn, bufferSize);
 }

 @After
 public void tearDown() {
 charStream.Done();
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals('\r', c);
 c = charStream.BeginToken();
 assertEquals('\n', c);
 c = charStream.BeginToken();
 assertEquals('t', c);
 }

 @Test
 public void testExpandBuff() throws IOException {
 charStream.ExpandBuff(false);
 assertEquals(4096 + 2048, charStream.bufsize);
 }

 @Test(expected = IOException.class)
 public void testExpandBuffIOException() throws IOException {
 charStream.ExpandBuff(false);
 charStream.ExpandBuff(false);
 }

 @Test
 public void testFillBuff() throws IOException {
 char c;
 for (int i = 0; i < 10; ++i) {
 c = charStream.readChar();
 assertEquals('t', c);
 }
 charStream.FillBuff();
 c = charStream.readChar();
 assertEquals('e', c);
 }

 @Test(expected = IOException.class)
 public void testFillBuffIOException() throws IOException {
 char c = charStream.readChar();
 charStream.FillBuff();
 charStream.FillBuff();
 }

 @Test
 public void testGetBeginColumn() {
 assertEquals(0, charStream.getBeginColumn());
 }

 @Test
 public void testGetBeginLine() {
 assertEquals(startLine, charStream.getBeginLine());
 }

 @Test
 public void testGetColumn() {
 assertEquals(0, charStream.getColumn());
 }

 @Test
 public void testGetEndColumn() {
 assertEquals(0, charStream.getEndColumn());
 }

 @Test
 public void testGetEndLine() {
 assertEquals(startLine, charStream.getEndLine());
 }

 @Test
 public void testGetImage() {
 assertEquals("test\r\ntest\r\n12345\r\ntest\n", SimpleCharStream.GetImage());
 }

 @Test
 public void testGetSuffix() {
 char[] suffix = SimpleCharStream.GetSuffix(4);
 assertEquals("test", new String(suffix));
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < 11; ++i) {
 c = charStream.readChar();
 if (i < 10) {
 assertEquals('t', c);
 } else {
 assertEquals('5', c);
 }
 }
 assertNull(charStream.readChar());
 }

 @Test
 public void testbackup() throws IOException {
 charStream.backup(3);
 assertEquals(8, charStream.getColumn());
 charStream.backup(5);
 assertEquals(3, charStream.getColumn());
 }

 @Test
 public void testReInit() throws IOException {
 charStream.ReInit(inputStream, startLine, startColumn, bufferSize);
 char c = charStream.readChar();
 assertEquals('t', c);
 }

 @Test
 public void testDone() {
 assertFalse(charStream.inputStream.isOpen());
 }

 @Test
 public void testAdjustBeginLineColumn() {
 charStream.AdjustBeginLineColumn(2, 5);
 assertEquals(2, charStream.getBeginLine());
 assertEquals(5, charStream.getBeginColumn());
 }
}