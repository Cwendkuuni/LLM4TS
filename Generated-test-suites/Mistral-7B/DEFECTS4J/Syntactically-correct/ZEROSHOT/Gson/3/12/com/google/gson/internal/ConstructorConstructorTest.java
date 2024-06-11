import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertNotNull;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RunWith(Parameterized.class)
public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;
 private TypeToken typeToken;

 public ConstructorConstructorTest(TypeToken typeToken) {
 this.typeToken = typeToken;
 }

 @Parameterized.Parameters(name = "{index}: testing constructor for type {0}")
 public static Iterable<Object> data() {
 return Arrays.asList(new Object[] {
 new TypeToken<ArrayList<Integer>>(){},
 new TypeToken<LinkedList<String>>(){},
 new TypeToken<TreeMap<String, Integer>>(){},
 new TypeToken<EnumSet<Weekday>>(){},
 new TypeToken<SortedSet<Integer>>(){},
 new TypeToken<Queue<Character>>(){},
 new TypeToken<Map<String, Map<String, String>>>(){},
 new TypeToken<Collection<Collection<String>>>(){},
 new TypeToken<SortedMap<Integer, Integer>>(){}
 });
 }

 @Test
 public void testGetConstructor() {
 constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
 ObjectConstructor<Object> constructor = constructorConstructor.get(typeToken);
 assertNotNull(constructor);
 }
}