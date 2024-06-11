package gnu.trove.decorator.test;

import static org.junit.Assert.*;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;

import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyDecorator() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertNull(decorator.get(null));
        assertNull(decorator.get(new Double(0.0)));
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testDecoratorWithMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Short value = Short.valueOf((short) 1);
        decorator.put(new Double(1.0), value);
        decorator.put(new Double(2.0), Short.valueOf((short) 2));

        assertEquals(2, decorator.size());
        assertEquals(value, decorator.get(new Double(1.0)));
        assertEquals(Short.valueOf((short) 2), decorator.get(new Double(2.0)));
        assertNull(decorator.get(null));
        assertNull(decorator.get(new Double(3.0)));

        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testDecoratorWithNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertNull(decorator.get(null));
        decorator.put(null, (short) 1);
        assertNull(decorator.get(null));
        assertEquals(1, decorator.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDecoratorAddEntry() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Entry<Double, Short> entry = new Entry<Double, Short>() {
            private Double key = new Double(1.0);
            private Short value = Short.valueOf((short) 1);

            public boolean equals(Object obj) {
                if (obj instanceof Entry) {
                    Entry<?, ?> other = (Entry<?, ?>)obj;
                    return this.key.equals(other.getKey()) && this.value.equals(other.getValue());
                }
                return false;
            }

            public Double getKey() {
                return this.key;
            }

            public Short getValue() {
                return this.value;
            }
        };
        decorator.add(entry);
    }

    @Test
    public void testDecoratorRemoveEntry() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(new Double(1.0), Short.valueOf((short) 1));
        decorator.put(new Double(2.0), Short.valueOf((short) 2));

        assertTrue(decorator.containsKey(new Double(1.0)));
        assertTrue(decorator.containsKey(new Double(2.0)));
        assertEquals(Short.valueOf((short) 1), decorator.get(new Double(1.0)));
        assertEquals(Short.valueOf((short) 2), decorator.get(new Double(2.0)));

        decorator.remove(new Double(1.0));
        assertFalse(decorator.containsKey(new Double(1.0)));
        assertTrue(decorator.containsKey(new Double(2.0)));
        assertEquals(Short.valueOf((short) 2), decorator.get(new Double(2.0)));
    }

    @Test
    public void testDecoratorRemoveNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(null, Short.valueOf((short) 1));
        decorator.put(new Double(2.0), Short.valueOf((short) 2));

        assertTrue(decorator.containsKey(null));
        assertTrue(decorator.containsKey(new Double(2.0)));
        assertEquals(Short.valueOf((short) 1), decorator.get(null));
        assertEquals(Short.valueOf((short) 2), decorator.get(new Double(2.0)));

        decorator.remove(null);
        assertFalse(decorator.containsKey(null));
        assertTrue(decorator.containsKey(new Double(2.0)));
        assertEquals(Short.valueOf((short) 2), decorator.get(new Double(2.0)));
    }

    @Test
    public void testDecoratorExternalizable() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.put(new Double(1.0), Short.valueOf((short) 1));
        decorator.put(new Double(2.0), Short.valueOf((short) 2));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        decorator.writeExternal(out);
        out.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bais);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
        in.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.get(new Double(1.0)), deserializedDecorator.get(new Double(1.0)));
        assertEquals(decorator.get(new Double(2.0)), deserializedDecorator.get(new Double(2.0)));
    }
}