import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    // Given: A SourceMap instance and a Node
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = Node.newIntLiteral(1);

    // When: Call addMapping method with valid parameters
    String sourceFile = "TestSource.java";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Then: Assert that mapping has been added
    assertTrue(sourceMap.generator.getMappings().size() > 0);

    // Edge case: Test with empty source file and negative lineno
    SourceMap sourceMapEdge = new SourceMap(SourceMap.Format.V1.getInstance());
    node = Node.newIntLiteral(-1);
    sourceFile = "";
    outputStartPosition = new FilePosition(0, 0);
    outputEndPosition = new FilePosition(0, 0);
    sourceMapEdge.addMapping(node, outputStartPosition, outputEndPosition);

    // Error case: Test with null Node
    SourceMap sourceMapError = new SourceMap(SourceMap.Format.V1.getInstance());
    node = null;
    sourceFile = "TestSource.java";
    outputStartPosition = new FilePosition(1, 1);
    outputEndPosition = new FilePosition(1, 2);
    try {
      sourceMapError.addMapping(node, outputStartPosition, outputEndPosition);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // ... Add other test methods for other public methods
}