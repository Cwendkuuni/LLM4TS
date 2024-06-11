import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private Object beanInstance;
 private DeserializationContext context;

 @Before
 public void setup() throws Exception {
 property = new CreatorProperty(PropertyName.create("testProperty"),
 JsonTypeFactory.instance.constructSimpleType(String.class), null, null,
 new Annotations(), mock(AnnotatedParameter.class), 0, null, null);
 beanInstance = new Object() {};
 context = mock(DeserializationContext.class);
 }

 @Test
 public void testBasicFunctionality() throws Exception {
 String value = "test";
 when(context.findInjectableValue(anyObject(), eq(property), eq(beanInstance)))
 .thenReturn("injectedValue");

 property.inject(context, beanInstance);
 assertEquals("injectedValue", property.get(beanInstance));
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingInjectableValueId() throws Exception {
 property._injectableValueId = null;
 property.inject(context, beanInstance);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectValue() throws Exception {
 when(context.findInjectableValue(anyObject(), eq(property), eq(beanInstance)))
 .thenReturn(null);
 property.inject(context, beanInstance);
 }

 @Test
 public void testSetFunctionality() throws Exception {
 property.set(beanInstance, "testSet");
 assertEquals("testSet", property.get(beanInstance));
 }

 @Test
 public void testSetAndReturnFunctionality() throws Exception {
 Object returnedValue = "testReturned";
 when(property.setAndReturn(beanInstance, anyObject()))
 .thenReturn(returnedValue);
 assertEquals(returnedValue, property.setAndReturn(beanInstance, "testSet"));
 }

 @Test(expected = IOException.class)
 public void testMissingSetter() throws Exception {
 property._fallbackSetter = null;
 property.set(beanInstance, "testSet");
 }

 @Test
 public void testMarkerFlag() throws Exception {
 property.markAsIgnorable();
 assertNull(property.get(beanInstance));
 }
}