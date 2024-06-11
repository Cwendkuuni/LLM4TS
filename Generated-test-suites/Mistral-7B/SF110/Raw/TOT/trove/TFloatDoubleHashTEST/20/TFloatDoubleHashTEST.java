import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Assert;
import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        Assert.assertEquals(0, hash.size());
        Assert.assertFalse(hash.contains(0.1f));
        Assert.assertFalse(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                return false;
            }
        }));
    }

    @Test
    public void testBasicOperations() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        Assert.assertEquals(3, hash.size());
        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));

        Assert.assertEquals(1.0, hash.get(1.0f), 0.001);
        Assert.assertEquals(2.0, hash.get(2.0f), 0.001);
        Assert.assertEquals(3.0, hash.get(3.0f), 0.001);

        Assert.assertFalse(hash.contains(4.0f));
        Assert.assertFalse(hash.get(4.0f) instanceof Float);

        hash.remove(1.0f);
        Assert.assertEquals(2, hash.size());
        Assert.assertFalse(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));

        hash.remove(3.0f);
        Assert.assertEquals(1, hash.size());
        Assert.assertFalse(hash.contains(1.0f));
        Assert.assertFalse(hash.contains(2.0f));
        Assert.assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.5f);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        Assert.assertEquals(5, hash.size());
        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));
        Assert.assertTrue(hash.contains(4.0f));
        Assert.assertTrue(hash.contains(5.0f));

        hash.put(6.0f, 6.0); // should trigger rehashing

        Assert.assertEquals(10, hash.size());
        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));
        Assert.assertTrue(hash.contains(4.0f));
        Assert.assertTrue(hash.contains(5.0f));
        Assert.assertTrue(hash.contains(6.0f));
    }

    @Test
    public void testNegativeCases() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);

        try {
            hash.put(-1.0f, -1.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            hash.get(-1.0f);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            hash.remove(-1.0f);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            hash.index(-1.0f);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            hash.insertKey(-1.0f);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProcedureOperations() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                count++;
                return true;
            }
        });

        Assert.assertEquals(3, count);
    }
}