package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.AnnotationMap;

public class AnnotationMapTest {

 private static <A extends Annotation> Annotation createMockAnnotation(Class<A> annotationType) {
 return (Annotation) new Object() {
 };
 }

 @Test
 public void testEmptyAnnotationMap() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertEquals(0, annotationMap.size());
 assertNull(annotationMap.get(JsonDeserialize.class));
 }

 @Test
 public void testAnnotationMapWithSingleAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(createMockAnnotation(JsonDeserialize.class));
 assertEquals(1, annotationMap.size());
 assertEquals(createMockAnnotation(JsonDeserialize.class), annotationMap.get(JsonDeserialize.class));
 }

 @Test
 public void testAnnotationMapWithMultipleAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(createMockAnnotation(JsonDeserialize.class));
 annotationMap.add(createMockAnnotation(JsonPOJOBuilder.class));
 assertEquals(2, annotationMap.size());
 List<Annotation> annotations = annotationMap.annotations();
 assertEquals(2, annotations.size());
 assertTrue(annotations.contains(createMockAnnotation(JsonDeserialize.class)));
 assertTrue(annotations.contains(createMockAnnotation(JsonPOJOBuilder.class)));
 }

 @Test
 public void testMergingAnnotationMaps() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(createMockAnnotation(JsonDeserialize.class));
 secondaryAnnotationMap.add(createMockAnnotation(JsonPOJOBuilder.class));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(2, mergedAnnotationMap.size());
 List<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
 assertEquals(2, mergedAnnotations.size());
 assertTrue(mergedAnnotations.contains(createMockAnnotation(JsonDeserialize.class)));
 assertTrue(mergedAnnotations.contains(createMockAnnotation(JsonPOJOBuilder.class)));
 }

 @Test
 public void testMergingAnnotationMapsWithDuplicateAnnotations() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(createMockAnnotation(JsonDeserialize.class));
 secondaryAnnotationMap.add(createMockAnnotation(JsonDeserialize.class));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(1, mergedAnnotationMap.size());
 List<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
 assertEquals(1, mergedAnnotations.size());
 assertTrue(mergedAnnotations.contains(createMockAnnotation(JsonDeserialize.class)));
 }

 @Test
 public void testMergingAnnotationMapsWhenPrimaryIsEmpty() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(createMockAnnotation(JsonDeserialize.class));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(1, mergedAnnotationMap.size());
 List<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
 assertEquals(1, mergedAnnotations.size());
 assertTrue(mergedAnnotations.contains(createMockAnnotation(JsonDeserialize.class)));
 }

 @Test
 public void testMergingAnnotationMapsWhenSecondaryIsEmpty() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(createMockAnnotation(JsonDeserialize.class));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(1, mergedAnnotationMap.size());
 List<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
 assertEquals(1, mergedAnnotations.size());
 assertTrue(mergedAnnotations.contains(createMockAnnotation(JsonDeserialize.class)));
 }

 @Test
 public void testMergingAnnotationMapsWithNullAnnotationMaps() {
 AnnotationMap primaryAnnotationMap = null;
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(createMockAnnotation(JsonDeserialize.class));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(1, mergedAnnotationMap.size());
 List<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
 assertEquals(1, mergedAnnotations.size());
 assertTrue(mergedAnnotations.contains(createMockAnnotation(JsonDeserialize.class)));
 }

 @Test
 public void testMergingAnnotationMapsWithNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(createMockAnnotation(JsonDeserialize.class));
 try {
 annotationMap.add(null);
 fail("Should not be able to add a null annotation");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetAnnotationMapSize() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(createMockAnnotation(JsonDeserialize.class));
 annotationMap.add(createMockAnnotation(JsonPOJOBuilder.class));
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testGetAnnotationMapIterable() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(createMockAnnotation(JsonDeserialize.class));
 annotationMap.add(createMockAnnotation(JsonPOJOBuilder.class));
 List<Annotation> annotations = annotationMap.annotations();
 assertEquals(2, annotations.size());
 }

 @Test
 public void testGetAnnotationMapToString() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(createMockAnnotation(JsonDeserialize.class));
 annotationMap.add(createMockAnnotation(JsonPOJOBuilder.class));
 String actualToString = annotationMap.toString();
 String expectedToString = "[class com.fasterxml.jackson.databind.util.AnnotationMap@3d341c6a]"; // Different hashCode but same class and size
 assertTrue(actualToString.contains(expectedToString));
 }
}