import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SourceMapTest {

 @Test
 public void testSourceMapV1() throws IOException {
 SourceMap sourceMap = new SourceMap.Format.V1().getInstance();
 // Setting up test data
 String inputSource = "function main() {\n" +
 "  var a = 1;\n" +
 "}";
 String expectedSource = "function main() {\n" +
 "  var a = 1;\n" +
 "}\n" +
 "//# sourceMappingURL=data:application/json;base64,eyJ2ZXJpZmllZCI6IiM1ZmZmMTIifQ0RQUiIsICsic291cmNlcyI6IiIsIiwic291cmNlcmluZCI6IiM1ZmZmMTIifQ0RQUIpO1zfdXNpcywNCjx0aW1lIHVwIHlvdSBQYXVsIiwic291cmNsIjoiIiwic291cmNpZCI6IjEuMSIpO1wwc3dlcnM6bW1hcml6ZS5zcGxpdHMifQ==";

 String inputFileName = "TestSourceMapInput.js";
 String expectedFileName = "TestSourceMapInput.js";

 Node inputNode = Node.importNode(JSContext.compileStringToNode(inputSource), null);
 FilePosition inputStartPosition = new FilePosition(1, 1);
 FilePosition inputEndPosition = new FilePosition(1, 11);

 sourceMap.addMapping(inputNode, inputStartPosition, inputEndPosition);
 sourceMap.setStartingPosition(1, 1);
 String output = "";
 try (Appendable out = sourceMap.newAppendable()) {
 sourceMap.appendTo(out, expectedFileName);
 }
 assertEquals(expectedSource, sourceMap.toSource());
 }

 @Test
 public void testSourceMapDefault() throws IOException {
 SourceMap sourceMap = new SourceMap.Format.DEFAULT.getInstance().getInstance();
 // Setting up test data
 String inputSource = "function main() {\n" +
 "  var a = 1;\n" +
 "}";
 String expectedSource = "function main() {\n" +
 "  var a = 1;\n" +
 "}\n" +
 "//# sourceMappingURL=data:application/json;base64,eyJ2ZXJpZmllZCI6IiM1ZmZmMTIifQ0RQUiIsICsic291cmNlcyI6IiIsIiwic291cmNlcmluZCI6IiM1ZmZmMTIifQ0RQUIpO1zfdXNpcywNCjx0aW1lIHVwIHlvdSBQYXVsIiwic291cmNsIjoiIiwic291cmNpZCI6IjEuMSIpO1wwc3dlcnM6bW1hcml6ZS5zcGxpdHMifQ==";

 String inputFileName = "TestSourceMapInput.js";
 String expectedFileName = "TestSourceMapInput.js";

 Node inputNode = Node.importNode(JSContext.compileStringToNode(inputSource), null);
 FilePosition inputStartPosition = new FilePosition(1, 1);
 FilePosition inputEndPosition = new FilePosition(1, 11);

 sourceMap.addMapping(inputNode, inputStartPosition, inputEndPosition);
 sourceMap.setStartingPosition(1, 1);
 String output = "";
 try (Appendable out = sourceMap.newAppendable()) {
 sourceMap.appendTo(out, expectedFileName);
 }
 assertEquals(expectedSource, sourceMap.toSource());
 }

 @Test
 public void testSourceMapV2() throws IOException {
 SourceMap sourceMap = new SourceMap.Format.V2().getInstance().getInstance();
 // Setting up test data
 String inputSource = "function main() {\n" +
 "  var a = 1;\n" +
 "}";
 String expectedSource = "{\n" +
 "  \"version\": 3,\n" +
 "  \"file\": \"TestSourceMapInput.js\",\n" +
 "  \"sources\": [\"TestSourceMapInput.js\"],\n" +
 "  \"sourceRoot\": \"\",\n" +
 "  \"mappings\": \"AA,GA,SA{C}1,OA{B}:1:1:1:1\"\n" +
 "}";

 String inputFileName = "TestSourceMapInput.js";
 String expectedFileName = "TestSourceMapInput.js";

 Node inputNode = Node.importNode(JSContext.compileStringToNode(inputSource), null);
 FilePosition inputStartPosition = new FilePosition(1, 1);
 FilePosition inputEndPosition = new FilePosition(1, 11);

 sourceMap.addMapping(inputNode, inputStartPosition, inputEndPosition);
 sourceMap.setDetailLevel(DetailLevel.ALL);
 sourceMap.setStartingPosition(1, 1);
 String output = "";
 try (Appendable out = sourceMap.newAppendable()) {
 sourceMap.appendTo(out, expectedFileName);
 }
 assertEquals(expectedSource, sourceMap.toSource());
 }

 @Test
 public void testSourceMapInvalidInput() {
 SourceMap sourceMap = new SourceMap.Format.V1().getInstance().getInstance();
 // Setting up test data
 Node inputNode = Node.importNode(JSContext.compileStringToNode("function main() {}"), null);
 FilePosition inputStartPosition = new FilePosition(1, 1);
 FilePosition inputEndPosition = new FilePosition(1, 11);

 sourceMap.addMapping(inputNode, inputStartPosition, inputEndPosition);
 // Test with null source file name
 try {
 sourceMap.setStartingPosition(1, 1);
 String output = "";
 try (Appendable out = sourceMap.newAppendable()) {
 sourceMap.appendTo(out, null);
 }
 assertEquals("", sourceMap.toSource());
 } catch (IOException e) {
 // Expected
 }

 // Test with empty source file name
 try {
 sourceMap.setStartingPosition(1, 1);
 String output = "";
 try (Appendable out = sourceMap.newAppendable()) {
 sourceMap.appendTo(out, "");
 }
 assertEquals("", sourceMap.toSource());
 } catch (IOException e) {
 // Expected
 }
 }
}