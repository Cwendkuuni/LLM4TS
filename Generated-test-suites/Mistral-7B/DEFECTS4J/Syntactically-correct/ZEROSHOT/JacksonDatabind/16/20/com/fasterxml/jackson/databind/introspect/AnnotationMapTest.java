package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@ interface TestAnnotation {
 String value();
}

public class AnnotationMapTest {

 @Test
 public void testDefaultConstruction() {
 AnnotationMap map = new AnnotationMap();
 assertEquals(0, map.size());
 assertNull(map.get(TestAnnotation.class));
 }

 @Test
 public void testGet() {
 AnnotationMap map = new AnnotationMap();
 map.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 Annotation ann = map.get(TestAnnotation.class);
 assertNotNull(ann);
 assertEquals("Val1", ann.value());
 }

 @Test
 public void testGetNull() {
 AnnotationMap map = new AnnotationMap();
 assertNull(map.get(TestAnnotation.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap map = new AnnotationMap();
 map.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map.add(new TestAnnotation() { @Override public String value() { return "Val2"; } });
 List<Annotation> annotations = new ArrayList<>(map.annotations());
 assertEquals(2, annotations.size());
 for (Annotation annotation : annotations) {
 assertInstanceOf(TestAnnotation.class, annotation);
 }
 }

 @Test
 public void testMergeEmpty() {
 AnnotationMap map1 = new AnnotationMap();
 AnnotationMap map2 = new AnnotationMap();
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(0, result.size());
 }

 @Test
 public void testMergeOneAnnotation() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new TestAnnotation() { @Override public String value() { return "Val2"; } });
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(2, result.size());
 Annotation ann1 = result.get(TestAnnotation.class);
 assertNotNull(ann1);
 assertEquals("Val1", ann1.value());
 Annotation ann2 = result.get(TestAnnotation.class);
 assertNotNull(ann2);
 assertEquals("Val2", ann2.value());
 }

 @Test
 public void testMergeSameAnnotations() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(1, result.size());
 Annotation ann = result.get(TestAnnotation.class);
 assertNotNull(ann);
 assertEquals("Val1", ann.value());
 }

 @Test
 public void testMergeSameAnnotationValue() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map2.add(new TestAnnotation() { @Override public String value() { return "Val2"; } });
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(2, result.size());
 Annotation ann1 = result.get(TestAnnotation.class);
 assertNotNull(ann1);
 assertEquals("Val1", ann1.value());
 Annotation ann2 = result.get(TestAnnotation.class);
 assertNotNull(ann2);
 assertEquals("Val2", ann2.value());
 }

 @Test
 public void testMergeSameAnnotationTypeButDifferentClasses() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new Annotation1() {});
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new Annotation2() {});
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(0, result.size());
 }

 @Test
 public void testMergeDifferentAnnotationTypesButSameClass() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new TestAnnotation1() {});
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new TestAnnotation2() {});
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(0, result.size());
 }

 @Test
 public void testMergeDifferentAnnotationTypesButSameClassValue() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new TestAnnotation1() {});
 map1.add(new TestAnnotation1() {});
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new TestAnnotation2() {});
 map2.add(new TestAnnotation1() {});
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(2, result.size());
 Annotation ann1 = result.get(TestAnnotation1.class);
 assertNotNull(ann1);
 assertNotNull(ann1.annotationType());
 Annotation ann2 = result.get(TestAnnotation1.class);
 assertNotNull(ann2);
 assertNotNull(ann2.annotationType());
 assertEquals(ann1.annotationType(), ann2.annotationType());
 }

 @Test
 public void testMergeDifferentAnnotationTypesButSameClassValueAndDifferentClasses() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new TestAnnotation1() {});
 map1.add(new TestAnnotation1() {});
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new TestAnnotation2() {});
 map2.add(new TestAnnotation1() {});
 AnnotationMap result = AnnotationMap.merge(map1, map2);
 assertEquals(3, result.size());
 Annotation ann1 = result.get(TestAnnotation1.class);
 assertNotNull(ann1);
 assertNotNull(ann1.annotationType());
 Annotation ann2 = result.get(TestAnnotation2.class);
 assertNotNull(ann2);
 assertNotNull(ann2.annotationType());
 }

 @Test
 public void testSize() {
 AnnotationMap map = new AnnotationMap();
 map.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map.add(new TestAnnotation() { @Override public String value() { return "Val2"; } });
 assertEquals(2, map.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap map = new AnnotationMap();
 map.addIfNotPresent(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map.addIfNotPresent(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map.addIfNotPresent(new TestAnnotation() { @Override public String value() { return "Val3"; } });
 assertEquals(2, map.size());
 Annotation ann1 = map.get(TestAnnotation.class);
 assertNotNull(ann1);
 assertEquals("Val1", ann1.value());
 Annotation ann2 = map.get(TestAnnotation.class);
 assertNotNull(ann2);
 assertEquals("Val1", ann2.value());
 Annotation ann3 = map.get(TestAnnotation.class);
 assertNotNull(ann3);
 assertEquals("Val3", ann3.value());
 }

 @Test
 public void testAdd() {
 AnnotationMap map = new AnnotationMap();
 map.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map.add(new TestAnnotation() { @Override public String value() { return "Val2"; } });
 map.add(new TestAnnotation() { @Override public String value() { return "Val3"; } });
 assertEquals(3, map.size());
 Annotation ann1 = map.get(TestAnnotation.class);
 assertNotNull(ann1);
 assertEquals("Val1", ann1.value());
 Annotation ann2 = map.get(TestAnnotation.class);
 assertNotNull(ann2);
 assertEquals("Val2", ann2.value());
 Annotation ann3 = map.get(TestAnnotation.class);
 assertNotNull(ann3);
 assertEquals("Val3", ann3.value());
 }

 @Test
 public void testToString() {
 AnnotationMap map = new AnnotationMap();
 map.add(new TestAnnotation() { @Override public String value() { return "Val1"; } });
 map.add(new TestAnnotation() { @Override public String value() { return "Val2"; } });
 assertEquals("[Lcom.fasterxml.jackson.databind.introspect$AnnotationMap;@41561822]", map.toString());
 }
}