import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TFloatCharHashTest {

    private TFloatCharHash floatCharHash;

    @Before
    public void setUp() {
        floatCharHash = new TFloatCharHash(5);
    }

    @Test
    public void testDefaultConstructor() {
        TFloatCharHash emptyFloatCharHash = new TFloatCharHash();
        assertEquals(0, emptyFloatCharHash.size());
        assertEquals(0.0f, emptyFloatCharHash.getNoEntryKey());
        assertEquals('\u0000', emptyFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TFloatCharHash floatCharHash = new TFloatCharHash(10);
        assertEquals(10, floatCharHash.size());
        assertEquals(0.0f, floatCharHash.getNoEntryKey());
        assertEquals('\u0000', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityLoadFactorConstructor() {
        TFloatCharHash floatCharHash = new TFloatCharHash(15, 0.75f);
        assertEquals(15, floatCharHash.size());
        assertEquals(0.0f, floatCharHash.getNoEntryKey());
        assertEquals('\u0000', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityLoadFactorNoEntryKeyNoEntryValueConstructor() {
        TFloatCharHash floatCharHash = new TFloatCharHash(15, 0.75f, 1.0f, 'A');
        assertEquals(15, floatCharHash.size());
        assertEquals(1.0f, floatCharHash.getNoEntryKey());
        assertEquals('A', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash floatCharHash = new TFloatCharHash(5);
        floatCharHash.setUp(5);
        assertEquals(5, floatCharHash.size());
        assertNull(floatCharHash._set);
    }

    @Test
    public void testContains() {
        floatCharHash.put(1.0f, 'A');
        assertTrue(floatCharHash.contains(1.0f));
        assertFalse(floatCharHash.contains(2.0f));
    }

    @Test
    public void testforEach() {
        floatCharHash.put(1.0f, 'A');
        floatCharHash.put(2.0f, 'B');

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(key, 1.0f);
                return true;
            }
        };

        assertTrue(floatCharHash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        floatCharHash.put(1.0f, 'A');
        floatCharHash.removeAt(0);
    }

    @Test
    public void testIndex() {
        floatCharHash.put(1.0f, 'A');
        floatCharHash.put(2.0f, 'B');
        assertEquals(1, floatCharHash.index(1.0f));
        assertEquals(-1, floatCharHash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        floatCharHash.put(1.0f, 'A');
        floatCharHash.put(2.0f, 'B');
        floatCharHash.put(3.0f, 'C');
        assertEquals(3, floatCharHash.size());
        assertEquals(1.0f, floatCharHash.get(1));
        assertEquals('C', floatCharHash.get(2));
        assertSame(1, floatCharHash.index(1.0f));
        assertSame(-1, floatCharHash.index(4.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        TFloatCharHash floatCharHash = new TFloatCharHash(5);
        floatCharHash.put(1.0f, 'A');
        floatCharHash.writeExternal(objectOutput);
        objectOutput.close();
        byte[] bytes = outputStream.toByteArray();
        TFloatCharHash loadedFloatCharHash = new TFloatCharHash();
        loadedFloatCharHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(1, loadedFloatCharHash.size());
        assertEquals(1.0f, loadedFloatCharHash.get(0));
        assertEquals('A', loadedFloatCharHash.get(0));
    }
}