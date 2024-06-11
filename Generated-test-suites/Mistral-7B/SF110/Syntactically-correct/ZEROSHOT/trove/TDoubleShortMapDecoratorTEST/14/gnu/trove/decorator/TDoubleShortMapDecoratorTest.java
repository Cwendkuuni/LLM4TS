import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.EntryIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 _decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 // test constructor with empty map
 assertNull(_decorator.getMap());
 assertTrue(_decorator.isEmpty());
 assertFalse(_decorator.containsKey(null));
 assertFalse(_decorator.containsKey(Double.valueOf(0.0)));
 assertFalse(_decorator.containsValue((short)0));
 assertEquals(0, _decorator.size());

 // test methods on empty map
 assertNull(_decorator.put(null, (short)0));
 assertNull(_decorator.put(Double.valueOf(0.0), (short)0));
 assertNull(_decorator.get(null));
 assertNull(_decorator.get(Double.valueOf(0.0)));
 assertFalse(_decorator.containsValue((short)1));
 assertFalse(_decorator.containsKey(Double.valueOf(1.0)));
 assertFalse(_decorator.remove(null));
 assertFalse(_decorator.remove(Double.valueOf(0.0)));
 assertSame(_decorator, _decorator.clear());
 }

 @Test
 public void testPutAndGet() {
 // test put and get with valid key and value
 double key = 1.0;
 short value = (short)1;
 _decorator.put(Double.valueOf(key), value);
 assertSame(_decorator, _decorator.put(Double.valueOf(key), value));
 assertSame(value, _decorator.get(Double.valueOf(key)));

 // test get with null key
 assertNull(_decorator.get(null));

 // test get with invalid key
 assertNull(_decorator.get(Double.valueOf(2.0)));

 // test put and get with the same key but different values
 value = (short)2;
 _decorator.put(Double.valueOf(key), value);
 assertSame(value, _decorator.get(Double.valueOf(key)));
 }

 @Test
 public void testClear() {
 // test clear
 _decorator.put(Double.valueOf(1.0), (short)1);
 _decorator.put(Double.valueOf(2.0), (short)2);
 _decorator.clear();
 assertTrue(_decorator.isEmpty());
 assertFalse(_decorator.containsKey(Double.valueOf(1.0)));
 assertFalse(_decorator.containsKey(Double.valueOf(2.0)));
 }

 @Test
 public void testRemove() {
 // test remove with valid key and value
 _decorator.put(Double.valueOf(1.0), (short)1);
 _decorator.put(Double.valueOf(2.0), (short)2);
 assertSame(_decorator.get(Double.valueOf(1.0)), (short)1);
 assertSame(_decorator.get(Double.valueOf(2.0)), (short)2);
 _decorator.remove(Double.valueOf(1.0));
 assertSame(_decorator.get(Double.valueOf(1.0)), null);
 assertSame(_decorator.get(Double.valueOf(2.0)), (short)2);

 // test remove with null key
 assertFalse(_decorator.remove(null));

 // test remove with invalid key
 assertFalse(_decorator.remove(Double.valueOf(3.0)));
 }

 @Test
 public void testEntrySet() {
 // test entry set iteration order
 _decorator.put(Double.valueOf(1.0), (short)1);
 _decorator.put(Double.valueOf(2.0), (short)2);
 _decorator.put(Double.valueOf(3.0), (short)3);
 List<Entry<Double, Short>> entries = new ArrayList<>(_decorator.entrySet());
 List<Double> keys = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
 Collections.sort(keys);
 for (int i = 0; i < entries.size(); i++) {
 Double entryKey = entries.get(i).getKey();
 assertEquals(keys.get(i), entryKey);
 }

 // test entry set size
 _decorator.put(Double.valueOf(4.0), (short)4);
 assertEquals(4, _decorator.size());
 }

 @Test
 public void testContainsValue() {
 // test containsValue with valid value
 _decorator.put(Double.valueOf(1.0), (short)1);
 _decorator.put(Double.valueOf(2.0), (short)1);
 assertTrue(_decorator.containsValue((short)1));

 // test containsValue with invalid value
 assertFalse(_decorator.containsValue((short)2));
 }

 @Test
 public void testContainsKey() {
 // test containsKey with valid key
 _decorator.put(Double.valueOf(1.0), (short)1);
 assertTrue(_decorator.containsKey(Double.valueOf(1.0)));

 // test containsKey with invalid key
 assertFalse(_decorator.containsKey(Double.valueOf(2.0)));
 }

 @Test
 public void testPutAll() {
 // test putAll
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);
 _decorator.putAll(map);
 assertEquals(map.size(), _decorator.size());
 for (Entry<Double, Short> entry : _decorator.entrySet()) {
 assertEquals(entry.getKey(), entry.getValue().getKey());
 assertEquals(entry.getValue().getValue(), entry.getValue());
 }
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 // test serialization and deserialization
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 _decorator.writeExternal(objectOutput);
 ObjectInput objectInput = new ObjectInput(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(objectInput);
 assertSame(_map, deserializedDecorator.getMap());
 assertTrue(deserializedDecorator.isEmpty());
 assertFalse(deserializedDecorator.containsKey(Double.valueOf(0.0)));
 assertFalse(deserializedDecorator.containsValue((short)0));
 assertEquals(0, deserializedDecorator.size());
 }
}