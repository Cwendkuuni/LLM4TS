import static org.junit.Assert.*;
import wheel.components.*;
import wheel.util.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("new ArrayList<String>()");
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("toString()");

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals("Size of the objects collection should match the getOptionCount()", objects.size(), dynamicSelectModel.getOptionCount());
    }
}