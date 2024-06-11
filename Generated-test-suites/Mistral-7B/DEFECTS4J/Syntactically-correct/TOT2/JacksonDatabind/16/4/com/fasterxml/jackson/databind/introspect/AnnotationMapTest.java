import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AnnotationMapTest {
    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        Class<MyAnnotation> myAnnotationClass = MyAnnotation.class;
        MyAnnotation expected = myAnnotation;
        MyAnnotation actual = annotationMap.get(myAnnotationClass);

        assertEquals(expected, actual);
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
}