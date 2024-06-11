import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ParserPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CoreOperationCompareTest {

    private Expression left;
    private Expression right;
    private CoreOperationCompare operation;

    public CoreOperationCompareTest(String leftXPath, String rightXPath) {
        ParserPool parserPool = new ParserPool();
        try {
            this.left = parserPool.parseExpression(leftXPath);
            this.right = parserPool.parseExpression(rightXPath);
            this.operation = new CoreOperationCompare(this.left, this.right);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Parameterized.Parameters(name = "{index}: compare({0}) == {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/test/a[1]", "/test/a[1]"},
                {"/test/a[1]", "/test/a[2]"},
                {"/test/a[1]/b[1]", "/test/a[1]/b[2]"},
                {"/test/a[1]/b[1]", "/test/a[2]/b[1]"},
                {"/test/a[1]/b[1]", "/test/a[2]/b[2]"},
                {"/test/a[1]/b[1]", "/test/a[2]/c[1]"},
                {"/test/a[1]/b[1]", "/test/c[1]"},
                {"/test/a[1]/b[1]", null},
                {null, "/test/a[1]/b[1]"},
                {"1", "1"},
                {"1", "2"},
                {"1", "1.0"},
                {"1", "1.0"},
                {"1", "true"},
                {"1", "false"},
                {"1", "NaN"},
                {"NaN", "NaN"},
                {"\"Hello\"", "\"Hello\""},
                {"\"Hello\"", "\"World\""},
                {"\"Hello\"", "Hi"},
                {"\"Hello\"", null},
                {null, "\"Hello\""},
                {"true", "true"},
                {"true", "false"},
                {"false", "false"},
                {"false", "true"},
                {new Integer(1), new Integer(1)},
                {new Integer(1), new Integer(2)},
                {new Integer(1), new Double(1.0)},
                {new Integer(1), new Double(1.0)},
                {new Integer(1), new Boolean(true)},
                {new Integer(1), new Boolean(false)},
                {new Boolean(true), new Boolean(true)},
                {new Boolean(true), new Boolean(false)},
                {new Boolean(false), new Boolean(false)},
                {new Boolean(false), new Boolean(true)}
        });
    }

    @Test
    public void testCompare() throws EvaluationException {
        JXPathContext context = new JXPathContext(Collections.emptyMap());
        assertTrue("Compare equal expressions", operation.evaluate(context));
        assertFalse("Compare not equal expressions", operation.evaluate(context));
    }

    @Test(expected = EvaluationException.class)
    public void testError() throws EvaluationException {
        JXPathContext context = new JXPathContext(Collections.emptyMap());
        operation.evaluate(context);
    }
}