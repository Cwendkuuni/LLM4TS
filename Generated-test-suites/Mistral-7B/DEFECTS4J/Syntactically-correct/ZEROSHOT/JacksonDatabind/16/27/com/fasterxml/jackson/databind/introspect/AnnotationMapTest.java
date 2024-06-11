package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;

@SuppressWarnings("unchecked")
public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        Annotation annotation = annotationMap.get(MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals(annotation.annotationType(), MyAnnotation.class);
    }

    @Test
    public void testGetAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new MyOtherAnnotation());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(new MyAnnotation()));
        assertTrue(annotations.contains(new MyOtherAnnotation()));
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new MyAnnotation());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new MyOtherAnnotation());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(new MyAnnotation()));
        assertTrue(mergedAnnotations.contains(new MyOtherAnnotation()));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new MyOtherAnnotation());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));

        annotationMap.add(new MyOtherAnnotation());
        assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.add(new MyAnnotation()));

        annotationMap.add(new MyOtherAnnotation());
        assertTrue(annotationMap.add(new MyAnnotation()));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new MyOtherAnnotation());
        String toString = annotationMap.toString();
        assertTrue(toString.contains("[class com.fasterxml.jackson.databind.introspect.AnnotationMap@"));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MyAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MyOtherAnnotation {
    }
}