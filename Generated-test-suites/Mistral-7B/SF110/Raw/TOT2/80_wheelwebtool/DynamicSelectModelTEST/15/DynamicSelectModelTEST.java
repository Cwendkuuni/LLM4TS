package wheel.util;
import org.junit.Assert;
import org.junit.Test;
import wheel.*;
import java.util.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("new ArrayList<String>()");
        model.label("toString()");
        model.value("toString()");
        List<String> options = model.getObjects();
        assertEquals(0, model.getOptionCount());
        model.collection("new ArrayList<String>()").add("Option 1");
        model.collection("new ArrayList<String>()").add("Option 2");
        options = model.getObjects();
        assertEquals(2, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("new ArrayList<String>()");
        model.label("toString()");
        model.value("toString()");
        List<String> options = Arrays.asList("Option 1", "Option 2");
        model.setObjects(options);
        assertEquals("Option 1", model.getLabel(0));
        assertEquals("Option 2", model.getLabel(1));
        try {
            model.getLabel(2);
            fail("Index out of bounds exception expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("new ArrayList<String>()");
        model.label("toString()");
        model.value("toString()");
        assertEquals(0, model.getOptionCount());
        model.collection("new ArrayList<String>()").add("Option 1");
        model.collection("new ArrayList<String>()").add("Option 2");
        List<Object> options = model.getObjects();
        assertEquals(2, options.size());
        assertEquals("Option 1", options.get(0).toString());
        assertEquals("Option 2", options.get(1).toString());
        model.collection(null);
        try {
            model.getObjects();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetObjectsEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("new ArrayList<String>()");
        model.label("toString()");
        model.value("toString()");
        assertEquals(0, model.getOptionCount());
        List<Object> options = model.getObjects();
        assertEquals(0, options.size());
    }

    @Test
    public void testGetObjectsNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection(null);
        model.label("toString()");
        model.value("toString()");
        try {
            model.getObjects();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetObjectsNonStringElements() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("new ArrayList<Integer>()");
        model.label("toString()");
        model.value("toString()");
        try {
            model.getObjects();
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // Expected
        }
    }
}