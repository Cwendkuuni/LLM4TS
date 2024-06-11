package org.apache.commons.cli; // Note: This package should be replaced with the actual package name of TDoubleShortMapDecorator.

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMap _map;
    private TDoubleShortMapDecorator _decorator;

    @Before
    public void setup() {
        _map = mock(TDoubleShortMap.class);
        _decorator = new TDoubleShortMapDecorator(_map);
    }

    @Test
    public void testEmptyMap() {
        assertEquals(0, _decorator.size());
        assertEmpty(_decorator.entrySet());
    }

    @Test
    public void testPut() {
        doReturn(new Short((short) 1)).when(_map).put(anyDouble(), anyShort());

        _decorator.put(1.0, (short) 1);
        _decorator.put(2.0, (short) 2);

        verify(_map).put(1.0, (short) 1);
        verify(_map).put(2.0, (short) 2);

        assertEquals(2, _decorator.size());
    }

    @Test
    public void testGet() {
        doReturn(new Short((short) 1)).when(_map).get(anyDouble());

        _decorator.put(1.0, (short) 1);

        assertEquals((short) 1, _decorator.get(1.0));
    }

    @Test
    public void testClear() {
        doReturn(new Short((short) 1)).when(_map).put(anyDouble(), anyShort());

        _decorator.put(1.0, (short) 1);
        _decorator.clear();

        verify(_map).clear();
        assertEquals(0, _decorator.size());
    }

    @Test
    public void testRemove() {
        doReturn(new Short((short) 1)).when(_map).put(anyDouble(), anyShort());
        doReturn(true).when(_map).remove(anyDouble());

        _decorator.put(1.0, (short) 1);
        _decorator.remove(1.0);

        verify(_map).remove(1.0);
        assertEquals(0, _decorator.size());
    }

    @Test
    public void testEntrySet() {
        doReturn(mock(Set.class)).when(_map).entrySet();

        Set<Map.Entry<Double, Short>> entrySet = _decorator.entrySet();

        assertEquals(0, entrySet.size());

        _decorator.put(1.0, (short) 1);
        _decorator.put(2.0, (short) 2);

        Map.Entry<Double, Short> entry = _decorator.entrySet().iterator().next();
        assertEquals(1.0, entry.getKey());
        assertEquals((short) 1, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        doReturn(new Short((short) 1)).when(_map).get(anyDouble());

        _decorator.put(1.0, (short) 1);

        assertTrue(_decorator.containsValue((short) 1));
    }

    @Test
    public void testContainsKey() {
        doReturn(true).when(_map).containsKey(anyDouble());

        _decorator.put(1.0, (short) 1);

        assertTrue(_decorator.containsKey(1.0));
    }

    @Test
    public void testSize() {
        doReturn(2).when(_map).size();

        assertEquals(2, _decorator.size());
    }

    @Test
    public void testIsEmpty() {
        doReturn(false).when(_map).isEmpty();

        assertFalse(_decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        doReturn(new Short((short) 1)).when(_map).put(anyDouble(), anyShort());

        _decorator.putAll(map);

        verify(_map).putAll(map);
        assertEquals(2, _decorator.size());
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        _decorator.writeExternal(objectOutput);
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.readExternal(objectInput);

        assertEquals(0, decorator.size());

        _decorator.put(1.0, (short) 1);
        _decorator.put(2.0, (short) 2);

        objectOutput.writeByte(0);
        objectOutput.writeObject(_decorator._map);
        objectInput.readByte();
        TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
        deserializedDecorator.readExternal(objectInput);

        assertEquals(2, deserializedDecorator.size());
        assertTrue(deserializedDecorator.containsKey(1.0));
        assertTrue(deserializedDecorator.containsKey(2.0));
    }
}