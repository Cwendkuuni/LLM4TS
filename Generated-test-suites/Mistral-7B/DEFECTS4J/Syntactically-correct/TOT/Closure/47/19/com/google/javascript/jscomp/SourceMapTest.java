import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tests for the SourceMap class.
 */
public class SourceMapTest {

  private SourceMap sourceMap;
  private File outputFile;

  @Before
  public void setUp() throws IOException {
    outputFile = new File("output.js.map");
    FileWriter writer = new FileWriter(outputFile);
    sourceMap = new SourceMap(new SourceMapGeneratorV2());
    sourceMap.setPrefixMappings(getPrefixMappings());
    sourceMap.setStartingPosition(1, 0);
  }

  @Test
  public void testAddMapping() {
    Node node = Node.newNode("fn() {}");
    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 1));
    String sourceFile = "testSourceFile.js";
    String originalName = "testFunction";
    sourceMap.addMapping(node, new FilePosition(2, 2), new FilePosition(1, 1)); // Invalid
    sourceMap.addMapping(node, new FilePosition(1, 2), new FilePosition(2, 2));

    String mappedFileName = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("testSourceFile_fixed.js", mappedFileName);

    sourceMap.appendTo(new FileWriter("output.js"), "sourceContent");
    sourceMap.validate(true);
  }

  @Test
  public void testReset() {
    Node node = Node.newNode("fn() {}");
    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.reset();
    assertFalse(sourceMap.generator.isOpen());
  }

  @Test
  public void testValidate() {
    Node node = Node.newNode("fn() {}");
    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.validate(false);
    assertTrue(sourceMap.generator.isOpen());
    sourceMap.validate(true);
    assertFalse(sourceMap.generator.isOpen());
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "test_prefix";
    sourceMap.setWrapperPrefix(prefix);
    String mappedFileName = sourceMap.fixupSourceLocation("testSourceFile.js");
    assertEquals("test_prefixtestSourceFile.js", mappedFileName);
  }

  @Test
  public void testAppendTo() throws IOException {
    Node node = Node.newNode("fn() {}");
    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.appendTo(new FileWriter("output.js"), "sourceContent");
    File file = new File("output.js.map");
    Map<String, String> map = Maps.newHashMap();
    Function<String, String> reader = new Function<String, String>() {
      @Override public String apply(String input) {
        map.put("input", input);
        return null;
      }
    };
    sourceMap.generator.readMapFile(reader, file);
    assertEquals("input", map.get("sources"));
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorV2());
    sourceMap.setStartingPosition(1, 0);
    FilePosition position = new FilePosition(1, 2);
    Node node = Node.newNode("fn() {}");
    sourceMap.addMapping(node, position, new FilePosition(1, 1));
    String sourceFile = "testSourceFile.js";
    String originalName = "testFunction";
    sourceMap.addMapping(node, position, new FilePosition(2, 2));
    String mappedFileName = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("testSourceFile_fixed.js", mappedFileName);

    sourceMap.setStartingPosition(2, 0);
    mappedFileName = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("testSourceFile.js", mappedFileName);
  }

  private List<LocationMapping> getPrefixMappings() {
    return Lists.newArrayList(new LocationMapping("test_prefix", "fixed_prefix"));
  }

  @After
  public void tearDown() throws IOException {
    sourceMap.reset();
    outputFile.delete();
  }
}