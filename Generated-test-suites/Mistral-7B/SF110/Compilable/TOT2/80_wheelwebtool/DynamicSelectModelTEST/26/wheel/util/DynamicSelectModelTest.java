package wheel.util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import wheel.util.DynamicSelectModel;
import java.util.Arrays;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection("Arrays.asList(1, 2, 3, 4)")
                .label("toString")
                .value("toString");
        int optionCount = dynamicSelectModel.getOptionCount();
        assertEquals(4, optionCount);
    }
}