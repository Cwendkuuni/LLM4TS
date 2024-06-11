package test;

import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertNull(decorator.getMap());
        Assert.assertEquals(0, decorator.size());
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testConstructorWithMap() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Assert.assertEquals(map, decorator.getMap());
        Assert.assertEquals(map.size(), decorator.size());
        Assert.assertEquals(map.isEmpty(), decorator.isEmpty());
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        Byte returnedValue = decorator.put(key, value);
        Assert.assertEquals(value, returnedValue);
        Assert.assertEquals(1, decorator.size());
        Assert.assertTrue(decorator.containsKey(key));
        Assert.assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        Byte returnedValue = decorator.get(key);
        Assert.assertEquals(value, returnedValue);
    }

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte returnedValue = decorator.get(null);
        Assert.assertNull(returnedValue);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key2, value2);
        decorator.clear();
        Assert.assertEquals(0, decorator.size());
        Assert.assertFalse(decorator.containsKey(key1));
        Assert.assertFalse(decorator.containsKey(key2));
        Assert.assertFalse(decorator.containsValue(value1));
        Assert.assertFalse(decorator.containsValue(value2));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        Byte removedValue = decorator.remove(key);
        Assert.assertEquals(value, removedValue);
        Assert.assertEquals(0, decorator.size());
        Assert.assertFalse(decorator.containsKey(key));
        Assert.assertFalse(decorator.containsValue(value));
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte removedValue = decorator.remove(null);
        Assert.assertNull(removedValue);
        Assert.assertEquals(0, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key2, value2);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        Assert.assertEquals(2, entrySet.size());
        Assert.assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            {
                this.key = key1;
                this.value = value1;
            }

            Short key;
            Byte value;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Entry<Short, Byte> that = (Entry<Short, Byte>) o;
                return this.key.equals(that.key) && this.value.equals(that.value);
            }

            @Override
            public int hashCode() {
                int result = this.key.hashCode();
                result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
                return result;
            }
        }));
        Assert.assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            {
                this.key = key2;
                this.value = value2;
            }

            Short key;
            Byte value;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Entry<Short, Byte> that = (Entry<Short, Byte>) o;
                return this.key.equals(that.key) && this.value.equals(that.value);
            }

            @Override
            public int hashCode() {
                int result = this.key.hashCode();
                result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
                return result;
            }
        }));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        Assert.assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        Assert.assertTrue(decorator.containsKey(key));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        decorator.put(key2, value2);
        Assert.assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertTrue(decorator.isEmpty());
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        Assert.assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        map.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1));
        map.put(Short.valueOf((short) 2), Byte.valueOf((byte) 2));
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.putAll(map);
        Assert.assertEquals(map.size(), decorator.size());
        Assert.assertEquals(map, decorator.getMap());
    }

    @Test
    public void testWrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Assert.assertEquals(key, decorator.wrapKey(key.shortValue()));
    }

    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Assert.assertEquals(key.shortValue(), decorator.unwrapKey(key));
    }

    @Test
    public void testWrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte value = Byte.valueOf((byte) 1);
        Assert.assertEquals(value, decorator.wrapValue(value.byteValue()));
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte value = Byte.valueOf((byte) 1);
        Assert.assertEquals(value.byteValue(), decorator.unwrapValue(value));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) in.readObject();
        in.close();
        Assert.assertEquals(decorator.getMap(), deserializedDecorator.getMap());
        Assert.assertEquals(decorator.size(), deserializedDecorator.size());
        Assert.assertTrue(decorator.containsKey(key));
        Assert.assertTrue(decorator.containsValue(value));
    }
}