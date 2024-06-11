package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Test default constructor
        Method defaultConstructor = TestBean.class.getDeclaredConstructor();
        AnnotatedWithParams defaultConstructorParam = new AnnotatedWithParams(defaultConstructor);
        collector.setDefaultCreator(defaultConstructorParam);

        // Test string constructor
        Method stringConstructor = TestBean.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams stringConstructorParam = new AnnotatedWithParams(stringConstructor);
        collector.addStringCreator(stringConstructorParam, true);

        // Test int constructor
        Method intConstructor = TestBean.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams intConstructorParam = new AnnotatedWithParams(intConstructor);
        collector.addIntCreator(intConstructorParam, true);

        // Test long constructor
        Method longConstructor = TestBean.class.getDeclaredConstructor(long.class);
        AnnotatedWithParams longConstructorParam = new AnnotatedWithParams(longConstructor);
        collector.addLongCreator(longConstructorParam, true);

        // Test double constructor
        Method doubleConstructor = TestBean.class.getDeclaredConstructor(double.class);
        AnnotatedWithParams doubleConstructorParam = new AnnotatedWithParams(doubleConstructor);
        collector.addDoubleCreator(doubleConstructorParam, true);

        // Test boolean constructor
        Method booleanConstructor = TestBean.class.getDeclaredConstructor(boolean.class);
        AnnotatedWithParams booleanConstructorParam = new AnnotatedWithParams(booleanConstructor);
        collector.addBooleanCreator(booleanConstructorParam, true);

        // Test delegate constructor
        Method delegateConstructor = TestBeanWithDelegate.class.getDeclaredConstructor(TestDelegate.class);
        AnnotatedWithParams delegateConstructorParam = new AnnotatedWithParams(delegateConstructor);
        CreatorProperty[] delegateArgs = new CreatorProperty[] { new CreatorProperty() };
        collector.addDelegatingCreator(delegateConstructorParam, true, delegateArgs);

        // Test property-based constructor
        TestBeanWithProperties testBeanWithProperties = new TestBeanWithProperties();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBeanWithProperties.class);
        BeanDescription beanDescription = mapper.findBeanDescription(javaType);
        Method setFooMethod = TestBeanWithProperties.class.getDeclaredMethod("setFoo", String.class);
        AnnotatedMethod annotatedSetFooMethod = beanDescription.findMethod("setFoo", String.class);
        AnnotatedParameter annotatedParameter = annotatedSetFooMethod.getParameters()[0];
        collector.addPropertyCreator(new AnnotatedWithParams(setFooMethod), true, new CreatorProperty[] { annotatedParameter });

        // Test type identification
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        JavaType type = mapper.getTypeFactory().constructType(TestBean.class);
        assertEquals(TestBean.class.getName(), inst.getValueTypeDesc());
        assertEquals(type.getRawClass(), inst.getValueTypeDesc());
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertNotNull(stdInst.getDelegateType());
        assertEquals(TestDelegate.class.getName(), stdInst.getDelegateType().getRawClass().getName());

        // Test default constructor usage
        TestBean testBean = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory()), TestBean.class);
        assertNotNull(testBean);

        // Test string constructor usage
        String foo = "foo";
        TestBean testBeanWithString = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBean(foo)))), TestBean.class);
        assertEquals(foo, testBeanWithString.getFoo());

        // Test int constructor usage
        int bar = 42;
        TestBean testBeanWithInt = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBean(bar)))), TestBean.class);
        assertEquals(bar, testBeanWithInt.getBar());

        // Test long constructor usage
        long baz = 42L;
        TestBean testBeanWithLong = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBean(baz)))), TestBean.class);
        assertEquals(baz, testBeanWithLong.getBaz());

        // Test double constructor usage
        double qux = 42.0;
        TestBean testBeanWithDouble = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBean(qux)))), TestBean.class);
        assertEquals(qux, testBeanWithDouble.getQux(), 0.001);

        // Test boolean constructor usage
        boolean quux = false;
        TestBean testBeanWithBoolean = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBean(quux)))), TestBean.class);
        assertEquals(quux, testBeanWithBoolean.isQuux());

        // Test delegate constructor usage
        TestDelegate testDelegate = new TestDelegate();
        TestBeanWithDelegate testBeanWithDelegate = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBeanWithDelegate(testDelegate)))), TestBeanWithDelegate.class);
        assertNotNull(testBeanWithDelegate.getDelegate());
        assertEquals(testDelegate, testBeanWithDelegate.getDelegate());

        // Test property-based constructor usage
        TestBeanWithProperties testBeanWithProperties = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBeanWithProperties("foo", "bar")))), TestBeanWithProperties.class);
        assertEquals("foo", testBeanWithProperties.getFoo());
        assertEquals("bar", testBeanWithProperties.getBar());

        // Test incomplete parameter
        TestBean testBeanWithIncompleteParameter = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(
                new ObjectWriter.WriterImpl(mapper.getConfig()).writeValueAsString(new TestBean()))), TestBean.class);
        assertNotNull(testBeanWithIncompleteParameter);
        assertNotNull(collector.getIncompleteParameter());
        assertEquals(TestBean.class.getDeclaredField("incompleteParameter").getName(), collector.getIncompleteParameter().getName());

        // Test vanilla constructors
        TestVanilla testVanilla = mapper.readValue(mapper.getFactory().createParser(new ObjectStreamFactory().wrap(mapper.writeValueAsString(new TestVanilla()))), TestVanilla.class);
        assertNotNull(testVanilla);
    }

    /**
     * Test bean with simple fields
     */
    static class TestBean {
        private String foo;
        private int bar;
        private long baz;
        private double qux;
        private boolean quux;

        @JsonCreator
        public TestBean(String foo) {
            this.foo = foo;
        }

        @JsonCreator
        public TestBean(int bar) {
            this.bar = bar;
        }

        @JsonCreator
        public TestBean(long baz) {
            this.baz = baz;
        }

        @JsonCreator
        public TestBean(double qux) {
            this.qux = qux;
        }

        @JsonCreator
        public TestBean(boolean quux) {
            this.quux = quux;
        }

        @JsonProperty("foo")
        public String getFoo() {
            return foo;
        }

        @JsonProperty("bar")
        public int getBar() {
            return bar;
        }

        @JsonProperty("baz")
        public long getBaz() {
            return baz;
        }

        @JsonProperty("qux")
        public double getQux() {
            return qux;
        }

        @JsonProperty("quux")
        public boolean isQuux() {
            return quux;
        }

        public static class AnnotatedWithParams extends AnnotatedMember {
            public AnnotatedWithParams(Member member) {
                super(member);
            }
        }
    }

    /**
     * Test bean with delegate constructor
     */
    static class TestBeanWithDelegate {
        private TestDelegate delegate;

        @JsonCreator
        public TestBeanWithDelegate(TestDelegate delegate) {
            this.delegate = delegate;
        }

        @JsonProperty
        public TestDelegate getDelegate() {
            return delegate;
        }

        public static class AnnotatedWithParams extends AnnotatedMethod {
            public AnnotatedWithParams(Method method) {
                super(method);
            }
        }

        public static class TestDelegate {}
    }

    /**
     * Test bean with properties constructor
     */
    static class TestBeanWithProperties {
        private String foo;
        private String bar;

        @JsonCreator
        public TestBeanWithProperties(String foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        @JsonProperty("foo")
        public String getFoo() {
            return foo;
        }

        @JsonProperty("bar")
        public String getBar() {
            return bar;
        }

        public static class AnnotatedWithParams extends AnnotatedMethod {
            public AnnotatedWithParams(Method method) {
                super(method);
            }
        }
    }

    /**
     * Test vanilla constructor
     */
    static class TestVanilla {}
}