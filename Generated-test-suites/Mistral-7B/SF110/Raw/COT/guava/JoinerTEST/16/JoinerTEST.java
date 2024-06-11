package com.example.junit;

import static org.junit.Assert.*;

import java.util.*;

import com.google.common.base.*;

@SuppressWarnings("unchecked")
public class JoinerTest {

    @SuppressWarnings("rawtypes")
    private static <T> Iterator<T> createIterator(T[] array) {
        return new Iterator() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < array.length;
            }

            @Override
            public T next() {
                return array[currentIndex++];
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private static <T> Iterator<T> createIterator(Collection<T> collection) {
        return collection.iterator();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(",").append("banana").append(",").append("orange");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(new String[]{"apple", "banana", "orange"}));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('/');
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append('/').append("banana").append('/').append("orange");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(new String[]{"apple", "banana", "orange"}));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(new String[]{"apple", "banana", "orange"}));
        StringBuilder expected = new StringBuilder();
        expected.append("apple,banana,orange");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = createIterator(new String[]{"apple", "banana", "orange"});
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, iterator);
        StringBuilder expected = new StringBuilder();
        expected.append("apple,banana,orange");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object[] objectArray = new Object[]{"apple", "banana", "orange"};
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, objectArray);
        StringBuilder expected = new StringBuilder();
        expected.append("apple,banana,orange");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithNullFirstNullSecondAndObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(new Object[]{null, null, "orange", "apple", null}));
        StringBuilder expected = new StringBuilder();
        expected.appendNull().append(",").appendNull().append(",").append("orange,").append("apple,").appendNull();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithIOException() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, Arrays.asList(new String[]{"apple", "banana", "orange"}));
        fail("IOException expected but not thrown");
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("apple,banana,orange");
        assertEquals(expected.toString(), joiner.join(Arrays.asList(new String[]{"apple", "banana", "orange"})));
    }

    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = createIterator(new String[]{"apple", "banana", "orange"});
        StringBuilder expected = new StringBuilder();
        expected.append("apple,banana,orange");
        assertEquals(expected.toString(), joiner.join(iterator));
    }

    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] objectArray = new Object[]{"apple", "banana", "orange"};
        StringBuilder expected = new StringBuilder();
        expected.append("apple,banana,orange");
        assertEquals(expected.toString(), joiner.join(objectArray));
    }

    @Test
    public void testJoinWithNullFirstNullSecondAndObjects() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerNullText = joiner.useForNull("NULL");
        String[] objectArray = new String[]{"null", null, "orange", null, "apple", null};
        StringBuilder expected = new StringBuilder();
        expected.append("NULL,NULL,orange,NULL,apple,NULL");
        assertEquals(expected.toString(), joinerNullText.join(objectArray));
    }

    @Test
    public void testJoinWithMap() {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");
        StringBuilder expected = new StringBuilder();
        expected.append("key1=>value1,key2=>value2,key3=>value3");
        assertEquals(expected.toString(), mapJoiner.join(map.entrySet()));
    }

    @Test
    public void testJoinWithMapEntryIterator() {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");
        Iterator<Map.Entry<String, String>> mapEntryIterator = map.entrySet().iterator();
        StringBuilder expected = new StringBuilder();
        expected.append("key1=>value1,key2=>value2,key3=>value3");
        assertEquals(expected.toString(), mapJoiner.join(mapEntryIterator));
    }

    @Test
    public void testJoinWithMapEntryIterable() {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");
        List<Map.Entry<String, String>> mapEntryList = new ArrayList<>(map.entrySet());
        StringBuilder expected = new StringBuilder();
        expected.append("key1=>value1,key2=>value2,key3=>value3");
        assertEquals(expected.toString(), mapJoiner.join(mapEntryList));
    }

    @Test
    public void testJoinWithNullMap() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerNullText = joiner.useForNull("NULL");
        Map<String, String> nullMap = null;
        MapJoiner mapJoiner = joinerNullText.withKeyValueSeparator("=>");
        StringBuilder expected = new StringBuilder();
        expected.appendNull();
        assertEquals(expected.toString(), mapJoiner.join(nullMap));
    }
}