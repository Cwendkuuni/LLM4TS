package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object foo = inst.createValueInstance(null);
        assertNotNull(foo);
    }

    @Test
    public void testStringCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        String constructorName = "Foo";
        Class<Foo> fooClass = Foo.class;
        Object[] args = new Object[]{"test"};
        AnnotatedWithParams creator = new AnnotatedWithParams(Foo.class.getConstructor(String.class), new Introspector(), null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addStringCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Foo foo = (Foo) inst.createValueInstance(args);
        assertEquals(foo.getValue(), "test");
    }

    @Test
    public void testIntCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        String constructorName = "Foo";
        Class<Foo> fooClass = Foo.class;
        Object[] args = new Object[]{42};
        AnnotatedWithParams creator = new AnnotatedWithParams(Foo.class.getConstructor(int.class), new Introspector(), null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addIntCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Foo foo = (Foo) inst.createValueInstance(args);
        assertEquals(foo.getValue(), 42);
    }

    @Test
    public void testLongCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        String constructorName = "Foo";
        Class<Foo> fooClass = Foo.class;
        Object[] args = new Object[]{42L};
        AnnotatedWithParams creator = new AnnotatedWithParams(Foo.class.getConstructor(long.class), new Introspector(), null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addLongCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Foo foo = (Foo) inst.createValueInstance(args);
        assertEquals(foo.getValue(), 42L);
    }

    @Test
    public void testDoubleCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        String constructorName = "Foo";
        Class<Foo> fooClass = Foo.class;
        Object[] args = new Object[]{3.14};
        AnnotatedWithParams creator = new AnnotatedWithParams(Foo.class.getConstructor(double.class), new Introspector(), null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addDoubleCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Foo foo = (Foo) inst.createValueInstance(args);
        assertEquals(foo.getValue(), 3.14, 0.001);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        String constructorName = "Foo";
        Class<Foo> fooClass = Foo.class;
        Object[] args = new Object[]{true};
        AnnotatedWithParams creator = new AnnotatedWithParams(Foo.class.getConstructor(boolean.class), new Introspector(), null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addBooleanCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Foo foo = (Foo) inst.createValueInstance(args);
        assertEquals(foo.getValue(), true);
    }

    @Test
    public void testDelegateCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Bar.class);
        String constructorName = "Bar";
        Class<Bar> barClass = Bar.class;
        Object[] args = new Object[]{new Foo()};
        AnnotatedWithParams creator = new AnnotatedWithParams(Bar.class.getConstructor(Foo.class), new Introspector(), null, null, null);
        SettableBeanProperty delegateArg = new SettableBeanProperty("foo", Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addDelegatingCreator(creator, false, new SettableBeanProperty[]{delegateArg});
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Bar bar = (Bar) inst.createValueInstance(args);
        assertNotNull(bar.getFoo());
        assertEquals(bar.getFoo().getValue(), "test");
    }

    @Test
    public void testArrayDelegateCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Baz.class);
        String constructorName = "Baz";
        Class<Baz> bazClass = Baz.class;
        Object[] args = new Object[]{new Foo[]{new Foo("test1"), new Foo("test2")}};
        AnnotatedWithParams creator = new AnnotatedWithParams(Baz.class.getConstructor(Foo[].class), new Introspector(), null, null, null);
        SettableBeanProperty arrayDelegateArg = new SettableBeanProperty("foos", Foo[].class);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addArrayDelegateCreator(creator, false, new SettableBeanProperty[]{arrayDelegateArg});
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Baz baz = (Baz) inst.createValueInstance(args);
        assertNotNull(baz.getFoos());
        assertEquals(baz.getFoos().length, 2);
        assertEquals(baz.getFoos()[0].getValue(), "test1");
        assertEquals(baz.getFoos()[1].getValue(), "test2");
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Quux.class);
        String constructorName = "Quux";
        Class<Quux> quuxClass = Quux.class;
        Object[] args = new Object[]{42, "test"};
        AnnotatedWithParams creator = new AnnotatedWithParams(Quux.class.getConstructor(int.class, String.class), new Introspector(), null, null, null);
        SettableBeanProperty arg1 = new SettableBeanProperty("i", int.class);
        SettableBeanProperty arg2 = new SettableBeanProperty("s", String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addPropertyCreator(creator, false, new SettableBeanProperty[]{arg1, arg2});
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Quux quux = (Quux) inst.createValueInstance(args);
        assertEquals(quux.getValue().i, 42);
        assertEquals(quux.getValue().s, "test");
    }

    @Test
    public void testIncompleteParameter() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Quux2.class);
        AnnotatedParameter incompleteParameter = new AnnotatedParameter(Quux2.class.getMethod("setValue", Object.class), new Introspector(), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addIncompleteParameter(incompleteParameter);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Quux2 quux2 = (Quux2) inst.createValueInstance(new Object[]{});
        assertNotNull(quux2);
        quux2.setValue("test");
        assertEquals(quux2.getValue(), "test");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuplicateCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        String constructorName = "Foo";
        Class<Foo> fooClass = Foo.class;
        AnnotatedWithParams creator1 = new AnnotatedWithParams(Foo.class.getConstructor(String.class), new Introspector(), null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addStringCreator(creator1, false);
        collector.addStringCreator(creator1, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuplicatePropertyBasedCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(Quux3.class);
        String constructorName = "Quux3";
        Class<Quux3> quuxClass = Quux3.class;
        AnnotatedWithParams creator1 = new AnnotatedWithParams(Quux3.class.getConstructor(String.class), new Introspector(), null, null, null);
        SettableBeanProperty arg1 = new SettableBeanProperty("s", String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.Standard());
        collector.addPropertyCreator(creator1, false, new SettableBeanProperty[]{arg1});
        collector.addPropertyCreator(creator1, false, new SettableBeanProperty[]{arg1});
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
    }

    private static class Foo {
        private String value;

        {
            value = "test";
        }

        public Foo() { }

        public Foo(String s) {
            value = s;
        }

        public String getValue() {
            return value;
        }
    }

    private static class Bar {
        private Foo foo;

        public Bar() { }

        public Bar(Foo foo) {
            this.foo = foo;
        }

        public Foo getFoo() {
            return foo;
        }
    }

    private static class Baz {
        private Foo[] foos;

        public Baz() { }

        public Baz(Foo[] foos) {
            this.foos = foos;
        }

        public Foo[] getFoos() {
            return foos;
        }
    }

    private static class Quux {
        private Value value;

        public Quux() { }

        public Quux(int i, String s) {
            this.value = new Value(i, s);
        }

        public Value getValue() {
            return value;
        }

        private static class Value {
            int i;
            String s;

            public Value(int i, String s) {
                this.i = i;
                this.s = s;
            }
        }
    }

    private static class Quux2 {
        private int i;
        private String s;

        public Quux2() { }

        public Quux2(int i, String s) {
            this.i = i;
            this.s = s;
        }

        public void setValue(Object value) {
            if (value instanceof IntValue) {
                i = ((IntValue) value).value;
            } else if (value instanceof StringValue) {
                s = ((StringValue) value).value;
            }
        }

        private static class IntValue {
            int value;
        }

        private static class StringValue {
            String value;
        }
    }

    private static class Quux3 {
        private String s;

        public Quux3() { }

        public Quux3(String s) {
            this.s = s;
        }

        public void setValue(Object value) {
            if (value instanceof StringValue) {
                s = ((StringValue) value).value;
            }
        }

        private static class StringValue {
            String value;
        }
    }
}