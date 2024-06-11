import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {
  @Test
  public void testSourceMapConstructor() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    assertNotNull(sourceMap);
  }
}