package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.databind.util.AnnotationMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap am1;
 private AnnotationMap am2;
 private AnnotationMap am3;
 private AnnotationMap mergedMap;

 @Before
 public void setUp() {
 am1 = new AnnotationMap();
 am2 = new AnnotationMap();
 am3 = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 MyAnnotation annotation1 = new MyAnnotation();
 MyAnnotation annotation2 = new MyAnnotation();
 am1.add(annotation1);
 am2.add(annotation2);

 assertEquals(annotation1, am1.get(MyAnnotation.class));
 assertEquals(annotation2, am2.get(MyAnnotation.class));
 assertEquals(null, am1.get(SomeOtherAnnotation.class));
 }

 @Test
 public void testMerge() {
 MyAnnotation annotation1 = new MyAnnotation();
 MyAnnotation annotation2 = new MyAnnotation();
 MyAnnotation annotation3 = new MyAnnotation();
 am1.add(annotation1);
 am2.add(annotation2);
 am3.add(annotation3);

 mergedMap = AnnotationMap.merge(am1, am2);
 mergedMap = AnnotationMap.merge(mergedMap, am3);

 List<Annotation> annotations = new ArrayList<>(mergedMap.annotations());
 assertEquals(3, annotations.size());
 assertEquals(annotation1, annotations.get(0));
 assertEquals(annotation2, annotations.get(1));
 assertEquals(annotation3, annotations.get(2));
 }

 @Test
 public void testSize() {
 AnnotationMap am = new AnnotationMap();
 assertEquals(0, am.size());
 am.add(new MyAnnotation());
 assertEquals(1, am.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap am = new AnnotationMap();
 MyAnnotation annotation = new MyAnnotation();
 assertTrue(am.addIfNotPresent(annotation));
 assertFalse(am.addIfNotPresent(annotation));
 }

 @Test
 public void testAdd() {
 AnnotationMap am = new AnnotationMap();
 MyAnnotation annotation = new MyAnnotation();
 assertTrue(am.add(annotation));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnableToModifyMap() {
 AnnotationMap am = new AnnotationMap();
 am.add(new MyAnnotation());
 try {
 am.add(new MyAnnotation());
 } catch (UnsupportedOperationException e) {
 // pass
 }
 }

 @Test
 public void testToString() {
 AnnotationMap am = new AnnotationMap();
 am.add(new MyAnnotation());
 am.add(new MyAnnotation());
 String stringRepresentation = am.toString();
 assertTrue(stringRepresentation.contains("AnnotationMap"));
 assertTrue(stringRepresentation.contains("MyAnnotation"));
 }

 @Test
 public void testNullAnnotationMap() {
 AnnotationMap am = null;
 assertNull(am.get(MyAnnotation.class));
 }

 @Test
 public void testEmptyAnnotationMap() {
 AnnotationMap am = new AnnotationMap();
 assertFalse(am.annotations().hasNext());
 }

 @Test
 public void testGetAnnotationFromEmptyMap() {
 AnnotationMap am = new AnnotationMap();
 assertNull(am.get(MyAnnotation.class));
 }

 @Test
 public void testGetAnnotationFromNullMap() {
 AnnotationMap am = null;
 assertNull(am.get(MyAnnotation.class));
 }

 @Test
 public void testGetAnnotationTypeMismatch() {
 AnnotationMap am = new AnnotationMap();
 am.add(new MyAnnotation());
 assertNull(am.get(SomeOtherAnnotation.class));
 }

 @Test
 public void testGetAnnotationFromMapWithMultipleAnnotations() {
 AnnotationMap am = new AnnotationMap();
 MyAnnotation annotation1 = new MyAnnotation();
 MyAnnotation annotation2 = new MyAnnotation();
 am.add(annotation1);
 am.add(annotation2);
 assertEquals(annotation1, am.get(MyAnnotation.class));
 assertEquals(annotation2, am.get(MyAnnotation.class));
 }

 @Test
 public void testMergeEmptyMaps() {
 AnnotationMap am1 = new AnnotationMap();
 AnnotationMap am2 = new AnnotationMap();
 AnnotationMap mergedMap = AnnotationMap.merge(am1, am2);
 assertEquals(am1, mergedMap);
 }

 @Test
 public void testMergeOneEmptyOneNonEmptyMap() {
 AnnotationMap am1 = new AnnotationMap();
 AnnotationMap am2 = new AnnotationMap();
 MyAnnotation annotation = new MyAnnotation();
 am2.add(annotation);
 AnnotationMap mergedMap = AnnotationMap.merge(am1, am2);
 List<Annotation> mergedAnnotations = new ArrayList<>(mergedMap.annotations());
 assertEquals(1, mergedAnnotations.size());
 assertEquals(annotation, mergedAnnotations.get(0));
 }

 @Test
 public void testMergeNonEmptyOneEmptyOneMap() {
 AnnotationMap am1 = new AnnotationMap();
 MyAnnotation annotation = new MyAnnotation();
 am1.add(annotation);
 AnnotationMap am2 = new AnnotationMap();
 AnnotationMap mergedMap = AnnotationMap.merge(am1, am2);
 List<Annotation> mergedAnnotations = new ArrayList<>(mergedMap.annotations());
 assertEquals(1, mergedAnnotations.size());
 assertEquals(annotation, mergedAnnotations.get(0));
 }

 @Test
 public void testMergeTwoNonEmptyMaps() {
 AnnotationMap am1 = new AnnotationMap();
 MyAnnotation annotation1 = new MyAnnotation();
 am1.add(annotation1);
 AnnotationMap am2 = new AnnotationMap();
 MyAnnotation annotation2 = new MyAnnotation();
 am2.add(annotation2);
 AnnotationMap mergedMap = AnnotationMap.merge(am1, am2);
 List<Annotation> mergedAnnotations = new ArrayList<>(mergedMap.annotations());
 assertEquals(2, mergedAnnotations.size());
 assertEquals(annotation1, mergedAnnotations.get(0));
 assertEquals(annotation2, mergedAnnotations.get(1));
 }
}