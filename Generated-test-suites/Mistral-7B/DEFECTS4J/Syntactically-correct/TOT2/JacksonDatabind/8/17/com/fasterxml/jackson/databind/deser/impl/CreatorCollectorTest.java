import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private DeserializationConfig config;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        config = new DeserializationConfig();
        mapper = new ObjectMapper(config);
    }

    @After
    public void tearDown() throws Exception {
        // No cleanup needed
    }

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Map.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType mapType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Map<String, String> map = instantiator.createValueInstance(mapType);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(String[].class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String[] strings = {"one", "two", "three"};
        collector.addStringCreator(mapper.createMethodHandle(String[].class, "create", new Class[0], new Object[0]), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType stringArrayType = mapper.getTypeFactory().constructType(String[].class);
        String[] result = instantiator.createValueInstance(stringArrayType);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("one", result[0]);
        assertEquals("two", result[1]);
        assertEquals("three", result[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(String[].class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String[] strings = {"one", "two", "three"};
        collector.addStringCreator(mapper.createMethodHandle(String[].class, "create", new Class[0], new Object[0]), true);
        collector.addStringCreator(mapper.createMethodHandle(String[].class, "create", new Class[0], new Object[0]), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        // should not reach here
    }

    @Test
    public void testDelegateCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(LinkedHashMap.class);
        CreatorProperty[] injectables = new CreatorProperty[] {
                mapper.findInjectableValue("keyCreator")
        };
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mapper.createMethodHandle(LinkedHashMap.class, "create", LinkedHashMap.class, new Class[] {Map.Entry.class.getType()}, new Object[] {mapper.createValueInstantiator(config).createValueInstance(mapper.getTypeFactory().constructType(Map.Entry.class))});
        collector.addDelegatingCreator(creator, true, injectables);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        MapType mapType = mapper.getTypeFactory().constructMapType(Map.Entry.class, String.class, String.class);
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) instantiator.createValueInstance(mapType);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDelegateCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(LinkedHashMap.class);
        CreatorProperty[] injectables = new CreatorProperty[] {
                mapper.findInjectableValue("keyCreator")
        };
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mapper.createMethodHandle(LinkedHashMap.class, "create", LinkedHashMap.class, new Class[] {Map.Entry.class.getType()}, new Object[] {mapper.createValueInstantiator(config).createValueInstance(mapper.getTypeFactory().constructType(Map.Entry.class))});
        collector.addDelegatingCreator(creator, true, injectables);
        collector.addDelegatingCreator(creator, true, injectables);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        // should not reach here
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(BeanWithProperties.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        PropertyName[] propertyNames = new PropertyName[] {
                PropertyName.construct("firstName"),
                PropertyName.construct("lastName")
        };
        CreatorProperty[] properties = new CreatorProperty[2];
        for (int i = 0; i < 2; i++) {
            properties[i] = mapper.createPropertyName(propertyNames[i].getId()).getAnnotation(CreatorProperty.class);
            properties[i].setName(propertyNames[i].getId());
            properties[i].setValueId(propertyNames[i].getName());
        }
        collector.addPropertyCreator(mapper.createAnnotatedMethod(BeanWithProperties.class, "create", new Class[0], new Object[0]), true, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType valueType = mapper.getTypeFactory().constructType(BeanWithProperties.class);
        BeanWithProperties obj = (BeanWithProperties) instantiator.createValueInstance(valueType);
        assertNotNull(obj);
        assertEquals("John", obj.getFirstName());
        assertEquals("Doe", obj.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(BeanWithProperties.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        PropertyName[] propertyNames = new PropertyName[] {
                PropertyName.construct("firstName"),
                PropertyName.construct("firstName")
        };
        CreatorProperty[] properties = new CreatorProperty[2];
        for (int i = 0; i < 2; i++) {
            properties[i] = mapper.createPropertyName(propertyNames[i].getId()).getAnnotation(CreatorProperty.class);
            properties[i].setName(propertyNames[i].getId());
            properties[i].setValueId(propertyNames[i].getName());
        }
        collector.addPropertyCreator(mapper.createAnnotatedMethod(BeanWithProperties.class, "create", new Class[0], new Object[0]), true, properties);
        collector.addPropertyCreator(mapper.createAnnotatedMethod(BeanWithProperties.class, "create", new Class[0], new Object[0]), true, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        // should not reach here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingPropertyCreator() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(BeanWithProperties.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = mapper.createPropertyName("nonExistentProperty").getAnnotation(CreatorProperty.class);
        collector.addPropertyCreator(mapper.createAnnotatedMethod(BeanWithProperties.class, "create", new Class[0], new Object[0]), true, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType valueType = mapper.getTypeFactory().constructType(BeanWithProperties.class);
        BeanWithProperties obj = (BeanWithProperties) instantiator.createValueInstance(valueType);
        // should not reach here
    }

    @Test
    public void testIncompleteParameter() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(BeanWithIncompleteParameter.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedParameter incompleteParameter = mapper.findParameter("nonExistentParameter", mapper.createAnnotatedMethod(BeanWithIncompleteParameter.class, "create", new Class[0], new Object[0]));
        collector.addIncompleteParameter(incompleteParameter);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType valueType = mapper.getTypeFactory().constructType(BeanWithIncompleteParameter.class);
        BeanWithIncompleteParameter obj = (BeanWithIncompleteParameter) instantiator.createValueInstance(valueType);
        assertNotNull(obj);
        assertEquals("defaultValue", obj.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIncompleteParameter() throws Exception {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(BeanWithIncompleteParameter.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedParameter incompleteParameter1 = mapper.findParameter("nonExistentParameter1", mapper.createAnnotatedMethod(BeanWithIncompleteParameter.class, "create", new Class[0], new Object[0]));
        AnnotatedParameter incompleteParameter2 = mapper.findParameter("nonExistentParameter2", mapper.createAnnotatedMethod(BeanWithIncompleteParameter.class, "create", new Class[0], new Object[0]));
        collector.addIncompleteParameter(incompleteParameter1);
        collector.addIncompleteParameter(incompleteParameter2);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType valueType = mapper.getTypeFactory().constructType(BeanWithIncompleteParameter.class);
        BeanWithIncompleteParameter obj = (BeanWithIncompleteParameter) instantiator.createValueInstance(valueType);
        // should not reach here
    }

}