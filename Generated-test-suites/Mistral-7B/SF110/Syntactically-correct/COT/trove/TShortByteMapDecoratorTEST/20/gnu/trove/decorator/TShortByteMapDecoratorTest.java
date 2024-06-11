package test;

import static org.junit.Assert.*;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 }

 // Testing constructor
 @Test
 public void testTShortByteMapDecoratorConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 assertNotNull(decorator);
 }

 // Testing constructor with TShortByteMap argument
 @Test
 public void testTShortByteMapDecoratorConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
 assertNotNull(decorator);
 assertSame(map, decorator.getMap());
 }

 // Testing getMap method
 @Test
 public void testGetMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 // Testing put method
 @Test
 public void testPut() {
 final byte expectedValue = 1;
 short key = 1;
 byte actualValue = decorator.put(key, expectedValue);
 assertEquals(expectedValue, actualValue);
 byte actualValue2 = decorator.put((short) 2, (byte) 2);
 assertEquals(actualValue2, decorator.get((short) 1));
 assertEquals(expectedValue, decorator.get((short) 1));
 }

 // Testing get method
 @Test
 public void testGet() {
 final byte expectedValue = 1;
 short key = 1;
 byte actualValue = decorator.put(key, expectedValue).get((short) 1);
 assertEquals(expectedValue, actualValue);
 short key2 = 2;
 assertEquals(null, decorator.get(key2));
 }

 // Testing clear method
 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 // Testing remove method
 @Test
 public void testRemove() {
 final byte expectedValue = 1;
 short key = 1;
 byte actualValue = decorator.put(key, expectedValue).get((short) 1);
 assertEquals(expectedValue, actualValue);
 byte actualValueRemoved = decorator.remove((short) 1);
 assertEquals(expectedValue, actualValueRemoved);
 assertEquals(0, decorator.size());
 }

 // Testing entrySet method
 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertNotNull(entrySet);
 }

 // Testing containsValue method
 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertTrue(decorator.containsValue((byte) 1));
 assertFalse(decorator.containsValue((byte) 3));
 }

 // Testing containsKey method
 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertTrue(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));
 assertFalse(decorator.containsKey(null));
 }

 // Testing size method
 @Test
 public void testSize() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertEquals(2, decorator.size());
 }

 // Testing isEmpty method
 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put((short) 1, (byte) 1);
 assertEquals(false, decorator.isEmpty());
 }

 // Testing putAll method
 @Test
 public void testPutAll() {
 TShortByteMap anotherMap = new TShortByteMap();
 anotherMap.put((short) 3, (byte) 3);
 anotherMap.put((short) 4, (byte) 4);
 decorator.putAll(anotherMap);
 assertEquals(4, decorator.size());
 }

 // Testing wrapKey method
 @Test (expected = AssertionError.class)
 public void testWrapKeyNullKey() {
 decorator.wrapKey(null);
 }

 // Testing unwrapKey method with null key
 @Test (expected = ClassCastException.class)
 public void testUnwrapKeyNullKey() {
 decorator.unwrapKey(null);
 }

 // Testing wrapValue method with null value
 @Test (expected = AssertionError.class)
 public void testWrapValueNullValue() {
 decorator.wrapValue(null);
 }

 // Testing unwrapValue method with null value
 @Test (expected = ClassCastException.class)
 public void testUnwrapValueNullValue() {
 decorator.unwrapValue(null);
 }

 // Testing readExternal method
 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.readExternal(objIn);
 assertEquals(decorator, decorator2);
 }

 // Testing writeExternal method
 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
 ObjectInput objIn = new ObjectInputStream(in);
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.readExternal(objIn);
 assertEquals(decorator, decorator2);
 }

 // Testing clone method
 @Test
 public void testClone() throws CloneNotSupportedException {
 TShortByteMapDecorator original = new TShortByteMapDecorator(map);
 TShortByteMapDecorator clone = (TShortByteMapDecorator)original.clone();
 assertEquals(original, clone);
 }
}