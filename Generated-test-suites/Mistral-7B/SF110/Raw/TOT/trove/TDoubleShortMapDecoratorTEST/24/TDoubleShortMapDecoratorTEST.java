import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.TObjectDoubleMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.1)));
 assertEquals((short)2, decorator.get(new Double(2.2)));
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(3.3)));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(1.1)));
 assertNull(decorator.get(new Double(2.2)));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 decorator.remove(new Double(1.1));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.1)));
 assertEquals((short)2, decorator.get(new Double(2.2)));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 assertTrue(decorator.containsKey(new Double(1.1)));
 assertTrue(decorator.containsKey(new Double(2.2)));
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(3.3)));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 List<Entry<Double, Short>> entries = new ArrayList<>();
 entries.add(new AbstractMap.SimpleEntry<>(new Double(1.1), (short)1));
 entries.add(new AbstractMap.SimpleEntry<>(new Double(2.2), (short)2));
 assertEquals(entries, decorator.entrySet());
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
 ObjectOutput objectOutput = new ObjectOutputStream(out);
 ObjectInput objectInput = new ObjectInputStream(in);
 decorator.writeExternal(objectOutput);
 decorator = new TDoubleShortMapDecorator();
 decorator.readExternal(objectInput);
 decorator.put(new Double(1.1), (short)1);
 decorator.put(new Double(2.2), (short)2);
 ObjectOutput output = new ObjectOutputStream(out);
 ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(input);
 assertEquals(decorator.getMap(), readDecorator.getMap());
 assertEquals(decorator.size(), readDecorator.size());
 assertEquals(decorator.get(new Double(1.1)), readDecorator.get(new Double(1.1)));
 assertEquals(decorator.get(new Double(2.2)), readDecorator.get(new Double(2.2)));
 }

 @Test
 public void testCloneable() {
 try {
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();
 decorator.put(new Double(1.1), (short)1);
 cloned.put(new Double(2.2), (short)2);
 assertNotSame(decorator, cloned);
 assertEquals(decorator.getMap(), cloned.getMap());
 assertEquals(decorator.size(), cloned.size());
 assertEquals(decorator.get(new Double(1.1)), cloned.get(new Double(1.1)));
 assertEquals(decorator.get(new Double(2.2)), cloned.get(new Double(2.2)));
 } catch (CloneNotSupportedException e) {
 fail("clone should not throw CloneNotSupportedException.");
 }
 }

 @Test
 public void testNullKey() {
 decorator.put(new Double(1.1), (short)1);
 try {
 decorator.put(null, (short)2);
 fail("Should not be able to put null key.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testNullValue() {
 decorator.put(new Double(1.1), (short)1);
 try {
 decorator.put(new Double(2.2), null);
 fail("Should not be able to put null value.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetWithNullValue() {
 decorator.put(new Double(1.1), (short)1);
 assertEquals((Short)1, decorator.get(new Double(1.1)));
 assertNull(decorator.get(new Double(2.2)));
 }

 @Test
 public void testPutWithNullValue() {
 decorator.put(new Double(1.1), (short)1);
 assertEquals((Short)1, decorator.get(new Double(1.1)));
 decorator.put(new Double(2.2), null);
 assertEquals(null, decorator.get(new Double(2.2)));
 }

 @Test
 public void testPutAllWithNullEntry() {
 TObjectShortMap map = new TObjectShortMap();
 map.put(new Double(1.1), (short)1);
 TObjectDoubleMap decoratorMap = new TObjectDoubleMap();
 decoratorMap.putAll(map);
 TDoubleShortMapDecorator testDecorator = new TDoubleShortMapDecorator();
 testDecorator.putAll(decoratorMap);
 assertNull(testDecorator.get(null));
 }

 @Test
 public void testPutAllWithNullEntryValue() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(new Double(1.1), (short)1);
 TObjectShortMap decoratorMap = new TObjectShortMap();
 decoratorMap.put(new Double(1.1), null);
 TDoubleShortMapDecorator testDecorator = new TDoubleShortMapDecorator();
 testDecorator.putAll(decoratorMap);
 assertNull(testDecorator.get(new Double(1.1)));
 }
}