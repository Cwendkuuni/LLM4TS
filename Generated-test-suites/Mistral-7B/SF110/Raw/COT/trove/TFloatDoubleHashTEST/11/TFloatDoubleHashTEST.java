package tests; // Make sure to replace this package name with the actual package name of your test file in your project

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import junit.framework.TestCase;

public class TFloatDoubleHashTest extends TestCase {

    private TFloatDoubleHash hashMap;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        hashMap = new TFloatDoubleHash(5);
    }

    // Test constructor methods
    public void testConstructorNoArguments() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
    }

    public void testConstructorInitialCapacity() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(3);
        assertEquals(3, hashMap.size());
    }

    public void testConstructorInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
        assertEquals(5, hashMap.size());
    }

    public void testConstructorInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f, 1.0f, 2.0);
        assertEquals(1.0f, hashMap.getNoEntryKey());
        assertEquals(2.0, hashMap.getNoEntryValue());
        assertEquals(5, hashMap.size());
    }

    // Test getters
    public void testGetNoEntryKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(0.0f, hashMap.getNoEntryKey());
    }

    public void testGetNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(0.0, hashMap.getNoEntryValue());
    }

    // Test setUp method
    public void testSetUp() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(5, hashMap.setUp(5));
    }

    // Test contains method
    public void testContainsWhenValueExists() {
        hashMap.put(1.0f, 1.0);
        assertTrue(hashMap.contains(1.0f));
    }

    public void testContainsWhenValueDoesNotExist() {
        assertFalse(hashMap.contains(2.0f));
    }

    // Test forEach method
    public void testForEach() {
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f); // Checks the value in the first iteration
                return true;
            }
        };
        assertTrue(hashMap.forEach(procedure));
    }

    // Test removeAt method
    public void testRemoveAt() {
        hashMap.put(1.0f, 1.0);
        int index = hashMap.index(1.0f);
        hashMap.removeAt(index);
        assertFalse(hashMap.contains(1.0f));
    }

    // Test index method
    public void testIndexWhenValueExists() {
        hashMap.put(1.0f, 1.0);
        int index = hashMap.index(1.0f);
        assertEquals(index, hashMap.size() - 1);
    }

    public void testIndexWhenValueDoesNotExist() {
        int index = hashMap.index(2.0f);
        assertEquals(-1, index);
    }

    // Test insertKey method
    public void testInsertKeyWhenKeyDoesNotExist() {
        hashMap.put(1.0f, 1.0);
        int index = hashMap.insertKey(2.0f);
        assertEquals(2, hashMap.size());
        assertTrue(hashMap.contains(2.0f));
        assertEquals(index, 1);
    }

    public void testInsertKeyWhenKeyAlreadyExists() {
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        int index = hashMap.insertKey(2.0f);
        assertEquals(2, hashMap.size());
        assertTrue(hashMap.contains(2.0f));
        assertEquals(index, -2);
    }

    // Test edge cases and exceptions
    public void testSetUpWhenInitialCapacityIsNegative() {
        try {
            hashMap.setUp(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testContainsWhenLoadFactorIsGreaterThanOne() {
        hashMap = new TFloatDoubleHash(5, 1.5f);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        hashMap.put(6.0f, 6.0);
        assertTrue(hashMap.contains(1.0f));
        assertTrue(hashMap.contains(2.0f));
        assertTrue(hashMap.contains(3.0f));
        assertTrue(hashMap.contains(4.0f));
        assertTrue(hashMap.contains(5.0f));
        assertTrue(hashMap.contains(6.0f));
        assertTrue(hashMap.size() == 6);
    }

    public void testIndexRehashedWhenKeyDoesNotExist() {
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        hashMap.put(6.0f, 6.0);
        hashMap.put(7.0f, 7.0); // This key is intentionally added to trigger rehashing
        int index = hashMap.index(8.0f);
        assertEquals(-1, index);
    }

    public void testInsertKeyRehashWhenKeyAlreadyExists() {
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        hashMap.put(6.0f, 6.0);
        hashMap.put(7.0f, 7.0); // This key is intentionally added to trigger rehashing
        int index = hashMap.insertKey(2.0f); // This key already exists
        assertEquals(-2, index);
    }

    public void testInsertKeyRehashWhenNoFreeSlotAvailable() {
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.put(3.0f, 3.0);
        hashMap.put(4.0f, 4.0);
        hashMap.put(5.0f, 5.0);
        hashMap.put(6.0f, 6.0);
        hashMap.put(7.0f, 7.0); // This key is intentionally added to trigger rehashing
        try {
            hashMap.insertKey(8.0f); // No free slot available
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWriteExternalAndReadExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.0f, 1.0);
        hashMap.put(2.0f, 2.0);
        hashMap.writeExternal(objectOutput);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TFloatDoubleHash deserializedHashMap = (TFloatDoubleHash) objectInput.readObject();
        assertEquals(2, deserializedHashMap.size());
        assertTrue(deserializedHashMap.contains(1.0f));
        assertTrue(deserializedHashMap.contains(2.0f));
    }
}