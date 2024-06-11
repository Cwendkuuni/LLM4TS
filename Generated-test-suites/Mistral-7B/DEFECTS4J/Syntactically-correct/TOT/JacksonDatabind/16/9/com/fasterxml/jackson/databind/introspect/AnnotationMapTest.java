import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;

import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setup() {
 annotationMap = new AnnotationMap();
 mergedAnnotationMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Add annotation to annotationMap
 annotationMap.add(new JsonDeserialize(Int.class));
 // Get annotation from annotationMap
 Annotation ann = annotationMap.get(JsonDeserialize.class);
 // Check if the annotation is the same instance as added
 assertTrue(ann instanceof JsonDeserialize);
 assertEquals(JsonDeserialize.class, ann.annotationType());
 }

 @Test
 public void testAnnotations() {
 // Add multiple annotations to annotationMap
 annotationMap.add(new JsonDeserialize(Int.class));
 annotationMap.add(new JsonTypeInfo(JsonType.ID.CLASS));
 // Get all annotations from annotationMap
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 // Check if the annotations are the same instances as added
 for (Annotation ann : annotations) {
 assertTrue(ann instanceof JsonDeserialize || ann instanceof JsonTypeInfo);
 }
 }

 @Test
 public void testMerge() {
 // Add annotations to both annotationMap and mergedAnnotationMap
 annotationMap.add(new JsonDeserialize(Int.class));
 mergedAnnotationMap.add(new JsonTypeInfo(JsonType.ID.CLASS));
 mergedAnnotationMap.add(new JsonDeserialize(Double.class));
 // Merge annotationMaps and check if mergedAnnotationMap contains all annotations from both
 AnnotationMap merged = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
 List<Annotation> mergedAnnotations = new ArrayList<>(merged.annotations());
 // Check if mergedAnnotations contains all annotations from both annotationMaps
 for (Annotation ann : mergedAnnotations) {
 assertTrue(ann instanceof JsonDeserialize || ann instanceof JsonTypeInfo);
 }
 }

 @Test
 public void testSize() {
 // Check size of empty AnnotationMap
 assertEquals(0, new AnnotationMap().size());
 // Add annotations to AnnotationMap and check size
 annotationMap.add(new JsonDeserialize(Int.class));
 annotationMap.add(new JsonTypeInfo(JsonType.ID.CLASS));
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 // Add existing annotation to AnnotationMap
 annotationMap.add(new JsonDeserialize(Int.class));
 // Try to add existing annotation again
 assertFalse(annotationMap.addIfNotPresent(new JsonDeserialize(Int.class)));
 // Add new annotation to AnnotationMap
 annotationMap.add(new JsonTypeInfo(JsonType.ID.FIELD));
 // Try to add existing annotation again
 assertFalse(annotationMap.addIfNotPresent(new JsonDeserialize(Int.class)));
 // Try to add new annotation
 assertTrue(annotationMap.addIfNotPresent(new JsonTypeInfo(JsonType.ID.FIELD)));
 }

 @Test
 public void testAdd() {
 // Add existing annotation to AnnotationMap
 annotationMap.add(new JsonDeserialize(Int.class));
 // Try to add existing annotation again
 assertFalse(annotationMap.add(new JsonDeserialize(Int.class)));
 // Add new annotation to AnnotationMap
 assertTrue(annotationMap.add(new JsonTypeInfo(JsonType.ID.FIELD)));
 }
}