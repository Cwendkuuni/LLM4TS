package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SuppressWarnings("unchecked")
public class BeanConfigurationTest {

 private SensitivePropertyHandler sensitivePropertyHandler;
 private CustomOutputSerializer customOutputSerializer;

 @Before
 public void setUp() {
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 customOutputSerializer = mock(CustomOutputSerializer.class);
 when(customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer()))
 .thenReturn(customOutputSerializer);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 String plainTextPassword = "testPassword";
 String encodedPassword = passwordEncoder.encode(plainTextPassword);
 assertEquals(true, passwordEncoder.matches(plainTextPassword, encodedPassword));
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 // Add your assertions here for ObjectMapper
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 // Add your assertions here for DefaultHttpFirewall
 }

 @Test
 public void testNullCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
 // Add more assertions as needed for NullCloudMetadataClient
 }

 @Test
 public void testCookieSerializer() {
 DefaultCookieSerializer serializer = new DefaultCookieSerializer();
 serializer.setCookieName("JSESSIONID");
 // Add your assertions here for CookieSerializer
 }
}