package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertTrue(registeredGroups.size() > 0);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
    assertEquals("deprecated", deprecated.getName());
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsNullCompilerOptions() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.setWarningLevels(null, Arrays.asList("deprecated"), CheckLevel.WARNING);
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsNullDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsNonExistentDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, Arrays.asList("nonExistentGroup"), CheckLevel.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsInvalidCheckLevel() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, Arrays.asList("deprecated"), null);
  }

  // Add more tests as needed
}