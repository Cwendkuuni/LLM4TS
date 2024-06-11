import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap annotationMapSecondary;
 private AnnotationMap annotationMapPrimary;
 private Annotation jacksonAnnotation1;
 private Annotation jacksonAnnotation2;
 private Annotation jacksonAnnotation3;
 private Annotation customAnnotation;

 @Before
 public void setUp() {
 jacksonAnnotation1 = new MyJacksonAnnotation1();
 jacksonAnnotation2 = new MyJacksonAnnotation2();
 jacksonAnnotation3 = new MyJacksonAnnotation3();
 customAnnotation = new CustomAnnotation();
 annotationMap = new AnnotationMap();
 annotationMapSecondary = new AnnotationMap();
 annotationMapPrimary = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 annotationMap.add(jacksonAnnotation1);
 assertEquals(jacksonAnnotation1, annotationMap.get(MyJacksonAnnotation1.class));
 }

 @Test
 public void testGetNullAnnotation() {
 assertNull(annotationMap.get(Object.class));
 }

 @Test
 public void testAnnotations() {
 annotationMap.add(jacksonAnnotation1);
 annotationMap.add(jacksonAnnotation2);
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertEquals(jacksonAnnotation1, annotations.get(0));
 assertEquals(jacksonAnnotation2, annotations.get(1));
 }

 @Test
 public void testMergeEmptyAnnotationMaps() {
 AnnotationMap result = AnnotationMap.merge(null, null);
 assertNull(result);
 }

 @Test
 public void testMergePrimaryIsNull() {
 AnnotationMap result = AnnotationMap.merge(null, annotationMap);
 assertEquals(annotationMap, result);
 }

 @Test
 public void testMergeSecondaryIsNull() {
 AnnotationMap result = AnnotationMap.merge(annotationMap, null);
 assertEquals(annotationMap, result);
 }

 @Test
 public void testMergeAnnotationMaps() {
 annotationMap.add(jacksonAnnotation1);
 annotationMapSecondary.add(jacksonAnnotation2);
 annotationMapPrimary.add(jacksonAnnotation3);
 AnnotationMap result = AnnotationMap.merge(annotationMap, annotationMapSecondary);
 assertEquals(annotationMapPrimary, result);

 annotationMapPrimary.add(customAnnotation);
 AnnotationMap result2 = AnnotationMap.merge(annotationMapPrimary, annotationMap);
 assertEquals(annotationMapPrimary, result2);
 }

 @Test
 public void testSize() {
 assertEquals(0, annotationMap.size());
 annotationMap.add(jacksonAnnotation1);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 assertFalse(annotationMap.addIfNotPresent(jacksonAnnotation1));
 assertTrue(annotationMap.addIfNotPresent(jacksonAnnotation2));
 }

 @Test
 public void testAdd() {
 AnnotationMap map = new AnnotationMap();
 map.add(jacksonAnnotation1);
 map.add(jacksonAnnotation1);
 assertEquals(1, map.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddDuplicateAnnotationTypeException() {
 annotationMap.add(jacksonAnnotation1);
 annotationMap.add(jacksonAnnotation1);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 annotationMap.add(null);
 }
}