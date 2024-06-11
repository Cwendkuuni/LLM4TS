import org.junit.Test;
import static org.junit.Assert.assertEquals;
import wheel.util.DynamicSelectModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel ds = new DynamicSelectModel()
                .collection("new ArrayList<String>()")
                .label("")
                .value("");
        List<String> objects = Arrays.asList("Option 1", "Option 2", "Option 3");
        DynamicSelectModel dsWithObjects = ds.collection("objects")
                .label("toString()")
                .value("toString()")
                .objects(objects);
        assertEquals(3, dsWithObjects.getOptionCount());

        // Edge case: Empty collection
        DynamicSelectModel dsEmpty = new DynamicSelectModel()
                .collection("new ArrayList<>()")
                .label("")
                .value("");
        assertEquals(0, dsEmpty.getOptionCount());

        // Error case: Null collection
        DynamicSelectModel dsNull = new DynamicSelectModel()
                .collection(null)
                .label("")
                .value("");
        try {
            dsNull.getOptionCount();
            fail("Expected WheelException but got none");
        } catch (WheelException e) {
            // Expected
        }
    }
}