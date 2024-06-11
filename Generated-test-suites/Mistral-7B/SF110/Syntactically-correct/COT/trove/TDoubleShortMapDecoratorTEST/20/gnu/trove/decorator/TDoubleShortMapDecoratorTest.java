import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Arrays;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator emptyDecorator = new TDoubleShortMapDecorator();
 assertNotNull(emptyDecorator);
 TDoubleShortMapDecorator decoratedMap = new TDoubleShortMapDecorator(map);
 assertSame(map, decoratedMap.getMap());
 }

 @Test
 public void testPut() {
 double key = 1.0;
 short value = 1;
 short expectedValue = value;
 short actualValue = decorator.put(key, value);
 assertEquals(expectedValue, actualValue);
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));

 key = 2.0;
 value = 2;
 expectedValue = value;
 actualValue = decorator.put(key, value);
 assertEquals(expectedValue, actualValue);
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));

 assertEquals(2, decorator.size());
}

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short) 1);
 fail("Should throw NullPointerException");
}

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 fail("Should throw NullPointerException");
}

 @Test(expected = IllegalArgumentException.class)
 public void testPutDuplicateKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(1.0, (short) 2);
 fail("Should throw IllegalArgumentException");
}

 @Test
 public void testGet() {
 double key = 1.0;
 short value = 1;
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));

 key = 2.0;
 value = 2;
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));

 key = 3.0;
 assertEquals(null, decorator.get(key));
}

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 decorator.get(null);
 fail("Should throw NullPointerException");
}

 @Test(expected = ClassCastException.class)
 public void testGetNonDoubleKey() {
 decorator.put("key", (short) 1);
 decorator.get("key");
 fail("Should throw ClassCastException");
}

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
}

 @Test
 public void testRemove() {
 double key = 1.0;
 short value = 1;
 decorator.put(key, value);
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));

 decorator.remove(key);
 assertFalse(decorator.containsKey(key));
 assertNull(decorator.get(key));
}

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 fail("Should throw NullPointerException");
}

 @Test(expected = ClassCastException.class)
 public void testRemoveNonDoubleKey() {
 decorator.put("key", (short) 1);
 decorator.remove("key");
 fail("Should throw ClassCastException");
}

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());

 double key = 1.0;
 short value = 1;
 decorator.put(key, value);

 Entry<Double, Short> entry = decorator.entrySet().iterator().next();
 assertEquals(key, entry.getKey());
 assertEquals(value, entry.getValue());
}

 @Test
 public void testContainsValue() {
 short value = 1;
 decorator.put(1.0, value);
 decorator.put(2.0, value);
 assertTrue(decorator.containsValue(value));

 value = 2;
 decorator.put(1.0, value);
 decorator.put(2.0, value);
 assertFalse(decorator.containsValue(value));
}

 @Test
 public void testContainsKey() {
 double key = 1.0;
 decorator.put(key, (short) 1);
 assertTrue(decorator.containsKey(key));

 key = 2.0;
 assertFalse(decorator.containsKey(key));
}

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());

 double key = 1.0;
 short value = 1;
 decorator.put(key, value);
 assertEquals(1, decorator.size());

 key = 2.0;
 decorator.put(key, value);
 assertEquals(2, decorator.size());
}

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());

 decorator.put(1.0, (short) 1);
 assertFalse(decorator.isEmpty());
}

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short) 1);
 anotherMap.put(2.0, (short) 2);
 decorator.putAll(anotherMap);

 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
}

 @Test
 public void testWrapKey() {
 double key = 1.0;
 assertEquals(key, decorator.wrapKey(key));
}

 @Test
 public void testUnwrapKey() {
 double key = 1.0;
 assertEquals(key, decorator.unwrapKey(decorator.wrapKey(key)));
}

 @Test
 public void testWrapValue() {
 short value = 1;
 assertEquals(value, decorator.wrapValue(value));
}

 @Test
 public void testUnwrapValue() {
 short value = 1;
 assertEquals(value, decorator.unwrapValue(decorator.wrapValue(value)));
}

 @Test
 public void testExternalizable() {
 TDoubleShortMapDecorator cloneDecorator = decorator.clone();
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());) {
 decorator.writeExternal(new ObjectOutputStream(bais));
 deserializedDecorator.readExternal(new ObjectInputStream(bais));
 }
 assertEquals(decorator, cloneDecorator);
 assertEquals(decorator, deserializedDecorator);
}

 @Test
 public void testCloneable() {
 TDoubleShortMapDecorator cloneDecorator = decorator.clone();
 assertEquals(decorator, cloneDecorator);
}
}