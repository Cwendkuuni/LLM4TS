import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortMap());
 assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TDoubleShortMap expectedMap = _map;
 TDoubleShortMap actualMap = decorator.getMap();
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = (short)1;
 Short actualValue = decorator.put(key, value);
 assertEquals(value, actualValue);
 Short existingValue = (short)2;
 decorator.put(key, existingValue);
 Short newValue = (short)3;
 Short actualNewValue = decorator.put(key, newValue);
 assertEquals(newValue, actualNewValue);
 assertEquals(existingValue, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 Double key = 1.0;
 Short value = (short)1;
 decorator.put(key, value);
 Short actualValue = decorator.get(key);
 assertEquals(value, actualValue);
 }

 @Test
 public void testGetWithNullKey() {
 Short actualValue = decorator.get(null);
 assertNull(actualValue);
 }

 @Test
 public void testClear() {
 int sizeBeforeClear = decorator.size();
 decorator.clear();
 int sizeAfterClear = decorator.size();
 assertEquals(sizeBeforeClear, sizeAfterClear);
 }

 @Test
 public void testRemove() {
 Double key = 1.0;
 Short value = (short)1;
 decorator.put(key, value);
 Short actualValue = decorator.remove(key);
 assertEquals(value, actualValue);
 Short nonExistingValue = (short)2;
 Short actualNonExistingValue = decorator.remove(nonExistingValue);
 assertNull(actualNonExistingValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> expectedEntrySet = _map.entrySet();
 Set<Entry<Double, Short>> actualEntrySet = decorator.entrySet();
 assertEquals(expectedEntrySet, actualEntrySet);
 }

 @Test
 public void testContainsValue() {
 Short value = (short)1;
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Double key = 1.0;
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 int size = 3;
 for (int i = 0; i < size; i++) {
 decorator.put((double)i, (short)i);
 }
 int actualSize = decorator.size();
 assertEquals(size, actualSize);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap mapToAdd = new TDoubleShortMap();
 mapToAdd.put(1.0, (short)1);
 mapToAdd.put(2.0, (short)2);
 decorator.putAll(mapToAdd);
 Set<Entry<Double, Short>> actualEntrySet = decorator.entrySet();
 int size = mapToAdd.size();
 for (Entry<Double, Short> entry : actualEntrySet) {
 assertEquals(entry.getKey(), entry.getValue());
 }
 assertEquals(size, decorator.size());
 }

 @Test
 public void testWrapKey() {
 double key = 1.0;
 Double wrappedKey = decorator.wrapKey(key);
 assertEquals(key, wrappedKey.doubleValue());
 }

 @Test
 public void testUnwrapKey() {
 Double key = new Double(1.0);
 double unwrappedKey = decorator.unwrapKey(key);
 assertEquals(key.doubleValue(), unwrappedKey, 0.0);
 }

 @Test
 public void testWrapValue() {
 short value = (short)1;
 Short wrappedValue = decorator.wrapValue(value);
 assertEquals(value, wrappedValue.shortValue());
 }

 @Test
 public void testUnwrapValue() {
 Short value = new Short((short)1);
 short unwrappedValue = decorator.unwrapValue(value);
 assertEquals(value.shortValue(), unwrappedValue);
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(in);
 Set<Entry<Double, Short>> readEntrySet = readDecorator.entrySet();
 assertEquals(_map.size(), readDecorator.size());
 for (Entry<Double, Short> entry : readEntrySet) {
 assertEquals(entry.getKey(), entry.getValue());
 }
 }

 @Test
 public void testCloneable() {
 TDoubleShortMapDecorator originalDecorator = decorator;
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)originalDecorator.clone();
 Set<Entry<Double, Short>> originalEntrySet = decorator.entrySet();
 Set<Entry<Double, Short>> clonedEntrySet = clonedDecorator.entrySet();
 for (Entry<Double, Short> entry : originalEntrySet) {
 assertEquals(entry.getKey(), entry.getValue());
 }
 for (Entry<Double, Short> entry : clonedEntrySet) {
 assertEquals(entry.getKey(), entry.getValue());
 }
 }
}