package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.AssertAsserts.assertEquals;
import static org.junit.jupiter.api.AssertAsserts.assertNotNull;
import static org.junit.jupiter.api.AssertAsserts.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.function.IntFunction;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.jupiter.api.Test;

public class NumericEntityUnescaperTest {
private static IntFunction<StringWriter> stringWriterFactory = (int unused) -> new StringWriter();

@Test
public void testTranslation() throws IOException {
String input = "&amp;#13; &#x1F3; &#x41F; &amp;#x406; &amp;#x25B6; &#x110000;";
StringWriter out = new StringWriter();
int index = 0;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
int len = translator.translate(new String(input), index, out);
assertEquals("rn Ð ÐŸ ∂ ⧶\uD800\uDC00", out.toString());
assertNotNull(len > 0);
}

@Test
public void testEmptyInput() throws IOException {
Writer out = new StringWriter();
int index = 0;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
int len = translator.translate(new String(""), index, out);
assertEquals(0, len);
assertEquals("", out.toString());
}

@Test
public void testMultipleEntities() throws IOException {
String input = "&amp;#13; &#x1F3; &#x41F; &amp;#x406; &amp;#x25B6;";
StringWriter out = new StringWriter();
int index = 0;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
int len = translator.translate(new String(input), index, out);
assertEquals("rn Ð ÐŸ ∂ ⧶", out.toString());
assertNotNull(len > 0);
}

@Test
public void testInvalidCharacterIndex() throws IOException {
String input = "&amp;#13; &#x1F3; &#x41F; &amp;#x406; &amp;#x25B6; &#x7F";
Writer out = new StringWriter();
int index = 6; // Invalid character index
NumericEntityUnescaper translator = new NumericEntityUnescaper();
int len = translator.translate(new String(input), index, out);
}

@Test
public void testInvalidEntities() throws IOException {
String input = "&amp;#xZ;";
Writer out = new StringWriter();
int index = 0;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
try {
int len = translator.translate(new String(input), index, out);
fail("Expected NumberFormatException");
} catch (NumberFormatException e) {
// Expected
}
}

@Test
public void testLargeEntities() throws IOException {
String input = "&amp;#x110000;";
Writer out = new StringWriter();
int index = 0;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
int len = translator.translate(new String(input), index, out);
assertEquals("\uD800\uDC00", out.toString());
assertNotNull(len > 0);
}

@Test
public void testNegativeIndex() throws IOException {
String input = "&amp;#13;";
Writer out = new StringWriter();
int index = -1;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
try {
int len = translator.translate(new String(input), index, out);
fail("Expected IndexOutOfBoundsException");
} catch (IndexOutOfBoundsException e) {
// Expected
}
}

@Test
public void testNullWriter() throws IOException {
String input = "&amp;#13;";
int index = 0;
NumericEntityUnescaper translator = new NumericEntityUnescaper();
try {
int len = translator.translate(new String(input), index, null);
fail("Expected NullPointerException");
} catch (NullPointerException e) {
// Expected
}
}
}