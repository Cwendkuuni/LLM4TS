import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setUp() {
 annotationMap = new AnnotationMap();
 mergedAnnotationMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Test getting annotation with get method
 JsonDeserialize annotation1 = new JsonDeserialize();
 annotationMap.add(annotation1);
 Annotation actualAnnotation = annotationMap.get(JsonDeserialize.class);
 Annotation expectedAnnotation = annotation1;
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testIterableAnnotations() {
 // Test getting all annotations with annotations() method
 JsonDeserialize annotation1 = new JsonDeserialize();
 JsonSerialize annotation2 = new JsonSerialize();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 Set<Annotation> actualAnnotations = new HashSet<>(annotationMap.annotations());
 Set<Annotation> expectedAnnotations = new HashSet<>();
 expectedAnnotations.add(annotation1);
 expectedAnnotations.add(annotation2);
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testMergeAnnotationMaps() {
 // Test merging two AnnotationMaps
 JsonDeserialize annotation1 = new JsonDeserialize();
 JsonDeserialize annotation2 = new JsonDeserialize();
 JsonSerialize annotation3 = new JsonSerialize();
 annotationMap.add(annotation1);
 mergedAnnotationMap.add(annotation2);
 mergedAnnotationMap.add(annotation3);
 AnnotationMap mergedAnnotationMapExpected = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
 Set<Annotation> mergedAnnotationMapActualAnnotations = new HashSet<>(mergedAnnotationMapExpected.annotations());
 Set<Annotation> mergedAnnotationMapExpectedAnnotations = new HashSet<>();
 mergedAnnotationMapExpectedAnnotations.add(annotation1);
 mergedAnnotationMapExpectedAnnotations.add(annotation2);
 mergedAnnotationMapExpectedAnnotations.add(annotation3);
 assertEquals(mergedAnnotationMapExpectedAnnotations, mergedAnnotationMapActualAnnotations);
 }

 @Test
 public void testSize() {
 // Test getting size of AnnotationMap
 JsonDeserialize annotation1 = new JsonDeserialize();
 JsonDeserialize annotation2 = new JsonDeserialize();
 JsonSerialize annotation3 = new JsonSerialize();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 annotationMap.add(annotation3);
 int actualSize = annotationMap.size();
 int expectedSize = 3;
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testAddIfNotPresent() {
 // Test adding annotation with addIfNotPresent method
 JsonDeserialize annotation1 = new JsonDeserialize();
 annotationMap.addIfNotPresent(annotation1);
 Annotation existingAnnotation = annotationMap.get(JsonDeserialize.class);
 Annotation newAnnotation = new JsonDeserialize();
 boolean isAdded = annotationMap.addIfNotPresent(newAnnotation);
 Annotation newAnnotationFromMap = annotationMap.get(JsonDeserialize.class);
 assertTrue(isAdded);
 assertEquals(existingAnnotation, newAnnotationFromMap);
 }

 @Test
 public void testAdd() {
 // Test adding annotation with add method
 JsonDeserialize annotation1 = new JsonDeserialize();
 annotationMap.add(annotation1);
 int initialSize = annotationMap.size();
 Annotation newAnnotation = new JsonDeserialize();
 annotationMap.add(newAnnotation);
 int finalSize = annotationMap.size();
 assertEquals(initialSize + 1, finalSize);
 }

 @Test
 public void testToString() {
 // Test toString method
 JsonDeserialize annotation1 = new JsonDeserialize();
 JsonDeserialize annotation2 = new JsonDeserialize();
 JsonSerialize annotation3 = new JsonSerialize();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 annotationMap.add(annotation3);
 String actualToString = annotationMap.toString();
 String expectedToString = "{" +
         "[class com.fasterxml.jackson.databind.annotation.JsonDeserialize], " +
         "[class com.fasterxml.jackson.databind.annotation.JsonDeserialize], " +
         "[class com.fasterxml.jackson.databind.annotation.JsonSerialize]" +
         "}";
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testNullAnnotationMap() {
 // Test null annotation map cases
 AnnotationMap nullAnnotationMap = null;
 assertNull(nullAnnotationMap.get(JsonDeserialize.class));
 assertFalse(nullAnnotationMap.annotations().isEmpty());
 }

 @Test
 public void testEmptyAnnotationMap() {
 // Test empty annotation map cases
 AnnotationMap emptyAnnotationMap = new AnnotationMap();
 assertNull(emptyAnnotationMap.get(JsonDeserialize.class));
 assertTrue(emptyAnnotationMap.annotations().isEmpty());
 }

 @Test
 public void testAddSameAnnotationType() {
 // Test adding same annotation type to AnnotationMap
 JsonDeserialize annotation1 = new JsonDeserialize();
 JsonDeserialize annotation2 = new JsonDeserialize();
 annotationMap.add(annotation1);
 AnnotationMap newAnnotationMap = new AnnotationMap();
 newAnnotationMap.add(annotation1);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, newAnnotationMap);
 Set<Annotation> mergedAnnotationMapActualAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
 Set<Annotation> mergedAnnotationMapExpectedAnnotations = new HashSet<>();
 mergedAnnotationMapExpectedAnnotations.add(annotation1);
 assertEquals(mergedAnnotationMapExpectedAnnotations, mergedAnnotationMapActualAnnotations);
 }

 @Test
 public void testAddSameAnnotationInstance() {
 // Test adding same annotation instance to AnnotationMap
 JsonDeserialize annotation1 = new JsonDeserialize();
 AnnotationMap annotationMap1 = new AnnotationMap();
 annotationMap1.add(annotation1);
 AnnotationMap annotationMap2 = new AnnotationMap();
 annotationMap2.add(annotation1);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 Set<Annotation> mergedAnnotationMapActualAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
 Set<Annotation> mergedAnnotationMapExpectedAnnotations = new HashSet<>();
 mergedAnnotationMapExpectedAnnotations.add(annotation1);
 assertEquals(mergedAnnotationMapExpectedAnnotations, mergedAnnotationMapActualAnnotations);
 }

 @Test
 public void testGetNonExistentAnnotation() {
 // Test getting non-existent annotation
 JsonDeserialize nonExistentAnnotation = new JsonDeserialize();
 AnnotationMap annotationMap = new AnnotationMap();
 assertNull(annotationMap.get(JsonSerialize.class));
 }

 @Test
 public void testAddAnnotationWithRetentionPOLICYVALUE() {
 // Test adding annotation with RetentionPolicy.SOURCE
 @Retention(RetentionPolicy.SOURCE)
 @Target(ElementType.METHOD)
 Annotation annotation = new MyAnnotation();
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(annotation);
 int initialSize = annotationMap.size();
 Annotation newAnnotation = new MyAnnotation();
 annotationMap.add(newAnnotation);
 int finalSize = annotationMap.size();
 assertEquals(initialSize + 1, finalSize);
 }

 @Test
 public void testAddAnnotationWithRetentionPOLICYCLASS() {
 // Test adding annotation with RetentionPolicy.CLASS
 @Retention(RetentionPolicy.CLASS)
 @Target(ElementType.METHOD)
 Annotation annotation = new MyAnnotation();
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(annotation);
 int initialSize = annotationMap.size();
 Annotation newAnnotation = new MyAnnotation();
 annotationMap.add(newAnnotation);
 int finalSize = annotationMap.size();
 assertEquals(initialSize + 1, finalSize);
 }

 @Test
 public void testAddAnnotationWithRetentionRUNTIME() {
 // Test adding annotation with RetentionPolicy.RUNTIME
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 Annotation annotation = new MyAnnotation();
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(annotation);
 int initialSize = annotationMap.size();
 Annotation newAnnotation = new MyAnnotation();
 annotationMap.add(newAnnotation);
 int finalSize = annotationMap.size();
 assertEquals(initialSize + 1, finalSize);
 }
}