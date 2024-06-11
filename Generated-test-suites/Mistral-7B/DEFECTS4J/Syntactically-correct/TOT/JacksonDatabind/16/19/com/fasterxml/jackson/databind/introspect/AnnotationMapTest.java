package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonProperty.class));
        assertTrue(annotationMap.annotations().isEmpty());
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertNotNull(annotationMap.get(MyAnnotation.class));
        assertEquals(1, annotationMap.size());
        List<Annotation> annotations = Arrays.asList(myAnnotation);
        assertTrue(annotationMap.annotations().containsAll(annotations));
    }

    @Test
    public void testAddSameAnnotationTwice() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertFalse(annotationMap.add(myAnnotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        MyAnnotation primaryAnnotation1 = new MyAnnotation();
        MyAnnotation primaryAnnotation2 = new MyAnnotation();

        MyAnnotation secondaryAnnotation1 = new MyAnnotation();
        MyAnnotation secondaryAnnotation2 = new MyAnnotation();

        primaryAnnotationMap.add(primaryAnnotation1);
        primaryAnnotationMap.add(primaryAnnotation2);

        secondaryAnnotationMap.add(secondaryAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        // Merged map should contain both primary and secondary annotations
        assertEquals(4, mergedAnnotationMap.size());
        List<Annotation> mergedAnnotations = Arrays.asList(primaryAnnotation1, primaryAnnotation2, secondaryAnnotation1);
        assertTrue(mergedAnnotationMap.annotations().containsAll(mergedAnnotations));
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(0, mergedAnnotationMap.size());
    }

    @Test
    public void testMergePrimaryNotNullSecondaryNull() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(1, mergedAnnotationMap.size());
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergePrimaryNullSecondaryNotNull() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(1, mergedAnnotationMap.size());
        assertEquals(secondaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotationByType() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);

        assertSame(myAnnotation, retrievedAnnotation);
    }

    @Test
    public void testGetAnnotationByTypeNull() {
        AnnotationMap annotationMap = new AnnotationMap();

        assertNull(annotationMap.get(JsonUnknownAnnotation.class));
    }

    @Test
    public void testGetAnnotationByTypeAbsent() {
        AnnotationMap annotationMap = new AnnotationMap();

        assertNull(annotationMap.get(JsonProperty.class));
    }

    @Test
    public void testIterableAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap.add(myAnnotation1);
        annotationMap.add(myAnnotation2);

        List<Annotation> annotations = Arrays.asList(myAnnotation1, myAnnotation2);

        Iterable<Annotation> annotationIterable = annotationMap.annotations();

        assertTrue(annotationIterable.containsAll(annotations));
    }

    @Test
    public void testEmptyListAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        Iterable<Annotation> annotationIterable = annotationMap.annotations();

        assertTrue(annotationIterable.isEmpty());
        assertTrue(annotationIterable.contains(null));
    }

    @Test
    public void testSizeAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testSizeAnnotationMapEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();

        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testToStringAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        String annotationMapString = annotationMap.toString();

        assertTrue(annotationMapString.startsWith("["));
        assertTrue(annotationMapString.endsWith("]"));
    }

    @Test
    public void testToStringAnnotationMapEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();

        String annotationMapString = annotationMap.toString();

        assertTrue(annotationMapString.equals("[null]"));
    }
}