
package org.jcvi.jillion.assembly.consed.phd;

import org.junit.*;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        // Initialize any static resources if needed
    }

    @AfterClass
    public static void tearDownAfterClass() {
        // Clean up any static resources if needed
    }

    @Before
    public void setUp() {
        // Initialize any instance variables if needed
    }

    @After
    public void tearDown() {
        // Clean up any instance variables if needed
    }

    @Test
    public void testDefaultPhdReadTagInitialization() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, phdTag.getType());
        assertEquals(source, phdTag.getSource());
        assertEquals(ungappedRange, phdTag.getUngappedRange());
        assertEquals(date, phdTag.getDate());
        assertEquals(comment, phdTag.getComment());
        assertEquals(freeFormData, phdTag.getFreeFormData());
    }

    @Test
    public void testGettersWithValidValues() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, phdTag.getType());
        assertEquals(source, phdTag.getSource());
        assertEquals(ungappedRange, phdTag.getUngappedRange());
        assertEquals(date, phdTag.getDate());
        assertEquals(comment, phdTag.getComment());
        assertEquals(freeFormData, phdTag.getFreeFormData());
    }

    @Test
    public void testGettersWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertNull(phdTag.getType());
        assertNull(phdTag.getSource());
        assertNull(phdTag.getUngappedRange());
        assertNull(phdTag.getDate());
        assertNull(phdTag.getComment());
        assertNull(phdTag.getFreeFormData());
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";
        DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";
        DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertEquals(phdTag1, phdTag3);
        assertNotEquals(phdTag1, phdTag2);

        int hashCode1 = phdTag1.hashCode();
        int hashCode2 = phdTag2.hashCode();
        int hashCode3 = phdTag3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }
}
