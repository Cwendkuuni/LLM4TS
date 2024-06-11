import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedLine = "function testFunction() {\n" +
 "  // some code here\n}";
 try {
 sourceFile.setCode(expectedLine);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = 2;
 String expectedResult = "  // some code here";
 String result = sourceFile.getLine(lineNumber);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineWithInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedErrorMessage = "Expected line number between 1 and " +
 sourceFile.getNumLines() +
 "\nActual: ";
 sourceFile.getLine(10);
 fail(expectedErrorMessage);
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n}\n" +
 "function anotherFunction() {\n" +
 "  // some other code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = 2;
 Region expectedRegion = new Region(1, 2,
 "  // some code here");
 Region result = sourceFile.getRegion(lineNumber);
 assertEquals(expectedRegion.getStartLine(), result.getStartLine());
 assertEquals(expectedRegion.getEndLine(), result.getEndLine());
 assertEquals(expectedRegion.getCode(), result.getCode());
 }

 @Test(expected = NullPointerException.class)
 public void testGetRegionWithNullCode() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 Region result = sourceFile.getRegion(2);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionWithInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 Region result = sourceFile.getRegion(10);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetRegionWithLargeRegion() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function testFunction() {\n" +
 "  // some code here\n}\n" +
 "function anotherFunction() {\n" +
 "  // some other code here\n}\n" +
 "function yetAnotherFunction() {\n" +
 "  // some yet other code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }

 int lineNumber = 3;
 int regionSize = 5;
 Region expectedRegion = new Region(1, 4,
 "function anotherFunction() {\n" +
 "  // some other code here\n}\nfunction yetAnotherFunction() {\n" +
 "  // some yet other code here\n}");
 Region result = sourceFile.getRegion(lineNumber, regionSize);
 assertEquals(expectedRegion.getStartLine(), result.getStartLine());
 assertEquals(expectedRegion.getEndLine(), result.getEndLine());
 assertEquals(expectedRegion.getCode(), result.getCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionWithNegativeRegionSize() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 int regionSize = -1;
 Region result = sourceFile.getRegion(1, regionSize);
 fail("Expected IllegalArgumentException");
 }
}