import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("new ArrayList<String>()");
        List<String> options = new ArrayList<String>() {{
            add("Option 1");
            add("Option 2");
            add("Option 3");
        }};
        dynamicSelectModel.objects = options;
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }
}