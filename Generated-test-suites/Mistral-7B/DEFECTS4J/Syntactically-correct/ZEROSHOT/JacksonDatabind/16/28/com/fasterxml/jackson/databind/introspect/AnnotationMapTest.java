package com.fasterxml.jackson.databind.introspect.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    private AnnotationMap annotationMap1;
    private AnnotationMap annotationMap2;
    private AnnotationMap mergedAnnotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
        annotationMap1 = new AnnotationMap();
        annotationMap2 = new AnnotationMap();
    }

    @After
    public void teardown() {
        annotationMap = null;
        annotationMap1 = null;
        annotationMap2 = null;
        mergedAnnotationMap = null;
    }

    @Test
    public void testNullAnnotationMap() {
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testEmptyAnnotationMap() {
        assertNotNull(annotationMap);
        assertTrue(annotationMap.size() == 0);
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testGetAnnotation() {
        annotationMap.add(new MyAnnotation());
        Annotation myAnnotation = annotationMap.get(MyAnnotation.class);
        assertNotNull(myAnnotation);
        assertEquals(MyAnnotation.class, myAnnotation.annotationType());
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullAnnotationType() {
        annotationMap.get(null);
    }

    @Test
    public void testGetAnnotationThatIsNotPresent() {
        assertNull(annotationMap.get(OtherAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        annotationMap.add(new MyAnnotation());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(1, annotations.size());
        assertNotNull(annotations.get(0));
        assertEquals(MyAnnotation.class, annotations.get(0).annotationType());
        assertTrue(annotations.contains(annotations.get(0)));
        assertTrue(annotations.isEmpty() || annotations.containsAll(annotationMap.annotations()));
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertNotNull(mergedAnnotationMap);
        assertTrue(mergedAnnotationMap.size() == 0);
        assertTrue(mergedAnnotationMap.annotations().isEmpty());
    }

    @Test
    public void testMergeOneAnnotationMap() {
        annotationMap1.add(new MyAnnotation());
        mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertNotNull(mergedAnnotationMap);
        assertEquals(1, mergedAnnotationMap.size());
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(1, mergedAnnotations.size());
        assertNotNull(mergedAnnotations.get(0));
        assertEquals(MyAnnotation.class, mergedAnnotations.get(0).annotationType());
    }

    @Test
    public void testMergeTwoAnnotationMapsWithSameAnnotation() {
        annotationMap1.add(new MyAnnotation());
        annotationMap2.add(new MyAnnotation());
        mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertNotNull(mergedAnnotationMap);
        assertEquals(1, mergedAnnotationMap.size());
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(1, mergedAnnotations.size());
        assertNotNull(mergedAnnotations.get(0));
        assertEquals(MyAnnotation.class, mergedAnnotations.get(0).annotationType());
    }

    @Test
    public void testMergeTwoAnnotationMapsWithDifferentAnnotations() {
        annotationMap1.add(new MyAnnotation());
        annotationMap2.add(new OtherAnnotation());
        mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertNotNull(mergedAnnotationMap);
        assertEquals(2, mergedAnnotationMap.size());
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(1, mergedAnnotations.size()); // one is common
        assertNotNull(mergedAnnotations.get(0));
        assertEquals(MyAnnotation.class, mergedAnnotations.get(0).annotationType());
        assertEquals(1, mergedAnnotations.size() - mergedAnnotations.indexOf(mergedAnnotations.get(0)));
        assertNotNull(mergedAnnotations.get(1));
        assertEquals(OtherAnnotation.class, mergedAnnotations.get(1).annotationType());
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());
        annotationMap.add(new MyAnnotation());
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        annotationMap.addIfNotPresent(new MyAnnotation());
        assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
    }

    @Test
    public void testAdd() {
        annotationMap.add(new MyAnnotation());
        assertTrue(annotationMap.add(new MyAnnotation()));
        assertTrue(annotationMap.add(new MyAnnotation()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateAnnotationType() {
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new MyAnnotation());
    }

    @Test
    public void testToString() {
        annotationMap.add(new MyAnnotation());
        assertEquals("{class com.fasterxml.jackson.databind.introspect.test.MyAnnotation=Annotation@2f34e0}", annotationMap.toString());
    }
}

class MyAnnotation {}
class OtherAnnotation {}