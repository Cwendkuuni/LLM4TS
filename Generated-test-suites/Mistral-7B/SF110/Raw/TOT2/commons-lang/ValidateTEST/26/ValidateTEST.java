
package org.apache.commons.lang3;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ValidateTest {
    @Test
    public void testIsTrueWithBoolExpressionAndMessage() {
        Validate.isTrue(true, "Message: %s");
    }

    @Test
    public void testIsTrueWithBoolExpressionAndMessageAndValidValue() {
        Validate.isTrue(true, "Message with long value: %d", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBoolExpressionAndFalseValue() {
        Validate.isTrue(false, "Message: %s");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBoolExpressionAndEmptyString() {
        Validate.isTrue(true, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBoolExpressionAndNullValue() {
        Validate.isTrue(true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBoolExpressionAndNegativeLongValue() {
        Validate.isTrue(true, "Message with long value: %d", -1L);
    }

    @Test
    public void testIsTrueWithBoolExpressionOnly() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBoolExpressionAndFalseValueOnly() {
        Validate.isTrue(false);
    }
}