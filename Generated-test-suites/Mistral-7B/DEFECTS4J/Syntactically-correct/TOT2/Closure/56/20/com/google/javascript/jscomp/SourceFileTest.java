import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedLine = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";

 try {
 sourceFile.setCode(expectedLine);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = 1;
 String line = sourceFile.getLine(lineNumber);

 assertEquals("function testFunction() {", line);
 }

 @Test
 public void testGetLineNegativeNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");

 int lineNumber = -1;
 String line = sourceFile.getLine(lineNumber);

 assertNull(line);
 }

 @Test
 public void testGetLineOutOfBound() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";

 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = sourceFile.getNumLines() + 1;
 String line = sourceFile.getLine(lineNumber);

 assertNull(line);
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";

 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = 1;
 Region region = sourceFile.getRegion(lineNumber);

 assertEquals(new SimpleRegion(1, 1, "function testFunction() {\n"), region);
 }

 @Test
 public void testGetRegionNegativeNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");

 int lineNumber = -1;
 Region region = sourceFile.getRegion(lineNumber);

 assertNull(region);
 }

 @Test
 public void testGetRegionOutOfBound() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";

 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = sourceFile.getNumLines() + 1;
 Region region = sourceFile.getRegion(lineNumber);

 assertNull(region);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");

 int lineNumber = 0;
 String line = sourceFile.getLine(lineNumber);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");

 int lineNumber = 0;
 Region region = sourceFile.getRegion(lineNumber);
 }

 @Test
 public void testGetRegionWithException() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  throw new Error('Test error');\n" +
 "}";

 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = 1;
 try {
 Region region = sourceFile.getRegion(lineNumber);
 } catch (IOException e) {
 fail("IOException should not be thrown.");
 }

 String expectedRegionCode = "throw new Error('Test error');";
 Region region = sourceFile.getRegion(lineNumber);

 assertEquals(new SimpleRegion(1, 1, expectedRegionCode), region);
 }
}