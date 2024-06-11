
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(0.5f, 1.0);
        assertTrue(hash.contains(0.5f));
    }

    @Test
    public void testContainsEmptyHashMap() {
        TFloatDoubleHash hash = new TFloatDoubleHash(0);
        assertFalse(hash.contains(0.5f));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNullInput() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNegativeInput() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(-0.5f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.put(0.3f, 3.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 0.1f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testForEachEmptyHashMap() {
        TFloatDoubleHash hash = new TFloatDoubleHash(0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = NullPointerException.class)
    public void testForEachNullProcedure() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        TFloatProcedure procedure = null;
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(0.5f));
        hash.put(0.5f, 1.0);
        assertTrue(hash.contains(0.5f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullHashMap() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1);
        hash.put(0.5f, 1.0);
        assertFalse(hash.contains(0.6f));
        try {
            hash.put(0.6f, 2.0);
            fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testInsertKeyNullKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(null));
        try {
            hash.put(null, 1.0);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}