import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

 private static SourceMap createSourceMap(Format format) {
 SourceMap sourceMap = null;
 switch (format) {
 case V1:
 sourceMap = SourceMap.Format.V1.getInstance().getInstance();
 break;
 case DEFAULT:
 sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();
 break;
 case V2:
 sourceMap = SourceMap.Format.V2.getInstance().getInstance();
 break;
 case V3:
 sourceMap = SourceMap.Format.V3.getInstance().getInstance();
 break;
 }
 return sourceMap;
 }

 @Test
 public void testAddMapping() throws IOException {
 SourceMap sourceMap = createSourceMap(Format.V1);
 Node node = new Node();
 node.setSourceFileName("test.js");
 node.setLineno(1);
 node.setCharno(5);
 FilePosition outputStartPosition = new FilePosition(1, 5);
 FilePosition outputEndPosition = new FilePosition(1, 10);

 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 sourceMap.appendTo(outStream, "map.js");

 String mapContent = outStream.toString();
 String expectedContent = "1 0 test.js test.js test.js 1 5-5,6-10\n";

 Assert.assertEquals(expectedContent, mapContent);
 }

 @Test
 public void testFixupSourceLocation() {
 SourceMap sourceMap = createSourceMap(Format.V1);

 // Test without prefix mappings
 String sourceFile = "test/prefix1/test.js";
 String fixed = sourceMap.fixupSourceLocation(sourceFile);
 Assert.assertEquals(sourceFile, fixed);

 // Test with prefix mappings
 sourceMap.setPrefixMappings(Lists.newArrayList(
 new SourceMap.LocationMapping("/prefix1/", "prefix2/")));
 sourceFile = "test/prefix1/test.js";
 fixed = sourceMap.fixupSourceLocation(sourceFile);
 Assert.assertEquals("prefix2/test.js", fixed);
 }

 @Test
 public void testAppendTo() throws IOException {
 SourceMap sourceMap = createSourceMap(Format.V1);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 sourceMap.appendTo(outStream, "map.js");

 Assert.assertEquals(0, outStream.size());

 // Test with addMapping
 sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(1, 1));
 sourceMap.appendTo(outStream, "map.js");

 String mapContent = outStream.toString();
 String expectedContent = "1 0 test.js test.js test.js 1 1\n";
 Assert.assertEquals(expectedContent, mapContent);

 // Test with setPrefixMappings
 sourceMap.setPrefixMappings(Lists.newArrayList(
 new SourceMap.LocationMapping("/prefix1/", "prefix2/")));
 sourceMap.appendTo(outStream, "map.js");
 sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(1, 1));

 mapContent = outStream.toString();
 expectedContent = "1 0 prefix2/test.js test.js test.js 1 1\n";
 Assert.assertEquals(expectedContent, mapContent);
 }

 @Test
 public void testReset() throws IOException {
 SourceMap sourceMap = createSourceMap(Format.V1);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 sourceMap.appendTo(outStream, "map.js");
 sourceMap.reset();
 sourceMap.appendTo(outStream, "map.js");

 String mapContent = outStream.toString();
 String expectedContent = "";
 Assert.assertEquals(expectedContent, mapContent);
 }

 @Test
 public void testSetStartingPosition() throws IOException {
 SourceMap sourceMap = createSourceMap(Format.V1);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 sourceMap.appendTo(outStream, "map.js");
 sourceMap.setStartingPosition(1, 1);
 sourceMap.appendTo(outStream, "map.js");

 String mapContent = outStream.toString();
 String expectedContent = "0: test.js\n1: test.js\n";
 Assert.assertEquals(expectedContent, mapContent);
 }

 @Test
 public void testSetWrapperPrefix() throws IOException {
 SourceMap sourceMap = createSourceMap(Format.V1);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 sourceMap.appendTo(outStream, "map.js");
 sourceMap.setWrapperPrefix("prefix_");
 sourceMap.appendTo(outStream, "map.js");

 String mapContent = outStream.toString();
 String expectedContent = "1 0 prefix_test.js test.js test.js 1 1\n";
 Assert.assertEquals(expectedContent, mapContent);
 }

 @Test
 public void testValidate() throws IOException {
 SourceMap sourceMap = createSourceMap(Format.V1);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 sourceMap.appendTo(outStream, "map.js");

 try {
 sourceMap.validate(false);
 } catch (AssertionError e) {
 // Expected
 }

 sourceMap.validate(true);
 }
}