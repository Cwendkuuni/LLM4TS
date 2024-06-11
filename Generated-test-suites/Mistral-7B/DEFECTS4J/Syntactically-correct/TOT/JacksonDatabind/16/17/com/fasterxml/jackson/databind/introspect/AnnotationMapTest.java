import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.util.AnnotationMap;

public class AnnotationMapTest {

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 annotationMap.add(jsonDeserialize);
 Annotation expectedAnnotation = jsonDeserialize;
 Annotation actualAnnotation = annotationMap.get(JsonDeserialize.class);
 Assert.assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 JsonDeserialize jsonDeserialize1 = new JsonDeserialize();
 JsonDeserialize jsonDeserialize2 = new JsonDeserialize();
 JsonPOJOBuilder jsonPOJOBuilder = new JsonPOJOBuilder();
 JsonSubTypes jsonSubTypes = new JsonSubTypes();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(jsonDeserialize2);
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(jsonDeserialize1);
 primaryAnnotationMap.add(jsonPOJOBuilder);
 primaryAnnotationMap.add(jsonSubTypes);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 List<Annotation> actualAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
 List<Annotation> expectedAnnotations = new ArrayList<>();
 expectedAnnotations.add(jsonDeserialize1);
 expectedAnnotations.add(jsonDeserialize2);
 expectedAnnotations.add(jsonPOJOBuilder);
 expectedAnnotations.add(jsonSubTypes);
 Assert.assertArrayEquals(expectedAnnotations.stream().mapToLong(Annotation -> Annotation.annotationType().hashCode()).toArray(), actualAnnotations.stream().mapToLong(Annotation -> Annotation.annotationType().hashCode()).toArray());
 }

 @Test
 public void testAddAnnotationIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 Annotation alreadyPresentAnnotation = new JsonDeserialize();
 boolean addResult = annotationMap.addIfNotPresent(jsonDeserialize);
 Assert.assertTrue(addResult);
 Assert.assertEquals(jsonDeserialize, annotationMap.get(JsonDeserialize.class));
 addResult = annotationMap.addIfNotPresent(alreadyPresentAnnotation);
 Assert.assertFalse(addResult);
 }

 @Test
 public void testAddAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 boolean addResult = annotationMap.add(jsonDeserialize);
 Assert.assertTrue(addResult);
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = new AnnotationMap();
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 annotationMap.add(jsonDeserialize);
 int actualSize = annotationMap.size();
 int expectedSize = 1;
 Assert.assertEquals(expectedSize, actualSize);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 Assert.assertNull(annotationMap.get(null));
 }

 @Test(expected = ClassCastException.class)
 public void testGetWrongTypeAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 Object wrongTypeAnnotation = new Object();
 Assert.assertNull(annotationMap.get(wrongTypeAnnotation.getClass()));
 }
}