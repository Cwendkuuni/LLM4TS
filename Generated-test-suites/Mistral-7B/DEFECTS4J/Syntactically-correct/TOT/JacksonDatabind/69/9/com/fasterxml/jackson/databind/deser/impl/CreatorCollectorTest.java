import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import java.io.IOException;
import java.util.*;

/**
 * Test for CreatorCollector class
 */
public class CreatorCollectorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test default creator
     */
    @Test
    public void testDefaultCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(null);
        assertNotNull(person);
    }

    /**
     * Test string creator
     */
    @Test
    public void testStringCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorString stringCreator = new PersonCreatorString("name");
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addStringCreator(stringCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(mapper.valueToTree("{\"name\":\"John\"}"));
        assertEquals("John", person.getName());
    }

    /**
     * Test int creator
     */
    @Test
    public void testIntCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorInt intCreator = new PersonCreatorInt(10);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addIntCreator(intCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(mapper.valueToTree("{}"), null, new TypeReference<Integer>() {});
        assertEquals(10, person.getAge());
    }

    /**
     * Test long creator
     */
    @Test
    public void testLongCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorLong longCreator = new PersonCreatorLong(10L);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addLongCreator(longCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(mapper.valueToTree("{}"), null, new TypeReference<Long>() {});
        assertEquals(10, person.getLongProperty());
    }

    /**
     * Test double creator
     */
    @Test
    public void testDoubleCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorDouble doubleCreator = new PersonCreatorDouble(10.5);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addDoubleCreator(doubleCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(mapper.valueToTree("{}"), null, new TypeReference<Double>() {});
        assertEquals(10.5, person.getDoubleProperty(), 0.001);
    }

    /**
     * Test boolean creator
     */
    @Test
    public void testBooleanCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorBoolean booleanCreator = new PersonCreatorBoolean(true);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addBooleanCreator(booleanCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(mapper.valueToTree("{}"), null, new TypeReference<Boolean>() {});
        assertTrue(person.isBooleanProperty());
    }

    /**
     * Test delegate creator
     */
    @Test
    public void testDelegateCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorDelegate delegateCreator = new PersonCreatorDelegate();
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] {new SettableBeanProperty.Std("name", String.class)};
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addDelegatingCreator(delegateCreator.getClass().getMethod("fromString", String.class), true, delegateArgs);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance("Jackson");
        assertEquals("Jackson", person.getName());
    }

    /**
     * Test property-based creator
     */
    @Test
    public void testPropertyBasedCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        PersonCreatorProperties propertiesCreator = new PersonCreatorProperties();
        SettableBeanProperty[] properties = new SettableBeanProperty[] {
            new SettableBeanProperty.Std("name", String.class),
            new SettableBeanProperty.Std("age", Integer.class),
            new SettableBeanProperty.Std("longProperty", Long.class)
        };
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addPropertyCreator(propertiesCreator.getClass().getConstructor(), true, properties);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Person person = inst.createValueInstance(mapper.valueToTree("{\"name\":\"John\", \"age\":10, \"longProperty\":10L}"));
        assertEquals("John", person.getName());
        assertEquals(10, person.getAge());
        assertEquals(10L, person.getLongProperty());
    }

    /**
     * Test error scenarios
     */
    @Test(expected = IllegalArgumentException.class)
    public void testErrorScenarios() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        // Test non-constructor method
        collector.addPropertyCreator(Person.class.getMethod("setName", String.class), false);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        inst.createValueInstance(null);

        // Test every type with same name
        PersonCreatorString stringCreator = new PersonCreatorString("name");
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, false);
        inst = collector.constructValueInstantiator(mapper.getConfig());
        inst.createValueInstance(null);

        // Test explicit and implicit creators
        PersonCreatorInt intCreator = new PersonCreatorInt(10);
        collector.addIntCreator(intCreator, true);
        collector.addIntCreator(Person.class.getConstructor(), false);
        inst = collector.constructValueInstantiator(mapper.getConfig());
        inst.createValueInstance(null, new TypeReference<Integer>() {});

        // Test int and string creators
        PersonCreatorInt intCreator2 = new PersonCreatorInt(20);
        collector.addIntCreator(intCreator2, true);
        collector.addStringCreator(PersonCreatorString.class.getConstructor(), false);
        inst = collector.constructValueInstantiator(mapper.getConfig());
        inst.createValueInstance(null, new TypeReference<String>() {});
    }
}

class Person {
    private String name;
    private int age;
    private long longProperty;
    private boolean booleanProperty;

    public Person() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public long getLongProperty() {
        return longProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }
}

class PersonCreatorString {
    public static Person fromString(String name) {
        return new Person(name);
    }

    public PersonCreatorString(String name) {
        this.name = name;
    }

    private final String name;
}

class PersonCreatorInt {
    public static Person withAge(int age) {
        return new Person(null, age);
    }

    public PersonCreatorInt(int age) {
        this.age = age;
    }

    private final int age;
}

class PersonCreatorLong {
    public static Person withLongProperty(long longProperty) {
        return new Person(null, null, longProperty);
    }

    public PersonCreatorLong(long longProperty) {
        this.longProperty = longProperty;
    }

    private final long longProperty;
}

class PersonCreatorDouble {
    public static Person withDoubleProperty(double doubleProperty) {
        return new Person(null, null, null, doubleProperty);
    }

    public PersonCreatorDouble(double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    private final double doubleProperty;
}

class PersonCreatorBoolean {
    public static Person withBooleanProperty(boolean booleanProperty) {
        return new Person(null, null, null, null, booleanProperty);
    }

    public PersonCreatorBoolean(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    private final boolean booleanProperty;
}

class PersonCreatorDelegate {
    public static Person fromString(String name) {
        Person person = new Person();
        person.setName(name);
        return person;
    }
}

class PersonCreatorProperties {
    public PersonCreatorProperties() {}

    public Person create(String name, int age, long longProperty) {
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setLongProperty(longProperty);
        return person;
    }
}