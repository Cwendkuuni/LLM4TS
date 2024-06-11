package net.sf.sugar.fspath.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 *
 * Created on 08 April 2008, 16:21
 */

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
 File expectedResult = new File("testFile.txt");
 File actualResult = fsPathResult.getFile();
 assertEquals(expectedResult.getAbsolutePath(), actualResult.getAbsolutePath());
 }

 @Test
 public void testFSPathResultWithString() {
 FSPathResult fsPathResult = new FSPathResult("Hello World");
 String expectedResult = "Hello World";
 String actualResult = fsPathResult.getString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 Double expectedResult = 3.14;
 Double actualResult = fsPathResult.getDouble();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 Date expectedResult = date;
 Date actualResult = fsPathResult.getDate();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 URI expectedResult = uri;
 URI actualResult = fsPathResult.getURI();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 Boolean expectedResult = true;
 Boolean actualResult = fsPathResult.getBoolean();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
 String expectedResult = "testFile.txt";
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithString() {
 FSPathResult fsPathResult = new FSPathResult("Hello World");
 String expectedResult = "Hello World";
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 String expectedResult = "3.14";
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 String expectedResult = date.toString();
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 String expectedResult = uri.toString();
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 String expectedResult = "true";
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithEmptyResult() {
 FSPathResult fsPathResult = new FSPathResult();
 String expectedResult = "FSPathResult : empty";
 String actualResult = fsPathResult.toString();
 assertEquals(expectedResult, actualResult);
 }
}