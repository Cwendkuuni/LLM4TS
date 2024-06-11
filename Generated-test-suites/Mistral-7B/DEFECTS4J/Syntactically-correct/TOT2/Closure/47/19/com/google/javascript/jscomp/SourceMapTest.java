import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    // Given a new SourceMap instance and a Node with known source information
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node("sourceFile.js:5:10");

    // When a mapping is added to the SourceMap
    FilePosition outputStartPosition = new FilePosition(5, 15);
    FilePosition outputEndPosition = new FilePosition(5, 20);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Then the SourceMap should contain the added mapping
    assertTrue(sourceMap.generator.getMappings().size() > 0);

    // Basic edge case: Test mapping for a Node with no source file
    node = new Node();
    assertFalse(sourceMap.addMapping(node, outputStartPosition, outputEndPosition));
  }

  @Test
  public void testFixupSourceLocation() {
    // Given a SourceMap instance with prefix mappings
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    List<SourceMap.LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("oldPrefix/", "newPrefix/"));
    sourceMap.setPrefixMappings(prefixMappings);

    // When a source file is fixed up using the prefix mappings
    String sourceFile = "oldPrefix/sourceFile.js";
    String expectedFixed = "newPrefix/sourceFile.js";

    // Then the fixed up source file should match the expected value
    assertEquals(expectedFixed, sourceMap.fixupSourceLocation(sourceFile));

    // Edge case: Test mapping for a source file without a prefix
    sourceFile = "sourceFile.js";
    assertEquals(sourceFile, sourceMap.fixupSourceLocation(sourceFile));
  }

  @Test(expected = AssertionError.class)
  public void testAddMappingWithInvalidNode() {
    // Given an invalid Node with no source file or line number
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node();

    // When a mapping is added to the SourceMap
    FilePosition outputStartPosition = new FilePosition(5, 15);
    FilePosition outputEndPosition = new FilePosition(5, 20);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Then an AssertionError should be thrown
  }
}