import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 Set<Boolean> set = new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType setType = objectMapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), setType);
 Object deserializedSet = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(set), setType);
 assertEquals(Collections.singleton(Boolean.TRUE), deserializedSet);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType listType = objectMapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), listType);
 Object deserializedList = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(list), listType);
 assertEquals(Collections.singletonList(Boolean.TRUE), deserializedList);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 Map<String, Boolean> map = new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} ;
 JavaType mapType = objectMapper.getTypeFactory().constructType(Map.class, String.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), mapType);
 Object deserializedMap = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(map), mapType);
 assertEquals(Collections.singletonMap("a", Boolean.TRUE), deserializedMap);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 Set<Boolean> set = Collections.unmodifiableSet(new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType setType = objectMapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), setType);
 Object deserializedSet = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(set), setType);
 assertEquals(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)), deserializedSet);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 List<Boolean> list = Collections.unmodifiableList(new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType listType = objectMapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), listType);
 Object deserializedList = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(list), listType);
 assertEquals(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)), deserializedList);
 }

 @Test
 public void testFindForCollectionUnmodifiableMap() throws Exception {
 Map<String, Boolean> map = Collections.unmodifiableMap(new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} );
 JavaType mapType = objectMapper.getTypeFactory().constructType(Map.class, String.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), mapType);
 Object deserializedMap = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(map), mapType);
 assertEquals(Collections.unmodifiableMap(Collections.singletonMap("a", Boolean.TRUE)), deserializedMap);
 }

 @Test
 public void testFindForCollectionInvalidType() throws Exception {
 JavaType invalidType = objectMapper.getTypeFactory().constructType(Integer.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), invalidType);
 assertEquals(null, deserializer);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 Map<String, Boolean> map = new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} ;
 JavaType mapType = objectMapper.getTypeFactory().constructType(Map.class, String.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), mapType);
 Object deserializedMap = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(map), mapType);
 assertEquals(Collections.singletonMap("a", Boolean.TRUE), deserializedMap);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 Map<String, Boolean> map = Collections.unmodifiableMap(new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} );
 JavaType mapType = objectMapper.getTypeFactory().constructType(Map.class, String.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), mapType);
 Object deserializedMap = deserializer.deserialize(JavaUtilCollectionsDeserializers.toJsonString(map), mapType);
 assertEquals(Collections.unmodifiableMap(Collections.singletonMap("a", Boolean.TRUE)), deserializedMap);
 }

 @Test
 public void testFindForMapInvalidType() throws Exception {
 JavaType invalidType = objectMapper.getTypeFactory().constructType(Integer.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), invalidType);
 assertEquals(null, deserializer);
 }

 private static String toJsonString(Object obj) {
 return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
 }

 @Before
 public void setup() {
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

}