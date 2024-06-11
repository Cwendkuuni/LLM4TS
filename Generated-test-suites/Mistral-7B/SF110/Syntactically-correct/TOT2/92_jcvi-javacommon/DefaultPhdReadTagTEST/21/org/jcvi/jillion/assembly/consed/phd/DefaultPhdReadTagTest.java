import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.Range;
import org.jcvi.jillion.date.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testgetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2021-01-01"), "testComment", "testFreeFormData");
        assertEquals("testType", phdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2021-01-01"), "testComment", "testFreeFormData");
        assertEquals("testSource", phdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2021-01-01"), "testComment", "testFreeFormData");
        Range expectedRange = new Range(1, 10);
        assertEquals(expectedRange, phdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2021-01-01"), "testComment", "testFreeFormData");
        Date expectedDate = new Date("2021-01-01");
        assertEquals(expectedDate, phdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2021-01-01"), "testComment", "testFreeFormData");
        assertEquals("testComment", phdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2021-01-01"), "testComment", "testFreeFormData");
        assertEquals("testFreeFormData", phdReadTag.getFreeFormData());
    }
}