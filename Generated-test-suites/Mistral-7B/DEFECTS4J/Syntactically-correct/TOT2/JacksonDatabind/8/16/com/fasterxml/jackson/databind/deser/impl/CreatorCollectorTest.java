import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.property.BeanProperty;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private DeserializationConfig config;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        BeanDescription beanDesc = new BeanDescription(List.class);
        collector = new CreatorCollector(beanDesc, true);
        config = new DeserializationConfig();
        mapper = new ObjectMapper(config);
    }

    // Test default constructor
    @Test
    public void testDefaultConstructor() throws Exception {
        collector.setDefaultCreator(new AnnotatedWithParams(List.class.getDeclaredConstructor()));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        assertEquals(type, inst.createValue(null));
    }

    // Test String constructor
    @Test
    public void testStringConstructor() throws Exception {
        String constructorString = "new ArrayList<>()";
        collector.addStringCreator(new AnnotatedWithParams(constructorString));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        assertEquals(type, inst.createValue(""));
    }

    // Test int constructor
    @Test
    public void testIntConstructor() throws Exception {
        int initialSize = 3;
        String constructorString = "new ArrayList<>(" + initialSize + ")";
        collector.addIntCreator(new AnnotatedWithParams(constructorString, true));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        assertEquals(type, inst.createValue(initialSize));
    }

    // Test long constructor
    @Test(expected = IllegalArgumentException.class)
    public void testLongConstructor() throws Exception {
        long initialSize = 3L;
        String constructorString = "new ArrayList<>(" + initialSize + ")";
        collector.addLongCreator(new AnnotatedWithParams(constructorString, true));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        inst.createValue(initialSize); // Throws an exception
    }

    // Test double constructor
    @Test(expected = IllegalArgumentException.class)
    public void testDoubleConstructor() throws Exception {
        double initialSize = 3.0;
        String constructorString = "new ArrayList<>(" + initialSize + ")";
        collector.addDoubleCreator(new AnnotatedWithParams(constructorString, true));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        inst.createValue(initialSize); // Throws an exception
    }

    // Test boolean constructor
    @Test(expected = IllegalArgumentException.class)
    public void testBooleanConstructor() throws Exception {
        boolean initialSize = true;
        String constructorString = "new ArrayList<>(" + initialSize + ")";
        collector.addBooleanCreator(new AnnotatedWithParams(constructorString, true));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        inst.createValue(initialSize); // Throws an exception
    }

    // Test delegate constructor
    @Test
    public void testDelegateConstructor() throws Exception {
        Class<String> delegateType = String.class;
        String constructorString = "new ArrayList<>()";
        BeanProperty property = new BeanProperty("delegate", delegateType, null, null);
        collector.addDelegatingCreator(new AnnotatedWithParams(constructorString), null, new CreatorProperty[] { property });
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        assertNotNull(inst.getValueId("delegate"));
        assertEquals(delegateType, inst.getValueTypeId("delegate"));
        assertEquals(delegateType, inst.getValue(null).getClass());
    }

    // Test property-based constructor
    @Test
    public void testPropertyBasedConstructor() throws Exception {
        String propertyName = "size";
        int initialSize = 3;
        Class<Integer> propertyType = Integer.class;
        BeanProperty property = new BeanProperty(propertyName, propertyType, null, null);
        String constructorString = "new ArrayList<>(size: %s)";
        collector.addPropertyCreator(new AnnotatedWithParams(constructorString), null, new CreatorProperty[] { property });
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        assertEquals(List.class, inst.getValueType());
        assertEquals(initialSize, inst.getValue(null).toString().indexOf(propertyName) + propertyName.length() + 1);
        assertEquals(propertyType, inst.getValueTypeId(propertyName));
        assertEquals(propertyType, inst.getValue(null, propertyName).getClass());
    }

    // Test error cases
    @Test(expected = IllegalArgumentException.class)
    public void testErrorCases() throws Exception {
        Class<String> delegateType = String.class;
        String constructorString = "new ArrayList<>(" + delegateType.getName() + ")";
        collector.addDelegatingCreator(new AnnotatedWithParams(constructorString), null, null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        // Should throw an exception because the constructor does not have an explicit property passed
        inst.createValue(null);

        // Should throw an exception because the constructor is not marked as a creator
        Class<List<Integer>> listType = new ArrayList<>() {}.getClass();
        AnnotatedWithParams creator = new AnnotatedWithParams(listType.getDeclaredConstructor());
        collector.setDefaultCreator(creator);
        inst = collector.constructValueInstantiator(config);
        inst.createValue(null); // Should throw an exception
    }
}