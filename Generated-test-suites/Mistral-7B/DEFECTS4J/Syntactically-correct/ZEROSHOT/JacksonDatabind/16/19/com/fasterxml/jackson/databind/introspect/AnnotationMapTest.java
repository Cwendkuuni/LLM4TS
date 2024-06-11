package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestAnnotation {

}

public class AnnotationMapTest {

 @Test
 public void testEmptyAnnotationMap() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertTrue(annotationMap.size() == 0);
 assertNull(annotationMap.get(TestAnnotation.class));
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 TestAnnotation testAnnotation = new TestAnnotation();
 annotationMap.add(testAnnotation);
 assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 TestAnnotation testAnnotation1 = new TestAnnotation();
 TestAnnotation testAnnotation2 = new TestAnnotation();
 assertTrue(annotationMap.addIfNotPresent(testAnnotation1));
 assertFalse(annotationMap.addIfNotPresent(testAnnotation1));
 assertTrue(annotationMap.addIfNotPresent(testAnnotation2));
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = new AnnotationMap();
 TestAnnotation testAnnotation1 = new TestAnnotation();
 TestAnnotation testAnnotation2 = new TestAnnotation();
 assertTrue(annotationMap.add(testAnnotation1));
 assertEquals(testAnnotation1, annotationMap.get(TestAnnotation.class));
 assertTrue(annotationMap.add(testAnnotation2));
 assertEquals(testAnnotation2, annotationMap.get(TestAnnotation.class));
 }

 @Test
 public void testMerge() {
 AnnotationMap annotationMap1 = new AnnotationMap();
 AnnotationMap annotationMap2 = new AnnotationMap();
 TestAnnotation testAnnotation1 = new TestAnnotation();
 TestAnnotation testAnnotation2 = new TestAnnotation();
 TestAnnotation testAnnotation3 = new TestAnnotation();
 annotationMap1.add(testAnnotation1);
 annotationMap2.add(testAnnotation2);
 annotationMap2.add(testAnnotation3);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 assertEquals(3, mergedAnnotationMap.size());
 assertEquals(testAnnotation1, mergedAnnotationMap.get(TestAnnotation.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 TestAnnotation testAnnotation1 = new TestAnnotation();
 TestAnnotation testAnnotation2 = new TestAnnotation();
 annotationMap.add(testAnnotation1);
 annotationMap.add(testAnnotation2);
 List<Annotation> annotations = (List<Annotation>)annotationMap.annotations();
 assertEquals(2, annotations.size());
 assertEquals(testAnnotation1, annotations.get(0));
 assertEquals(testAnnotation2, annotations.get(1));
 }

}