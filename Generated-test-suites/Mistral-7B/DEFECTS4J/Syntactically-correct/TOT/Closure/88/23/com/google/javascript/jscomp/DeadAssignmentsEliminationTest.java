package com.google.javascript.jscomp.tests;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.Node;

/**
 * Tests for the DeadAssignmentsElimination Compiler Pass.
 */
public class DeadAssignmentsEliminationTest {

  public static void main(String[] args) {
    CompilationTestHelper helper = new CompilationTestHelper("testdata") {
      @Override
      protected CompilerOptions getOptions() {
        CompilerOptions options = super.getOptions();
        options.setCompilerPasses(CompilerPass.class, DeadAssignmentsElimination.class);
        return options;
      }
    };

    helper.test(
        "DeadAssignmentsEliminationTest01.js",
        "DeadAssignmentsEliminationTest01.js.out");

    helper.test(
        "DeadAssignmentsEliminationTest02.js",
        "DeadAssignmentsEliminationTest02.js.out");

    helper.test(
        "DeadAssignmentsEliminationTest03.js",
        "DeadAssignmentsEliminationTest03.js.out");

    helper.test(
        "DeadAssignmentsEliminationTest04.js",
        "DeadAssignmentsEliminationTest04.js.out");

    helper.test(
        "DeadAssignmentsEliminationTest05.js",
        "DeadAssignmentsEliminationTest05.js.out");

    helper.test(
        "DeadAssignmentsEliminationTestError.js",
        "DeadAssignmentsEliminationTestError.js.out");
  }
}

// Test Case 1 - Simple dead assignment elimination
// DeadAssignmentsEliminationTest01.js:
// function main() {
//   x = 1;
//   x = 2;
// }

// Expected output:
// DeadAssignmentsEliminationTest01.js.out:
// function main() {
// }

// Test Case 2 - Conditional block with dead assignment
// DeadAssignmentsEliminationTest02.js:
// function main() {
//   if (x > 5) {
//     x = 1;
//   }
//   x = 2;
// }

// Expected output:
// DeadAssignmentsEliminationTest02.js.out:
// function main() {
//   if (x > 5) {}
//   x = 2;
// }

// Test Case 3 - Dead assignment in a loop
// DeadAssignmentsEliminationTest03.js:
// function main() {
//   for (var i = 0; i < 10; i++) {
//     x = i;
//   }
//   x = 2;
// }

// Expected output:
// DeadAssignmentsEliminationTest03.js.out:
// function main() {
//   for (var i = 0; i < 10; i++) {}
//   x = 2;
// }

// Test Case 4 - Dead assignment in a switch statement
// DeadAssignmentsEliminationTest04.js:
// function main() {
//   var x = 1;
//   switch (x) {
//     case 1:
//       x = 3;
//       break;
//     case 2:
//       x = 4;
//       break;
//     default:
//       x = 5;
//   }
//   x = 6;
// }

// Expected output:
// DeadAssignmentsEliminationTest04.js.out:
// function main() {
//   var x = 1;
//   switch (x) {
//     case 1:
//       break;
//     case 2:
//       break;
//     default:
//       x = 6;
//   }
// }

// Test Case 5 - Dead assignment in a for-in loop
// DeadAssignmentsEliminationTest05.js:
// function main() {
//   for (var key in obj) {
//     x = obj[key];
//   }
//   x = 2;
// }

// Expected output:
// DeadAssignmentsEliminationTest05.js.out:
// function main() {
//   for (var key in obj) {}
//   x = 2;
// }

// Test Case Error - Dead assignment inside a function call
// DeadAssignmentsEliminationTestError.js:
// function main() {
//   foo(x = 1, 2);
// }

// Expected error:
// Error: Unexpected token: '='