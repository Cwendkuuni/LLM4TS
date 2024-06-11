import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private BeanDescription beanDesc;
 private DeserializationConfig config;
 private CreatorCollector creatorCollector;

 @Before
 public void setup() {
 beanDesc = new BeanDescription(null);
 config = new DeserializationConfig(new AnnotationIntrospector());
 creatorCollector = new CreatorCollector(beanDesc, true);
 }

 @Test
 public void testDefaultCreator() {
 // Create a default CreatorCollector instance
 creatorCollector = new CreatorCollector(beanDesc, true);

 // Test the default constructor
 StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
 Object value = instantiator.createValueInstance(config, null);
 assertNotNull(value);
 }

 @Test
 public void testStringCreator() throws Exception {
 // Add a string creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 creatorCollector.addStringCreator(creator, true);

 // Test the string creator
 instantiator = creatorCollector.constructValueInstantiator(config);
 String value = instantiator.createValueInstance(config, String.class);
 assertEquals("", value);
 }

 @Test
 public void testIntCreator() throws Exception {
 // Add an int creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 creatorCollector.addIntCreator(creator, true);

 // Test the int creator
 instantiator = creatorCollector.constructValueInstantiator(config);
 int value = instantiator.createValueInstance(config, int.class);
 assertEquals(0, value);
 }

 @Test
 public void testLongCreator() throws Exception {
 // Add a long creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 creatorCollector.addLongCreator(creator, true);

 // Test the long creator
 instantiator = creatorCollector.constructValueInstantiator(config);
 long value = instantiator.createValueInstance(config, long.class);
 assertEquals(0L, value);
 }

 @Test
 public void testDoubleCreator() throws Exception {
 // Add a double creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 creatorCollector.addDoubleCreator(creator, true);

 // Test the double creator
 instantiator = creatorCollector.constructValueInstantiator(config);
 double value = instantiator.createValueInstance(config, double.class);
 assertEquals(0.0, value, 0.001);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 // Add a boolean creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 creatorCollector.addBooleanCreator(creator, true);

 // Test the boolean creator
 instantiator = creatorCollector.constructValueInstantiator(config);
 boolean value = instantiator.createValueInstance(config, boolean.class);
 assertEquals(false, value);
 }

 @Test
 public void testDelegatingCreator() throws Exception {
 // Add a delegating creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 CreatorProperty[] injectables = new CreatorProperty[1];
 injectables[0] = mock(CreatorProperty.class);
 creatorCollector.addDelegatingCreator(creator, true, injectables);

 // Test the delegating creator
 instantiator = creatorCollector.constructValueInstantiator(config);
 instantiator.configureFromObjectSettings(creator, null, null, injectables, null, null);
 Object value = instantiator.createValueInstance(config, Object.class);
 assertNotNull(value);
 }

 @Test(expected = IllegalStateException.class)
 public void testDuplicateCreator() throws Exception {
 // Add a duplicate string creator to the CreatorCollector
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 creatorCollector.addStringCreator(creator, true);
 creatorCollector.addStringCreator(creator, true);
 creatorCollector.constructValueInstantiator(config);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncompleteParameter() throws Exception {
 // Add an incomplete parameter to the CreatorCollector
 AnnotatedParameter parameter = mock(AnnotatedParameter.class);
 creatorCollector.addIncompeteParameter(parameter);creatorCollector.constructValueInstantiator(config);
 }

 @Test
 public void testVanillaInstantiator() throws Exception {
 // Test the vanilla instantiator
 StdValueInstantiator vanillaInstantiator = new StdValueInstantiator(config, Object.class);
 Object value = vanillaInstantiator.createValueInstance(config, null);
 assertNotNull(value);
 }
}