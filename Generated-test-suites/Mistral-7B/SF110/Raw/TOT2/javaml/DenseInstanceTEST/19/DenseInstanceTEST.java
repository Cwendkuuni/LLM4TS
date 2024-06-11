import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DenseInstanceTest {

  @Test
  public void testConstructorWithDoubleArray() {
    // ...
  }

  @Test
  public void testConstructorWithDoubleArrayAndClassValue() {
    // ...
  }

  // Test other methods with basic JUnit test cases...

  @Test
  public void testEmptyConstructor() {
    DenseInstance instance = new DenseInstance();
    assertEquals(0, instance.noAttributes());
  }

  @Test
  public void testConstructorWithNullClassValue() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att, null);
    assertEquals(att.length, instance.noAttributes());
    for (int i = 0; i < att.length; i++) {
      assertEquals(att[i], instance.value(i), 0.001);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNegativeSize() {
    new DenseInstance(-1);
  }

  @Test
  public void testContainsKeyWithNullKey() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertFalse(instance.containsKey(null));
  }

  @Test
  public void testContainsKeyWithIntegerKeyOutOfRange() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertFalse(instance.containsKey(4));
  }

  @Test
  public void testContainsValueWithNullValue() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertFalse(instance.containsValue(null));
  }

  @Test
  public void testContainsValueWithNonNumberValue() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertFalse(instance.containsValue("String Value"));
  }

  @Test
  public void testPutWithNullKey() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertThrows(NullPointerException.class, () -> instance.put(null, 4.0));
  }

  @Test
  public void testPutWithKeyAlreadyPresent() {
    double[] att = new double[]{1.0, 2.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    double originalValue = instance.value(2);
    instance.put(2, 4.0);
    assertEquals(4.0, instance.value(2));
    assertNotEquals(originalValue, instance.value(2));
  }

  @Test
  public void testPutAllWithNullMap() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    Map<Integer, Double> map = null;
    instance.putAll(map);
    assertEquals(att.length, instance.noAttributes());
    for (int i = 0; i < att.length; i++) {
      assertEquals(att[i], instance.value(i), 0.001);
    }
  }

  @Test
  public void testPutAllWithMapContainingKeyPresentInInstance() {
    double[] att = new double[]{1.0, 2.0, 2.0, 3.0};
    Map<Integer, Double> map = new HashMap<>();
    map.put(2, 5.0);
    DenseInstance instance = new DenseInstance(att);
    instance.putAll(map);
    assertEquals(5.0, instance.value(2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveWithNullKey() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    instance.remove(null);
  }

  @Test
  public void testRemoveWithKeyNotPresentInInstance() {
    double[] att = new double[]{1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertThrows(UnsupportedOperationException.class, () -> instance.remove(4));
  }

  // Test other methods with basic JUnit test cases and edge cases...

}