import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testEmptyConstructor() {
 decorator = new TDoubleShortMapDecorator();
 assertEquals(0, decorator.size());
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 assertEquals(map.size(), decorator.size());
 assertSame(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 double key = 1.0;
 short value = 1;
 Short expected = decorator.put(key, value);
 assertEquals(value, expected);
 assertEquals(1, decorator.size());

 double anotherKey = 2.0;
 Short anotherValue = 2;
 Short anotherExpected = decorator.put(anotherKey, anotherValue);
 assertEquals(anotherValue, anotherExpected);
 assertEquals(2, decorator.size());

 assertEquals(value, decorator.get(key));
 assertEquals(anotherValue, decorator.get(anotherKey));
 }

 @Test
 public void testGetNullKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 assertEquals(decorator._map.getNoEntryValue(), decorator.get(null));
 }

 @Test
 public void testGetWrongTypeKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 Integer wrongKey = 1;
 assertEquals(null, decorator.get(wrongKey));
 }

 @Test
 public void testGetCorrectKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 Double key = 1.0;
 short value = 1;
 decorator.put(key, value);

 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testClear() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 double key1 = 1.0;
 short value1 = 1;
 decorator.put(key1, value1);

 double key2 = 2.0;
 short value2 = 2;
 decorator.put(key2, value2);

 assertEquals(2, decorator.size());

 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.getMap());
 }

 @Test
 public void testRemove() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 double key1 = 1.0;
 short value1 = 1;
 decorator.put(key1, value1);

 double key2 = 2.0;
 short value2 = 2;
 decorator.put(key2, value2);

 assertEquals(2, decorator.size());

 Short removedValue = decorator.remove(key1);
 assertEquals(value1, removedValue);
 assertEquals(1, decorator.size());

 assertNull(decorator.get(key1));
 assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testRemoveNullKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveWrongTypeKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 Integer wrongKey = 1;
 assertNull(decorator.remove(wrongKey));
 }

 @Test
 public void testRemoveNonExistentKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 assertNull(decorator.remove(1.0));
 }

 @Test
 public void testEntrySet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 double key1 = 1.0;
 short value1 = 1;
 decorator.put(key1, value1);

 double key2 = 2.0;
 short value2 = 2;
 decorator.put(key2, value2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 for (Entry<Double, Short> entry : entrySet) {
 if (entry.getKey().equals(key1)) {
 assertEquals(value1, entry.getValue());
 } else if (entry.getKey().equals(key2)) {
 assertEquals(value2, entry.getValue());
 }
 }
 }

 @Test
 public void testContainsValue() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 short value = 1;
 decorator.put(1.0, value);
 decorator.put(2.0, 2);

 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 double key = 1.0;
 decorator.put(key, 1);
 decorator.put(2.0, 2);

 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 assertEquals(0, decorator.size());

 double key = 1.0;
 short value = 1;
 decorator.put(key, value);

 assertEquals(1, decorator.size());

 double anotherKey = 2.0;
 decorator.put(anotherKey, value);

 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 assertTrue(decorator.isEmpty());

 double key = 1.0;
 short value = 1;
 decorator.put(key, value);

 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, 1);
 anotherMap.put(2.0, 2);

 decorator.putAll(anotherMap);
 assertEquals(2, decorator.size());

 assertEquals(1, decorator.get(1.0).shortValue());
 assertEquals(2, decorator.get(2.0).shortValue());
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, decorator.wrapKey(1.0));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals(1.0, decorator.unwrapKey(1.0), 0.001);
 }

 @Test
 public void testWrapValue() {
 assertEquals((short)1, decorator.wrapValue(1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals(1, decorator.unwrapValue((short)1));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TDoubleShortMapDecorator decoratedMap = (TDoubleShortMapDecorator) objIn.readObject();

 assertEquals(map.size(), decoratedMap.size());

 for (Entry<Double, Short> entry : decoratedMap.entrySet()) {
 assertEquals(map.get(entry.getKey()), entry.getValue());
 }
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator) decorator.clone();
 assertEquals(map.size(), clonedDecorator.size());

 for (Entry<Double, Short> entry : clonedDecorator.entrySet()) {
 assertEquals(map.get(entry.getKey()), entry.getValue());
 }
 }
}