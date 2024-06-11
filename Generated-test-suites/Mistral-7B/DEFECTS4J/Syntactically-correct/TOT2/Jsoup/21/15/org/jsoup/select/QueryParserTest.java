
package org.junit.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ Expert1Test.class, Expert2Test.class, Expert3Test.class })
public class QueryParserTestSuite {
}
