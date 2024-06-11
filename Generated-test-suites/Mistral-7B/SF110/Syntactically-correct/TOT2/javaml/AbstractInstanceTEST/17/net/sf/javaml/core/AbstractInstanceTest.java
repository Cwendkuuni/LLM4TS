import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0});
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next());
    }

    @Test
    public void testAbstractInstance() {
        AbstractInstance instance = new AbstractInstance(null);
        assertNotNull(instance);
        assertEquals(0, instance.getID());
    }

    @Test
    public void testAbstractInstanceWithClassValue() {
        AbstractInstance instance = new AbstractInstance("test");
        assertNotNull(instance);
        assertEquals("test", instance.classValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetClassValueWithNull() {
        AbstractInstance instance = new AbstractInstance(null);
        instance.setClassValue(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }

    @Test
    public void testMinusWithNegativeIndex() {
        AbstractInstance instance1 = new DenseInstance(new double[1]);
        AbstractInstance instance2 = new DenseInstance(new double[1]);
        Instance result = instance1.minus(-1);
        assertNotNull(result);
        assertEquals(0, result.noAttributes());
    }

    @Test(expected = NullPointerException.class)
    public void testMinusWithNullInstance() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        Instance result = instance.minus(null);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinusWithNegativeValue() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        Instance result = instance.minus(-1.0);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testDivideWithNullInstance() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        Instance result = instance.divide(null);
        assertNull(result);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        Instance result = instance.divide(0.0);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testMultiplyWithNullInstance() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        Instance result = instance.multiply(null);
        assertNull(result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSqrtWithNegativeValue() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        instance.put(0, -1.0);
        Instance result = instance.sqrt();
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testNoAttributesNullReturn() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        int noAttributes = ((Map<K, Double>)instance).size();
        assertEquals(1, noAttributes);
        noAttributes = ((Map<K, Double>)instance).noAttributes();
        assertNull(noAttributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeID() {
        AbstractInstance instance = new AbstractInstance(-1);
        assertNull(instance);
    }

    @Test
    public void testEmptyConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertNotNull(instance);
        assertEquals(0, instance.getID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInstance() {
        AbstractInstance instance = new DenseInstance(new double[0]);
        assertNotNull(instance);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testEqualsWithSelf() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0});
        AbstractInstance instance2 = instance1;
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithDifferentInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0});
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithSameDataButDifferentInstanceClass() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0});
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithSameDataSameClass() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0});
        DenseInstance instance2 = new DenseInstance(instance1.getData());
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithNull() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0});
        assertFalse(instance.equals(null));
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0});
        Instance result = instance.add(2.0);
        assertNotNull(result);
        assertEquals(2, result.noAttributes());
        assertEquals(3.0, result.value(0), 0.01);
        assertEquals(1.0, result.value(1), 0.01);
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0});
        Instance result = instance1.add(instance2);
        assertNotNull(result);
        assertEquals(2, result.noAttributes());
        assertEquals(2.0, result.value(0), 0.01);
        assertEquals(1.0, result.value(1), 0.01);
    }

    @Test
    public void testSubtractWithDouble() {
        AbstractInstance instance1 = new DenseInstance(new double[]{3.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 1.0});
        Instance result = instance1.minus(instance2);
        assertNotNull(result);
        assertEquals(2, result.noAttributes());
        assertEquals(1.0, result.value(0), 0.01);
        assertEquals(1.0, result.value(1), 0.01);
    }

    @Test
    public void testSubtractWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{3.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 1.0});
        Instance result = instance1.minus(instance2);
        assertNotNull(result);
        assertEquals(2, result.noAttributes());
        assertEquals(1.0, result.value(0), 0.01);
        assertEquals(1.0, result.value(1), 0.01);
    }
}