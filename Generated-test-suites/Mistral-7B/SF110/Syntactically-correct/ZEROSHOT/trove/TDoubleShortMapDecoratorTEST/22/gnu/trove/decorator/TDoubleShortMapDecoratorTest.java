import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TDoubleShortMapImpl;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Collections;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMapImpl();
 _decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void teardown() {
 _map.clear();
 _decorator = null;
 }

 @Test
 public void testConstructors() {
 // Test default constructor
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertSame(decorator.getMap(), null);

 // Test constructor with TDoubleShortMap
 TDoubleShortMap map = new TDoubleShortMapImpl();
 TDoubleShortMapDecorator decoratorWithMap = new TDoubleShortMapDecorator(map);
 assertSame(decoratorWithMap.getMap(), map);
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = (short)1;
 Short putValue = _decorator.put(key, value);
 assertTrue(_map.containsKey(key.doubleValue()));
 assertEquals(value, _map.get(key.doubleValue()));
 assertEquals(value, putValue);

 key = 2.0;
 value = (short)2;
 putValue = _decorator.put(key, value);
 assertTrue(_map.containsKey(key.doubleValue()));
 assertEquals(value, _map.get(key.doubleValue()));
 assertEquals(value, putValue);

 key = 1.0;
 value = (short)3;
 putValue = _decorator.put(key, value);
 assertFalse(_map.containsKey(key.doubleValue()));
 assertNull(putValue);
 }

 @Test
 public void testGet() {
 Double key = 1.0;
 Short value = (short)1;
 _decorator.put(key, value);
 assertEquals(value, _decorator.get(key));

 key = 2.0;
 assertNull(_decorator.get(key));
 }

 @Test
 public void testClear() {
 _decorator.clear();
 assertTrue(_map.isEmpty());
 }

 @Test
 public void testRemove() {
 Double key = 1.0;
 Short value = (short)1;
 _decorator.put(key, value);
 assertTrue(_map.containsKey(key.doubleValue()));
 Short removedValue = _decorator.remove(key);
 assertEquals(value, removedValue);
 assertFalse(_map.containsKey(key.doubleValue()));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 _decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = _decorator.entrySet();
 assertEquals(Collections.emptySet(), entrySet);

 Double key1 = 1.0;
 Short value1 = (short)1;
 _decorator.put(key1, value1);

 Double key2 = 2.0;
 Short value2 = (short)2;
 _decorator.put(key2, value2);

 Set<Entry<Double, Short>> expectedEntrySet = Collections.unmodifiableSet(new HashSet<Entry<Double, Short>>() {{
 add(new SimpleEntry<Double, Short>(key1, value1));
 add(new SimpleEntry<Double, Short>(key2, value2));
 }});

 assertEquals(expectedEntrySet, _decorator.entrySet());
 }

 @Test
 public void testContainsValue() {
 Short value = (short)1;
 assertTrue(_decorator.containsValue(value));

 Short anotherValue = (short)2;
 assertFalse(_decorator.containsValue(anotherValue));
 }

 @Test
 public void testContainsKey() {
 Double key = 1.0;
 assertTrue(_decorator.containsKey(key));

 key = 2.0;
 assertFalse(_decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, _decorator.size());

 Double key1 = 1.0;
 Short value1 = (short)1;
 _decorator.put(key1, value1);

 Double key2 = 2.0;
 Short value2 = (short)2;
 _decorator.put(key2, value2);

 assertEquals(2, _decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(_decorator.isEmpty());

 Double key = 1.0;
 Short value = (short)1;
 _decorator.put(key, value);

 assertFalse(_decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap otherMap = new TDoubleShortMapImpl();
 otherMap.put(1.0, (short)1);
 otherMap.put(2.0, (short)2);

 _decorator.putAll(otherMap);

 Set<Entry<Double, Short>> expectedEntrySet = Collections.unmodifiableSet(new HashSet<Entry<Double, Short>>() {{
 add(new SimpleEntry<Double, Short>(1.0, (short)1));
 add(new SimpleEntry<Double, Short>(2.0, (short)2));
 }});

 assertEquals(expectedEntrySet, _decorator.entrySet());
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, _decorator.wrapKey(1.0));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals(1.0, _decorator.unwrapKey((Double)1.0));
 }

 @Test
 public void testWrapValue() {
 assertEquals((short)1, _decorator.wrapValue((short)1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((short)1, _decorator.unwrapValue((Short)1));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 _decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator decoratorFromStream = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 assertEquals(_map, decoratorFromStream.getMap());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 _decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator decoratorFromStream = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 assertEquals(_map, decoratorFromStream.getMap());
 }
}