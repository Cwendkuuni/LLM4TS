import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class));
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, JavaType.of(Integer.class));
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> listClass = ArrayList.class;
 JavaType listType = CollectionType.construct(List.class, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).refine(listClass, new TypeBindings(), null, new JavaType[0]);
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(ArrayList.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 }

 @Test
 public void testWithContentType() {
 Class<List<String>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withContentType(JavaType.of(String.class));
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(String.class), listType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withTypeHandler(null);
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 JavaType listTypeWithHandler = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withTypeHandler(new Object() {});
 assertNotNull(listTypeWithHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withContentTypeHandler(new Object() {});
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 JavaType listTypeWithContentHandler = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withContentTypeHandler(new Object() {});
 assertNotNull(listTypeWithContentHandler.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withValueHandler(null);
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 JavaType listTypeWithHandler = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withValueHandler(new Object() {});
 assertNotNull(listTypeWithHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withContentValueHandler(null);
 assertEquals(listClass, listType.getRawClass());
 assertEquals(JavaType.of(Collection.class), listType.getRawClass().getSuperclass());
 assertEquals(JavaType.of(Iterable.class), listType.getRawClass().getInterfaces()[0]);
 assertEquals(JavaType.of(Integer.class), listType.getContentType());
 JavaType listTypeWithHandler = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withContentValueHandler(new Object() {});
 assertNotNull(listTypeWithHandler.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, new JavaType[0], JavaType.of(Integer.class)).withStaticTyping();
 assertEquals(listClass, listType.getRawClass());
 assertTrue(listType.getRawClass().isAnnotationPresent(StaticType.class));
 }
}