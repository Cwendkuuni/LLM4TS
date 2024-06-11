package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObject;
import org.jmock.lib.legacy.MockObjectTestCase;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest extends MockObjectTestCase {

    private static final String TEST_NAME = "testName";
    private static final Class<String> TEST_ID_TYPE = String.class;
    private static final JsonDeserializer<String> TEST_VALUE_DESERIALIZER = new StdSerializer<String>(String.class) {};
    private static final ObjectIdReader TEST_OBJECT_ID_READER = mock(ObjectIdReader.class);
    private static final AnnotatedField TEST_ANNOTATED_FIELD = mock(AnnotatedField.class);
    private static final AnnotatedMember TEST_ANNOTATED_MEMBER = mock(AnnotatedMember.class);

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() throws Exception {
        ObjectIdValueProperty.setDefaultUseGenerator(true);
        ObjectIdValueProperty.setDefaultUseResolver(true);

        ObjectIdValueProperty constructorArg1 = new ObjectIdValueProperty(TEST_OBJECT_ID_READER, createPropertyMetadata());
        this.objectIdValueProperty = new ObjectIdValueProperty(constructorArg1, TEST_VALUE_DESERIALIZER);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String testValue = "testValue";
        ObjectIdReader generator = mock(ObjectIdReader.class);
        ObjectIdReader resolver = mock(ObjectIdReader.class);

        when(TEST_OBJECT_ID_READER.getGenerator()).thenReturn(generator);
        when(TEST_OBJECT_ID_READER.getResolver()).thenReturn(resolver);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(generator.nextObjectId()).thenReturn(new ObjectIdReader.ObjectId("testId"));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertEquals("testId", instance.getClass().getDeclaredField(TEST_NAME).get(instance));
        verify(deserializationContext).findObjectId(eq("testId"), eq(generator), eq(resolver));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String testValue = "testValue";
        ObjectIdReader generator = mock(ObjectIdReader.class);
        ObjectIdReader resolver = mock(ObjectIdReader.class);

        when(TEST_OBJECT_ID_READER.getGenerator()).thenReturn(generator);
        when(TEST_OBJECT_ID_READER.getResolver()).thenReturn(resolver);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(generator.nextObjectId()).thenReturn(new ObjectIdReader.ObjectId("testId"));

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals("testId", result);
        verify(deserializationContext).findObjectId(eq("testId"), eq(generator), eq(resolver));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        String testValue = "testValue";
        Object instance = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, testValue);

        assertEquals(testValue, result);
    }

    @Test
    public void testSet() throws Exception {
        String testValue = "testValue";
        Object instance = new Object();

        objectIdValueProperty.set(instance, testValue);

        assertEquals(testValue, instance.getClass().getDeclaredField(TEST_NAME).get(instance));
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty result = objectIdValueProperty.withName(newName);

        assertEquals(newName.getShortName(), result.getName().shortName());
        assertEquals(newName.getName(), result.getName().getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> deserializer = new StdSerializer<String>(String.class) {};

        ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(deserializer);

        assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCallSetOnObjectIdPropertyWithNoIdProperty() throws Exception {
        Object instance = new Object();

        objectIdValueProperty.set(instance, "testValue");
    }

    private PropertyMetadata createPropertyMetadata() {
        PropertyMetadata propertyMetadata = new PropertyMetadata() {
            @Override
            public String getName() {
                return TEST_NAME;
            }

            @Override
            public JsonDeserializer<?> getDeserializer() {
                return TEST_VALUE_DESERIALIZER;
            }

            @Override
            public AnnotatedMember getMember() {
                return TEST_ANNOTATED_MEMBER;
            }

            @Override
            public AnnotatedMember getAnnotatedMember() {
                return TEST_ANNOTATED_MEMBER;
            }

            @Override
            public JsonSerializer<?> getSerializer() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return TEST_NAME;
            }

            @Override
            public String getSimpleName() {
                return TEST_NAME;
            }

            @Override
            public String getFullName() {
                return TEST_NAME;
            }

            @Override
            public SettableBeanProperty getSettableBeanProperty() {
                return null;
            }
        };
        propertyMetadata.setAnnotations(new JsonId());
        return propertyMetadata;
    }

    @BeforeClass
    public static void setupClass() {
        ClassImposteriser.install(getClass());
    }
}