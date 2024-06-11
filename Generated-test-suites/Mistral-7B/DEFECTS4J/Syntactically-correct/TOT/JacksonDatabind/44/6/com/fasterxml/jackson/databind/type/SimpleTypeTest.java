import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() throws Exception {
 Class<?> stringClass = String.class;
 TypeFactory typeFactory = new TypeFactory();
 SimpleType simpleType = typeFactory.construct(stringClass);
 assertEquals(String.class, simpleType.getClass());
 assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
 assertNull(simpleType.getSuperclass());
 assertArrayEquals(new Class[]{}, simpleType.getSuperTypes());
 }

 @Test
 public void testSimpleTypeWithBindings() throws Exception {
 Class<?> listClass = ArrayList.class;
 TypeFactory typeFactory = new TypeFactory();
 JavaType javaType = typeFactory.constructCollectionType(List.class, Integer.class);
 SimpleType simpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, new JavaType[] { javaType }, null, null, false);
 assertEquals(ArrayList.class, simpleType.getClass());
 assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
 assertEquals(List.class, simpleType.getSuperclass());
 assertArrayEquals(new JavaType[] { javaType }, simpleType.getSuperTypes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSimpleTypeConstruction() throws Exception {
 Class<?> mapClass = HashMap.class;
 TypeFactory typeFactory = new TypeFactory();
 typeFactory.construct(mapClass);
 }

 @Test
 public void testSimpleTypeWithValueHandler() throws Exception {
 Class<?> stringClass = String.class;
 TypeFactory typeFactory = new TypeFactory();
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, new Object(), false);
 assertEquals(String.class, simpleType.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() throws Exception {
 Class<?> stringClass = String.class;
 TypeFactory typeFactory = new TypeFactory();
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, true);
 assertEquals(String.class, simpleType.getTypeHandler());
 }
}