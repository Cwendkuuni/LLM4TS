import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorConstructorTest {

 @InjectMocks
 private ConstructorConstructor constructorConstructor;

 @Test
 public void testGet_TypeToken_ReturnsInstanceConstructor() {
     Type typeToken = new TypeToken<ArrayList<String>>(){}.getType();
     ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(TypeToken.valueOf(typeToken));
     assertNotNull(constructor);
 }

 @Test
 public void testGet_TypeToken_ReturnsInstanceCreatorWhenAvailable() {
     Type typeToken = new TypeToken<LinkedHashSet<Integer>>(){}.getType();
     InstanceCreator<LinkedHashSet<Integer>> instanceCreator = Mockito.mock(InstanceCreator.class);
     Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
     instanceCreators.put(typeToken, instanceCreator);

     ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
     ObjectConstructor<LinkedHashSet<Integer>> constructor = constructorConstructor.get(TypeToken.valueOf(typeToken));
     assertNotNull(constructor);
     Mockito.verify(instanceCreator).createInstance(typeToken);
 }

 @Test
 public void testGet_TypeToken_ReturnsDefaultConstructor() {
     Class<ArrayList<String>> rawType = ArrayList.class;
     ObjectConstructor<ArrayList<String>> defaultConstructor = constructorConstructor.newDefaultConstructor(rawType);
     assertNotNull(defaultConstructor);
 }

 @Test
 public void testGet_TypeToken_ReturnsDefaultImplementationConstructor() {
     Type type = new TypeToken<SortedMap<String, Object>>(){}.getType();
     Class<SortedMap<String, Object>> rawType = SortedMap.class;
     ObjectConstructor<SortedMap<String, Object>> defaultImplementationConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
     assertNotNull(defaultImplementationConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_ThrowsRuntimeExceptionForUnsupportedType() {
     Type typeToken = new TypeToken<Object>(){}.getType();
     constructorConstructor.get(TypeToken.valueOf(typeToken));
 }
}