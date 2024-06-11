import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

 private ObjectMapper mapper;
 private BeanDescription beanDescription;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
 try {
 beanDescription = mapper.getTypeFactory().constructType(Person.class);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testDefaultCreator() throws JsonMappingException {
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Person person = instantiator.createValueInstance(mapper.getConfig(), Person.class);
 assertNotNull(person);
 }

 @Test
 public void testStringCreator() throws JsonMappingException {
 String name = "John Doe";
 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(Person.class, "createWithName", new Class[]{}, null));
 setValue("John Doe");
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 collector.addStringCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Person person = (Person) instantiator.createValueInstance(mapper.getConfig(), Person.class);
 assertEquals(name, person.getName());
 }

 @Test
 public void testIntCreator() throws JsonMappingException {
 int age = 30;
 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(Person.class, "createWithAge", new Class[]{}, null));
 setValue(Integer.valueOf(age));
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 collector.addIntCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Person person = (Person) instantiator.createValueInstance(mapper.getConfig(), Person.class);
 assertEquals(age, person.getAge());
 }

 @Test
 public void testLongCreator() throws JsonMappingException {
 long id = 1L;
 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(Person.class, "createWithId", new Class[]{}, null));
 setValue(Long.valueOf(id));
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 collector.addLongCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Person person = (Person) instantiator.createValueInstance(mapper.getConfig(), Person.class);
 assertEquals(id, person.getId());
 }

 @Test
 public void testDoubleCreator() throws JsonMappingException {
 double height = 1.75;
 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(Person.class, "createWithHeight", new Class[]{}, null));
 setValue(Double.valueOf(height));
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 collector.addDoubleCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Person person = (Person) instantiator.createValueInstance(mapper.getConfig(), Person.class);
 assertEquals(height, person.getHeight(), 0.0);
 }

 @Test
 public void testBooleanCreator() throws JsonMappingException {
 boolean married = false;
 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(Person.class, "createWithMarried", new Class[]{}, null));
 setValue(Boolean.valueOf(married));
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 collector.addBooleanCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Person person = (Person) instantiator.createValueInstance(mapper.getConfig(), Person.class);
 assertEquals(married, person.isMarried());
 }

 @Test
 public void testDelegateCreator() throws JsonMappingException {
 class PersonWithDelegate {
 Person person;

 public PersonWithDelegate() {
 this.person = null;
 }

 public PersonWithDelegate(Person person) {
 this.person = person;
 }

 @JsonCreator
 public static PersonWithDelegate fromValues(final String name, final int age) {
 return new PersonWithDelegate(new Person(name, age));
 }
 }

 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(PersonWithDelegate.class, "fromValues", new Class[] {String.class, int.class}, null));
 }};
 PersonWithDelegate delegate = new PersonWithDelegate();
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 collector.addDelegatingCreator(creator, true, new CreatorProperty[] {});
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 PersonWithDelegate personWithDelegate = (PersonWithDelegate) instantiator.createValueInstance(mapper.getConfig(), PersonWithDelegate.class, "John Doe", 30);
 assertNotNull(personWithDelegate.person);
 assertEquals("John Doe", personWithDelegate.person.getName());
 assertEquals(30, personWithDelegate.person.getAge());
 }

 @Test
 public void testPropertyBasedCreator() throws JsonMappingException {
 class PersonWithProperties {
 private String name;
 private int age;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 @JsonCreator
 public PersonWithProperties(@JsonProperty("name") String name, @JsonProperty("age") int age) {
 this.name = name;
 this.age = age;
 }
 }

 CreatorProperty nameCreator = new CreatorProperty() {{
 setAnnotated(new AnnotatedField(PersonWithProperties.class, "name"));
 }};
 CreatorProperty ageCreator = new CreatorProperty() {{
 setAnnotated(new AnnotatedField(PersonWithProperties.class, "age"));
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 JavaType propertiesType = mapper.getTypeFactory().constructType(new TypeReference<Map<String,CreatorProperty>>(){}.getType());
 collector.addPropertyCreator(creator -> new CreatorProperty[]{nameCreator, ageCreator}, true, new CreatorProperty[] {});
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 PersonWithProperties personWithProperties = (PersonWithProperties) instantiator.createValueInstance(mapper.getConfig(), PersonWithProperties.class);
 assertEquals("", personWithProperties.getName());
 assertEquals(0, personWithProperties.getAge());

 Map<String,CreatorProperty> creatorProperties = instantiator.getValueInstantiatorInfo().getCreators();
 CreatorProperty nameProperty = creatorProperties.get("name");
 CreatorProperty ageProperty = creatorProperties.get("age");

 BeanDescription propertyDescription = mapper.getTypeFactory().constructType(PersonWithProperties.class);
 ObjectMapper subMapper = mapper.builder().forType(PersonWithProperties.class).build();
 ValueInstantiator nameInstantiator = subMapper.getTypeFactory().constructValueInstantiator(propertyDescription);
 ValueInstantiator ageInstantiator = subMapper.getTypeFactory().constructValueInstantiator(propertyDescription);

 PersonWithProperties createdPerson = (PersonWithProperties) instantiator.createValueInstance(mapper.getConfig(), PersonWithProperties.class, nameCreator.getName(), ageCreator.getValue().intValue());
 assertEquals("", createdPerson.getName());
 assertEquals(ageCreator.getValue().intValue(), createdPerson.getAge());

 PersonWithProperties createdPersonWithName = (PersonWithProperties) nameInstantiator.createValueInstance(subMapper.getConfig(), PersonWithProperties.class, nameCreator.getName());
 assertEquals(nameCreator.getName(), createdPersonWithName.getName());
 assertEquals(0, createdPersonWithName.getAge());

 PersonWithProperties createdPersonWithAge = (PersonWithProperties) ageInstantiator.createValueInstance(subMapper.getConfig(), PersonWithProperties.class, ageCreator.getValue().toString());
 assertEquals("", createdPersonWithAge.getName());
 assertEquals(ageCreator.getValue().intValue(), createdPersonWithAge.getAge());
 }

 @Test
 public void testInvalidCreator() throws JsonMappingException {
 CreatorProperty creator = new CreatorProperty() {{
 setAnnotated(new AnnotatedMethod(Person.class, "invalidCreate", new Class[]{}, null));
 }};
 CreatorCollector collector = new CreatorCollector(beanDescription, true);
 try {
 collector.addIntCreator(creator, true);
 fail("Should not reach here");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}