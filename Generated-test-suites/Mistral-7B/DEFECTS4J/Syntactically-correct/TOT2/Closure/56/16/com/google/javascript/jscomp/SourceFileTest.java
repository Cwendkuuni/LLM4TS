import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SourceFileTest {

 @Test
 public void testGetLine() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 sourceFile.setCode(code);

 String expectedLine = "function testFunction() {\n";
 String line = sourceFile.getLine(1);

 assertEquals(expectedLine, line);
 }

 @Test
 public void testGetLineNull() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "";
 sourceFile.setCode(code);

 assertNull(sourceFile.getLine(1));
 }

 @Test
 public void testGetRegion() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 sourceFile.setCode(code);

 int lineNumber = 1;
 Region region = sourceFile.getRegion(lineNumber);
 String expectedRegion = "function testFunction() {\n";

 assertEquals(expectedRegion, region.getCode());
 assertEquals(1, region.getStartLineNumber());
 assertEquals(1, region.getEndLineNumber());
 }

 @Test
 public void testGetRegionNull() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "";
 sourceFile.setCode(code);

 Region region = sourceFile.getRegion(1);

 assertNull(region);
 }

 @Test
 public void testGetLineOffset() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 sourceFile.setCode(code);

 int lineNumber = 1;
 int expectedOffset = 1; // line offset is 1 based
 int offset = sourceFile.getLineOffset(lineNumber);

 assertEquals(expectedOffset, offset);
 }

 @Test
 public void testGetLineOffsetNegative() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 sourceFile.setCode(code);

 int lineNumber = -1;
 int offset = sourceFile.getLineOffset(lineNumber);

 assertEquals(-1, offset);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineOffsetIllegalArgument() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 sourceFile.setCode(code);

 int lineNumber = 1000;
 int offset = sourceFile.getLineOffset(lineNumber);
 }

 @Test
 public void testGetNumLines() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}\n" +
 "function anotherFunction() {\n" +
 "  // some other code here\n" +
 "}";
 sourceFile.setCode(code);

 int expectedNumLines = 3; // including empty line at end
 int numLines = sourceFile.getNumLines();

 assertEquals(expectedNumLines, numLines);
 }

 @Test
 public void testGetCode() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 sourceFile.setCode(code);

 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n" +
 "}";
 String actualCode = sourceFile.getCode();

 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testGetCodeIOException() throws IOException {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String code = "";
 sourceFile.setCode(code);

 try {
 sourceFile.getCode();
 } catch (IOException e) {
 // expected
 }
 }
}