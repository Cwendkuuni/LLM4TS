import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
@WebAppConfiguration
public class BeanConfigurationTest {

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private HttpFirewall httpFirewall;

 @Autowired
 private CloudMetadataClient cloudMetadataClient;

 @Autowired
 private CookieSerializer cookieSerializer;

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 BCryptPasswordEncoder encoder = (BCryptPasswordEncoder) passwordEncoder;
 String encodedPassword = encoder.encode("test");
 assertTrue(encoder.matches("test", encodedPassword));
 }

 @Test
 public void testObjectMapper() throws Exception {
 List<String> list = new ArrayList<>();
 list.add("test");
 String json = objectMapper.writeValueAsString(list);
 List<String> result = objectMapper.readValue(json, List.class);
 assertEquals(list, result);
 }

 @Test
 public void testDefaultHttpFirewall() {
 httpFirewall.setAllowUrlEncodedSlash(true);
 assertTrue(httpFirewall.getAllowedUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCookieSerializer() {
 DefaultCookieSerializer serializer = (DefaultCookieSerializer) cookieSerializer;
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClient() throws Exception {
 cloudMetadataClient.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientGetTenant() throws Exception {
 cloudMetadataClient.getTenant("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientListDatabaseUsers() throws Exception {
 cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientGetTenantEndpoint() throws Exception {
 cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientGetSysTenantUser() throws Exception {
 cloudMetadataClient.getSysTenantUser("instanceId");
 }

}