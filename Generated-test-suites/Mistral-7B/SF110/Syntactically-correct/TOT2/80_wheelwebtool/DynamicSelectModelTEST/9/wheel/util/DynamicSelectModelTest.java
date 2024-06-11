import static org.junit.Assert.*;
import wheel.components.*;
import wheel.util.DynamicSelectModel;
import java.util.*;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("new ArrayList<String>()"), new ElExpression("toString"), new ElExpression("toString"));
        Collection collection = new ArrayList<>();
        collection.add("Option 1");
        collection.add("Option 2");
        collection.add("Option 3");

        dynamicSelectModel.setObjects(collection);

        int optionCount = dynamicSelectModel.getOptionCount();

        assertEquals(optionCount, 3);
    }
}