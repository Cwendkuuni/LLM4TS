import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

@Autowired
private BeanConfiguration beanConfiguration;

@Autowired
private SensitivePropertyHandler sensitivePropertyHandler;

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
String encodedPassword = passwordEncoder.encode("test123");
Assert.assertNotNull(encodedPassword);
}

@Test
public void testObjectMapper() throws Exception {
// TODO: Add test cases for ObjectMapper
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
Assert.assertNotNull(httpFirewall);
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
Assert.assertNotNull(cloudMetadataClient);
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
Assert.assertNotNull(cookieSerializer);
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
Assert.assertNotNull(nullCloudMetadataClient);
}

@Test
public void testSensitivePropertyHandler() {
String encryptedValue = sensitivePropertyHandler.encrypt("testValue");
Assert.assertNotNull(encryptedValue);
String decryptedValue = sensitivePropertyHandler.decrypt(encryptedValue);
Assert.assertEquals("testValue", decryptedValue);
}

@Test
public void testListInstances() {
List<OBInstance> instances = beanConfiguration.listInstances();
// TODO: Add test cases for listInstances
}

@Test
public void testListTenants() {
List<OBTenant> tenants = beanConfiguration.listTenants("instanceId");
// TODO: Add test cases for listTenants
}

@Test
public void testGetTenant() {
OBTenant tenant = beanConfiguration.getTenant("instanceId", "tenantId");
// TODO: Add test cases for getTenant
}

@Test
public void testListDatabaseUsers() {
List<OBDatabaseUser> users = beanConfiguration.listDatabaseUsers("instanceId", "tenantId");
// TODO: Add test cases for listDatabaseUsers
}

@Test
public void testGetTenantEndpoint() {
OBTenantEndpoint endpoint = beanConfiguration.getTenantEndpoint("instanceId", "tenantId");
// TODO: Add test cases for getTenantEndpoint
}

@Test
public void testGetSysTenantUser() {
OBDatabaseUser user = beanConfiguration.getSysTenantUser("instanceId");
// TODO: Add test cases for getSysTenantUser
}

@Test
public void testIncludeClusterNameForJdbcUsername() {
boolean includeClusterNameForJdbcUsername = beanConfiguration.includeClusterNameForJdbcUsername();
Assert.assertEquals(true, includeClusterNameForJdbcUsername);
}

@Test
public void testOceanbaseAccessMode() {
OceanBaseAccessMode oceanbaseAccessMode = beanConfiguration.oceanbaseAccessMode();
// TODO: Add test cases for oceanbaseAccessMode
}

@Test
public void testNeedsSysTenantUser() {
boolean needsSysTenantUser = beanConfiguration.needsSysTenantUser();
Assert.assertEquals(false, needsSysTenantUser);
}

@Test
public void testSupportsTenantInstance() {
boolean supportsTenantInstance = beanConfiguration.supportsTenantInstance();
Assert.assertEquals(false, supportsTenantInstance);
}

@Test
public void testSupportsCloudMetadata() {
boolean supportsCloudMetadata = beanConfiguration.supportsCloudMetadata();
Assert.assertEquals(false, supportsCloudMetadata);
}

@Test
public void testNeedsOBTenantName() {
boolean needsOBTenantName = beanConfiguration.needsOBTenantName();
Assert.assertEquals(false, needsOBTenantName);
}

}