
import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithAtt() {
        double[] att = new double[10];
        DenseInstance di = new DenseInstance(att);
        assertEquals(10, di.noAttributes());
    }

    @Test
    public void testDenseInstanceWithAttAndClassValue() {
        double[] att = new double[10];
        DenseInstance di = new DenseInstance(att, "label");
        assertEquals("label", di.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance di = new DenseInstance();
        assertEquals(0, di.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputInConstructor() {
        DenseInstance di = new DenseInstance(null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputForClassValueInConstructor() {
        double[] att = new double[10];
        DenseInstance di = new DenseInstance(att, null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSizeInConstructor() {
        DenseInstance di = new DenseInstance(-2);
        fail();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueOutOfBounds() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        di.value(-1);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyForContainsKey() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        assertFalse(di.containsKey(null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueForContainsValue() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        assertFalse(di.containsValue(null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullMapForPutAll() {
        double[] att = new double[10];
        DenseInstance di = new DenseInstance(att);
        Map<Integer, Double> emptyMap = null;
        di.putAll(emptyMap);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKeyForPut() {
        double[] att = new double[3];
        DenseInstance di = new DenseInstance(att);
        di.put(1, 1.0);
        di.put(1, 2.0);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyForGet() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        di.get(null);
        fail();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeWithNegativeIndex() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        di.removeAttribute(-1);
        fail();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeWithIndexOutOfBounds() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        di.removeAttribute(5);
        fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedRemoveOperation() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        di.remove(1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForCopy() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        DenseInstance copiedDi = di.copy(null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForRemoveAttributes() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        di.removeAttributes(null);
        fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptySetForRemoveAttributes() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        Set<Integer> emptySet = new HashSet<>();
        di.removeAttributes(emptySet);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndicesOutOfBoundsForRemoveAttributes() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        Set<Integer> indices = new HashSet<>();
        indices.add(-1);
        indices.add(5);
        di.removeAttributes(indices);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOverlappingIndicesForRemoveAttributes() {
        DenseInstance di = new DenseInstance(new double[]{1, 2, 3, 4, 5});
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        di.removeAttributes(indices);
        fail();
    }
}