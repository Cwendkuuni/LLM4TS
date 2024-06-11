import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithMembers;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class CreatorCollectorTest {

 private CreatorCollector _creatorCollector;
 private BeanDescription _beanDescription;
 private MapperFactory _mapperFactory;

 @Before
 public void setUp() throws Exception {
 _beanDescription = Mockito.mock(BeanDescription.class);
 _mapperFactory = new MapperFactory();
 _creatorCollector = new CreatorCollector(_beanDescription, _mapperFactory);
 }

 @Test
 public void testDefaultCreator() throws Exception {
 // Given
 _creatorCollector.setDefaultCreator(Mockito.mock(AnnotatedWithParams.class));

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object value = instantiator.createValueInstance(null);

 // Then
 assertNotNull(value);
 }

 @Test
 public void testStringCreator() throws Exception {
 // Given
 String value = "test";
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[0]);
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 Mockito.when(creator.getRawParameterType(0)).thenReturn(String.class);
 _creatorCollector.addStringCreator(creator, true);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object valueInstance = instantiator.createValueInstance(null, new Object[0]);

 // Then
 assertEquals(value, valueInstance);
 }

 @Test
 public void testIntCreator() throws Exception {
 // Given
 int value = 1;
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[] { int.class });
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 Mockito.when(creator.getRawParameterType(0)).thenReturn(int.class);
 _creatorCollector.addIntCreator(creator, true);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object valueInstance = instantiator.createValueInstance(null, new Object[] { value });

 // Then
 assertEquals(value, valueInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDefaultCreator() throws Exception {
 // Given
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 _creatorCollector.setDefaultCreator(creator);
 _creatorCollector.setDefaultCreator(creator);

 // When & Then
 // should throw an exception
 }

 @Test
 public void testLongCreator() throws Exception {
 // Given
 long value = 1L;
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[] { long.class });
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 Mockito.when(creator.getRawParameterType(0)).thenReturn(long.class);
 _creatorCollector.addLongCreator(creator, true);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object valueInstance = instantiator.createValueInstance(null, new Object[] { value });

 // Then
 assertEquals(value, valueInstance);
 }

 @Test
 public void testDoubleCreator() throws Exception {
 // Given
 double value = 1.0;
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[] { double.class });
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 Mockito.when(creator.getRawParameterType(0)).thenReturn(double.class);
 _creatorCollector.addDoubleCreator(creator, true);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object valueInstance = instantiator.createValueInstance(null, new Object[] { value });

 // Then
 assertEquals(value, valueInstance);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 // Given
 boolean value = true;
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[] { boolean.class });
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 Mockito.when(creator.getRawParameterType(0)).thenReturn(boolean.class);
 _creatorCollector.addBooleanCreator(creator, true);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object valueInstance = instantiator.createValueInstance(null, new Object[] { value });

 // Then
 assertEquals(value, valueInstance);
 }

 @Test
 public void testDelegateCreator() throws Exception {
 // Given
 Class<?> delegateType = Mockito.mock(Class.class);
 SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(Mockito.mock(AnnotatedMethod.class));
 Mockito.when(creator.getRawParameterType(0)).thenReturn(delegateType);
 _creatorCollector.addDelegatingCreator(creator, true, delegateArgs);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object delegateInstance = instantiator.createValueInstance(null, new Object[0]);
 Object delegateArgumentInstance = instantiator.createValueInstance(null, delegateArgs);

 // Then
 assertNotNull(delegateInstance);
 assertNotNull(delegateArgumentInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDelegateCreator() throws Exception {
 // Given
 Class<?> delegateType = Mockito.mock(Class.class);
 SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(Mockito.mock(AnnotatedMethod.class));
 Mockito.when(creator.getRawParameterType(0)).thenReturn(delegateType);
 _creatorCollector.addDelegatingCreator(creator, true, delegateArgs);
 _creatorCollector.addDelegatingCreator(creator, true, delegateArgs);

 // When & Then
 // should throw an exception
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 // Given
 Map<String,Object> properties = new HashMap<>();
 properties.put("key", "value");
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[0]);
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];
 Mockito.when(propertyBasedArgs[0].getName()).thenReturn("key");
 Mockito.when(propertyBasedArgs[0].getInjectableValueId()).thenReturn("value");
 _creatorCollector.addPropertyCreator(creator, true, propertyBasedArgs);

 // When
 ValueInstantiator instantiator = _creatorCollector.constructValueInstantiator(_mapperFactory.getConfig());
 Object propertyBasedInstance = instantiator.createValueInstance(null, new Object[0], properties);

 // Then
 assertEquals(properties, propertyBasedInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyCreator() throws Exception {
 // Given
 Map<String,Object> properties = new HashMap<>();
 properties.put("key", "value");
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(method.getParameterTypes()).thenReturn(new Class[0]);
 AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
 Mockito.when(creator.getAnnotatedMember()).thenReturn(method);
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];
 Mockito.when(propertyBasedArgs[0].getName()).thenReturn("key");
 Mockito.when(propertyBasedArgs[0].getInjectableValueId()).thenReturn("value");
 _creatorCollector.addPropertyCreator(creator, true, propertyBasedArgs);
 _creatorCollector.addPropertyCreator(creator, true, propertyBasedArgs);

 // When & Then
 // should throw an exception
 }
}