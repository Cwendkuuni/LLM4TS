import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 BeanDescription beanDesc = objectMapper.getSerializationConfig().findBeanDescriptionForClass(CreatorCollector.class);
 collector = new CreatorCollector(beanDesc, true);
 }

 // Basic test cases for each method

 @Test
 public void testDefaultCreator() throws Exception {
 Object result = collector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(null);
 assertEquals(CreatorCollector.class, result.getClass());
 }

 @Test
 public void testStringCreator() throws Exception {
 AnnotatedWithParams creator = objectMapper.findCreatorAnnotation(String.class, CreatorCollector.class);
 collector.addStringCreator(creator, false);
 Object result = collector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new String("test"));
 assertEquals(CreatorCollector.class, result.getClass());
 }

 @Test
 public void testIntCreator() throws Exception {
 AnnotatedWithParams creator = objectMapper.findCreatorAnnotation(int[].class, CreatorCollector.class);
 collector.addIntCreator(creator, true); // mark as explicit
 Object result = collector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new int[]{1, 2, 3});
 assertEquals(CreatorCollector.class, result.getClass());
 }

 // Edge cases and exception handling scenarios

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreatorType() throws Exception {
 AnnotatedWithParams creator = objectMapper.findCreatorAnnotation(int.class, CreatorCollector.class);
 collector.addIntCreator(creator, false);
 collector.addIntCreator(creator, false); // duplicate
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreatorAnnotated() throws Exception {
 AnnotatedMember member1 = objectMapper.findMemberValue("_delegateArgs", CreatorCollector.class);
 AnnotatedWithParams creator1 = objectMapper.findCreatorAnnotation(String.class, CreatorCollector.class, member1);
 collector.addDelegatingCreator(creator1, new CreatorProperty[0], true);
 AnnotatedMember member2 = objectMapper.findMemberValue("_delegateArgs", CreatorCollector.class);
 AnnotatedWithParams creator2 = objectMapper.findCreatorAnnotation(int.class, CreatorCollector.class, member2);
 collector.addDelegatingCreator(creator2, new CreatorProperty[0], true); // duplicate
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyCreator() throws Exception {
 AnnotatedWithParams creator = objectMapper.findCreatorAnnotation(MyBean.class, CreatorCollector.class);
 collector.addPropertyCreator(creator, false, new CreatorProperty[]{new CreatorProperty("name", new TypeReference<String>() {})});
 collector.addPropertyCreator(creator, false, new CreatorProperty[]{new CreatorProperty("name", new TypeReference<String>() {}), new CreatorProperty("age", new TypeReference<Integer>() {})}); // duplicate name
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonPublicCreator() throws Exception {
 AnnotatedMember member = objectMapper.findMemberValue("_delegateArgs", CreatorCollector.class);
 member.setAccessible(true);
 AnnotatedWithParams creator = objectMapper.findCreatorAnnotation(String.class, CreatorCollector.class, member);
 collector.addDelegatingCreator(creator, new CreatorProperty[0], true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCreator() throws Exception {
 AnnotatedWithParams creator = null;
 collector.addPropertyCreator(creator, false, new CreatorProperty[0]);
 }

 // Complete test file

}