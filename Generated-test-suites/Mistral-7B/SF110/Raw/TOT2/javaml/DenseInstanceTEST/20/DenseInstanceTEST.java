import static org.junit.Assert.*;

import net.sf.javaml.core.*;
import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(classValue, denseInstance.getClassValue());
    }

    // Add test cases for other methods here

}