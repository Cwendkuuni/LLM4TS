import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.ElementType;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedMap;

 @Before
 public void setUp() {
 annotationMap = new AnnotationMap();
 mergedMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Test getting annotation from empty map
 assertNull(annotationMap.get(JsonSerialize.class));

 // Test getting annotation from map with single annotation
 annotationMap.add(new JsonSerialize());
 assertEquals(annotationMap.get(JsonSerialize.class), new JsonSerialize());

 // Test getting annotation from map with multiple annotations
 annotationMap.add(new JsonDeserialize());
 assertEquals(annotationMap.get(JsonSerialize.class), new JsonSerialize());
 assertEquals(annotationMap.get(JsonDeserialize.class), new JsonDeserialize());
 }

 @Test
 public void testAnnotations() {
 // Test empty map
 assertEmpty(annotationMap.annotations());

 // Test map with single annotation
 annotationMap.add(new JsonSerialize());
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(1, annotations.size());
 assertEquals(JsonSerialize.class, annotations.get(0).annotationType());

 // Test map with multiple annotations
 mergedMap.add(new JsonSerialize());
 mergedMap.add(new JsonDeserialize());
 AnnotationMap merged = AnnotationMap.merge(annotationMap, mergedMap);
 annotations = new ArrayList<>(merged.annotations());
 assertEquals(2, annotations.size());
 assertEquals(JsonSerialize.class, annotations.get(0).annotationType());
 assertEquals(JsonDeserialize.class, annotations.get(1).annotationType());
 }

 @Test
 public void testSize() {
 // Test size of empty map
 assertEquals(0, annotationMap.size());

 // Test size of map with single annotation
 annotationMap.add(new JsonSerialize());
 assertEquals(1, annotationMap.size());

 // Test size of map with multiple annotations
 mergedMap.add(new JsonSerialize());
 mergedMap.add(new JsonDeserialize());
 merged = AnnotationMap.merge(annotationMap, mergedMap);
 assertEquals(2, merged.size());
 }

 @Test
 public void testAddIfNotPresent() {
 // Test adding annotation that doesn't exist
 annotationMap.addIfNotPresent(new JsonSerialize());
 assertTrue(annotationMap.addIfNotPresent(new JsonSerialize()));

 // Test adding annotation that already exists
 annotationMap.add(new JsonSerialize());
 assertFalse(annotationMap.addIfNotPresent(new JsonSerialize()));
 }

 @Test
 public void testAdd() {
 // Test adding annotation that doesn't exist
 annotationMap.add(new JsonSerialize());
 assertTrue(annotationMap.size() == 1);

 // Test adding annotation that already exists
 annotationMap.add(new JsonSerialize());
 assertFalse(annotationMap.add(new JsonSerialize()));
 assertFalse(annotationMap.size() == 2);
 }

 @Test
 public void testMerge() {
 // Test merging empty maps
 AnnotationMap merged1 = new AnnotationMap();
 AnnotationMap merged2 = new AnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(merged1, merged2);
 assertSame(merged1, merged);

 // Test merging maps with single annotation
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new JsonSerialize());
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new JsonDeserialize());
 merged = AnnotationMap.merge(map1, map2);
 assertEquals(2, merged.size());

 // Test merging maps with multiple annotations
 mergedMap.add(new JsonSerialize());
 mergedMap.add(new JsonDeserialize());
 merged = AnnotationMap.merge(annotationMap, mergedMap);
 assertEquals(3, merged.size());
 }
}