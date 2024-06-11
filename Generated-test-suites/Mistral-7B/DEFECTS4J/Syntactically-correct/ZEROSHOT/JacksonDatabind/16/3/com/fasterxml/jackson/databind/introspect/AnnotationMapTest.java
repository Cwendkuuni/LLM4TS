import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class AnnotationMapTest {

    @Test
    public void testAnnotationMapInstance() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(Object.class));
        Assert.assertFalse(annotationMap.isEmpty());
        Assert.assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);
        Assert.assertNotNull(retrievedAnnotation);
        Assert.assertEquals(myAnnotation, retrievedAnnotation);
    }

    @Test
    public void testEmptyListAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotationListWithOneAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(1, annotations.size());
        Assert.assertEquals(myAnnotation, annotations.get(0));
    }

    @Test
    public void testAnnotationListWithMultipleAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(annotation1, annotations.get(0));
        Assert.assertEquals(annotation2, annotations.get(1));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        Assert.assertEquals(1, annotationMap.size());

        AnnotationMap anotherAnnotationMap = new AnnotationMap();
        anotherAnnotationMap.add(myAnnotation);
        Assert.assertEquals(1, anotherAnnotationMap.size());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, anotherAnnotationMap);
        Assert.assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        Assert.assertFalse(annotationMap.addIfNotPresent(myAnnotation));
        Assert.assertNull(annotationMap.get(MyAnnotation.class));

        annotationMap.clear();
        Assert.assertTrue(annotationMap.addIfNotPresent(myAnnotation));
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        Assert.assertTrue(annotationMap.add(myAnnotation));
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));

        AnnotationMap anotherAnnotationMap = new AnnotationMap();
        MyAnnotation anotherMyAnnotation = new MyAnnotation();
        Assert.assertTrue(anotherAnnotationMap.add(anotherMyAnnotation));
        Assert.assertEquals(anotherMyAnnotation, anotherAnnotationMap.get(MyAnnotation.class));

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, anotherAnnotationMap);
        Assert.assertEquals(myAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        String toString = annotationMap.toString();
        Assert.assertTrue(toString.contains("class com.AnnotationMapTest$MyAnnotation"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        Iterator<Annotation> iterator = annotationMap.annotations().iterator();
        iterator.remove();
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        Assert.assertSame(annotationMap1, mergedAnnotationMap);
    }

    @Test
    public void testMergeNullAnnotationMap() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = null;
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        Assert.assertSame(annotationMap1, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap2.add(myAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
        Assert.assertEquals(myAnnotation2, annotations.get(1));
    }

    @Test
    public void testMergeAnnotationMapsWithDifferentAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        DifferentAnnotation differentAnnotation = new DifferentAnnotation();
        annotationMap2.add(differentAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
        Assert.assertEquals(differentAnnotation, annotations.get(1));
    }

    @Test
    public void testMergeAnnotationMapsWithOneEmptyAnnotationMap() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(1, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
    }

    @Test
    public void testMergeAnnotationMapsWithOneNullAnnotationMap() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = null;
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(1, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
    }

    @Test
    public void testMergeAnnotationMapsWithOneEmptyAnnotationMapAndOneNonEmptyAnnotationMap() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        DifferentAnnotation differentAnnotation = new DifferentAnnotation();
        annotationMap2.add(differentAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(myAnnotation1, annotations.get(0));
        Assert.assertEquals(differentAnnotation, annotations.get(1));
    }

    private static class MyAnnotation {}

    private static class DifferentAnnotation {}

}