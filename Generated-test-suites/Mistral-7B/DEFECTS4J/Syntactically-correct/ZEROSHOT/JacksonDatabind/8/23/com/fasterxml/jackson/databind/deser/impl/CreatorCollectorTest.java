package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedCreatorProperty;
import com.fasterxml.jackson.databind.introspect.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.ValuePropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeBindings;
import com.fasterxml.jackson.databind.util.TypeUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with simple bean
        BeanDescription simpleBeanDesc = mapper.getSerializationConfig().findBeanDescription(SimpleBean.class);
        CreatorCollector collector = new CreatorCollector(simpleBeanDesc, true);

        // Test default creator
        Method defaultCreator = SimpleBean.class.getDeclaredConstructor();
        AnnotatedWithParams defaultCreatorAnnotation = new AnnotatedWithParams(defaultCreator, null);
        collector.setDefaultCreator(defaultCreatorAnnotation);

        // Test string creator
        Method stringCreator = SimpleBean.class.getDeclaredMethod("stringCreator");
        AnnotatedWithParams stringCreatorAnnotation = new AnnotatedWithParams(stringCreator, null);
        collector.addStringCreator(stringCreatorAnnotation, false);

        // Test int creator
        Method intCreator = SimpleBean.class.getDeclaredMethod("intCreator", int.class);
        AnnotatedWithParams intCreatorAnnotation = new AnnotatedWithParams(intCreator, null);
        collector.addIntCreator(intCreatorAnnotation, false);

        // Test long creator
        Method longCreator = SimpleBean.class.getDeclaredMethod("longCreator", long.class);
        AnnotatedWithParams longCreatorAnnotation = new AnnotatedWithParams(longCreator, null);
        collector.addLongCreator(longCreatorAnnotation, false);

        // Test double creator
        Method doubleCreator = SimpleBean.class.getDeclaredMethod("doubleCreator", double.class);
        AnnotatedWithParams doubleCreatorAnnotation = new AnnotatedWithParams(doubleCreator, null);
        collector.addDoubleCreator(doubleCreatorAnnotation, false);

        // Test boolean creator
        Method booleanCreator = SimpleBean.class.getDeclaredMethod("booleanCreator", boolean.class);
        AnnotatedWithParams booleanCreatorAnnotation = new AnnotatedWithParams(booleanCreator, null);
        collector.addBooleanCreator(booleanCreatorAnnotation, false);

        // Test delegate creator
        Method delegateCreator = SimpleBean.class.getDeclaredMethod("delegateCreator", String.class);
        AnnotatedWithParams delegateCreatorAnnotation = new AnnotatedWithParams(delegateCreator, null);
        collector.addDelegatingCreator(delegateCreatorAnnotation, new CreatorProperty[0], null);

        // Test property-based creator
        BeanDescription nestedBeanDesc = mapper.getSerializationConfig().findBeanDescription(NestedBean.class);
        JavaType nestedBeanType = TypeUtil.handleInnerType(nestedBeanDesc.getType(), SimpleBean.class);
        ValuePropertyDefinition propDef = nestedBeanDesc.findPropertyDefinition("nested");
        BeanPropertyDefinition nestedPropDef = new BeanPropertyDefinition(propDef.getName(), nestedBeanDesc, null, null, null, null, null, null);
        CreatorProperty[] propCreators = new CreatorProperty[] { new NestedCreatorProperty(nestedPropDef) };
        AnnotatedWithParams propertyCreatorAnnotation = new AnnotatedWithParams(null, null, null, nestedBeanType, null, propCreators);
        collector.addPropertyCreator(propertyCreatorAnnotation, false, propCreators);

        // Test constructor with no arguments
        assertTrue(collector.hasDefaultCreator());

        // Test string creator
        assertEquals("java.lang.String", collector._beanDesc.getType().getName());
        StdValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getSerializationConfig());
        assertNotNull(instantiator);
        instantiator.configureFromStringCreator(collector._creators[CreatorCollector.C_STRING]);
        Object stringInstance = instantiator.createUsingString(mapper, "Hello World");
        assertEquals("Hello World", stringInstance.toString());

        // Test int creator
        instantiator = collector.constructValueInstantiator(mapper.getSerializationConfig());
        instantiator.configureFromIntCreator(collector._creators[CreatorCollector.C_INT]);
        Object intInstance = instantiator.createUsingInt(mapper, 42);
        assertEquals(42, intInstance.intValue());

        // Test long creator
        instantiator = collector.constructValueInstantiator(mapper.getSerializationConfig());
        instantiator.configureFromLongCreator(collector._creators[CreatorCollector.C_LONG]);
        Object longInstance = instantiator.createUsingLong(mapper, 42L);
        assertEquals(42L, longInstance.longValue());

        // Test double creator
        instantiator = collector.constructValueInstantiator(mapper.getSerializationConfig());
        instantiator.configureFromDoubleCreator(collector._creators[CreatorCollector.C_DOUBLE]);
        Object doubleInstance = instantiator.createUsingDouble(mapper, 42.0);
        assertEquals(42.0, doubleInstance.doubleValue(), 0.0);

        // Test boolean creator
        instantiator = collector.constructValueInstantiator(mapper.getSerializationConfig());
        instantiator.configureFromBooleanCreator(collector._creators[CreatorCollector.C_BOOLEAN]);
        Object booleanInstance = instantiator.createUsingBoolean(mapper, true);
        assertTrue(booleanInstance.booleanValue());

        // Test delegate creator
        instantiator = collector.constructValueInstantiator(mapper.getSerializationConfig());
        instantiator.configureFromObjectSettings(collector._creators[CreatorCollector.C_DEFAULT],
                null, null, null,
                collector._creators[CreatorCollector.C_PROPS], collector._propertyBasedArgs);
        instantiator.configureFromDelegatedProperty(collector._creators[CreatorCollector.C_DELEGATE], 0, null);
        Object delegateInstance = instantiator.createUsingDefault(mapper);
        assertEquals("Hello Delegate", delegateInstance.toString());

        // Test property-based creator
        SerializationConfig config = mapper.getSerializationConfig();
        SerializationConfig propertyConfig = config.with(new PropertyNamingStrategy.DefaultWithDotsStrategy());
        ObjectMapper propertyMapper = new ObjectMapper(propertyConfig);
        instantiator = collector.constructValueInstantiator(propertyConfig);
        instantiator.configureFromObjectSettings(collector._creators[CreatorCollector.C_DEFAULT],
                null, null, null,
                collector._creators[CreatorCollector.C_PROPS], collector._propertyBasedArgs);
        Object propertyInstance = instantiator.createUsingDefault(propertyMapper);
        assertEquals("SimpleBean{nested=NestedBean{value=Hello World}}", propertyInstance.toString());

        // Test with empty creator list
        BeanDescription emptyBeanDesc = mapper.getSerializationConfig().findBeanDescription(EmptyBean.class);
        CreatorCollector emptyCollector = new CreatorCollector(emptyBeanDesc, true);
        assertTrue(emptyCollector.hasDefaultCreator());
        StdValueInstantiator emptyInstantiator = emptyCollector.constructValueInstantiator(mapper.getSerializationConfig());
        assertNotNull(emptyInstantiator);
        Object emptyInstance = emptyInstantiator.createUsingDefault(mapper);
        assertNotNull(emptyInstance);
    }

    static class SimpleBean {
        private String value;

        @JsonCreator
        public SimpleBean() {
            this("Default");
        }

        public SimpleBean(String value) {
            this.value = value;
        }

        public String stringCreator(String arg) {
            return arg;
        }

        public SimpleBean intCreator(int arg) {
            return new SimpleBean("IntCreator-" + arg);
        }

        public SimpleBean longCreator(long arg) {
            return new SimpleBean("LongCreator-" + arg);
        }

        public SimpleBean doubleCreator(double arg) {
            return new SimpleBean("DoubleCreator-" + arg);
        }

        public SimpleBean booleanCreator(boolean arg) {
            return new SimpleBean("BooleanCreator-" + arg);
        }

        public SimpleBean delegateCreator(String arg) {
            return new SimpleBean("DelegateCreator-" + arg);
        }

        @JsonProperty
        public String getValue() {
            return value;
        }

        @JsonProperty
        public void setValue(String value) {
            this.value = value;
        }
    }

    static class NestedBean {
        private String value;

        @JsonCreator
        public NestedBean(@JsonProperty("value") String value) {
            this.value = value;
        }

        @JsonProperty
        public String getValue() {
            return value;
        }

        @JsonProperty
        public void setValue(String value) {
            this.value = value;
        }
    }

    static class EmptyBean {

    }

    static class AnnotatedWithParams implements AnnotatedMember {
        private Member _member;
        private Class<? extends Annotation>[] _annotations;
        private JavaType _genericParameterType;

        public AnnotatedWithParams(Method method, Class<?>[] annotations) {
            _member = method;
            _annotations = annotations;
        }

        public AnnotatedWithParams(Method method, Class<?>[] annotations, JavaType genericParameterType) {
            _member = method;
            _annotations = annotations;
            _genericParameterType = genericParameterType;
        }

        public AnnotatedWithParams(AnnotatedMember member) {
            _member = member;
        }

        @Override
        public AnnotatedClass<?> getAnnotated() {
            return _member.getDeclaringClass().getAnnotation(SimpleCreator.class);
        }

        @Override
        public Method getMember() {
            return (Method) _member;
        }

        @Override
        public Class<?> getRawType() {
            return _member.getDeclaringClass();
        }

        @Override
        public int getModifiers() {
            return _member.getModifiers();
        }

        @Override
        public String getName() {
            return _member.getName();
        }

        @Override
        public Annotation[] getAnnotations() {
            if (_annotations != null) {
                return _annotations;
            }
            return new Annotation[0];
        }

        @Override
        public Converter<Annotation, Class<? extends Annotation>> findConverterForAnnotations(Class<? extends Annotation>[] annotations) {
            return null;
        }

        @Override
        public AnnotatedParameter[] getParameters() {
            return new AnnotatedParameter[0];
        }

        @Override
        public AnnotatedParameter getParameterAnnotation(int i) {
            return null;
        }

        @Override
        public String getSimpleName() {
            return _member.getName();
        }

        @Override
        public int getParameterIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeBindings getBindings() {
            return null;
        }

        @Override
        public String toString() {
            if (_annotations != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(_member.getName());
                sb.append("(");
                for (int i = 0; i < _annotations.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(_annotations[i].getName());
                }
                sb.append(")");
                if (_genericParameterType != null) {
                    sb.append("<");
                    sb.append(_genericParameterType.toString());
                    sb.append(">");
                }
                return sb.toString();
            }
            return _member.toString();
        }
    }
}

class SimpleCreator {

}