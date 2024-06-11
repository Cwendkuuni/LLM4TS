package JSci.maths.symbolic.test;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleCharStreamTest {

 @Test
 public void testBeginToken() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
 char c = scs.BeginToken();
 Assert.assertEquals('t', c);
 }

 @Test(expected = IOException.class)
 public void testBeginToken_IOException() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
 scs.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2\r\n".getBytes())));
 char c = scs.readChar();
 scs.UpdateLineColumn(c);
 Assert.assertEquals(1, scs.getLine());
 Assert.assertEquals(1, scs.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2\r\n".getBytes())));
 Assert.assertEquals('t', scs.readChar());
 Assert.assertEquals(2, scs.getLine());
 Assert.assertEquals(2, scs.getColumn());
 }

 @Test
 public void testGetLineColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2\r\n".getBytes())));
 Assert.assertEquals(1, scs.getLine());
 Assert.assertEquals(1, scs.getColumn());
 Assert.assertEquals(2, scs.getLine());
 Assert.assertEquals(2, scs.getColumn());
 Assert.assertEquals(3, scs.getLine());
 Assert.assertEquals(1, scs.getColumn());
 }

 @Test
 public void testBackup() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2\r\n".getBytes())));
 scs.Backup(2);
 Assert.assertEquals(1, scs.getLine());
 Assert.assertEquals(8, scs.getColumn());
 }

 @Test
 public void testReInit() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2\r\n".getBytes())));
 scs.ReInit(new InputStreamReader(new ByteArrayInputStream("test3\r\nline3\r\ntest4\r\nline4\r\n".getBytes())));
 Assert.assertEquals(3, scs.getLine());
 Assert.assertEquals(1, scs.getColumn());
 }

 @Test
 public void testExpandBuff() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline3\r\ntest4\r\nline5\r\nline6\r\n".getBytes())));
 scs.ExpandBuff(false);
 Assert.assertEquals(8192, scs.bufsize);
 Assert.assertEquals(8192, scs.available);
 }

 @Test
 public void testFillBuff() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline3\r\ntest4\r\nline5\r\nline6\r\n".getBytes())));
 scs.FillBuff();
 Assert.assertEquals('\u0000', scs.readChar());
 Assert.assertEquals('\u0000', scs.readChar());
 }
}