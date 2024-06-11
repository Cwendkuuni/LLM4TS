import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

@Autowired
private BeanConfiguration beanConfiguration;

@Autowired
private SensitivePropertyHandler sensitivePropertyHandler;

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
String encodedPassword = passwordEncoder.encode("test123");
assertNotNull(encodedPassword);
}

@Test
public void testObjectMapper() throws Exception {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
String output = objectMapper.writeValueAsString(new Internationalizable("test", "testValue"));
assertEquals("{\"i18nKey\":\"test\",\"i18nValues\":{\"test\":\"testValue\"},\"i18nDefault\":\"testValue\"}",
output);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
assertEquals(true, firewall.getAllowUrlEncodedSlash());
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
if (cloudMetadataClient instanceof NullCloudMetadataClient) {
NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
}
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
}

@Test
public void testNullCloudMetadataClient_supportsTenantInstance() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, cloudMetadataClient.supportsTenantInstance());
}

@Test
public void testNullCloudMetadataClient_needsOBTenantName() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, cloudMetadataClient.needsOBTenantName());
}

@Test
public void testNullCloudMetadataClient_needsSysTenantUser() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, cloudMetadataClient.needsSysTenantUser());
}

@Test
public void testNullCloudMetadataClient_includeClusterNameForJdbcUsername() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
}

@Test
public void testNullCloudMetadataClient_oceanbaseAccessMode() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
}

@Test
public void testNullCloudMetadataClient_listInstances() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
}

@Test
public void testNullCloudMetadataClient_listTenants() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
}

@Test
public void testNullCloudMetadataClient_getTenant() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClient_listDatabaseUsers() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClient_getTenantEndpoint() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
}

@Test
public void testNullCloudMetadataClient_getSysTenantUser() {
NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
}
}