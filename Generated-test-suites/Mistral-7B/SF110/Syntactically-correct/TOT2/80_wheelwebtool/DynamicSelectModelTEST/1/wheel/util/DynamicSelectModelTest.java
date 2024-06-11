package wheel.util;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import wheel.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel().collection(Arrays.asList("Option 1", "Option 2", "Option 3")).label("toString").value("toString");
        int optionCount = model.getOptionCount();

        assertEquals(3, optionCount);
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel().collection(Arrays.asList("Option 1: Label 1", "Option 2: Label 2", "Option 3: Label 3")).label("toString").value("toString");
        String label = model.getLabel(0);

        assertEquals("Option 1: Label 1", label);
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel().collection(Arrays.asList("Option 1: Value 1", "Option 2: Value 2", "Option 3: Value 3")).label("toString").value("toString");
        String value = model.getValue(0);

        assertEquals("Value 1", value);
    }

    @Test
    public void testGetObjectsEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel().collection(new ArrayList<>());
        Collection objects = model.getObjects();

        assertNull(objects);
    }

    @Test
    public void testGetObjectsNullObjects() {
        DynamicSelectModel model = new DynamicSelectModel().collection(new ArrayList<>()).add(null);
        Collection objects = model.getObjects();

        assertFalse(objects.isEmpty());
        assertNull(objects.iterator().next());
    }

    @Test
    public void testGetObjectsMixedTypes() {
        DynamicSelectModel model = new DynamicSelectModel().collection(Arrays.asList("String", 1, new Object()));

        try {
            model.getObjects();
            fail("Should have thrown an exception");
        } catch (WheelException e) {
            // Expected
        }
    }
}