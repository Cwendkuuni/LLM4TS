package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.jxpath.ri.test.JXPathTestUtils.newJXPathContext;
import static org.apache.commons.jxpath.ri.test.JXPathTestUtils.newPath;

/**
 * Tests for {@link CoreOperationGreaterThan} class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThan() {
        // Test with valid double values
        EvaluationContext context = newJXPathContext();
        InfoSet infoSet = context.setSource(new Object[]{5.0, 3.0});
        Path path = newPath("$[a > b]");
        Assert.assertTrue(path.value(context, infoSet));

        // Test with equal double values
        context = newJXPathContext();
        infoSet = context.setSource(new Object[]{5.0, 5.0});
        path = newPath("$[a > b]");
        Assert.assertFalse(path.value(context, infoSet));

        // Test with invalid double values
        context = newJXPathContext();
        infoSet = context.setSource(new Object[]{Double.NaN, 3.0});
        path = newPath("$[a > b]");
        Assert.assertFalse(path.value(context, infoSet));

        context = newJXPathContext();
        infoSet = context.setSource(new Object[]{3.0, Double.NaN});
        path = newPath("$[a > b]");
        Assert.assertFalse(path.value(context, infoSet));

        context = newJXPathContext();
        infoSet = context.setSource(new Object[]{Double.NaN, Double.NaN});
        path = newPath("$[a > b]");
        Assert.assertFalse(path.value(context, infoSet));
    }
}