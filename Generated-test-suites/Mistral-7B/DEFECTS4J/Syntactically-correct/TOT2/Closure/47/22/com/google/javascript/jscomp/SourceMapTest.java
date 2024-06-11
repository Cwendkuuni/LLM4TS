import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.rhino.Node;

public class SourceMapTest {
   @Test
   public void testAddMapping() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      Node node = new Node();
      FilePosition outputStartPosition = new FilePosition(1, 1);
      FilePosition outputEndPosition = new FilePosition(1, 2);
      String sourceFile = "test.js";

      sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
      String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
      assertEquals("test.js", fixedSourceFile);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingWithNullNode() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      Node node = null;
      FilePosition outputStartPosition = new FilePosition(1, 1);
      FilePosition outputEndPosition = new FilePosition(1, 2);

      sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingWithNullStartPosition() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      Node node = new Node();
      FilePosition outputStartPosition = null;
      FilePosition outputEndPosition = new FilePosition(1, 2);

      sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingWithNullEndPosition() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      Node node = new Node();
      FilePosition outputStartPosition = new FilePosition(1, 1);
      FilePosition outputEndPosition = null;

      sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
   }

   @Test
   public void testFixupSourceLocation() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      String sourceFile = "test.js";
      List<LocationMapping> prefixMappings = Collections.singletonList(
          new SourceMap.LocationMapping("test", "test_fixed"));
      sourceMap.setPrefixMappings(prefixMappings);

      String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
      assertEquals("test_fixed.js", fixedSourceFile);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testFixupSourceLocationWithNullSourceFile() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      String sourceFile = null;

      String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
   }

   @Test
   public void testValidate() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      boolean validate = true;

      sourceMap.validate(validate);
   }

   @Test
   public void testValidateWithFalse() {
      SourceMap sourceMap = SourceMap.V1.getInstance();
      boolean validate = false;

      sourceMap.validate(validate);
   }
}