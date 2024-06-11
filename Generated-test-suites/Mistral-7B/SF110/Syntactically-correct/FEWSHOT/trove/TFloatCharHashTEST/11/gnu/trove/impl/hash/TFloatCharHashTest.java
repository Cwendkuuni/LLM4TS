package org.apache.commons.cli; // Assuming you want to test TFloatCharHash in the same package

import static org.junit.Assert.*;
import gnu.trove.impl.*;
import gnu.trove.procedure.*;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

    @Test
    public void testBasicOperations() {
        TFloatCharHash hashMap = new TFloatCharHash(10);
        assertNull(hashMap.getNoEntryKey());
        assertEquals('\u0000', hashMap.getNoEntryValue());
        assertEquals(0, hashMap.size());

        hashMap.put((float) 1.0, 'a');
        hashMap.put((float) 2.0, 'b');
        hashMap.put((float) 3.0, 'c');

        assertEquals(3, hashMap.size());
        assertTrue(hashMap.containsKey((float) 1.0));
        assertTrue(hashMap.containsKey((float) 2.0));
        assertTrue(hashMap.containsKey((float) 3.0));

        assertEquals('a', hashMap.get((float) 1.0));
        assertEquals('b', hashMap.get((float) 2.0));
        assertEquals('c', hashMap.get((float) 3.0));

        hashMap.remove((float) 2.0);
        assertEquals(2, hashMap.size());
        assertNull(hashMap.get((float) 2.0));
    }

    @Test
    public void testPutAndGetWithNullKeyOrValue() {
        TFloatCharHash hashMap = new TFloatCharHash(10);
        assertNull(hashMap.getNoEntryKey());
        assertEquals('\u0000', hashMap.getNoEntryValue());

        hashMap.put((float) null, 'a');
        assertEquals(0, hashMap.size());

        hashMap.put((float) 1.0, null);
        assertEquals(1, hashMap.size());

        assertNull(hashMap.get((float) null));
        assertEquals(null, hashMap.get((float) 1.0));
    }

    @Test
    public void testPutAndGetWithLoadFactor() {
        TFloatCharHash hashMap = new TFloatCharHash(5, 0.75f);

        for (int i = 0; i < 10; i++) {
            hashMap.put((float) i, (char) i);
        }

        assertEquals(8, hashMap.size());

        for (int i = 0; i < 10; i++) {
            assertEquals((char) i, hashMap.get((float) i));
        }
    }

    @Test
    public void testPutAndGetWithNoEntryKeyAndValue() {
        TFloatCharHash hashMap = new TFloatCharHash(10);
        hashMap.put((float) 1.0, 'a');
        hashMap.put((float) 2.0, 'b');

        assertEquals(2, hashMap.size());

        float noEntryKey = hashMap.getNoEntryKey();
        char noEntryValue = hashMap.getNoEntryValue();

        assertEquals(0.0f, noEntryKey);
        assertEquals('\u0000', noEntryValue);

        hashMap.put((float) 3.0, 'c');
        assertEquals(3, hashMap.size());

        assertEquals((float) 0.0f, hashMap.getNoEntryKey());
        assertEquals('\u0000', hashMap.getNoEntryValue());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatCharHash hashMap = new TFloatCharHash(10);
        hashMap.put((float) 1.0, 'a');
        hashMap.put((float) 2.0, 'b');

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hashMap.writeExternal(objectOutput);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TFloatCharHash deserializedHashMap = (TFloatCharHash) objectInput.readObject();

        assertEquals(hashMap, deserializedHashMap);
        assertEquals(2, deserializedHashMap.size());
        assertEquals('a', deserializedHashMap.get((float) 1.0));
        assertEquals('b', deserializedHashMap.get((float) 2.0));
    }
}