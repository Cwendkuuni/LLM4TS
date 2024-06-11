package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerResult.Error;
import com.google.javascript.jscomp.CompilerResult.Message;
import com.google.javascript.jscomp.CompilerResult.Warning;
import com.google.javascript.jscomp.test_util.AbstractCompilerTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link RenamePrototypes}.
 */
public class RenamePrototypesTest extends AbstractCompilerTestCase {

  private static final String SOURCE_FILE = "test.js";
  private static final String RENAMED_SOURCE_FILE = "renamed_test.js";
  private static final String RESERVED_FILE = "reserved_test.js";
  private static final String AGGRESSIVE_FILE = "aggressive_test.js";
  private static final String AMBIGUOUS_FILE = "ambiguous_test.js";

  private CompilerPass pass;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    CompilerOptions options = new CompilerOptions();
    pass = new RenamePrototypes(getCompiler(), true, null, null);
  }

  public void testRenamePrototypes() throws IOException {
    CompilerInput input = getInput(SOURCE_FILE);
    CompilerResult result = getCompiler().compile(input, Collections.emptyMap());
    assertFalse(result.hasErrors());
    assertFalse(result.hasWarnings());

    CompilerResult renamedResult = getCompiler().compile(input, Collections.emptyMap(), pass);
    List<Message> messages = renamedResult.getMessages();
    List<Error> errors = renamedResult.getErrors();
    List<Warning> warnings = renamedResult.getWarnings();

    assertFalse(errors.isEmpty());
    assertFalse(warnings.isEmpty());
    assertFalse(messages.isEmpty());

    String renamedSourceCode = getOutput(RENAMED_SOURCE_FILE);
    String originalSourceCode = getOutput(SOURCE_FILE);

    // Check that the code was changed as expected
    assertArrayEquals(originalSourceCode.getBytes(), renamedSourceCode.getBytes());

    // Check that the new names are as expected
    String[] expectedNames = new String[]{"oldProp", "oldProp2", "objLitProp"};
    for (int i = 0; i < expectedNames.length; i++) {
      String expectedName = expectedNames[i];
      String actualName = getNameForOriginalName(messages, expectedName);
      assertTrue("Expected name '" + expectedName + "' to be renamed to '" + actualName + "'",
                  actualName.equals(expectedName.replaceAll("_", "_old")));
    }

    // Check that the errors, warnings, and messages are as expected
    checkError(errors.get(0), 1, 0, "Renaming error for 'oldProp'");
    checkWarning(warnings.get(0), 2, 0, "Renaming warning for 'oldProp2'");
    checkMessage(messages.get(0), 3, 0, "Renamed property 'objLitProp' to 'objLitProp_old'");
  }

  public void testRenameReservedProperties() throws IOException {
    CompilerInput input = getInput(RESERVED_FILE);
    CompilerResult result = getCompiler().compile(input, Collections.emptyMap());
    assertFalse(result.hasErrors());
    assertFalse(result.hasWarnings());

    CompilerResult renamedResult = getCompiler().compile(input, Collections.emptyMap(), pass);
    List<Message> messages = renamedResult.getMessages();
    List<Error> errors = renamedResult.getErrors();
    List<Warning> warnings = renamedResult.getWarnings();

    assertTrue(errors.isEmpty());
    assertTrue(warnings.isEmpty());
    assertTrue(messages.isEmpty());

    String renamedSourceCode = getOutput(RESERVED_SOURCE_FILE);
    String originalSourceCode = getOutput(RESERVED_FILE);

    // Check that the code was not changed
    assertArrayEquals(originalSourceCode.getBytes(), renamedSourceCode.getBytes());

    // Check that no errors, warnings, or messages were generated
    assertTrue(errors.isEmpty());
    assertTrue(warnings.isEmpty());
    assertTrue(messages.isEmpty());
  }

  public void testAggressiveRenaming() throws IOException {
    CompilerInput input = getInput(AGGRESSIVE_FILE);
    CompilerResult result = getCompiler().compile(input, Collections.emptyMap());
    assertFalse(result.hasErrors());
    assertFalse(result.hasWarnings());

    CompilerResult renamedResult = getCompiler().compile(input, Collections.emptyMap(), pass);
    List<Message> messages = renamedResult.getMessages();
    List<Error> errors = renamedResult.getErrors();
    List<Warning> warnings = renamedResult.getWarnings();

    assertTrue(errors.isEmpty());
    assertFalse(warnings.isEmpty());
    assertFalse(messages.isEmpty());

    String renamedSourceCode = getOutput(AGGRESSIVE_SOURCE_FILE);
    String originalSourceCode = getOutput(AGGRESSIVE_FILE);

    // Check that the code was changed as expected
    assertArrayEquals(originalSourceCode.getBytes(), renamedSourceCode.getBytes());

    // Check that all property names are renamed
    String[] expectedNames = new String[]{"prop1", "prop2", "objLitProp"};
    for (int i = 0; i < expectedNames.length; i++) {
      String expectedName = expectedNames[i];
      String actualName = getNameForOriginalName(messages, expectedName);
      assertTrue("Expected name '" + expectedName + "' to be renamed to '" + actualName + "'",
                  actualName.equals("_prop_" + expectedName.replaceAll("[^_]", "_")));
    }

    // Check that the warnings are as expected
    String warningMessage = "Renaming warning for property 'prop1'";
    checkWarning(warnings.get(0), 1, 0, warningMessage);
    checkWarning(warnings.get(1), 3, 0, warningMessage);
    checkWarning(warnings.get(2), 5, 0, warningMessage);
  }

  public void testAmbiguousProperties() throws IOException {
    CompilerInput input = getInput(AMBIGUOUS_FILE);
    CompilerResult result = getCompiler().compile(input, Collections.emptyMap());
    assertFalse(result.hasErrors());
    assertFalse(result.hasWarnings());

    CompilerResult renamedResult = getCompiler().compile(input, Collections.emptyMap(), pass);
    List<Message> messages = renamedResult.getMessages();
    List<Error> errors = renamedResult.getErrors();
    List<Warning> warnings = renamedResult.getWarnings();

    assertTrue(errors.isEmpty());
    assertTrue(warnings.isEmpty());
    assertTrue(messages.isEmpty());

    String renamedSourceCode = getOutput(AMBIGUOUS_SOURCE_FILE);
    String originalSourceCode = getOutput(AMBIGUOUS_FILE);

    // Check that the code was not changed
    assertArrayEquals(originalSourceCode.getBytes(), renamedSourceCode.getBytes());

    // Check that no errors, warnings, or messages were generated
    assertTrue(errors.isEmpty());
    assertTrue(warnings.isEmpty());
    assertTrue(messages.isEmpty());

    // Check that the first 'oldProp' was not renamed
    String oldPropName = "oldProp";
    String oldPropNameRenamed = getNameForOriginalName(messages, oldPropName);
    assertFalse("Expected name '" + oldPropName + "' to be renamed", oldPropNameRenamed.equals(oldPropName));

    // Check that the second 'oldProp' was renamed
    String oldPropName2 = "_oldProp";
    String oldPropName2Renamed = getNameForOriginalName(messages, oldPropName2);
    assertTrue("Expected name '" + oldPropName2 + "' to be renamed to a different name",
               !oldPropName2Renamed.equals(oldPropName2));
  }

  private String getNameForOriginalName(List<Message> messages, String name) {
    for (Message message : messages) {
      if (message.getMessage().contains("Renamed property '" + name + "' to ")) {
        return message.getMessage().substring(message.getMessage().indexOf("to") + 2, message.getMessage().length());
      }
    }
    throw new IllegalStateException("Could not find name '" + name + "' in messages");
  }

  private void checkError(Error error, int line, int charPositionInLine, String message) {
    assertEquals(line, error.getLineNumber());
    assertEquals(charPositionInLine, error.getCharacterPositionInLine());
    assertEquals(message, error.getMessage());
  }

  private void checkWarning(Warning warning, int line, int charPositionInLine, String message) {
    assertEquals(line, warning.getLineNumber());
    assertEquals(charPositionInLine, warning.getCharacterPositionInLine());
    assertEquals(message, warning.getMessage());
  }

  private void checkMessage(Message message, int line, int charPositionInLine, String message) {
    assertEquals(line, message.getLineNumber());
    assertEquals(charPositionInLine, message.getCharacterPositionInLine());
    assertEquals(message, message.getMessage());
  }
}