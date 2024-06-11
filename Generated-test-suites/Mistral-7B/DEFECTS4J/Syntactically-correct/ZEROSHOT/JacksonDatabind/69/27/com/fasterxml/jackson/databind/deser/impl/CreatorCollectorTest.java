package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedClass;
import com.fasterxml.jackson.core.base.AnnotatedElement;
import com.fasterxml.jackson.core.base.AnnotatedMember;
import com.fasterxml.jackson.core.base.AnnotatedWithParams;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test file for CreatorCollector class
 */
public class CreatorCollectorTest {

    private CreatorCollector collector;
    private BeanDescription beanDesc;
    private MapperConfig<ObjectMapper> config;

    @Before
    public void setup() throws Exception {
        beanDesc = new BeanDescription(Object.class);
        config = new MapperConfig<ObjectMapper>() {{
            addHandler(new StdDateParser());
            addHandler(new StdSerializer<String>(String.class));
            addHandler(new StdSerializer<Integer>(Integer.class));
            addHandler(new StdSerializer<Long>(Long.class));
            addHandler(new StdSerializer<Double>(Double.class));
            addHandler(new StdSerializer<Boolean>(Boolean.class));
        }};
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedConstructor(MyClass.class), null);
        collector.setDefaultCreator(creator);
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromString", String.class), null);
        collector.addStringCreator(creator, true);
        assertEquals(creator, collector.getCreator(CreatorCollector.C_STRING));
    }

    @Test
    public void testIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromInt", int.class), null);
        collector.addIntCreator(creator, true);
        assertEquals(creator, collector.getCreator(CreatorCollector.C_INT));
    }

    @Test
    public void testLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromLong", long.class), null);
        collector.addLongCreator(creator, true);
        assertEquals(creator, collector.getCreator(CreatorCollector.C_LONG));
    }

    @Test
    public void testDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromDouble", double.class), null);
        collector.addDoubleCreator(creator, true);
        assertEquals(creator, collector.getCreator(CreatorCollector.C_DOUBLE));
    }

    @Test
    public void testBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromBoolean", boolean.class), null);
        collector.addBooleanCreator(creator, true);
        assertEquals(creator, collector.getCreator(CreatorCollector.C_BOOLEAN));
    }

    @Test
    public void testDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "delegateConstructor", Object.class, Object.class), null);
        SettableBeanProperty arg = new SettableBeanProperty("arg1", String.class, null, null);
        collector.addDelegatingCreator(creator, true, new SettableBeanProperty[] { arg });
        assertEquals(arg, collector.getDelegateArgs()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromString", String.class), null);
        collector.addStringCreator(creator, true);
        collector.addStringCreator(creator, true);
    }

    @Test
    public void testConstructValueInstantiator() throws Exception {
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromString", String.class), null);
        collector.addStringCreator(stringCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);
        assertEquals(String.class, inst.getType(null).getRawClass());
        assertEquals(stringCreator, inst.getCreator(CreatorCollector.C_STRING));
    }

    @Test
    public void testGetCreator() {
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(new AnnotatedMethod(MyClass.class, "fromString", String.class), null);
        collector.addStringCreator(stringCreator, true);
        assertEquals(stringCreator, collector.getCreator(CreatorCollector.C_STRING));
    }

    @Test
    public void testGetCreator_nonExistent() {
        assertNull(collector.getCreator(CreatorCollector.C_NONE));
    }

    private static class MyClass {
        @JsonCreator
        public MyClass() {}

        @JsonCreator
        public MyClass(@JsonProperty("string") String s) {}

        @JsonCreator
        public MyClass(@JsonProperty("int") int i) {}

        @JsonCreator
        public MyClass(@JsonProperty("long") long l) {}

        @JsonCreator
        public MyClass(@JsonProperty("double") double d) {}

        @JsonCreator
        public MyClass(@JsonProperty("boolean") boolean b) {}

        @JsonCreator
        public MyClass(@JsonProperty("arg1") String arg1, @JsonProperty("arg2") Object arg2) {}

        public static MyClass fromString(String s) { return new MyClass(s); }
        public static MyClass fromInt(int i) { return new MyClass(i); }
        public static MyClass fromLong(long l) { return new MyClass(l); }
        public static MyClass fromDouble(double d) { return new MyClass(d); }
        public static MyClass fromBoolean(boolean b) { return new MyClass(b); }
    }
}