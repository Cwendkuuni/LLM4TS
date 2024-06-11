import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSchema;
import com.fasterxml.jackson.databind.annotation.JsonValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

public class AnnotationMapTest {

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();
 annotationMap.add(myAnnotation);
 Annotation expectedAnnotation = myAnnotation;
 Annotation actualAnnotation = annotationMap.get(MyAnnotation.class);
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertNull(annotationMap.get(NullAnnotation.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation annotation1 = new MyAnnotation();
 JsonDeserialize annotation2 = new JsonDeserialize();
 JsonPOJOBuilder annotation3 = new JsonPOJOBuilder();
 JsonSchema annotation4 = new JsonSchema();
 JsonValue annotation5 = new JsonValue();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 annotationMap.add(annotation3);
 annotationMap.add(annotation4);
 annotationMap.add(annotation5);
 List<Annotation> expectedAnnotations = Arrays.asList(annotation1, annotation2, annotation3, annotation4, annotation5);
 List<Annotation> actualAnnotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testMerge() {
 AnnotationMap annotationMap1 = new AnnotationMap();
 MyAnnotation1 annotation1 = new MyAnnotation1();
 annotationMap1.add(annotation1);
 AnnotationMap annotationMap2 = new AnnotationMap();
 MyAnnotation2 annotation2 = new MyAnnotation2();
 annotationMap2.add(annotation2);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 Annotation expectedAnnotation1 = annotation1;
 Annotation expectedAnnotation2 = annotation2;
 List<Annotation> expectedAnnotations = Arrays.asList(annotation1, annotation2);
 List<Annotation> actualAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
 assertEquals(expectedAnnotation1, actualAnnotations.get(0));
 assertEquals(expectedAnnotation2, actualAnnotations.get(1));
 assertEquals(expectedAnnotations.size(), actualAnnotations.size());
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation annotation1 = new MyAnnotation();
 annotationMap.add(annotation1);
 MyAnnotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation2);
 int expectedSize = 2;
 int actualSize = annotationMap.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation annotation = new MyAnnotation();
 boolean expectedAdded = true;
 boolean actualAdded = annotationMap.addIfNotPresent(annotation);
 assertTrue(expectedAdded);
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation annotation = new MyAnnotation();
 boolean expectedAdded = true;
 boolean actualAdded = annotationMap.add(annotation);
 assertTrue(expectedAdded);
 }

 @Test(expected = IllegalArgumentException.class)
 @Target(ElementType.METHOD)
 @Retention(RetentionPolicy.RUNTIME)
 public static @interface MyAnnotation {
 }

 @Test(expected = IllegalArgumentException.class)
 @Target(ElementType.TYPE)
 @Retention(RetentionPolicy.RUNTIME)
 @interface MyAnnotation1 {
 }

 @Test(expected = NullPointerException.class)
 @Target(ElementType.METHOD)
 @Retention(RetentionPolicy.RUNTIME)
 public static class MyAnnotation2 {
 }

 @Test(expected = NullPointerException.class)
 @Target(ElementType.TYPE)
 @Retention(RetentionPolicy.RUNTIME)
 public static class MyAnnotation3 {
 }
}