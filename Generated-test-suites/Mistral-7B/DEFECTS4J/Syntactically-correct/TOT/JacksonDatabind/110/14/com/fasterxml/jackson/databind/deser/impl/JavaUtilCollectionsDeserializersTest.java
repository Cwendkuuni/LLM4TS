import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 String json = "[\"true\"]";
 Object result = mapper.readValue(json, JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType).forProperty(""));
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 String json = "[true]";
 Object result = mapper.readValue(json, JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType).forProperty(""));
 assertTrue(result.getClass().equals(HashSet.class));
 assertEquals(Boolean.TRUE, ((Set<?>) result).iterator().next());
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 Object list = new ArrayList<>();
 list.add(Boolean.TRUE);
 JavaType unmodifiableListType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE, List.class);
 Object unmodifiableList = Collections.unmodifiableList(list);
 assertEquals(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableListType).getClass(), JavaUtilCollectionsDeserializers.StdDelegatingDeserializer.class);
 assertEquals(list, JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableListType).getValue());
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 Object set = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Boolean.TRUE)));
 assertEquals(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType).getClass(), JavaUtilCollectionsDeserializers.StdDelegatingDeserializer.class);
 assertEquals(set, JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType).getValue());
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 String json = "{\"a\":\"b\"}";
 Object result = mapper.readValue(json, JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType).forProperty(""));
 assertEquals("b", result.getClass().getMethod("get", String.class).invoke(result, "a"));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Object map = Collections.unmodifiableMap(new HashMap<>());
 map.put("a", "b");
 assertEquals(JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType).getClass(), JavaUtilCollectionsDeserializers.StdDelegatingDeserializer.class);
 assertEquals(map, JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType).getValue());
 }

}