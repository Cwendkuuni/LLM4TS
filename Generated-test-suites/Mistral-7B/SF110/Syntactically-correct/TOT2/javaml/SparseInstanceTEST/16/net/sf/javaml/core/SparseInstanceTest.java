import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

  @Test
  public void testSparseInstance() {
    SparseInstance sparseInstance = new SparseInstance();
    assertNull(sparseInstance.data);
    assertEquals(-1, sparseInstance.noAttributes);
  }

  // ...

  @Test(expected = IllegalArgumentException.class)
  public void testEmptySparseInstance() {
    new SparseInstance();
  }

  // ...
}