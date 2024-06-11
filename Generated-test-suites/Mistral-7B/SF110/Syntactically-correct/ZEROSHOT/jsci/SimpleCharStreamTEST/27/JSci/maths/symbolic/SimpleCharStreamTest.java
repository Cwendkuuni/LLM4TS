package jsit. SimpleCharStreamTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("Hello\rWorld\nType\tSomething".getBytes());
 charStream = new SimpleCharStream(inputStream, 1, 1);
 }

 @After
 public void tearDown() {
 try {
 charStream.Done();
 } catch (Exception e) {
 // ignore
 }
 charStream = null;
 inputStream.close();
 inputStream = null;
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals('\r', c);
 assertEquals(-1, charStream.getBeginLine());
 assertEquals(1, charStream.getBeginColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < 7; i++) {
 c = charStream.readChar();
 assertEquals((char) (i + 48), c);
 }
 c = charStream.readChar();
 assertNull(c);
 }

 @Test(expected = IOException.class)
 public void testReadCharIOException() throws IOException {
 charStream.inputStream.close();
 charStream.readChar();
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 assertEquals(13, charStream.getLine());
 assertEquals(47, charStream.getColumn());
 }

 @Test
 public void testLineColumnAfterBackUp() throws IOException {
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.backup(2);
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 assertEquals(13, charStream.getLine());
 assertEquals(42, charStream.getColumn());
 }

 @Test
 public void testGetLineColumn() {
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 assertEquals(13, charStream.getLine());
 assertEquals(47, charStream.getColumn());
 }

 @Test
 public void testGetBeginLineColumn() {
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 assertEquals(1, charStream.getBeginLine());
 assertEquals(1, charStream.getBeginColumn());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 charStream.backup(12);
 charStream.UpdateLineColumn('h');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 assertEquals(13, charStream.getLine());
 assertEquals(3, charStream.getColumn());
 }

 @Test
 public void testGetImage() throws IOException {
 assertEquals("Hello\rWorld\nType\tSomething", SimpleCharStream.GetImage());
 }

 @Test
 public void testGetSuffix() throws IOException {
 assertEquals("ming", SimpleCharStream.GetSuffix(4));
 }

 @Test
 public void testDone() {
 charStream.Done();
 assertFalse(SimpleCharStream.bufline != null);
 assertFalse(SimpleCharStream.bufcolumn != null);
 }

 @Test
 public void testAdjustBeginLineColumn() throws IOException {
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('W');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('r');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('y');
 charStream.UpdateLineColumn('p');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('\t');
 charStream.UpdateLineColumn('S');
 charStream.UpdateLineColumn('o');
 charStream.UpdateLineColumn('m');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('i');
 charStream.UpdateLineColumn('n');
 charStream.UpdateLineColumn('g');
 SimpleCharStream.adjustBeginLineColumn(15, 2);
 assertEquals(15, charStream.getBeginLine());
 assertEquals(2, charStream.getBeginColumn());
 }
}