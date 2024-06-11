import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceFile.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("test.js");
 String code = "function testFunction() {" +
 "  var x = 1;" +
 "  var y = 2;" +
 "}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 // This should not happen since setCode is not supposed to throw an IOException
 fail("IOException should not have been thrown");
 }

 assertEquals("function testFunction() {" +
 "  var x = 1;" +
 "  var y = 2;" +
 "", sourceFile.getLine(1));
 assertEquals("  var y = 2;", sourceFile.getLine(3));

 // Test edge case: line number out of bounds
 try {
 sourceFile.getLine(100);
 fail("IndexOutOfBoundsException should have been thrown");
 } catch (IndexOutOfBoundsException e) {
 // This is expected
 }
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("test.js");
 String code = "function testFunction() {" +
 "  var x = 1;" +
 "  var y = 2;" +
 "}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 // This should not happen since setCode is not supposed to throw an IOException
 fail("IOException should not have been thrown");
 }

 assertEquals(new SimpleRegion(1, 1, "function testFunction() {" +
 "  var x = 1;" +
 "}"), sourceFile.getRegion(1));
 assertEquals(new SimpleRegion(1, 2, "var y = 2;"), sourceFile.getRegion(2));
 }

 @Test(expected = NullPointerException.class)
 public void testGetLineWithNullSourceFile() {
 SourceFile sourceFile = null;
 assertEquals(null, sourceFile.getLine(1));
 }

 @Test(expected = IOException.class)
 public void testGetCodeWithNullFileName() {
 try {
 new SourceFile(null);
 fail("IOException should have been thrown");
 } catch (IOException e) {
 // This is expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCodeWithEmptyFileName() {
 try {
 new SourceFile("");
 fail("IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // This is expected
 }
 }
}