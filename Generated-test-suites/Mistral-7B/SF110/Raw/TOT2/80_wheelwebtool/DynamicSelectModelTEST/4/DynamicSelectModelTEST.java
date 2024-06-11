import static org.junit.Assert.*;
import wheel.util.DynamicSelectModel;
import java.util.Arrays;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("Arrays.asList(1, 2, 3)"), new ElExpression("toString"), new ElExpression("toString"));
        int count = model.getOptionCount();
        assertEquals(3, count);
    }
}