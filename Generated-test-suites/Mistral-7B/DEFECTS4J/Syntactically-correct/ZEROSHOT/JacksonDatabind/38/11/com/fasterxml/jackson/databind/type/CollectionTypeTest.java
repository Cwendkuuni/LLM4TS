package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType javaType = CollectionType.construct(ArrayList.class, String.class);
            assertNotNull(javaType);
            assertEquals(ArrayList.class, javaType.getRawClass());
            assertEquals(String.class, javaType.getContentType());
        } catch (DeprecatedException de) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType linkedListType = CollectionType.construct(LinkedList.class, null, null, null, String.class);

        CollectionType narrowedType = (CollectionType) arrayListType.narrow(LinkedList.class);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowUnsupported() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType narrowedType = (CollectionType) arrayListType.narrow(HashMap.class);
    }

    @Test
    public void testWithContentType() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithIntContent = arrayListType.withContentType(Integer.TYPE);
        assertEquals(ArrayList.class, listTypeWithIntContent.getRawClass());
        assertEquals(Integer.TYPE, listTypeWithIntContent.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithHandler = arrayListType.withTypeHandler(new MyTypeHandler());
        assertEquals(ArrayList.class, listTypeWithHandler.getRawClass());
        assertEquals(String.class, listTypeWithHandler.getContentType());
        assertNotNull(listTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithContentHandler = arrayListType.withContentTypeHandler(new MyContentTypeHandler());
        assertEquals(ArrayList.class, listTypeWithContentHandler.getRawClass());
        assertEquals(String.class, listTypeWithContentHandler.getContentType());
        assertNotNull(listTypeWithContentHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithHandler = arrayListType.withValueHandler(new MyValueHandler());
        assertEquals(ArrayList.class, listTypeWithHandler.getRawClass());
        assertEquals(String.class, listTypeWithHandler.getContentType());
        assertNotNull(listTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithContentHandler = arrayListType.withContentValueHandler(new MyContentValueHandler());
        assertEquals(ArrayList.class, listTypeWithContentHandler.getRawClass());
        assertEquals(String.class, listTypeWithContentHandler.getContentType());
        assertNotNull(listTypeWithContentHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType staticListType = arrayListType.withStaticTyping();
        assertEquals(ArrayList.class, staticListType.getRawClass());
        assertEquals(ArrayList.class, staticListType.getRawClass()); // should match
    }

    @Test
    public void testRefine() {
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType linkedListType = CollectionType.refine(ArrayList.class, null, null, null, LinkedList.class);
        assertEquals(LinkedList.class, linkedListType.getRawClass());
        assertEquals(String.class, linkedListType.getContentType());
    }
}

class MyTypeHandler {}
class MyContentTypeHandler {}
class MyValueHandler {}
class MyContentValueHandler {}