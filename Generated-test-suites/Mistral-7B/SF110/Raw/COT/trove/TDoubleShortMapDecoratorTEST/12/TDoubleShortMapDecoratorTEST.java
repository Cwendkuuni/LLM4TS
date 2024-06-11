import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
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
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
 assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
 double key = _map.getNoEntryKey();
 Short value = Short.valueOf((short) 1);
 Short result = decorator.put(key, value);
 assertNull(result);
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testPutNullValue() {
 Double key = 1.0;
 Short value = null;
 Short result = decorator.put(key, value);
 assertNull(result);
 assertEquals((Short) _map.getNoEntryValue(), decorator.get(key));
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = Short.valueOf((short) 1);
 Short result = decorator.put(key, value);
 assertNull(result);
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testGetNullKey() {
 Double key = null;
 assertNull(decorator.get(key));
 }

 @Test
 public void testGetWrongTypeKey() {
 Integer key = 1;
 assertNull(decorator.get(key));
 }

 @Test
 public void testGet() {
 Double key = 1.0;
 Short value = Short.valueOf((short) 1);
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
 Double key = null;
 Short result = decorator.remove(key);
 assertNull(result);
 }

 @Test
 public void testRemoveWrongTypeKey() {
 Integer key = 1;
 Short result = decorator.remove(key);
 assertNull(result);
 }

 @Test
 public void testRemove() {
 Double key = 1.0;
 Short value = Short.valueOf((short) 1);
 decorator.put(key, value);
 Short result = decorator.remove(key);
 assertNotNull(result);
 assertEquals(value, result);
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleMap<Double, Short> map = new TDoubleHashMap<Double, Short>() {{
 put(1.0, (short) 1);
 put(2.0, (short) 2);
 put(3.0, (short) 3);
 }};
 decorator.putAll(map);
 assertEquals(map.size(), decorator.size());
 TShortMap<Short, Double> inverseMap = decorator.entrySet().stream().collect(Collectors.toMap(Entry::getValue, Map.Entry::getKey, (e1, e2) -> e1));
 assertEquals(map, inverseMap);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 decorator.entrySet().add(new AbstractMap.SimpleEntry<>(1.0, (short) 1));
 }

 @Test
 public void testRemoveEntrySet() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 decorator.entrySet().remove(new AbstractMap.SimpleEntry<>(1.0, (short) 1));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testEntrySetIterator() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 TDoubleShortIterator it = _map.iterator();
 while (it.hasNext()) {
 Entry<Double, Short> entry = decorator.entrySet().iterator().next();
 assertEquals(it.getKey(), entry.getKey());
 assertEquals(it.value(), entry.getValue());
 it.advance();
 }
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(outputStream);
 decorator.writeExternal(out);
 out.close();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput in = new ObjectInputStream(inputStream);
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(in);
 assertEquals(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(outputStream);
 decorator.writeExternal(out);
 out.close();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput in = new ObjectInputStream(inputStream);
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(in);
 assertEquals(_map, deserializedDecorator.getMap());
 }

}