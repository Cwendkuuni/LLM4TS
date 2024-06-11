package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.CreatorProperty;
import com.fasterxml.jackson.databind.introspect.InjectableValue;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class CreatorCollectorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public void setup() {
        // Set up an object mapper for use in tests
    }

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithDefaultConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.setDefaultCreator(ClassIntrospector.findConstructor(MyClassWithDefaultConstructor.class));

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), null), new MyClassWithDefaultConstructor());
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithStringConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithStringConstructor.class, "str");
        collector.addStringCreator(constructor, true);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), "test"), new MyClassWithStringConstructor("test"));
    }

    @Test
    public void testIntCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithIntConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithIntConstructor.class, int.class);
        collector.addIntCreator(constructor, true);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), 42), new MyClassWithIntConstructor(42));
    }

    @Test
    public void testLongCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithLongConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithLongConstructor.class, long.class);
        collector.addLongCreator(constructor, true);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), 42L), new MyClassWithLongConstructor(42L));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithDoubleConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithDoubleConstructor.class, double.class);
        collector.addDoubleCreator(constructor, true);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), 3.14), new MyClassWithDoubleConstructor(3.14));
    }

    @Test
    public void testBooleanCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithBooleanConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithBooleanConstructor.class, boolean.class);
        collector.addBooleanCreator(constructor, true);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), true), new MyClassWithBooleanConstructor(true));
    }

    @Test
    public void testDelegateCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithDelegateConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithDelegateConstructor.class, MyClassWithDelegate.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public String getName() {
                return "delegate";
            }

            @Override
            public Class<?> getParameterType() {
                return MyClassWithDelegate.class;
            }

            @Override
            public Converter<?> getValueConverter() {
                return StdConverter.Instance;
            }

            @Override
            public boolean isNullable() {
                return true;
            }
        };
        collector.addDelegatingCreator(constructor, true, injectables);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertEquals(inst.createValueInstance(MAPPER.getConfig(), new MyClassWithDelegate()),
                new MyClassWithDelegateConstructor(new MyClassWithDelegate()));
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithPropertiesConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(MyClassWithPropertiesConstructor.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty() {
            @Override
            public AnnotatedMember getMember() {
                return new AnnotatedMethod(MyClassWithPropertiesConstructor.class.getDeclaredMethod("setStringProp", String.class));
            }

            @Override
            public String getName() {
                return "stringProp";
            }

            @Override
            public Class<?> getParameterType() {
                return String.class;
            }

            @Override
            public Converter<?> getValueConverter() {
                return StdConverter.Instance;
            }

            @Override
            public boolean isNullable() {
                return true;
            }
        };
        properties[1] = new CreatorProperty() {
            @Override
            public AnnotatedMember getMember() {
                return new AnnotatedField(MyClassWithPropertiesConstructor.class.getDeclaredField("intProp"));
            }

            @Override
            public String getName() {
                return "intProp";
            }

            @Override
            public Class<?> getParameterType() {
                return int.class;
            }

            @Override
            public Converter<?> getValueConverter() {
                return StdConverter.Instance;
            }

            @Override
            public boolean isNullable() {
                return true;
            }
        };
        collector.addPropertyCreator(constructor, true, properties);

        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        MyClassWithPropertiesConstructor obj = (MyClassWithPropertiesConstructor) inst.createValueInstance(MAPPER.getConfig(), new Object[] {"test", 42});
        assertEquals(obj.getStringProp(), "test");
        assertEquals(obj.getIntProp(), 42);
    }

    @Test
    public void testMissingCreator() throws Exception {
        BeanDescription beanDesc = ClassIntrospector.findClassInfo(MyClassWithMissingConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        ValueInstantiator inst = collector.constructValueInstantiator(MAPPER.getConfig());
        assertNull(inst.createValueInstance(MAPPER.getConfig(), null));
    }

    static class MyClassWithDefaultConstructor {
        @JsonCreator
        public MyClassWithDefaultConstructor() {}

        @JsonProperty
        public String prop;
    }

    static class MyClassWithStringConstructor {
        @JsonCreator
        public MyClassWithStringConstructor(String s) {
            this.stringProp = s;
        }

        @JsonProperty
        public String stringProp;
    }

    static class MyClassWithIntConstructor {
        @JsonCreator
        public MyClassWithIntConstructor(int i) {
            this.intProp = i;
        }

        @JsonProperty
        public int intProp;
    }

    static class MyClassWithLongConstructor {
        @JsonCreator
        public MyClassWithLongConstructor(long l) {
            this.longProp = l;
        }

        @JsonProperty
        public long longProp;
    }

    static class MyClassWithDoubleConstructor {
        @JsonCreator
        public MyClassWithDoubleConstructor(double d) {
            this.doubleProp = d;
        }

        @JsonProperty
        public double doubleProp;
    }

    static class MyClassWithBooleanConstructor {
        @JsonCreator
        public MyClassWithBooleanConstructor(boolean b) {
            this.booleanProp = b;
        }

        @JsonProperty
        public boolean booleanProp;
    }

    static class MyClassWithDelegate {
        public int delegateMethod(int i) { return i * 2; }
    }

    static class MyClassWithDelegateConstructor {
        @JsonCreator
        public MyClassWithDelegateConstructor(MyClassWithDelegate delegate) {
            this.delegate = delegate;
        }

        @JsonProperty
        public int prop;

        MyClassWithDelegate delegate;
    }

    static class MyClassWithPropertiesConstructor {
        @JsonCreator
        public MyClassWithPropertiesConstructor(String stringProp, int intProp) {
            this.stringProp = stringProp;
            this.intProp = intProp;
        }

        @JsonProperty
        public String stringProp;

        @JsonProperty
        public int intProp;
    }

    static class MyClassWithMissingConstructor {
        @JsonProperty
        public int prop;
    }
}