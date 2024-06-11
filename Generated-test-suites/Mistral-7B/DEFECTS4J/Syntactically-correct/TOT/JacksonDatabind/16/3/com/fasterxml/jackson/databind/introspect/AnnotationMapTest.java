import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setup() {
 @JsonSerialize(using = MyJsonSerializer.class)
 @JsonDeserialize(using = MyJsonDeserializer.class)
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 private static class MyAnnotation extends Annotation {}

 annotationMap = new AnnotationMap();
 mergedAnnotationMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 MyAnnotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 Annotation expected = annotation;
 Annotation actual = annotationMap.get(MyAnnotation.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testAnnotations() {
 MyAnnotation annotation1 = new MyAnnotation();
 MyAnnotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 List<Annotation> expected = Arrays.asList(annotation1, annotation2);
 List<Annotation> actual = new ArrayList<>(annotationMap.annotations());
 assertEquals(expected, actual);
 }

 @Test
 public void testMergeEmpty() {
 AnnotationMap primary = null;
 AnnotationMap secondary = mergedAnnotationMap;
 AnnotationMap actual = AnnotationMap.merge(primary, secondary);
 assertTrue(secondary._annotations != null && !secondary._annotations.isEmpty());
 assertEquals(secondary, actual);
 }

 @Test
 public void testMergePrimaryEmpty() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = mergedAnnotationMap;
 AnnotationMap actual = AnnotationMap.merge(primary, secondary);
 assertTrue(secondary._annotations != null && !secondary._annotations.isEmpty());
 assertEquals(secondary, actual);
 }

 @Test
 public void testMergeEqual() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = mergedAnnotationMap;
 secondary.add(new MyAnnotation());
 AnnotationMap actual = AnnotationMap.merge(primary, secondary);
 assertTrue(primary._annotations != null && !primary._annotations.isEmpty());
 assertEquals(primary, actual);
 }

 @Test
 public void testMergeSecondaryOverrides() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new JsonSerialize(MySerializer.class) {});
 AnnotationMap actual = AnnotationMap.merge(primary, secondary);
 assertTrue(secondary._annotations != null && !secondary._annotations.isEmpty());
 assertEquals(secondary, actual);
 }

 @Test
 public void testSize() {
 MyAnnotation annotation1 = new MyAnnotation();
 MyAnnotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 int expected = 2;
 int actual = annotationMap.size();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddIfNotPresent() {
 MyAnnotation annotation = new MyAnnotation();
 boolean added = annotationMap.addIfNotPresent(annotation);
 assertTrue(added);
 MyAnnotation existingAnnotation = new MyAnnotation();
 annotationMap.add(existingAnnotation);
 boolean notAdded = annotationMap.addIfNotPresent(annotation);
 assertFalse(notAdded);
 }

 @Test
 public void testAdd() {
 MyAnnotation annotation = new MyAnnotation();
 boolean added = annotationMap.add(annotation);
 assertTrue(added);
 MyAnnotation existingAnnotation = new MyAnnotation();
 annotationMap.add(existingAnnotation);
 boolean notAdded = annotationMap.add(annotation);
 assertFalse(notAdded);
 }

 @Test
 public void testToString() {
 MyAnnotation annotation1 = new MyAnnotation();
 MyAnnotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 String expected = "{class com.fasterxml.jackson.databind.annotation.JsonSerialize, class com.fasterxml.jackson.databind.annotation.JsonDeserialize, class com.fasterxml.jackson.databind.annotation.JsonSerialize$MyJsonSerializer, class com.fasterxml.jackson.databind.annotation.JsonDeserialize$MyJsonDeserializer}";
 String actual = annotationMap.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testNullConstructor() {
 AnnotationMap nullAnnotationMap = null;
 List<Annotation> nullList = null;
 try {
 AnnotationMap mergeResult = AnnotationMap.merge(annotationMap, nullAnnotationMap);
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // Expected
 }
 try {
 List<Annotation> mergedAnnotations = AnnotationMap.merge(nullList, mergedAnnotationMap.annotations());
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testEmptyAnnotationMap() {
 AnnotationMap emptyAnnotationMap = new AnnotationMap();
 List<Annotation> nullList = null;
 try {
 AnnotationMap mergeResult = AnnotationMap.merge(annotationMap, emptyAnnotationMap);
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // Expected
 }
 try {
 List<Annotation> mergedAnnotations = AnnotationMap.merge(mergedAnnotationMap.annotations(), nullList);
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testEmptySecondaryAnnotationMap() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap emptySecondaryAnnotationMap = new AnnotationMap();
 AnnotationMap actual = AnnotationMap.merge(primary, emptySecondaryAnnotationMap);
 assertTrue(primary._annotations != null && !primary._annotations.isEmpty());
 assertEquals(primary, actual);
 }

 @Test
 public void testEmptyPrimaryAnnotationMap() {
 AnnotationMap secondary = new AnnotationMap();
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap actual = AnnotationMap.merge(primary, secondary);
 assertTrue(secondary._annotations != null && !secondary._annotations.isEmpty());
 assertEquals(secondary, actual);
 }

 @Test
 public void testMergeWithNullAnnotation() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(null);
 try {
 AnnotationMap mergeResult = AnnotationMap.merge(primary, secondary);
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testMergeWithNullAnnotationValue() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new MyAnnotation() {});
 secondary._annotations.put(MyAnnotation.class, null);
 try {
 AnnotationMap mergeResult = AnnotationMap.merge(primary, secondary);
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}