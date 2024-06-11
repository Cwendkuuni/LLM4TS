package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock = mock(String.class);
        Answer<String> answer = Mockito.answer(returnsEmptyValues);

        String actual = answer.answer(Mockito.methodCall(mock, "toString"));

        assertEquals("Mock for String, hashCode: " + mock.hashCode(), actual);
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        Answer<Integer> answer = Mockito.answer(returnsEmptyValues);

        int actual = answer.answer(Mockito.methodCall(mock1, "compareTo", mock2));

        assertEquals(1, actual);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Integer mockInteger = mock(Integer.class);
        Float mockFloat = mock(Float.class);
        Character mockCharacter = mock(Character.class);
        Byte mockByte = mock(Byte.class);
        Short mockShort = mock(Short.class);
        Long mockLong = mock(Long.class);
        Boolean mockBoolean = mock(Boolean.class);

        Answer<Integer> answerInteger = Mockito.answer(returnsEmptyValues);
        Answer<Float> answerFloat = Mockito.answer(returnsEmptyValues);
        Answer<Character> answerCharacter = Mockito.answer(returnsEmptyValues);
        Answer<Byte> answerByte = Mockito.answer(returnsEmptyValues);
        Answer<Short> answerShort = Mockito.answer(returnsEmptyValues);
        Answer<Long> answerLong = Mockito.answer(returnsEmptyValues);
        Answer<Boolean> answerBoolean = Mockito.answer(returnsEmptyValues);

        assertEquals(0, answerInteger.answer(Mockito.methodCall(mockInteger, "intValue")).intValue());
        assertEquals(0f, answerFloat.answer(Mockito.methodCall(mockFloat, "floatValue")).floatValue(), 0.01f);
        assertEquals((char) 0, answerCharacter.answer(Mockito.methodCall(mockCharacter, "charValue")).charValue());
        assertEquals((byte) 0, answerByte.answer(Mockito.methodCall(mockByte, "byteValue")).byteValue());
        assertEquals((short) 0, answerShort.answer(Mockito.methodCall(mockShort, "shortValue")).shortValue());
        assertEquals(0L, answerLong.answer(Mockito.methodCall(mockLong, "longValue")).longValue());
        assertEquals(false, answerBoolean.answer(Mockito.methodCall(mockBoolean, "booleanValue")).booleanValue());
    }

    @Test
    public void testEmptyCollections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Class<?> collectionType = Collection.class;
        Class<?> setType = Set.class;
        Class<?> hashSetType = HashSet.class;
        Class<?> linkedHashSetType = LinkedHashSet.class;
        Class<?> listType = List.class;
        Class<?> linkedListType = LinkedList.class;
        Class<?> ArrayListType = ArrayList.class;
        Class<?> mapType = Map.class;
        Class<?> hashMapType = HashMap.class;
        Class<?> linkedHashMapType = LinkedHashMap.class;
        Class<?> sortedMapType = SortedMap.class;
        Class<?> treeMapType = TreeMap.class;

        Answer<Collection<?>> answerCollection = Mockito.answer(returnsEmptyValues);
        Answer<Set<?>> answerSet = Mockito.answer(returnsEmptyValues);
        Answer<HashSet<?>> answerHashSet = Mockito.answer(returnsEmptyValues);
        Answer<LinkedHashSet<?>> answerLinkedHashSet = Mockito.answer(returnsEmptyValues);
        Answer<List<?>> answerList = Mockito.answer(returnsEmptyValues);
        Answer<LinkedList<?>> answerLinkedList = Mockito.answer(returnsEmptyValues);
        Answer<ArrayList<?>> answerArrayList = Mockito.answer(returnsEmptyValues);
        Answer<Map<?, ?>> answerMap = Mockito.answer(returnsEmptyValues);
        Answer<HashMap<?, ?>> answerHashMap = Mockito.answer(returnsEmptyValues);
        Answer<LinkedHashMap<?, ?>> answerLinkedHashMap = Mockito.answer(returnsEmptyValues);
        Answer<SortedMap<?, ?>> answerSortedMap = Mockito.answer(returnsEmptyValues);
        Answer<TreeMap<?, ?>> answerTreeMap = Mockito.answer(returnsEmptyValues);

        Collection<?> actualCollection = answerCollection.answer(Mockito.methodCall(mock(collectionType), ""));
        Set<?> actualSet = answerSet.answer(Mockito.methodCall(mock(setType), ""));
        HashSet<?> actualHashSet = answerHashSet.answer(Mockito.methodCall(mock(hashSetType), ""));
        LinkedHashSet<?> actualLinkedHashSet = answerLinkedHashSet.answer(Mockito.methodCall(mock(linkedHashSetType), ""));
        List<?> actualList = answerList.answer(Mockito.methodCall(mock(listType), ""));
        LinkedList<?> actualLinkedList = answerLinkedList.answer(Mockito.methodcall(mock(linkedListType), ""));
        ArrayList<?> actualArrayList = answerArrayList.answer(Mockito.methodcall(mock(ArrayListType), ""));
        Map<?, ?> actualMap = answerMap.answer(Mockito.methodcall(mock(mapType), ""));
        HashMap<?, ?> actualHashMap = answerHashMap.answer(Mockito.methodcall(mock(hashMapType), ""));
        LinkedHashMap<?, ?> actualLinkedHashMap = answerLinkedHashMap.answer(Mockito.methodcall(mock(linkedHashMapType), ""));
        SortedMap<?, ?> actualSortedMap = answerSortedMap.answer(Mockito.methodcall(mock(sortedMapType), ""));
        TreeMap<?, ?> actualTreeMap = answerTreeMap.answer(Mockito.methodcall(mock(treeMapType), ""));

        assertNotNull(actualCollection);
        assertNotNull(actualSet);
        assertNotNull(actualHashSet);
        assertNotNull(actualLinkedHashSet);
        assertNotNull(actualList);
        assertNotNull(actualLinkedList);
        assertNotNull(actualArrayList);
        assertNotNull(actualMap);
        assertNotNull(actualHashMap);
        assertNotNull(actualLinkedHashMap);
        assertNotNull(actualSortedMap);
        assertNotNull(actualTreeMap);

        assertEquals(0, actualCollection.size());
        assertEquals(0, actualSet.size());
        assertEquals(0, actualHashSet.size());
        assertEquals(0, actualLinkedHashSet.size());
        assertEquals(0, actualList.size());
        assertEquals(0, actualLinkedList.size());
        assertEquals(0, actualArrayList.size());
        assertEquals(0, actualMap.size());
        assertEquals(0, actualHashMap.size());
        assertEquals(0, actualLinkedHashMap.size());
        assertEquals(0, actualSortedMap.size());
        assertEquals(0, actualTreeMap.size());
    }

    @Test
    public void testNullValue() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock = mock(Object.class);
        Answer<Object> answer = Mockito.answer(returnsEmptyValues);

        assertNull(answer.answer(Mockito.methodcall(mock, "")));
    }
}