package com.fasterxml.jackson.databind.introspect.test;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(MyAnnotation.class));
        Assert.assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap.add(myAnnotation1);
        annotationMap.add(myAnnotation2);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
        Assert.assertEquals(myAnnotation2, annotations.get(1));
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap2.add(myAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, mergedAnnotations.size());
        Assert.assertEquals(myAnnotation1, mergedAnnotations.get(0));
        Assert.assertEquals(myAnnotation2, mergedAnnotations.get(1));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        Assert.assertFalse(annotationMap.addIfNotPresent(myAnnotation1));
        Assert.assertTrue(annotationMap.addIfNotPresent(myAnnotation2));
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
        Assert.assertEquals(myAnnotation2, annotations.get(1));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        Assert.assertFalse(annotationMap.add(myAnnotation));
        Assert.assertNull(annotationMap.get(MyAnnotation.class));

        annotationMap = new AnnotationMap();
        annotationMap.add(myAnnotation);
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap.add(myAnnotation1);
        annotationMap.add(myAnnotation2);
        Assert.assertEquals("{@com.fasterxml.jackson.databind.introspect.AnnotationMap$Annotation@15db9742, @com.fasterxml.jackson.databind.introspect.AnnotationMap$Annotation@15db9740}", annotationMap.toString());
    }

    @Test
    public void testEmptyListWhenNoAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMergeWhenOneNull() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(new MyAnnotation());

        AnnotationMap annotationMap2 = null;

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        Assert.assertEquals(annotationMap1, mergedAnnotationMap);
    }

    @Test
    public void testMergeWhenBothNull() {
        AnnotationMap annotationMap1 = null;
        AnnotationMap annotationMap2 = null;

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        Assert.assertNull(mergedAnnotationMap);
    }

    @Test
    public void testAnnotationMapWithMultipleSameAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        annotationMap.add(myAnnotation);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(myAnnotation, annotations.get(0));
        Assert.assertEquals(myAnnotation, annotations.get(1));
    }

    @Test
    public void testMergeWithSameAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap2.add(myAnnotation2);
        annotationMap2.add(myAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, mergedAnnotations.size());
        Assert.assertEquals(myAnnotation1, mergedAnnotations.get(0));
        Assert.assertEquals(myAnnotation1, mergedAnnotations.get(1));
    }

    @Test
    public void testSizeWithEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSizeWithNonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentWithNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        Assert.assertFalse(annotationMap.addIfNotPresent(null));
        Assert.assertNull(annotationMap.get(MyAnnotation.class));
        Assert.assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddWithNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        Assert.assertFalse(annotationMap.add(null));
        Assert.assertNull(annotationMap.get(MyAnnotation.class));
        Assert.assertEquals(0, annotationMap.size());
    }

    private static class MyAnnotation {}
}