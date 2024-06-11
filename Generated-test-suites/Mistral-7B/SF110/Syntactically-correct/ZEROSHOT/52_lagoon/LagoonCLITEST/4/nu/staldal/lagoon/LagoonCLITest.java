package nu.staldal.lagoon.test;

import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import static nu.staldal.lagoon.LagoonCLI.*;

public class LagoonCLITest {

 @Test
 public void testValidArguments() {
 String[] args = new String[]{"valid Property File.xml"};
 assertFalse("main should not return when valid arguments are provided", LagoonCLI.main(args).equals(0));
 }

 @Test(expected = LagoonException.class)
 public void testMissingArguments() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testInvalidArgumentFormat() {
 String[] args = new String[]{"invalidArgument.txt"};
 LagoonCLI.main(args);
 }

 @Test
 public void testValidPropertyFile() {
 String[] args = new String[]{"valid Property File.xml"};
 LagoonCLI.main(args);
 assertTrue("Lagoon should be initialized successfully", LagoonCLI.properties != null);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPropertyFile() {
 String[] args = new String[]{"missing Property File.xml"};
 LagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testInvalidPassword() {
 String[] args = new String[]{"valid Property File.xml", "invalidPassword"};
 LagoonCLI.main(args);
 }

 @Test
 public void testBuildSuccess() {
 String[] args = new String[]{"valid Property File.xml", "build"};
 LagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testForceBuildFailure() {
 String[] args = new String[]{"valid Property File.xml", "force"};
 LagoonCLI.main(args);
 }

 @Test
 public void testInterval() {
 String[] args = new String[]{"valid Property File.xml", "1500"};
 LagoonCLI.main(args);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalFormat() {
 String[] args = new String[]{"valid Property File.xml", "invalidInterval"};
 LagoonCLI.main(args);
 }

 @Test
 public void testInvalidURL() {
 String[] args = new String[]{"invalidURL Property File.xml"};
 LagoonCLI.main(args);
 assertFalse("Lagoon should not be initialized successfully", LagoonCLI.processor != null);
 }

 @Test
 public void testBuildFailure() {
 String[] args = new String[]{"valid Property File.xml", "build"};
 LagoonCLI.main(args);
 assertFalse("Build should have failed", LagoonCLI.processor.isBuildSuccess());
 }

 @Test
 public void testForceBuildSuccess() {
 String[] args = new String[]{"valid Property File.xml", "force"};
 LagoonCLI.main(args);
 assertTrue("Force build should have succeeded", LagoonCLI.processor.isBuildSuccess());
 }
}