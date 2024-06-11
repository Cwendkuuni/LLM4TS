import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.util.Arrays.asList;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap annotationMap1;
 private AnnotationMap annotationMap2;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setUp() {
 annotationMap = new AnnotationMap();
 annotationMap1 = new AnnotationMap();
 annotationMap2 = new AnnotationMap();
 mergedAnnotationMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // test getting annotation that exists
 Annotation testAnnotation = new TestAnnotation();
 annotationMap.add(testAnnotation);
 Annotation retrievedAnnotation = annotationMap.get(TestAnnotation.class);
 assertNotNull(retrievedAnnotation);
 assertEquals(testAnnotation, retrievedAnnotation);

 // test getting annotation that does not exist
 assertNull(annotationMap.get(Object.class));
 }

 @Test
 public void testAnnotations() {
 // test empty annotations list
 List<Annotation> annotationsList = annotationMap.annotations();
 assertTrue(annotationsList.isEmpty());

 // test annotations list with one annotation
 annotationMap1.add(new TestAnnotation());
 mergedAnnotationMap = AnnotationMap.merge(annotationMap, annotationMap1);
 annotationsList = mergedAnnotationMap.annotations();
 assertEquals(1, annotationsList.size());

 // test annotations list with multiple annotations
 annotationMap2.add(new TestAnnotation());
 annotationMap2.add(new TestAnnotation2());
 mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 annotationsList = mergedAnnotationMap.annotations();
 assertEquals(2, annotationsList.size());

 // test annotations list with duplicate annotations
 annotationMap1.add(new TestAnnotation());
 annotationMap1.add(new TestAnnotation());
 mergedAnnotationMap = AnnotationMap.merge(annotationMap, annotationMap1);
 annotationsList = mergedAnnotationMap.annotations();
 assertEquals(1, annotationsList.size());
 }

 @Test
 public void testSize() {
 List<Annotation> annotationsList = new ArrayList<>();
 annotationsList.add(new TestAnnotation());
 AnnotationMap annotationMap = new AnnotationMap();
 for (Annotation annotation : annotationsList) {
 annotationMap.add(annotation);
 }
 assertEquals(annotationsList.size(), annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 Annotation testAnnotation = new TestAnnotation();
 Annotation testAnnotation2 = new TestAnnotation();
 // test adding existing annotation
 annotationMap.add(testAnnotation);
 assertFalse(annotationMap.addIfNotPresent(testAnnotation));

 // test adding new annotation
 assertFalse(annotationMap.addIfNotPresent(testAnnotation2));
 annotationMap.add(testAnnotation2);
 assertTrue(annotationMap.addIfNotPresent(testAnnotation2));
 }

 @Test
 public void testAdd() {
 Annotation testAnnotation = new TestAnnotation();
 Annotation testAnnotation2 = new TestAnnotation();
 // test adding existing annotation
 annotationMap.add(testAnnotation);
 assertFalse(annotationMap.add(testAnnotation));

 // test adding new annotation
 assertFalse(annotationMap.add(testAnnotation2));
 annotationMap.add(testAnnotation2);
 assertNotNull(annotationMap.get(TestAnnotation2.class));
 }
}

 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 @interface TestAnnotation {
 }

 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 @interface TestAnnotation2 {
 }