import static org.junit.Assert.*;

import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        // ... test case from Expert 1
    }

    @Test
    public void testClear() {
        // ... test case from Expert 2
    }

    @Test
    public void testContainsKey() {
        // ... test case from Expert 3
    }

    // Add more test cases for other methods here

    @Test
    public void testPutAndGet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        double newValue = 4.5;
        int key = 1;

        instance.put(key, newValue);
        assertEquals(newValue, instance.get(key), 1e-8);
    }

    // Add more test cases for other methods and various scenarios

    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);
        indicesToRemove.add(2);

        instance.removeAttributes(indicesToRemove);
        assertEquals(1, instance.noAttributes());
        assertTrue(Arrays.deepEquals(new double[] {1.0}, instance.attributes));
    }

    @Test
    public void testHashCodeAndEquals() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[] {1.0, 3.0, 3.0});

        assertEquals(instance1, instance1); // reflexive property
        assertEquals(instance1, instance2); // test identical objects
        assertNotEquals(instance1, instance3); // test non-identical objects

        DenseInstance instance4 = instance1.copy();
        assertEquals(instance1, instance4); // test shallow copy

        instance1.removeAttribute(0);
        assertNotEquals(instance1, instance4); // test attribute changes
    }

    // Add more test cases for other methods and various scenarios

    @Test
    public void testUnsupportedOperationException() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});

        try {
            instance.remove(1); // test remove method with unsupported operation exception
            fail();
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    // Add more test cases for other methods and various scenarios
}