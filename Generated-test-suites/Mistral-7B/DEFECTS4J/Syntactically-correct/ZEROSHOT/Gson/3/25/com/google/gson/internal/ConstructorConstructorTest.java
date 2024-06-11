import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

    @Test
    public void testGetInstance() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ConstructorConstructor.ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class, String.class, String.class));
        ConstructorConstructor.ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(TypeToken.get(ArrayList.class, String.class));
        ConstructorConstructor.ObjectConstructor<LinkedHashMap<String, String>> linkedHashMapConstructor = constructorConstructor.get(TypeToken.get(LinkedHashMap.class, String.class, String.class));

        Map<String, String> map = mapConstructor.construct();
        Assert.assertNotNull(map);
        assertTrue(map instanceof TreeMap);

        ArrayList<String> list = listConstructor.construct();
        Assert.assertNotNull(list);
        assertTrue(list instanceof LinkedList);

        LinkedHashMap<String, String> linkedHashMap = linkedHashMapConstructor.construct();
        Assert.assertNotNull(linkedHashMap);
        assertTrue(linkedHashMap instanceof LinkedTreeMap);

        // Test null cases
        try {
            constructorConstructor.get(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            constructorConstructor.get(TypeToken.get(null));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCollectionDefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ConstructorConstructor.ObjectConstructor<SortedSet<String>> sortedSetConstructor = constructorConstructor.get(TypeToken.get(SortedSet.class, String.class));
        ConstructorConstructor.ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(TypeToken.get(EnumSet.class, String.class));
        ConstructorConstructor.ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(TypeToken.get(Set.class, String.class));
        ConstructorConstructor.ObjectConstructor<Queue<String>> queueConstructor = constructorConstructor.get(TypeToken.get(Queue.class, String.class));

        SortedSet<String> sortedSet = sortedSetConstructor.construct();
        Assert.assertNotNull(sortedSet);
        assertTrue(sortedSet instanceof TreeSet);

        EnumSet<String> enumSet = enumSetConstructor.construct();
        Assert.assertNotNull(enumSet);
        assertTrue(enumSet instanceof EnumSet.None);

        Set<String> set = setConstructor.construct();
        Assert.assertNotNull(set);
        assertTrue(set instanceof LinkedHashSet);

        Queue<String> queue = queueConstructor.construct();
        Assert.assertNotNull(queue);
        assertTrue(queue instanceof LinkedList);
    }

    @Test
    public void testMapDefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ConstructorConstructor.ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class, String.class, Object.class));
        ConstructorConstructor.ObjectConstructor<SortedMap<String, String>> sortedMapConstructor = constructorConstructor.get(TypeToken.get(SortedMap.class, String.class, String.class));

        Map<String, Object> map = mapConstructor.construct();
        Assert.assertNotNull(map);
        assertTrue(map instanceof LinkedTreeMap);

        SortedMap<String, String> sortedMap = sortedMapConstructor.construct();
        Assert.assertNotNull(sortedMap);
        assertTrue(sortedMap instanceof TreeMap);

        // Test null cases
        try {
            constructorConstructor.get(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            constructorConstructor.get(TypeToken.get(null));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }
}