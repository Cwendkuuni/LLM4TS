import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation ann = new MyAnnotation();
        map.add(ann);

        Annotation expected = ann;
        Annotation result = map.get(MyAnnotation.class);

        assertEquals(expected, result);
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap map = new AnnotationMap();

        Annotation result = map.get(SomeOtherAnnotation.class);

        assertNull(result);
    }
}