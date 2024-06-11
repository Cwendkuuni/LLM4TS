package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.*;

/**
 * Test class for {@link AnnotationMap}.
 */
public class AnnotationMapTest {

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        assertNull(annotationMap.get(MyAnnotation.class));
        assertNotNull(annotationMap.annotations());
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
        assertNotNull(annotationMap.annotations());
        assertFalse(annotationMap.annotations().isEmpty());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(annotation, annotations.get(0));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation1);
        annotationMap.addIfNotPresent(annotation2);
        assertEquals(2, annotationMap.size());
        assertEquals(annotation1, annotationMap.get(MyAnnotation.class));
        assertEquals(annotation2, annotationMap.get(MyAnnotation.class));
        assertNotNull(annotationMap.annotations());
        assertFalse(annotationMap.annotations().isEmpty());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
    }

    @Test
    public void testMergeAnnotationMaps() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(2, mergedAnnotationMap.size());
        assertEquals(annotation1, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(annotation2, mergedAnnotationMap.get(MyAnnotation.class));
        assertNotNull(mergedAnnotationMap.annotations());
        assertFalse(mergedAnnotationMap.annotations().isEmpty());
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals("{@" + MyAnnotation.class.getName() + "=[" + annotation + "]}", annotationMap.toString());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyAnnotation {
    }
}