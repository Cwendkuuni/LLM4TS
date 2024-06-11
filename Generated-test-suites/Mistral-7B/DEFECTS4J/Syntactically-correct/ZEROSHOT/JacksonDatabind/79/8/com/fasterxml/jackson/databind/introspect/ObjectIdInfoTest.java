import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
public class ObjectIdInfoTest {

 private static final PropertyName PROPERTY_NAME = new PropertyName("testProperty");
 private static final Class<ObjectIdInfo> OBJECT_ID_INFO_CLASS = ObjectIdInfo.class;

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.Int.class);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerators.Int.class, objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class, true);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithGeneratorAndAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.Int.class, true, Mockito.mock(ObjectIdResolver.class));
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerators.Int.class, objectIdInfo.getGeneratorType());
 assertEquals(Mockito.mock(ObjectIdResolver.class).getClass(), objectIdInfo.getResolverType());
 assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class);
 ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals(objectIdInfo, objectIdInfo);
 assertEquals(PROPERTY_NAME, withAlwaysAsId.getPropertyName());
 assertEquals(Object.class, withAlwaysAsId.getScope());
 assertNull(withAlwaysAsId.getGeneratorType());
 assertNull(withAlwaysAsId.getResolverType());
 assertTrue(withAlwaysAsId.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class);
 String toString = objectIdInfo.toString();
 String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=false";
 assertEquals(expected, toString);
 }

 @Test
 public void testGetters() throws IllegalAccessException, NoSuchFieldException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class);

 Field propertyNameField = OBJECT_ID_INFO_CLASS.getDeclaredField("_propertyName");
 propertyNameField.setAccessible(true);
 assertEquals(PROPERTY_NAME, propertyNameField.get(objectIdInfo));

 Field scopeField = OBJECT_ID_INFO_CLASS.getDeclaredField("_scope");
 scopeField.setAccessible(true);
 assertEquals(Object.class, scopeField.get(objectIdInfo));

 Field generatorField = OBJECT_ID_INFO_CLASS.getDeclaredField("_generator");
 generatorField.setAccessible(true);
 assertNull(generatorField.get(objectIdInfo));

 Field resolverField = OBJECT_ID_INFO_CLASS.getDeclaredField("_resolver");
 resolverField.setAccessible(true);
 assertNull(resolverField.get(objectIdInfo));

 Field alwaysAsIdField = OBJECT_ID_INFO_CLASS.getDeclaredField("_alwaysAsId");
 alwaysAsIdField.setAccessible(true);
 assertFalse(alwaysAsIdField.get(objectIdInfo));
 }

 @Test
 public void testGetGeneratorTypeWithMockedResolver() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, null);
 ObjectIdResolver mockResolver = Mockito.mock(ObjectIdResolver.class);
 ObjectIdInfo withResolver = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class, false, mockResolver);

 Class<?> generatorType = withResolver.getGeneratorType();
 assertNotNull(generatorType);
 assertEquals(SimpleObjectIdResolver.class, generatorType);
 }

 @Test
 public void testGetResolverTypeWithMockedGenerator() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerators.None.class, false);
 ObjectIdGenerator mockGenerator = Mockito.mock(ObjectIdGenerator.class);
 ObjectIdInfo withGenerator = new ObjectIdInfo(PROPERTY_NAME, Object.class, mockGenerator.getClass(), false);

 Class<? extends ObjectIdResolver> resolverType = withGenerator.getResolverType();
 assertNotNull(resolverType);
 assertEquals(SimpleObjectIdResolver.class, resolverType);
 }
}