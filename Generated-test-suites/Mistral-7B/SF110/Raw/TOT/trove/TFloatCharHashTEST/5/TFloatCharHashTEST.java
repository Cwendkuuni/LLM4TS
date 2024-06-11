package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Assert;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        Assert.assertFalse(hash.contains(Float.NaN));
        Assert.assertFalse(hash.contains(0.0f));
        Assert.assertEquals(0, hash.size());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));
        Assert.assertEquals(3, hash.size());

        Assert.assertEquals('A', hash.get(1.0f));
        Assert.assertEquals('B', hash.get(2.0f));
        Assert.assertEquals('C', hash.get(3.0f));
        Assert.assertNull(hash.get(Float.NaN));
        Assert.assertNull(hash.get(4.0f));

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 1.0f;
            }
        };

        Assert.assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatCharHash(-5);
    }

    @Test
    public void testLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');
        hash.put(4.0f, 'D');

        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));
        Assert.assertTrue(hash.contains(4.0f));
        Assert.assertEquals(4, hash.size());

        hash.put(5.0f, 'E');
        Assert.assertFalse(hash.contains(Float.NaN));
        Assert.assertEquals(5, hash.size());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 2.0f;
            }
        };

        Assert.assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testNoEntryValues() {
        TFloatCharHash hash = new TFloatCharHash(10, Float.NaN, (char) 0);

        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertTrue(hash.contains(3.0f));
        Assert.assertEquals(3, hash.size());

        Assert.assertEquals(Float.NaN, hash.getNoEntryKey());
        Assert.assertEquals((char) 0, hash.getNoEntryValue());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'A');

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutput);

        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        TFloatCharHash deserializedHash = (TFloatCharHash) objectInput.readObject();

        Assert.assertEquals(1, deserializedHash.size());
        Assert.assertTrue(deserializedHash.contains(1.0f));
        Assert.assertEquals('A', deserializedHash.get(1.0f));
    }

    @Test
    public void testRemove() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        hash.remove(2.0f);

        Assert.assertEquals(2, hash.size());
        Assert.assertFalse(hash.contains(2.0f));
        Assert.assertEquals('A', hash.get(1.0f));
        Assert.assertEquals('C', hash.get(3.0f));
    }
}