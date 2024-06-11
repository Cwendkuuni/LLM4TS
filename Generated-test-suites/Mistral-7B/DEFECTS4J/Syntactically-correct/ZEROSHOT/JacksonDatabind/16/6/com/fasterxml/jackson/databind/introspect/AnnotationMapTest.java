package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.AnnotationMap;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MyAnnotation {
}

public class AnnotationMapTest {

    @Test
    public void testCreateAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertSame(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetEmptyAnnotationList() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap.annotations());
        assertTrue(annotationMap.annotations().isEmpty());
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

        Set<MyAnnotation> mergedAnnotationSet = new HashSet<>(annotationMap1.annotations());
        mergedAnnotationSet.add(annotation2);
        List<Annotation> mergedAnnotationList = new ArrayList<>(mergedAnnotationSet);

        assertEquals(mergedAnnotationList, Arrays.asList(annotation1, annotation2));
    }

    @Test
    public void testMergeAnnotationMapsWhenPrimaryIsEmpty() {
        AnnotationMap annotationMap1 = new AnnotationMap();

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        assertEquals(annotationMap2, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMapsWhenSecondaryIsEmpty() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        assertEquals(annotationMap1, mergedAnnotationMap);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap.add(annotation1);

        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation2);

        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();

        assertTrue(annotationMap.addIfNotPresent(annotation1));
        assertEquals(annotation1, annotationMap.get(MyAnnotation.class));
        assertFalse(annotationMap.addIfNotPresent(annotation1));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();

        assertTrue(annotationMap.add(annotation1));
        assertEquals(annotation1, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap.add(annotation1);
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation2);

        String expected = "[Lcom.fasterxml.jackson.databind.util.Annotation$AnnotationMap$Annotation;@"
                + System.identityHashCode(annotationMap.get(MyAnnotation.class))
                + ", Lcom.fasterxml.jackson.databind.util.Annotation$AnnotationMap$Annotation;@"
                + System.identityHashCode(annotationMap.get(MyAnnotation.class)) + "]";
        assertEquals(expected, annotationMap.toString());
    }

    @Test
    public void testCreateAnnotationMapFromMap() {
        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation annotation1 = new MyAnnotation();
        annotations.put(MyAnnotation.class, annotation1);

        AnnotationMap annotationMap = new AnnotationMap(annotations);
        assertSame(annotation1, annotationMap.get(MyAnnotation.class));
    }
}