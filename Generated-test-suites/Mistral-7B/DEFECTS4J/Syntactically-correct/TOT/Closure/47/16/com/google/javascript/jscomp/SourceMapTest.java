import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.common.collect.Lists;

public class SourceMapTest {

  @Test
  public void testSourceMapV1() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();

    Node node = Node.newCompoundStatement();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceMap = "{\n" +
        "  \"version\": 1,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"mappings\": \"#\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"names\": [],\n" +
        "  \"sourcesContent\": {\n" +
        "    \"test.js\": \"// sourceMappingURL=data:application/json;base64,..." +
        "  },\n" +
        "  \"fileMappings\": [\n" +
        "    { \"input\": { \"line\": 1, \"column\": 1 }, \"output\": { \"line\": 1, \"column\": 1 } }\n" +
        "  ]\n" +
        "}";

    try {
      String actualSourceMap = "";
      sourceMap.appendTo(new StringWriter(new StringWriter()), "source-map.js");
      actualSourceMap = new StringReader(new FileReader("source-map.js")).getText();

      assertEquals(expectedSourceMap, actualSourceMap);
    } catch (IOException e) {
      fail("Failed to write source map file: " + e.getMessage());
    }
  }

  @Test
  public void testSourceMapDetailLevel() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().newInstance();

    Node node = Node.newCallExpression(
        Node.newIdentifier("func"), Lists.newArrayList(Node.newInteger(1)));

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    DetailLevel detailLevel = SourceMap.DetailLevel.SYMBOLS;

    sourceMap.setPrefixMappings(Lists.newArrayList(
        new SourceMap.LocationMapping("test/", "/src/test/")
    ));

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceMap = "{\n" +
        "  \"version\": 3,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"names\": [],\n" +
        "  \"sourcesContent\": {\n" +
        "    \"test.js\": \"// sourceMappingURL=data:application/json;base64,...\"\n" +
        "  },\n" +
        "  \"fileMappings\": [\n" +
        "    { \"input\": { \"line\": 1, \"column\": 1 }, \"output\": { \"line\": 1, \"column\": 1 } }\n" +
        "  ],\n" +
        "  \"sourceRoot\": \"/src\"\n" +
        "}";

    try {
      String actualSourceMap = "";
      sourceMap.appendTo(new StringWriter(new StringWriter()), "source-map.js");
      actualSourceMap = new StringReader(new FileReader("source-map.js")).getText();

      assertEquals(expectedSourceMap, actualSourceMap);
    } catch (IOException e) {
      fail("Failed to write source map file: " + e.getMessage());
    }
  }
}