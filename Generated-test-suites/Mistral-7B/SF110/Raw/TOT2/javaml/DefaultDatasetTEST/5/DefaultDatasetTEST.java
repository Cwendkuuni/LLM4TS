import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;

public class DefaultDatasetTest {

  @Test
  public void testDefaultDatasetConstructorWithCollection() {
    Collection<Instance> coll = new ArrayList<>();
    DefaultDataset dataset = new DefaultDataset(coll);
    // Check if the maxAttributes and classes are initialized correctly
    assertEquals(0, dataset.maxAttributes);
    assertNotNull(dataset.classes);
    // Check if the instances are added correctly
    Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
    coll.add(instance);
    dataset.addAll(coll);
    assertEquals(1, dataset.size());
    Instance actualInstance = dataset.instance(0);
    assertEquals(instance, actualInstance);
  }

  @Test
  public void testDefaultDatasetConstructorWithoutCollection() {
    DefaultDataset dataset = new DefaultDataset();
    // Check if the maxAttributes and classes are initialized correctly
    assertEquals(0, dataset.maxAttributes);
    assertNotNull(dataset.classes);
  }
}