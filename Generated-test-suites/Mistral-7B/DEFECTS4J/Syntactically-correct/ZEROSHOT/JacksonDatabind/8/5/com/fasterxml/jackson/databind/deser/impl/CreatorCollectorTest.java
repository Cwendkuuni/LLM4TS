package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DeserializationConfig config = mapper.getConfig();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestClass.class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default Creator
        AnnotatedConstructor defaultConstructor = beanDesc.getConstructor(null);
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(defaultConstructor, null);
        collector.setDefaultCreator(defaultCreator);

        // String Creator
        AnnotatedMethod stringMethod = beanDesc.findCreator(String.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringMethod, null);
        collector.addStringCreator(stringCreator, true);

        // Int Creator
        AnnotatedMethod intMethod = beanDesc.findCreator(int.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intMethod, null);
        collector.addIntCreator(intCreator, true);

        // Long Creator
        AnnotatedMethod longMethod = beanDesc.findCreator(long.class);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longMethod, null);
        collector.addLongCreator(longCreator, true);

        // Double Creator
        AnnotatedMethod doubleMethod = beanDesc.findCreator(double.class);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleMethod, null);
        collector.addDoubleCreator(doubleCreator, true);

        // Boolean Creator
        AnnotatedMethod booleanMethod = beanDesc.findCreator(boolean.class);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanMethod, null);
        collector.addBooleanCreator(booleanCreator, true);

        // Delegating Creator
        AnnotatedConstructor delegateConstructor = TestClassWithDelegate.class.getDeclaredConstructor(String.class);
        ClassUtil.makeAccessible(delegateConstructor);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateConstructor, null);
        CreatorProperty[] delegateArgs = new CreatorProperty[1];
        CreatorProperty arg = new CreatatorProperty();
        arg.setName("myArg");
        arg.setInjectableValueId("myArgId");
        delegateArgs[0] = arg;
        collector.addDelegatingCreator(delegateCreator, true, delegateArgs);

        // Property Creator
        Class<? extends TestClassWithProperties> propertiesClass = TestClassWithProperties.class;
        BeanDescription propertiesBeanDesc = mapper.getTypeFactory().constructType(propertiesClass);
        AnnotatedConstructor propertiesConstructor = propertiesBeanDesc.getConstructor(null);
        AnnotatedMethod[] propertiesMethods = propertiesBeanDesc.getMethods();
        AnnotatedWithParams[] propertyCreators = new AnnotatedWithParams[2];
        for (int i = 0; i < 2; i++) {
            AnnotatedMethod method = propertiesMethods[i];
            AnnotatedParameter[] parameters = method.getParameters();
            AnnotatedWithParams creator = new AnnotatedWithParams(method, parameters[0]);
            propertyCreators[i] = creator;
            BeanPropertyDefinition propDef = propertiesBeanDesc.findProperty(method.getName());
            CreatorProperty arg = new CreatorProperty();
            arg.setName(propDef.getName());
            arg.setInjectableValueId(propDef.getInjectableValueId());
            CreatorProperty[] args = new CreatorProperty[1];
            args[0] = arg;
            collector.addPropertyCreator(creator, true, args);
        }

        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);

        // Test default creator
        TestClass defaultInstance = (TestClass) valueInstantiator.createUsingDefault(config);
        assertNotNull(defaultInstance);

        // Test string creator
        String name = "John Doe";
        TestClass stringInstance = (TestClass) valueInstantiator.createValueInstance(config, String.class, name);
        assertEquals(name, stringInstance.getName());

        // Test int creator
        int age = 25;
        TestClass intInstance = (TestClass) valueInstantiator.createValueInstance(config, int.class, age);
        assertEquals(age, intInstance.getAge());

        // Test long creator
        long id = 1L;
        TestClass longInstance = (TestClass) valueInstantiator.createValueInstance(config, long.class, id);
        assertEquals(id, longInstance.getId());

        // Test double creator
        double price = 12.5;
        TestClass doubleInstance = (TestClass) valueInstantiator.createValueInstance(config, double.class, price);
        assertEquals(price, doubleInstance.getPrice(), 0.001);

        // Test boolean creator
        boolean isMale = true;
        TestClass booleanInstance = (TestClass) valueInstantiator.createValueInstance(config, boolean.class, isMale);
        assertEquals(isMale, booleanInstance.isMale());

        // Test delegating creator
        TestClassWithDelegate delegateInstance = (TestClassWithDelegate) valueInstantiator.createValueInstance(config, TestClassWithDelegate.class, "Test");
        assertEquals("Test", delegateInstance.getMyArg());

        // Test property creator
        TestClassWithProperties propertiesInstance = (TestClassWithProperties) valueInstantiator.createValueInstance(config, TestClassWithProperties.class, null);
        assertEquals("Value1", propertiesInstance.getProperty1());
        assertEquals("Value2", propertiesInstance.getProperty2());

        // Test JsonDeserialize
        TypeDeserializer typeDeserializer = config.findValueDeserializer(new TypeReference<TestClassWithJsonDeserialize>() {}.getType());
        TestClassWithJsonDeserialize jsonInstance = mapper.readValue(new ObjectMapper().writeValueAsString(new TestClassWithJsonDeserialize("Json")), TestClassWithJsonDeserialize.class, typeDeserializer);
        assertEquals("Json", jsonInstance.getName());
    }

    static class TestClass {
        private String name;
        private int age;
        private long id;
        private double price;
        private boolean isMale;

        public TestClass() { }

        public TestClass(String name) {
            this.name = name;
        }

        public TestClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public TestClass(String name, int age, long id) {
            this.name = name;
            this.age = age;
            this.id = id;
        }

        public TestClass(String name, int age, long id, double price, boolean isMale) {
            this.name = name;
            this.age = age;
            this.id = id;
            this.price = price;
            this.isMale = isMale;
        }

        public String getName() {
            return name;
        }

        @JsonProperty("age")
        public int getAge() {
            return age;
        }

        @JsonProperty("id")
        public long getId() {
            return id;
        }

        @JsonProperty("price")
        public double getPrice() {
            return price;
        }

        @JsonProperty("isMale")
        public boolean isMale() {
            return isMale;
        }
    }

    static class TestClassWithDelegate {
        private String myArg;

        public TestClassWithDelegate(String myArg) {
            this.myArg = myArg;
        }

        public String getMyArg() {
            return myArg;
        }

        @JsonCreator(mode = Mode.DELEGATING)
        public static TestClassWithDelegate fromString(String myArg) {
            return new TestClassWithDelegate(myArg);
        }
    }

    static class TestClassWithProperties {
        private String property1;
        private String property2;

        public TestClassWithProperties() { }

        public TestClassWithProperties(String property1, String property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        public String getProperty1() {
            return property1;
        }

        @JsonProperty("property1")
        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty2() {
            return property2;
        }

        @JsonProperty("property2")
        public void setProperty2(String property2) {
            this.property2 = property2;
        }
    }

    static class TestClassWithJsonDeserialize {
        private String name;

        public TestClassWithJsonDeserialize() { }

        public TestClassWithJsonDeserialize(String name) {
            this.name = name;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonDeserialize(contentAs = String.class, propertyName = "name")
        public void setName(String name) {
            this.name = name;
        }
    }

    static class AnnotatedWithParams {
        private AnnotatedMember member;
        private AnnotatedParameter[] parameters;

        public AnnotatedWithParams(AnnotatedMember member, AnnotatedParameter[] parameters) {
            this.member = member;
            this.parameters = parameters;
        }

        public AnnotatedMember getAnnotated() {
            return member;
        }

        public AnnotatedParameter getParameter(int index) {
            return parameters[index];
        }

        public int getParameterCount() {
            return parameters.length;
        }

        public Class<?>[] getParameterTypes() {
            if (parameters == null) {
                return new Class[0];
            }
            Class<?>[] types = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = parameters[i].getType();
            }
            return types;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Method: ");
            Method method = member.getAnnotated().asMethod();
            sb.append(method.getName());
            sb.append("(");
            for (int i = 0; i < parameters.length; i++) {
                sb.append(parameters[i].getType().getName());
                if (i < parameters.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    }
}

class AnnotatedMember {
    private Object member;

    public AnnotatedMember(Object member) {
        this.member = member;
    }

    public Object getAnnotated() {
        return member;
    }

    public Class<?> getType() {
        return member.getClass();
    }
}

class AnnotatedConstructor extends AnnotatedMethod {
    public AnnotatedConstructor(Constructor<?> constructor) {
        super(constructor);
    }
}

class AnnotatedMethod extends AnnotatedMember {
    public AnnotatedMethod(Method method) {
        super(method);
    }
}