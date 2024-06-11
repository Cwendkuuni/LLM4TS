package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jmock.lib.legacy.AnnotationLookups;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import junit.framework.TestSuite;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest extends MockObjectTestCase {

    private static final JsonDeserializer<Object> NO_OP_DESERIALIZER = new StdSerializer<Object>(Object.class) {};

    private static final ObjectIdReader mockedObjectIdReader = mock(ObjectIdReader.class);

    private ObjectIdValueProperty testSubject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ObjectIdValueProperty.setDefaultUseGenerator(true);
        testSubject = new ObjectIdValueProperty(mockedObjectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
    }

    @Test
    public void testConstructorWithObjectIdReader() {
        // Given

        // When
        ObjectIdValueProperty testSubject = new ObjectIdValueProperty(mockedObjectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);

        // Then
        assertSame(mockedObjectIdReader, testSubject._objectIdReader);
    }

    @Test
    public void testConstructorWithObjectIdValueProperty() {
        // Given
        ObjectIdValueProperty src = testSubject;

        // When
        ObjectIdValueProperty testSubject = new ObjectIdValueProperty(src, null, NO_OP_DESERIALIZER);

        // Then
        assertSame(src, testSubject);
        assertSame(mockedObjectIdReader, testSubject._objectIdReader);
    }

    @Test
    public void testConstructorWithObjectIdValuePropertyAndName() {
        // Given
        ObjectIdValueProperty src = testSubject;

        // When
        ObjectIdValueProperty testSubject = new ObjectIdValueProperty(src, "newName");

        // Then
        assertEquals("newName", testSubject.getName());
        assertSame(src, testSubject);
        assertSame(mockedObjectIdReader, testSubject._objectIdReader);
    }

    @Test
    public void testGetName() {
        // Given

        // When
        String result = testSubject.getName().getText();

        // Then
        assertEquals(mockedObjectIdReader.propertyName.getText(), result);
    }

    @Test
    public void testSetName() {
        // Given
        String newName = "newName";

        // When
        ObjectIdValueProperty testSubject = new ObjectIdValueProperty(testSubject, newName);

        // Then
        assertEquals(newName, testSubject.getName().getText());
    }

    @Test
    public void testWithName() {
        // Given
        String newName = "newName";

        // When
        ObjectIdValueProperty testSubject = testSubject.withName(newName);

        // Then
        assertEquals(newName, testSubject.getName().getText());
    }

    @Test
    public void testGetIdType() {
        // Given

        // When
        IdType result = testSubject.getIdType();

        // Then
        assertEquals(mockedObjectIdReader.getIdType(), result);
    }

    @Test
    public void testGetValueDeserializer() {
        // Given

        // When
        JsonDeserializer<?> result = testSubject.getValueDeserializer();

        // Then
        assertSame(mockedObjectIdReader.getValueDeserializer(), result);
    }

    @Test
    public void testSetValueDeserializer() {
        // Given
        JsonDeserializer<?> newValueDeserializer = mock(JsonDeserializer.class);

        // When
        testSubject.setValueDeserializer(newValueDeserializer);

        // Then
        assertSame(newValueDeserializer, testSubject.getValueDeserializer());
    }

    @Test
    public void testGetNullProvider() {
        // Given

        // When
        NullValueProvider result = testSubject.getNullProvider();

        // Then
        assertNull(result);
    }

    @Test
    public void testSetNullProvider() {
        // Given
        NullValueProvider newNullProvider = mock(NullValueProvider.class);

        // When
        testSubject.setNullProvider(newNullProvider);

        // Then
        assertSame(newNullProvider, testSubject.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        // Given
        JsonId jsonIdAnnotation = new JsonId();

        // When
        Object result = testSubject.getAnnotation(JsonId.class);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        // Given

        // When
        AnnotatedMember result = testSubject.getMember();

        // Then
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // Given
        JsonParser parser = testParserWithObject();
        Object instance = new Object();

        // When
        testSubject.deserializeAndSet(parser, mock(DeserializationContext.class), instance);

        // Then
        assertNotNull(parser.nextToken());
        assertEquals(instance, parser.getCurrentValue());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        JsonParser parser = testParserWithObject();
        Object instance = new Object();
        Object id = new Object();

        // When
        Object result = testSubject.deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);

        // Then
        assertEquals(instance, result);
        assertNotNull(parser.nextToken());
        assertEquals(id, parser.getCurrentValue());
    }

    @Test
    public void testSet() throws IOException {
        // Given
        Object instance = new Object();
        Object id = new Object();

        // When
        testSubject.set(instance, id);

        // Then
        assertSame(instance, testSubject.getBean());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Given
        Object instance = new Object();
        Object id = new Object();

        // When
        Object result = testSubject.setAndReturn(instance, id);

        // Then
        assertEquals(instance, result);
    }

    @Test
    public void testFindObjectId() throws IOException {
        // Given
        Object id = new Object();

        // When
        ReadableObjectId result = mock(ReadableObjectId.class);
        when(mockedObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockedObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(mockedObjectIdReader.findObjectId(eq(id), any(ObjectIdGenerator.class), any(ObjectIdResolver.class))).thenReturn(result);

        // When
        Object result2 = testSubject.deserializeSetAndReturn(testParserWithObject(), mock(DeserializationContext.class), new Object());

        // Then
        verify(mockedObjectIdReader).findObjectId(eq(id), any(ObjectIdGenerator.class), any(ObjectIdResolver.class));
        assertEquals(result, result2);
    }

    @Test
    public void testWithValueDeserializer() {
        // Given
        JsonDeserializer<?> newValueDeserializer = mock(JsonDeserializer.class);

        // When
        ObjectIdValueProperty testSubject = new ObjectIdValueProperty(testSubject, newValueDeserializer, _nullProvider);

        // Then
        assertSame(newValueDeserializer, testSubject.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        // Given
        NullValueProvider newNullProvider = mock(NullValueProvider.class);

        // When
        ObjectIdValueProperty testSubject = new ObjectIdValueProperty(testSubject, null, newNullProvider);

        // Then
        assertSame(newNullProvider, testSubject.getNullProvider());
    }

    @Test
    public void testGetIdProperty() {
        // Given

        // When
        SettableBeanProperty idProperty = testSubject.getIdProperty();

        // Then
        assertNull(idProperty);
    }

    /**
     * Prepare test parser
     *
     * @return test parser
     */
    private JsonParser testParserWithObject() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonParser parser = mapper.readerFor(Object.class);
        parser.readValue(writer.writeValueAsString(new Object()));
        return parser;
    }

    @Override
    protected Test suite() {
        return new TestSuite(ObjectIdValuePropertyTest.class) {
            @Override
            protected void setUp() {
                imposteriser().addImposter(ObjectIdReader.class);
                imposteriser().addImposter(ObjectIdResolver.class);
                imposteriser().addImposter(ObjectIdGenerator.class);
                super.setUp();
            }
        };
    }

    @Override
    protected ClassImposteriser imposteriser() {
        return new ClassImposteriser() {
            @Override
            public Object newInstance(Class type) throws InstantiationException, IllegalAccessException {
                if (type == ObjectIdResolver.class) {
                    return mock(ObjectIdResolver.class);
                } else if (type == ObjectIdGenerator.class) {
                    return mock(ObjectIdGenerator.class);
                } else {
                    return super.newInstance(type);
                }
            }

            @Override
            public Object getMockInstance(Class type) throws InstantiationException, IllegalAccessException {
                if (type == ObjectIdResolver.class) {
                    return new ObjectIdResolver() {
                        @Override
                        public ObjectId idFromString(String id) {
                            return null;
                        }
                    };
                } else if (type == ObjectIdGenerator.class) {
                    return new ObjectIdGenerator() {
                        @Override
                        public ObjectId generateId(Class<?> beanType) {
                            return null;
                        }
                    };
                } else {
                    return super.getMockInstance(type);
                }
            }
        };
    }

    /**
     * Test suite main
     */
    public static void main(String[] args) {
        runTest(new ObjectIdValuePropertyTest(), args);
    }
}