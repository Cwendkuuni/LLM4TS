import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonCreatorProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        MapperFactory factory = new MapperFactory();
        objectMapper = factory.getObjectMapper();
        BeanDescription beanDesc = objectMapper.getSerializationConfig().findBeanDescription(String.class);
        creatorCollector = new CreatorCollector(beanDesc, factory.getConfig());
    }

    @Test
    public void testDefaultCreator() throws IOException {
        // Test default creator (no-arguments constructor)
        creatorCollector.setDefaultCreator(null);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals("", inst.createValue(null));
    }

    @Test
    public void testStringCreator() throws IOException {
        // Test String creator
        String[] args = {"arg1"};
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(String[].class, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf"));
        AnnotatedWithParams creatorParams = new AnnotatedWithParams(creator, new AnnotatedParameter(creator, 0), null, null, null);
        creatorCollector.addStringCreator(creatorParams, true);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals("arg1", inst.createValue(new String[]{"arg1"}));
    }

    @Test
    public void testIntCreator() throws IOException {
        // Test int creator
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(int[].class, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf"));
        AnnotatedWithParams creatorParams = new AnnotatedWithParams(creator, new AnnotatedParameter(creator, 0), null, null, null);
        creatorCollector.addIntCreator(creatorParams, true);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(42, inst.createValue(new int[]{42}));
    }

    @Test
    public void testLongCreator() throws IOException {
        // Test long creator
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(long[].class, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf"));
        AnnotatedWithParams creatorParams = new AnnotatedWithParams(creator, new AnnotatedParameter(creator, 0), null, null, null);
        creatorCollector.addLongCreator(creatorParams, true);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(42L, inst.createValue(new long[]{42L}));
    }

    @Test
    public void testDoubleCreator() throws IOException {
        // Test double creator
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(double[].class, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf"));
        AnnotatedWithParams creatorParams = new AnnotatedWithParams(creator, new AnnotatedParameter(creator, 0), null, null, null);
        creatorCollector.addDoubleCreator(creatorParams, true);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(3.14, inst.createValue(new double[]{3.14}), 0.001);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        // Test boolean creator
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(boolean[].class, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf"));
        AnnotatedWithParams creatorParams = new AnnotatedWithParams(creator, new AnnotatedParameter(creator, 0), null, null, null);
        creatorCollector.addBooleanCreator(creatorParams, true);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(true, inst.createValue(new boolean[]{true}));
    }

    @Test
    public void testDelegateCreator() throws IOException {
        // Test delegate creator
        Class<?> delegateClass = String[].class;
        AnnotatedWithParams delegateCreator = null;
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(delegateClass, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf", String[].class));
        SettableBeanProperty[] injectables = new SettableBeanProperty[0];
        creatorCollector.addDelegatingCreator(creator, true, injectables);

        JavaType delegateType = creatorCollector.constructValueInstantiator(objectMapper.getConfig()).getType();
        assertEquals(String[].class, delegateType);

        // Test calling delegate creator
        String[] args = {"arg1", "arg2"};
        creatorCollector.setDefaultCreator(null);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(new String[]{"arg1", "arg2"}, inst.createValue(args));
    }

    @Test
    public void testArrayDelegateCreator() throws IOException {
        // Test array delegate creator
        Class<?> delegateClass = int[].class;
        AnnotatedWithParams delegateCreator = null;
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(delegateClass, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("valueOf", int[].class));
        SettableBeanProperty[] injectables = new SettableBeanProperty[]{new SettableBeanProperty("elementType", new PropertyName("int"))};
        creatorCollector.addDelegatingCreator(creator, true, injectables);

        JavaType delegateType = creatorCollector.constructValueInstantiator(objectMapper.getConfig()).getType();
        assertEquals(int[].class, delegateType);

        // Test calling array delegate creator
        int[] args = {1, 2, 3};
        creatorCollector.setDefaultCreator(null);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(new int[]{1, 2, 3}, inst.createValue(args));
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        // Test property-based creator
        Class<?> targetClass = MyClass.class;
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreator(targetClass, new NopAnnotationIntrospector());
        AnnotatedMember creator = annotatedClass.findMember(new Method("create"));
        PropertyName[] propertyNames = new PropertyName[]{"name", "value"};
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        for (int i = 0; i < propertyNames.length; i++) {
            properties[i] = new SettableBeanProperty(propertyNames[i], null);
        }
        creatorCollector.addPropertyCreator(creator, true, properties);

        JavaType type = creatorCollector.constructValueInstantiator(objectMapper.getConfig()).getType();
        assertEquals(targetClass, type.getRawClass());

        // Test calling property-based creator
        MyClass obj = new MyClass("test", 42);
        creatorCollector.setDefaultCreator(null);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertEquals(obj, inst.createValue(null));
    }

    @Test
    public void testNoCreator() throws IOException {
        // Test no creator
        creatorCollector.setDefaultCreator(null);
        inst = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNull(inst.createValue(null));
    }

    private static class MyClass {
        private String name;
        private int value;

        @JsonCreator
        public MyClass(@JsonCreatorProperty(name = "name") String name,
                       @JsonCreatorProperty(name = "value") int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}