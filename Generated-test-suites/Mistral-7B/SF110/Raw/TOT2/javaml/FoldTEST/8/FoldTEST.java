import static org.junit.Assert.*;

import net.sf.javaml.core.*;
import org.junit.Test;

public class FoldTest {

  @Test
  public void testFoldConstructor() {
    Dataset parentDataset = new DefaultDataset();
    int[] indices = new int[5];
    Fold fold = new Fold(parentDataset, indices);
    assertNotNull(fold);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFoldConstructorNullParent() {
    int[] indices = new int[5];
    Fold fold = new Fold(null, indices);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFoldConstructorNullIndices() {
    Dataset parentDataset = new DefaultDataset();
    Fold fold = new Fold(parentDataset, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFoldConstructorEmptyIndices() {
    Dataset parentDataset = new DefaultDataset();
    int[] indices = new int[0];
    Fold fold = new Fold(parentDataset, indices);
  }

  @Test
  public void testGetParent() {
    Dataset parentDataset = new DefaultDataset();
    int[] indices = new int[5];
    Fold fold = new Fold(parentDataset, indices);
    assertEquals(parentDataset, fold.parent);
  }

  @Test
  public void testSize() {
    Dataset parentDataset = new DefaultDataset();
    int[] indices = new int[5];
    Fold fold = new Fold(parentDataset, indices);
    assertEquals(indices.length, fold.size());
  }

  @Test
  public void testSizeEmpty() {
    Dataset parentDataset = new DefaultDataset();
    Fold fold = new Fold(parentDataset, new int[0]);
    assertEquals(0, fold.size());
  }

  @Test
  public void testInstance() {
    Dataset parentDataset = new DefaultDataset();
    parentDataset.add(new DenseInstance(new double[]{1.0, 2.0}));
    parentDataset.add(new DenseInstance(new double[]{3.0, 4.0}));
    int[] indices = new int[]{0, 1};
    Fold fold = new Fold(parentDataset, indices);

    Instance expectedInstance = parentDataset.instance(0);
    Instance instance = fold.instance(0);
    assertSame(expectedInstance, instance);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInstanceOutOfBounds() {
    Dataset parentDataset = new DefaultDataset();
    int[] indices = new int[]{0, 1};
    Fold fold = new Fold(parentDataset, indices);
    fold.instance(indices.length);
  }
}