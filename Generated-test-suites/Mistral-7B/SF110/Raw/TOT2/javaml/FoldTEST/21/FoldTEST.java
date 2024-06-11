import static org.junit.Assert.*;
import javaml.core.*;
import org.junit.*;

public class FoldTest {
  Dataset parent;
  Fold fold;

  @Before
  public void setup() {
    parent = new DefaultDataset();
    Instance i = new DenseInstance(new double[] {1.0, 2.0});
    fold = new Fold(parent, new int[] {0, 1});
  }

  @Test
  public void testAddMethod() {
    // Basic test case
    assertThrows(UnsupportedOperationException.class, () -> fold.add(i));

    // Edge cases and exception handling
    try {
      fold.addAll(Arrays.asList(i));
    } catch (UnsupportedOperationException e) {
      // expected
    }

    try {
      fold.addAll(new ArrayList<>());
    } catch (UnsupportedOperationException e) {
      // expected
    }

    try {
      fold.addAll(Collections.emptyList());
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
}