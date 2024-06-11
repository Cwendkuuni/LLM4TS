package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseMapTest;

public class InnerClassPropertyTest extends BaseMapTest {

    private static class TestInnerClass {
        public static final String STATIC_FIELD = "staticField";
        public static int staticProperty = 1;

        private int innerProperty;

        public TestInnerClass(int innerProperty) {
            this.innerProperty = innerProperty;
        }

        public int getInnerProperty() {
            return innerProperty;
        }
    }

    private static class TestInnerClassDeserializer extends StdSerializer<TestInnerClass> {
        public TestInnerClassDeserializer() {
            this(null);
        }

        public TestInnerClassDeserializer(Class<TestInnerClass> tc) {
            this(null);
        }

        public TestInnerClassDeserializer(Class<TestInnerClass> tc, JsonDeserializer.None emptyValueIntruder) {
            super(tc, emptyValueIntruder);
        }

        @Override
        public TestInnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectNode node = jp.getCodec().readTree(jp);
            int innerProperty = node.getInt("innerProperty");
            return new TestInnerClass(innerProperty);
        }
    }

    private static class TestProperty extends InnerClassProperty {
        public TestProperty(BeansPropertyNode node, Constructor<TestInnerClass> constructor) {
            super(node, constructor);
        }
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpOnce() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(TestInnerClass.class, TestInnerClassDeserializer.class));
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(FindNulls.NON_EMPTY_LIST);
        jacksonObjectMapper = mapper;
    }

    @Before
    public void setUp() throws Exception {
        jsonParser = jacksonObjectMapper.getFactory().createParser(new StringReader("{\"innerProperty\": 42}"));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectNode node = mapper.readValue(new StringReader("{\"innerProperty\": 42}"), ObjectNode.class);
        TestInnerClass bean = new TestInnerClass(0);
        TestProperty property = new TestProperty(new BeanPropertyNode(new AnnotatedClass(TestInnerClass.class), "innerProperty", null, null, null),
                TestInnerClass.class.getDeclaredConstructor(int.class));

        property.deserializeAndSet(jsonParser, context, bean);

        assertEquals(42, bean.getInnerProperty());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        TestInnerClass bean = new TestInnerClass(0);
        TestProperty property = new TestProperty(new BeanPropertyNode(new AnnotatedClass(TestInnerClass.class), "innerProperty", null, null, null),
                TestInnerClass.class.getDeclaredConstructor(int.class));

        TestInnerClass returnedValue = property.deserializeSetAndReturn(jsonParser, context, bean);

        assertEquals(42, returnedValue.getInnerProperty());
        assertEquals(bean, returnedValue);
    }

    @Test
    public void testSet() throws Exception {
        TestInnerClass bean = new TestInnerClass(0);
        TestProperty property = new TestProperty(new BeanPropertyNode(new AnnotatedClass(TestInnerClass.class), "innerProperty", null, null, null),
                TestInnerClass.class.getDeclaredConstructor(int.class));

        property.set(bean, 42);

        assertEquals(42, bean.getInnerProperty());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        TestInnerClass bean = new TestInnerClass(0);
        TestProperty property = new TestProperty(new BeanPropertyNode(new AnnotatedClass(TestInnerClass.class), "innerProperty", null, null, null),
                TestInnerClass.class.getDeclaredConstructor(int.class));

        TestInnerClass returnedValue = property.setAndReturn(bean, 42);

        assertEquals(42, returnedValue.getInnerProperty());
        assertEquals(bean, returnedValue);
    }

    @Test
    public void testReadResolve() throws Exception {
        TestProperty property = new TestProperty(new BeanPropertyNode(new AnnotatedClass(TestInnerClass.class), "innerProperty", null, null, null),
                TestInnerClass.class.getDeclaredConstructor(int.class));

        Object resolved = property.readResolve();

        assertNotNull(resolved);
        assertEquals(TestProperty.class, resolved.getClass());
        TestProperty casted = (TestProperty) resolved;
        assertEquals(TestInnerClass.class, casted._creator.getDeclaringClass());
    }

    @Test
    public void testWriteReplace() throws Exception {
        TestProperty property = new TestProperty(new BeanPropertyNode(new AnnotatedClass(TestInnerClass.class), "innerProperty", null, null, null),
                TestInnerClass.class.getDeclaredConstructor(int.class));

        Object replaced = property.writeReplace();

        assertNotNull(replaced);
        assertEquals(TestProperty.class, replaced.getClass());
        TestProperty casted = (TestProperty) replaced;
        assertEquals(AnnotatedConstructor.class, casted._annotated.getClass());
        AnnotatedConstructor annotatedConstructor = casted._annotated;
        assertEquals(TestInnerClass.class, annotatedConstructor.getParameterType(0).getRawType());
        assertEquals(Modifier.PUBLIC, annotatedConstructor.getModifiers());
        assertEquals(Modifier.PRIVATE, annotatedConstructor.getDeclaringClass().getModifiers());
        assertEquals(0, annotatedConstructor.getParameters().size());
        assertEquals(TestInnerClassDeserializer.class, casted._valueDeserializer.getClass());
    }
}