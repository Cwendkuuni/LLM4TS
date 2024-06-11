package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.ExcerptFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LightweightMessageFormatterTest {

  @Mock private SourceExcerptProvider sourceExcerptProvider;
  @Mock private ExcerptFormatter excerptFormatter;
  @InjectMocks private LightweightMessageFormatter lightweightMessageFormatter;

  private List<JSError> errors;
  private TestSourceFile testSourceFile;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(sourceExcerptProvider.getExcerpt(LINE))
        .thenReturn("testSourceFile.js:1: foo = 1;");
    when(sourceExcerptProvider.getExcerpt(TestSourceFile.class.getSimpleName()))
        .thenReturn(testSourceFile);

    excerptFormatter = new LineNumberingFormatter();
    when(sourceExcerptProvider.getFormatter()).thenReturn(excerptFormatter);

    errors = Arrays.asList(
        new JSError(
            TestSourceFile.class.getSimpleName(), 1, "Expected ';'", 3, CheckLevel.ERROR),
        new JSError(
            TestSourceFile.class.getSimpleName(), 2, "Unexpected 'bar'", 5, CheckLevel.WARNING),
        new JSError(
            TestSourceFile.class.getSimpleName(), 3, "Unexpected 'baz'", 8, CheckLevel.ERROR),
        new JSError(
            TestSourceFile.class.getSimpleName(), 4, "Expected ';'", 11, CheckLevel.ERROR),
        new JSError(
            TestSourceFile.class.getSimpleName(), 5, "Expected ';'", 14, CheckLevel.ERROR));
  }

  @Test
  public void formatErrorTest() {
    assertEquals(
        "1: foo = 1;\n"
            + " 1: ^^^^^^^^^^^^^^^^^^^^^^^^^^ Error - ERROR - Expected ';'\n"
            + "testSourceFile.js:1:1: 'Expected ';''\n",
        lightweightMessageFormatter.formatError(errors.get(0)));
  }

  @Test
  public void formatWarningTest() {
    assertEquals(
        "2: bar\n"
            + " 2: ^^^^^^^^ Warning - WARNING - Unexpected 'bar'\n"
            + "testSourceFile.js:2:2: 'Unexpected 'bar''\n",
        lightweightMessageFormatter.formatWarning(errors.get(1)));
  }

  @Test
  public void formatErrorWithSourceExcerptTest() {
    assertEquals(
        "1: foo = 1;\n"
            + " 1: ^^^^^^^^^^^^^^^^^^^^^^^^^^ Error - ERROR - Expected ';'\n"
            + "testSourceFile.js:1:1: 'Expected ';''\n"
            + "foo = 1;\n",
        lightweightMessageFormatter.formatError(errors.get(0), sourceExcerptProvider));
  }

  @Test
  public void formatWarningWithSourceExcerptTest() {
    assertEquals(
        "2: bar\n"
            + " 2: ^^^^^^^^ Warning - WARNING - Unexpected 'bar'\n"
            + "testSourceFile.js:2:2: 'Unexpected 'bar''\n"
            + "bar\n",
        lightweightMessageFormatter.formatWarning(errors.get(1), sourceExcerptProvider));
  }

  @Test(expected = NullPointerException.class)
  public void formatErrorWithoutSourceProviderTest() {
    lightweightMessageFormatter = new LightweightMessageFormatter();
    lightweightMessageFormatter.formatError(errors.get(0));
  }

  @Test(expected = NullPointerException.class)
  public void formatWarningWithoutSourceProviderTest() {
    lightweightMessageFormatter = new LightweightMessageFormatter();
    lightweightMessageFormatter.formatWarning(errors.get(1));
  }
}

class TestSourceFile extends SourceFile {
  public TestSourceFile() throws IOException {
    super("testSourceFile.js", "testSourceFile content");
  }

  @Override
  public SourceExcerpt getSourceExcerpt() {
    return "testSourceFile content";
  }
}