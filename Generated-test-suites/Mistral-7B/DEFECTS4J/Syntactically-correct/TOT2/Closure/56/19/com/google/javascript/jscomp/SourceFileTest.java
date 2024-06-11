import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 3;
 String expectedLine = "  // some code here";
 String actualLine = sourceFile.getLine(lineNumber);
 assertEquals(expectedLine, actualLine);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 0;
 sourceFile.getLine(lineNumber);
 }

 @Test
 public void testGetLineNullCode() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 int lineNumber = 3;
 String actualLine = sourceFile.getLine(lineNumber);
 assertNull(actualLine);
 }

 @Test
 public void testGetLineIOError() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 int lineNumber = 3;
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 sourceFile.clearCachedSource(); // clear cached code
 String actualLine = sourceFile.getLine(lineNumber);
 assertNull(actualLine);
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "  // line 5\n" +
 "}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 5;
 Region expectedRegion = new Region(1, 2, "// line 5");
 Region actualRegion = sourceFile.getRegion(lineNumber);
 assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
 assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
 assertEquals(expectedRegion.getCode(), actualRegion.getCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "  // line 5\n" +
 "}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 0;
 sourceFile.getRegion(lineNumber);
 }

 @Test
 public void testGetRegionNullCode() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 int lineNumber = 5;
 Region actualRegion = sourceFile.getRegion(lineNumber);
 assertNull(actualRegion);
 }

 @Test
 public void testGetRegionIOError() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "  // line 5\n" +
 "}";
 int lineNumber = 5;
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 sourceFile.clearCachedSource(); // clear cached code
 Region actualRegion = sourceFile.getRegion(lineNumber);
 assertNull(actualRegion);
 }

 @Test
 public void testGetLineOffset() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 3;
 int expectedLineOffset = 25; // assuming line offsets are 1-indexed
 int actualLineOffset = sourceFile.getLineOffset(lineNumber);
 assertEquals(expectedLineOffset, actualLineOffset);
 }

 @Test
 public void testGetLineOffsetInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 int actualLineOffset = sourceFile.getLineOffset(0);
 assertNotEquals(0, actualLineOffset);
 }

 @Test
 public void testGetLineOffsetNullCode() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 int actualLineOffset = sourceFile.getLineOffset(0);
 assertNotEquals(0, actualLineOffset);
 }

 @Test
 public void testGetRegionLength() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int expectedRegionLength = 10;
 int actualRegionLength = sourceFile.getNumLines();
 assertEquals(expectedRegionLength, actualRegionLength);
 }
}