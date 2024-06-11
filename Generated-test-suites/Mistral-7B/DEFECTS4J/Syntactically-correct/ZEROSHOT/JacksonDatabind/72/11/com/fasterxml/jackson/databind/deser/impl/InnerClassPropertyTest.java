package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaBeanContext ctxt = new JavaBeanContext();

        // Test constructor with delegate and creator
        SettableBeanProperty beanProp = new LiteralProperty.Builder()
                .name("innerClassProp")
                .build();
        Constructor<?> creator = InnerTestClass.InnerClass.class.getDeclaredConstructor(String.class);
        InnerClassProperty prop = new InnerClassProperty(beanProp, creator);

        // Test basic set and get methods
        Object innerClassInstance = creator.newInstance("innerInstance");
        prop.set(new InnerTestClass(), innerClassInstance);
        assertEquals(innerClassInstance, prop.get(new InnerTestClass()));

        // Test deserialization
        JsonParser jp = mapper.getFactory().createParser("{\"innerClassProp\":\"innerInstance\"}");
        Object deserializedInstance = new InnerTestClass();
        prop.deserializeAndSet(jp, ctxt, deserializedInstance);
        assertEquals(innerClassInstance, prop.get(deserializedInstance));

        // Test JDK serialization
        Object writesReplace = prop.writeReplace();
        assertEquals(prop, writesReplace);
        Object readResolve = prop.readResolve();
        assertEquals(prop, readResolve);

        // Test with value deserializer
        JsonDeserializer<String> valueDeserializer = new StandardDeserializer<>(String.class);
        InnerClassProperty propWithDeserializer = new InnerClassProperty(prop, valueDeserializer);
        Converter<String, InnerTestClass.InnerClass> valueConverter = new StringToInnerClassConverter();
        JsonDeserializer<InnerTestClass.InnerClass> valueTypeDeserializer = new ValueDeserializerWithType<>(valueConverter);
        propWithDeserializer.setValueDeserializer(valueDeserializer);
        propWithDeserializer.setValueTypeDeserializer(valueTypeDeserializer);

        // Test deserialization with value deserializer
        JsonParser jpWithDeserializer = mapper.getFactory().createParser("{\"innerClassProp\":\"value\"}");
        Object deserializedInstanceWithDeserializer = new InnerTestClass();
        propWithDeserializer.deserializeAndSet(jpWithDeserializer, ctxt, deserializedInstanceWithDeserializer);
        assertEquals("value", propWithDeserializer.get(deserializedInstanceWithDeserializer).getName());

        // Test constructor with delegate, deserializer, and annotated constructor
        InnerClassProperty propWithAnnotated = new InnerClassProperty(prop, new AnnotatedConstructor(null, creator, null, null));
        assertEquals(propWithDeserializer, propWithAnnotated);

        // Test constructor with delegate, deserializer, and new name
        PropertyName newName = PropertyName.construct("newName");
        InnerClassProperty propWithNewName = new InnerClassProperty(propWithDeserializer, newName);
        assertEquals(propWithDeserializer, propWithNewName);

        // Test constructor with delegate, deserializer, and new name and index
        Object instanceWithIndex = new InnerTestClass();
        propWithNewName.assignIndex(1);
        propWithNewName.set(instanceWithIndex, new InnerTestClass.InnerClass("newInstance"));
        assertEquals("newInstance", propWithNewName.get(instanceWithIndex).getName());
        assertEquals(1, propWithNewName.getPropertyIndex());
    }

    static class InnerTestClass {
        private InnerClassProperty innerClassProp;

        public InnerClassProperty getInnerClassProp() {
            return innerClassProp;
        }

        public void setInnerClassProp(InnerClassProperty innerClassProp) {
            this.innerClassProp = innerClassProp;
        }

        static class InnerClass {
            private String name;

            public InnerClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }

    static class StringToInnerClassConverter extends StdDeserializer<InnerTestClass.InnerClass> {
        @Override
        public InnerTestClass.InnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String name = jp.getText();
            return new InnerTestClass.InnerClass(name);
        }
    }
}