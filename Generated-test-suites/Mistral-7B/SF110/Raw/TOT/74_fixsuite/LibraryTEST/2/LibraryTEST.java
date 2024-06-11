package org.junit.platform.suite.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.IncludePackage("org.fixsuite.message.test")
public class LibraryTestSuite {
}