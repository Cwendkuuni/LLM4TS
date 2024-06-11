import com.fasterxml.jackson.core.base.AnnotatedWithParams;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;

public class CreatorCollectorTest {

    private static final String TEST_CLASS_NAME = "com.example.TestBean";

    private BeanDescription beanDescription;
    private CreatorCollector creatorCollector;
    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setup() throws Exception {
        beanDescription = new BeanDescription(new BeanPropertyDefinition(null, "testBean", TEST_CLASS_NAME));
        creatorCollector = new CreatorCollector(beanDescription, true);
        objectMapper = new ObjectMapper();
    }

    // Testing the default constructor
    @Test
    public void testDefaultConstructor() throws Exception {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(null);
        assertNotNull(bean);
    }

    // Testing the constructor with an explicit String creator
    @Test
    public void testStringCreator() throws Exception {
        Method stringCreatorMethod = getClass().getDeclaredMethod("stringCreator");
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringCreatorMethod, null);
        creatorCollector.setStringCreator(stringCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.lang.String");
        assertEquals(bean, "test");
    }

    // Testing the constructor with an explicit int creator
    @Test
    public void testIntCreator() throws Exception {
        Method intCreatorMethod = getClass().getDeclaredMethod("intCreator");
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intCreatorMethod, null);
        creatorCollector.setIntCreator(intCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.lang.Integer");
        assertEquals(bean, 42);
    }

    // Testing the constructor with an explicit long creator
    @Test
    public void testLongCreator() throws Exception {
        Method longCreatorMethod = getClass().getDeclaredMethod("longCreator");
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longCreatorMethod, null);
        creatorCollector.setLongCreator(longCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.lang.Long");
        assertEquals(bean, 42L);
    }

    // Testing the constructor with an explicit double creator
    @Test
    public void testDoubleCreator() throws Exception {
        Method doubleCreatorMethod = getClass().getDeclaredMethod("doubleCreator");
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleCreatorMethod, null);
        creatorCollector.setDoubleCreator(doubleCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.lang.Double");
        assertEquals(bean, 42.0);
    }

    // Testing the constructor with an explicit boolean creator
    @Test
    public void testBooleanCreator() throws Exception {
        Method booleanCreatorMethod = getClass().getDeclaredMethod("booleanCreator");
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanCreatorMethod, null);
        creatorCollector.setBooleanCreator(booleanCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.lang.Boolean");
        assertEquals(bean, true);
    }

    // Testing the constructor with an explicit delegate creator
    @Test
    public void testDelegateCreator() throws Exception {
        Class<?> delegateClass = TestDelegate.class;
        Method delegateCreatorMethod = getClass().getDeclaredMethod("delegateCreator");
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateCreatorMethod, null);

        CreatorProperty[] delegateArgs = {new CreatorProperty(delegateClass)};
        creatorCollector.addDelegatingCreator(delegateCreator, true, delegateArgs);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass(), TestDelegate.class);
        assertNotNull(bean);
    }

    // Testing the constructor with an explicit property-based creator
    @Test
    public void testPropertyBasedCreator() throws Exception {
        JavaType testBeanType = objectMapper.getTypeFactory().constructType(TEST_CLASS_NAME);
        PropertyName propertyName = PropertyName.construct("property");
        PropertyName propertyName2 = PropertyName.construct("property2");

        Method propertyCreatorMethod = getClass().getDeclaredMethod("propertyCreator");
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(propertyCreatorMethod, null);

        CreatorProperty propertyBasedArgs[] = {new CreatorProperty(propertyName, PropertyType.FIELD, testBeanType),
                new CreatorProperty(propertyName2, PropertyType.FIELD, testBeanType)};
        creatorCollector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        TestBean testBean = (TestBean) bean;
        assertEquals(testBean.getProperty(), "testProperty");
        assertEquals(testBean.getProperty2(), "testProperty2");
    }

    // Testing the constructor with an implicit int creator
    @Test
    public void testImplicitIntCreator() throws Exception {
        // No need to set up any creators as there's an implicit int creator for ArrayList
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.util.ArrayList");
        assertEquals(bean, Collections.emptyList());
    }

    // Testing the constructor with an implicit Map creator
    @Test
    public void testImplicitMapCreator() throws Exception {
        // No need to set up any creators as there's an implicit Map creator for HashMap
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.util.HashMap");
        assertEquals(bean, Collections.emptyMap());
    }

    // Testing the constructor with an implicit HashMap creator
    @Test
    public void testImplicitHashMapCreator() throws Exception {
        // No need to set up any creators as there's an implicit HashMap creator for HashMap
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean.getClass().getName(), "java.util.HashMap");
        assertEquals(bean, new HashMap<>());
    }

    // Testing the constructor with an invalid injectable creator
    @Test
    public void testInvalidInjectableCreator() throws Exception {
        Method invalidInjectableCreatorMethod = getClass().getDeclaredMethod("invalidInjectableCreator");
        AnnotatedWithParams invalidInjectableCreator = new AnnotatedWithParams(invalidInjectableCreatorMethod, null);

        assertThrows(IllegalArgumentException.class, () -> creatorCollector.addDelegatingCreator(invalidInjectableCreator, true, new CreatorProperty[0]));
    }

    // Testing the constructor with an injectable creator without a name
    @Test
    public void testInjectableCreatorWithoutName() throws Exception {
        Method injectableCreatorMethod = getClass().getDeclaredMethod("injectableCreatorWithoutName");
        AnnotatedWithParams injectableCreator = new AnnotatedWithParams(injectableCreatorMethod, null);

        creatorCollector.addPropertyCreator(injectableCreator, false, new CreatorProperty[0]);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Object bean = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals(bean, null);
    }

    // Testing the constructor with multiple injectable creators with the same name
    @Test
    public void testMultipleInjectableCreatorsWithSameName() throws Exception {
        Method injectableCreatorMethod1 = getClass().getDeclaredMethod("injectableCreator1");
        Method injectableCreatorMethod2 = getClass().getDeclaredMethod("injectableCreator2");

        AnnotatedWithParams injectableCreator1 = new AnnotatedWithParams(injectableCreatorMethod1, null);
        AnnotatedWithParams injectableCreator2 = new AnnotatedWithParams(injectableCreatorMethod2, null);

        creatorCollector.addPropertyCreator(injectableCreator1, false, new CreatorProperty[0]);
        creatorCollector.addPropertyCreator(injectableCreator2, false, new CreatorProperty[0]);

        assertThrows(IllegalArgumentException.class, () -> creatorCollector.constructValueInstantiator(objectMapper.getConfig()));
    }

    private static class TestBean {
        private String property;
        private String property2;

        public TestBean(String property, String property2) {
            this.property = property;
            this.property2 = property2;
        }

        public String getProperty() {
            return property;
        }

        public String getProperty2() {
            return property2;
        }

        @Override
        public String toString() {
            return "TestBean{" +
                    "property='" + property + '\'' +
                    ", property2='" + property2 + '\'' +
                    '}';
        }
    }

    private static class TestDelegate {
    }

    private Method getMethod(String methodName) throws NoSuchMethodException {
        return getClass().getDeclaredMethod(methodName);
    }

    private AnnotatedWithParams getAnnotatedMethod(String methodName) throws NoSuchMethodException {
        return new AnnotatedWithParams(getClass().getDeclaredMethod(methodName), null);
    }

    private List<Method> getMethodsWithName(String methodName) {
        return Arrays.asList(getClass().getDeclaredMethods()).stream()
                .filter(m -> m.getName().equals(methodName))
                .collect(Collectors.toList());
    }
}