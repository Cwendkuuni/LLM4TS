import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanDescription;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest {

 private CreatorCollector creatorCollector;
 private MapperFactory mapperFactory;

 @Before
 public void setUp() throws IOException {
 mapperFactory = new MapperFactory();
 BeanDescription beanDescription = mapperFactory.findConcreteDeserializer(String[].class);
 creatorCollector = new CreatorCollector(beanDescription, mapperFactory.getConfig());
 }

 @Test
 public void testDefaultCreator() {
 CreatorCollector expectedCreatorCollector = new CreatorCollector(new AnnotatedClass(String[].class), mapperFactory.getConfig());
 expectedCreatorCollector.setDefaultCreator(expectedCreatorCollector.findDefaultConstructorCreator());
 CreatorCollector actualCreatorCollector = creatorCollector;
 CreatorCollector copyOfCreatorCollector = new CreatorCollector(actualCreatorCollector);
 ValueInstantiator actualInst = copyOfCreatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(null);
 assertEquals("Expected default constructor to create an instance of String[]", Arrays.asList(new String[0]).toArray(new String[0]), actual);
 }

 @Test
 public void testStringCreator() throws IOException {
 String[] values = new String[]{"test1", "test2"};
 AnnotatedMethod stringCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", String[].class));
 creatorCollector.addStringCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), stringCreator), true);
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(new Object[]{"test"});
 String[] expected = values;
 assertArrayEquals("Expected String creator to create an instance of String[] with given values", expected, actual);
 }

 @Test
 public void testIntCreator() throws IOException {
 int[] values = new int[]{1, 2};
 AnnotatedMethod intCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", int[].class));
 creatorCollector.addIntCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), intCreator), true);
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(new Object[]{new int[]{1, 2}});
 String[] expected = Arrays.copyOf(values, values.length);
 assertArrayEquals("Expected int creator to create an instance of String[] with given values", expected, actual);
 }

 @Test
 public void testLongCreator() throws IOException {
 long[] values = new long[]{1, 2};
 AnnotatedMethod longCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", long[].class));
 creatorCollector.addLongCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), longCreator), true);
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(new Object[]{new long[]{1, 2}});
 String[] expected = Arrays.copyOf(values, values.length);
 assertArrayEquals("Expected long creator to create an instance of String[] with given values", expected, actual);
 }

 @Test
 public void testDoubleCreator() throws IOException {
 double[] values = new double[]{1.0, 2.0};
 AnnotatedMethod doubleCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", double[].class));
 creatorCollector.addDoubleCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), doubleCreator), true);
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(new Object[]{new double[]{1.0, 2.0}});
 String[] expected = Arrays.copyOf(values, values.length);
 assertArrayEquals("Expected double creator to create an instance of String[] with given values", expected, actual);
 }

 @Test
 public void testBooleanCreator() throws IOException {
 boolean[] values = new boolean[]{true, false};
 AnnotatedMethod booleanCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", boolean[].class));
 creatorCollector.addBooleanCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), booleanCreator), true);
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(new Object[]{new boolean[]{true, false}});
 String[] expected = Arrays.copyOf(values, values.length);
 assertArrayEquals("Expected boolean creator to create an instance of String[] with given values", expected, actual);
 }

 @Test
 public void testDelegateCreator() throws IOException {
 List<Integer> delegateList = Arrays.asList(1, 2);
 SettableBeanProperty delegateProperty = new SettableBeanProperty("delegate", Integer.class);
 AnnotatedMethod delegateCreator = new AnnotatedMethod(List.class.getDeclaredMethod("of", Object[].class));
 creatorCollector.addDelegatingCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), delegateCreator), true, new SettableBeanProperty[]{delegateProperty});
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 Object actual = actualInst.createValueInstance(new Object[]{delegateList.toArray(new Object[0])});
 String[] expected = Arrays.copyOf(delegateList.toArray(new Integer[0]), delegateList.size());
 assertArrayEquals("Expected delegate creator to create an instance of String[] with given values", expected, actual);
 }

 @Test(expected=IllegalArgumentException.class)
 public void testDuplicateCreator() throws IOException {
 String[] values = new String[]{"test1", "test2"};
 // First, add a String creator
 AnnotatedMethod stringCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", String[].class));
 creatorCollector.addStringCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), stringCreator), true);
 // Then, add a duplicate String creator
 creatorCollector.addStringCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), stringCreator), true);
 }

 @Test(expected=IllegalArgumentException.class)
 public void testIncompatibleCreatorTypes() throws IOException {
 String[] values1 = new String[]{"test1", "test2"};
 int[] values2 = new int[]{1, 2};
 // Add a String creator
 AnnotatedMethod stringCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", String[].class));
 creatorCollector.addStringCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), stringCreator), true);
 // Add an incompatible int creator
 AnnotatedMethod intCreator = new AnnotatedMethod(String[].class.getDeclaredMethod("valueOf", int[].class));
 creatorCollector.addIntCreator(new AnnotatedWithParams(new AnnotatedClass(String[].class), intCreator), true);
 ValueInstantiator actualInst = creatorCollector.constructValueInstantiator(mapperFactory.getConfig());
 // This should throw an IllegalArgumentException because the int creator is incompatible with the String creator
 actualInst.createValueInstance(new Object[]{values1});
 }
}