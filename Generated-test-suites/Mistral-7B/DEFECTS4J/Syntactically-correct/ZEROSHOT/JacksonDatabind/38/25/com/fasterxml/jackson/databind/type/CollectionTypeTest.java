import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class CollectionTypeTest {

 private Class<?> collectionType;
 private JavaType elemType;
 private JavaType collectionInstance;

 public CollectionTypeTest(Class<?> collectionType, JavaType elemType) {
 this.collectionType = collectionType;
 this.elemType = elemType;

 // Create a JavaType instance of the collection type
 collectionInstance = CollectionType.construct(collectionType, null, null, null, elemType);
 }

 @Parameterized.Parameters(name = "{index}: CollectionType({0}, {1})")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][] {
 new Object[] {ArrayList.class, String.class},
 new Object[] {ArrayList.class, Integer.class},
 new Object[] {LinkedList.class, Integer.class},
 new Object[] {ArrayList.class, Double.class},
 new Object[] {LinkedHashSet.class, Integer.class},
 new Object[] {HashSet.class, Double.class}
 });
 }

 @Test
 public void testConstruct() {
 // Test the construct method with valid arguments
 CollectionType actual = CollectionType.construct(collectionType, null, null, null, elemType);

 // Check if the constructed JavaType is instance of CollectionType and has the correct type arguments
 assertTrue(actual instanceof CollectionType);
 assertEquals(collectionInstance, actual);
 }

 @Test
 public void testRefine() {
 // Test the refine method with valid arguments
 Class<?> refinedCollectionType = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType refinedSuperClass = null;
 JavaType[] refinedSuperInterfaces = new JavaType[0];
 JavaType refinedElemType = elemType;
 CollectionType refinedType = CollectionType.refine(refinedCollectionType, bindings, refinedSuperClass, refinedSuperInterfaces);

 // Check if the refined JavaType is instance of CollectionType and has the correct type arguments
 assertTrue(refinedType instanceof CollectionType);
 assertEquals(new CollectionType(refinedCollectionType, bindings, null, new JavaType[0], refinedElemType), refinedType);
 }

 @Test
 public void testWithContentType() {
 // Test the withContentType method with valid arguments
 JavaType contentType = JavaType.constructType(List.class, elemType);
 CollectionType withContentType = collectionInstance.withContentType(contentType);

 // Check if the withContentType JavaType has the correct type arguments
 assertEquals(new CollectionType(collectionType, null, null, new JavaType[0], contentType), withContentType);
 }

 @Test
 public void testWithTypeHandler() {
 // Test the withTypeHandler method with valid arguments
 Object handler = new Object() {};
 CollectionType withTypeHandler = collectionInstance.withTypeHandler(handler);

 // Check if the withTypeHandler JavaType has the correct type arguments and type handler
 assertEquals(new CollectionType(collectionType, null, null, new JavaType[0], elemType, handler, null, false), withTypeHandler);
 }

 @Test
 public void testWithContentTypeHandler() {
 // Test the withContentTypeHandler method with valid arguments
 Object contentHandler = new Object() {};
 CollectionType withContentTypeHandler = collectionInstance.withContentTypeHandler(contentHandler);

 // Check if the withContentTypeHandler JavaType has the correct type arguments and content handler
 assertEquals(new CollectionType(collectionType, null, null, new JavaType[0], elemType, null, contentHandler, false), withContentTypeHandler);
 }

 @Test
 public void testWithValueHandler() {
 // Test the withValueHandler method with valid arguments
 Object valueHandler = new Object() {};
 CollectionType withValueHandler = collectionInstance.withValueHandler(valueHandler);

 // Check if the withValueHandler JavaType has the correct type arguments and value handler
 assertEquals(new CollectionType(collectionType, null, null, new JavaType[0], elemType, valueHandler, null, false), withValueHandler);
 }

 @Test
 public void testWithContentValueHandler() {
 // Test the withContentValueHandler method with valid arguments
 Object contentValueHandler = new Object() {};
 CollectionType withContentValueHandler = collectionInstance.withContentValueHandler(contentValueHandler);

 // Check if the withContentValueHandler JavaType has the correct type arguments and content value handler
 assertEquals(new CollectionType(collectionType, null, null, new JavaType[0], elemType.withValueHandler(contentValueHandler), valueHandler, null, false), withContentValueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 // Test the withStaticTyping method with valid arguments
 CollectionType withStaticTyping = collectionInstance.withStaticTyping();

 // Check if the withStaticTyping JavaType has the same class but with static typing flag set to true
 assertEquals(new CollectionType(collectionType, null, null, new JavaType[0], elemType, null, null, true), withStaticTyping);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRefine() {
 // Test the refine method with invalid arguments (null collectionType)
 JavaType invalidRefinedType = CollectionType.refine(null, null, null, null);

 // An IllegalArgumentException should be thrown
 fail("IllegalArgumentException expected but not thrown");
 }
}