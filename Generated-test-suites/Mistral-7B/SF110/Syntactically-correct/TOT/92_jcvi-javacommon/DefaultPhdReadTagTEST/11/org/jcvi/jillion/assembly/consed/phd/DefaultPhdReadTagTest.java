package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals("quality", phdTag.getType());
        Assert.assertEquals("Sanger", phdTag.getSource());
        Assert.assertEquals(new Range(1, 10), phdTag.getUngappedRange());
        Assert.assertEquals(new Date(2021, 1, 1), phdTag.getDate());
        Assert.assertEquals("This is a test comment", phdTag.getComment());
        Assert.assertEquals("Test data", phdTag.getFreeFormData());

        // Test hashCode and equals methods
        DefaultPhdReadTag sameTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), new Date(2021, 1, 1), "This is a test comment", "Test data");
        DefaultPhdReadTag differentTypeTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), new Date(2021, 1, 1), "This is a test comment", "Test data");
        DefaultPhdReadTag differentSourceTag = new DefaultPhdReadTag("quality", "Illumina", new Range(1, 10), new Date(2021, 1, 1), "This is a test comment", "Test data");
        DefaultPhdReadTag differentCommentTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), new Date(2021, 1, 1), "Different test comment", "Test data");
        DefaultPhdReadTag differentDataTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), new Date(2021, 1, 1), "This is a test comment", "Different test data");
        DefaultPhdReadTag nullCommentTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), new Date(2021, 1, 1), null, "Test data");
        DefaultPhdReadTag nullDateTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), null, "This is a test comment", "Test data");
        DefaultPhdReadTag nullDataTag = new DefaultPhdReadTag("quality", "Sanger", new Range(1, 10), new Date(2021, 1, 1), "This is a test comment", null);

        Assert.assertEquals(phdTag.hashCode(), sameTag.hashCode());
        Assert.assertTrue(phdTag.equals(sameTag));
        Assert.assertFalse(phdTag.equals(differentTypeTag));
        Assert.assertFalse(phdTag.equals(differentSourceTag));
        Assert.assertFalse(phdTag.equals(differentCommentTag));
        Assert.assertFalse(phdTag.equals(differentDataTag));
        Assert.assertFalse(phdTag.equals(nullCommentTag));
        Assert.assertFalse(phdTag.equals(nullDateTag));
        Assert.assertFalse(phdTag.equals(nullDataTag));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagCreationWithNullType() {
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagCreationWithNullSource() {
        String type = "quality";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagCreationWithNullUngappedRange() {
        String type = "quality";
        String source = "Sanger";
        Date date = new Date(2021, 1, 1);
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagCreationWithNullDate() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }
}