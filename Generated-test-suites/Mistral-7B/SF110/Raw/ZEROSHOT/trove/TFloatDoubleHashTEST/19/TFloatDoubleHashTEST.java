package test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Assert;
import org.junit.Test;

import static gnu.trove.impl.hash.HashFunctions.hash;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        Assert.assertEquals(0, hash.size());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        Assert.assertEquals(10, hash.size());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        Assert.assertEquals(10, hash.size());
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);;
        Assert.assertEquals(10, hash.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        Assert.assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        Assert.assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        Assert.assertEquals(5, hash.size());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                Assert.assertEquals(value, 1.0f);
                return true;
            }
        };
        hash.forEach(procedure);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.removeAt(-1);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.removeAt(0);
        Assert.assertEquals(1, hash.size());
        Assert.assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        int index = hash.index(1.0f);
        Assert.assertEquals(0, index);
        index = hash.index(2.0f);
        Assert.assertEquals(1, index);
        index = hash.index(3.0f);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutput);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TFloatDoubleHash hashFromSerial = (TFloatDoubleHash) objectInput.readObject();
        Assert.assertEquals(2, hashFromSerial.size());
        Assert.assertTrue(hashFromSerial.contains(1.0f));
        Assert.assertTrue(hashFromSerial.contains(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        Assert.assertEquals(2, hash.size());
        Assert.assertTrue(hash.contains(1.0f));
        Assert.assertTrue(hash.contains(2.0f));
        int index = hash.insertKey(3.0f);
        Assert.assertEquals(3, hash.size());
        Assert.assertTrue(hash.contains(3.0f));
        Assert.assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
        Assert.assertEquals(5, hash.size());
        int index = hash.insertKey(6.0f);
        Assert.assertEquals(6, hash.size());
        Assert.assertTrue(hash.contains(6.0f));
        Assert.assertEquals(4, index);
    }

    @Test
    public void testInsertKeyWithFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
        hash.put(6.0f, 6.0);
        Assert.assertEquals(6, hash.size());
        int index = hash.insertKey(7.0f);
        Assert.assertEquals(7, hash.size());
        Assert.assertTrue(hash.contains(7.0f));
        Assert.assertEquals(0, index);
    }

    @Test
    public void testInsertKeyWithRemovedSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
        hash.put(6.0f, 6.0);
        hash.removeAt(3);
        int index = hash.insertKey(7.0f);
        Assert.assertEquals(7, hash.size());
        Assert.assertTrue(hash.contains(7.0f));
        Assert.assertEquals(3, index);
    }
}