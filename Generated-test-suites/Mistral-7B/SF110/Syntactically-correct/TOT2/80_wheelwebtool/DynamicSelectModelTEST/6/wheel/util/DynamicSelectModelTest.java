import static org.junit.Assert.*;
import wheel.util.DynamicSelectModel;
import java.util.ArrayList;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("new ArrayList<Object>()"), new ElExpression("toString"), new ElExpression("toString"));
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }
}