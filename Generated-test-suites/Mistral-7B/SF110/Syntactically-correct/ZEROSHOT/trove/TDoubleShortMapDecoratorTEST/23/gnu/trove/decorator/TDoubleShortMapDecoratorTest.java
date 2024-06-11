import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.io.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

private TDoubleShortMap _map;
private TDoubleShortMapDecorator _decorator;

@Before
public void setUp() {
_map = new TDoubleShortHashMap();
_decorator = new TDoubleShortMapDecorator(_map);
}

@After
public void tearDown() {
_map.clear();
_decorator = null;
}

@Test
public void testEmptyMap() {
assertEquals(0, _decorator.size());
assertFalse(_decorator.isEmpty());
assertNull(_decorator.get(null));
assertNull(_decorator.get(new Double(0.0)));
assertFalse(_decorator.containsKey(null));
assertFalse(_decorator.containsKey(new Double(0.0)));
assertFalse(_decorator.containsValue((short)0));
}

@Test
public void testPut() {
_decorator.put(new Double(1.0), (short)1);
assertEquals(1, _decorator.size());
assertNull(_decorator.get(null));
assertEquals((short)1, _decorator.get(new Double(1.0)));
assertTrue(_decorator.containsKey(new Double(1.0)));
assertFalse(_decorator.containsValue((short)2));

_decorator.put(new Double(2.0), (short)2);
assertEquals(2, _decorator.size());
assertEquals((short)1, _decorator.get(new Double(1.0)));
assertEquals((short)2, _decorator.get(new Double(2.0)));
assertTrue(_decorator.containsKey(new Double(1.0)));
assertTrue(_decorator.containsKey(new Double(2.0)));
assertTrue(_decorator.containsValue((short)1));
assertTrue(_decorator.containsValue((short)2));
}

@Test
public void testClear() {
_decorator.clear();
assertEquals(0, _decorator.size());
assertFalse(_decorator.isEmpty());
assertNull(_decorator.get(new Double(1.0)));
assertNull(_decorator.get(new Double(2.0)));
assertFalse(_decorator.containsKey(new Double(1.0)));
assertFalse(_decorator.containsKey(new Double(2.0)));
assertFalse(_decorator.containsValue((short)1));
assertFalse(_decorator.containsValue((short)2));
}

@Test
public void testEntrySet() {
Set<Entry<Double, Short>> entrySet = _decorator.entrySet();
assertEquals(2, entrySet.size());
assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(new Double(1.0), (short)1)));
assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(new Double(2.0), (short)2)));
}

@Test
public void testContainsValue() {
assertTrue(_decorator.containsValue((short)1));
assertTrue(_decorator.containsValue((short)2));
assertFalse(_decorator.containsValue((short)3));
}

@Test
public void testPutAll() {
TDoubleShortMap anotherMap = new TDoubleShortHashMap();
anotherMap.put(new Double(3.0), (short)3);
anotherMap.put(new Double(4.0), (short)4);
_decorator.putAll(anotherMap);
assertEquals(4, _decorator.size());
assertNull(_decorator.get(null));
assertEquals((short)1, _decorator.get(new Double(1.0)));
assertEquals((short)2, _decorator.get(new Double(2.0)));
assertEquals((short)3, _decorator.get(new Double(3.0)));
assertEquals((short)4, _decorator.get(new Double(4.0)));
assertTrue(_decorator.containsKey(new Double(1.0)));
assertTrue(_decorator.containsKey(new Double(2.0)));
assertTrue(_decorator.containsKey(new Double(3.0)));
assertTrue(_decorator.containsKey(new Double(4.0)));
assertTrue(_decorator.containsValue((short)1));
assertTrue(_decorator.containsValue((short)2));
assertTrue(_decorator.containsValue((short)3));
assertTrue(_decorator.containsValue((short)4));
}

@Test
public void testExternalizable() throws IOException, ClassNotFoundException {
ByteArrayOutputStream baos = new ByteArrayOutputStream();
ObjectOutput out = new ObjectOutputStream(baos);
_decorator.writeExternal(out);
ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
ObjectInput in = new ObjectInputStream(bais);
TDoubleShortMapDecorator decorator = (TDoubleShortMapDecorator)in.readObject();
assertEquals(4, decorator.size());
assertNull(decorator.get(null));
assertEquals((short)1, decorator.get(new Double(1.0)));
assertEquals((short)2, decorator.get(new Double(2.0)));
assertEquals((short)3, decorator.get(new Double(3.0)));
assertEquals((short)4, decorator.get(new Double(4.0)));
assertTrue(decorator.containsKey(new Double(1.0)));
assertTrue(decorator.containsKey(new Double(2.0)));
assertTrue(decorator.containsKey(new Double(3.0)));
assertTrue(decorator.containsKey(new Double(4.0)));
assertTrue(decorator.containsValue((short)1));
assertTrue(decorator.containsValue((short)2));
assertTrue(decorator.containsValue((short)3));
assertTrue(decorator.containsValue((short)4));
}
}