package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateValidNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test entities with semicolon
 String input1 = "&amp;#123; &#x1F4; &#x1F456; &#12345; &#xFFFF; &#x10FFFF;";
 String expectedOutput1 = "\u0001\u007B\u007D \uD835\uDC01 \uD835\uDCC6\uD835\uDCDF \u4E2D\u5341\u4E1C\u53F7 \uFFFF\uD83D\uDC9E\uD83D\uDC14\uD83D\uDC14\uD83D\uDC14";
 String output = StringUtils.EMPTY;
 try {
 out = new StringWriter();
 unescaper.translate(new StringReader(input1), 0, out);
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput1, out.toString());

 // Test entities without semicolon
 String input2 = "&amp;#123 &#x1F456 &#12345";
 String expectedOutput2 = "\u0001\u007B\u007D \uD835\uDCC6\uD835\uDCDF \u4E2D\u5341\u4E1C\u53F7";
 String output2 = StringUtils.EMPTY;
 try {
 out = new StringWriter();
 unescaper.translate(new StringReader(input2), 0, out);
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput2, out.toString());
 }

 @Test
 public void testTranslateInvalidNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test invalid entities with semicolon
 String input1 = "&amp;#a; &#xZ; &#x1F456z; &#12345; &#-1; &#1234z";
 String expectedErrorOutput1 = "FAIL: #a[" + 1 + "][" + 2 + "] FAIL: #xZ[" + 1 + "][" + 2 + "] FAIL: #1F456z[" + 3 + "][" + 4 + "] FAIL: #-1[" + 5 + "][" + 6 + "] FAIL: #1234z[" + 7 + "][" + 8 + "]";
 String output = StringUtils.EMPTY;
 try {
 out = new StringWriter();
 unescaper.translate(new StringReader(input1), 0, out);
 } catch (IOException e) {
 // Expected
 }
 assertEquals(expectedErrorOutput1, System.err.getErrorMessage());

 // Test invalid entities without semicolon
 String input2 = "&amp;#a &#xZ &#x1F456z &#12345 &#-1 &#1234z";
 String expectedErrorOutput2 = "FAIL: #a[" + 1 + "][" + 2 + "] FAIL: #xZ[" + 1 + "][" + 2 + "] FAIL: #1F456z[" + 3 + "][" + 4 + "] FAIL: #-1[" + 5 + "][" + 6 + "] FAIL: #1234z[" + 7 + "][" + 8 + "]";
 String errorOutput = StringUtils.EMPTY;
 try {
 errorOutput = new StringWriter().getErrorStream().getBuffer().toString();
 unescaper.translate(new StringReader(input2), 0, new StringWriter());
 } catch (IOException e) {
 // Expected
 }
 assertEquals(expectedErrorOutput2, errorOutput);
 }
}