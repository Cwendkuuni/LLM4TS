import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTagBuilder;
import static org.mockito.Mockito.*;
import java.time.*;
import java.util.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testgetType() {
        String type = "testType";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        String actual = phdReadTag.getType();
        assertEquals(type, actual);
    }
    
    // ... (other basic test cases)
    
    @Test(expected = NullPointerException.class)
    public void testNullType() {
        String type = null;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        String source = null;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, null, null, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullDate() {
        Date date = null;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullComment() {
        String comment = null;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, comment, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFreeFormData() {
        String freeFormData = null;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
    }
    
    @Test
    public void testEmptyComment() {
        String comment = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        String actual = phdReadTag.getComment();
        assertEquals(comment, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDate() {
        Date date = new Date("Invalid date");
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
    }
    
    @Test
    public void testOverlappingRanges() {
        Range range1 = new Range(1, 10);
        Range range2 = new Range(5, 15);
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(null, null, range1, null, null, null);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(null, null, range2, null, null, null);
        boolean actual = phdReadTag1.equals(phdReadTag2);
        assertFalse(actual);
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("testType1", "testSource1", new Range(1, 10), new Date(LocalDateTime.now()), "", "");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("testType2", "testSource2", new Range(11, 20), new Date(LocalDateTime.now()), "", "");
        int phdReadTag1HashCode = phdReadTag1.hashCode();
        int phdReadTag2HashCode = phdReadTag2.hashCode();
        assertNotEquals(phdReadTag1HashCode, phdReadTag2HashCode);
    }
    
    @Test
    public void testEquals() {
        Range range1 = new Range(1, 10);
        Range range2 = new Range(1, 10);
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("testType1", "testSource1", range1, new Date(LocalDateTime.now()), "", "");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("testType1", "testSource1", range2, new Date(LocalDateTime.now()), "", "");
        boolean actual = phdReadTag1.equals(phdReadTag2);
        assertTrue(actual);
    }
    
    @Test
    public void testEqualsDifferentObjects() {
        Range range1 = new Range(1, 10);
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("testType1", "testSource1", range1, new Date(LocalDateTime.now()), "", "");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("testType2", "testSource2", range1, new Date(LocalDateTime.now()), "", "");
        boolean actual = phdReadTag1.equals(phdReadTag2);
        assertFalse(actual);
    }
    
    @Test
    public void testEqualsSameInstance() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("testType1", "testSource1", new Range(1, 10), new Date(LocalDateTime.now()), "", "");
        boolean actual = phdReadTag.equals(phdReadTag);
        assertTrue(actual);
    }
}