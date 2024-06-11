import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.common.collect.Lists;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @org.junit.Test
  public void testGetInstance() {
    SourceMap sourceMap = SourceMap.getInstance(SourceMap.Format.DEFAULT);
    assertNotNull(sourceMap);
  }

  @org.junit.Test
  public void testAddMapping() throws Exception {
    StringWriter out = new StringWriter();
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    Node node = Node.newIntLiteral(1);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(out, "Test.java");
    String result = out.toString();
    String expected = "SourceMap\nversion=0.3.0\nsourcesFile=\"Test.java\"\nmappings=\"";
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testAppendTo() throws Exception {
    StringWriter out = new StringWriter();
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.appendTo(out, "Test.java");
    String result = out.toString();
    String expected = "SourceMap\nversion=0.3.0\nsourcesFile=\"Test.java\"\nmappings=\"\"";
    assertEquals(expected, result);
  }

  // Test the remaining methods with similar test cases as above
}