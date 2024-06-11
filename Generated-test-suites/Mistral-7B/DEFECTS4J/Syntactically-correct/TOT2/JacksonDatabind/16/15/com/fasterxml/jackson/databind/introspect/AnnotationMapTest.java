package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        assertEquals(annotation, annotationMap.get(MyAnnotation.class));

        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertNull(emptyAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        Collection<Annotation> annotations = annotationMap.annotations();
        assertNotNull(annotations);
        assertEquals(2, annotations.size());
        assertEquals(annotation1, annotations.iterator().next());
        assertEquals(annotation2, annotations.iterator().next());

        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        Collection<Annotation> emptyAnnotations = annotationMap.annotations();
        assertEquals(0, emptyAnnotations.size());
    }

    @Test
    public void testMerge() {
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap map2 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        map1.add(annotation1);
        map2.add(annotation2);

        AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);
        assertEquals(2, mergedMap.size());
        Map<Class<? extends Annotation>, Annotation> mergedAnnotations = mergedMap._annotations;
        assertEquals(annotation1, mergedAnnotations.get(MyAnnotation.class));
        assertEquals(annotation2, mergedAnnotations.get(MyAnnotation.class));

        AnnotationMap emptyMap1 = new AnnotationMap();
        AnnotationMap emptyMap2 = new AnnotationMap();
        AnnotationMap mergedEmptyMaps = AnnotationMap.merge(emptyMap1, emptyMap2);
        assertEquals(0, mergedEmptyMaps.size());

        AnnotationMap map3 = new AnnotationMap();
        map3.add(annotation1);
        AnnotationMap map4 = new AnnotationMap();
        AnnotationMap mergedMaps1And2 = AnnotationMap.merge(map3, map4);
        assertEquals(1, mergedMaps1And2.size());
        assertEquals(annotation1, mergedMaps1And2.get(MyAnnotation.class));

        AnnotationMap map5 = new AnnotationMap();
        map5.add(annotation2);
        AnnotationMap mergedMaps2And1 = AnnotationMap.merge(map5, map3);
        assertEquals(1, mergedMaps2And1.size());
        assertEquals(annotation2, mergedMaps2And1.get(MyAnnotation.class));
    }

    class MyAnnotation {}
}