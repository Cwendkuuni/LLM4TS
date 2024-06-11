import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;
 @Mock
 private CloudMetadataClient cloudMetadataClient;

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 public BeanConfigurationTest() {
 MockitoAnnotations.openMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertEquals(objectMapper, actualObjectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
 when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
 when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
 when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
 when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
 when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.listInstances()).thenThrow(new UnsupportedException("CloudMetadata not supported"));
 when(cloudMetadataClient.listTenants(anyString())).thenThrow(new UnsupportedException("CloudMetadata not supported"));
 when(cloudMetadataClient.getTenant(anyString(), anyString()))
 .thenThrow(new UnsupportedException("getTenant not supported in non-could"));
 when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString()))
 .thenThrow(new UnsupportedException("CloudMetadata not supported"));
 when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString()))
 .thenReturn(new OBTenantEndpoint());
 when(cloudMetadataClient.getSysTenantUser(anyString()))
 .thenThrow(new UnsupportedException("CloudMetadata not supported"));
 assertEquals(nullCloudMetadataClient.supportsCloudMetadata(), false);
 assertEquals(nullCloudMetadataClient.supportsTenantInstance(), false);
 assertEquals(nullCloudMetadataClient.needsOBTenantName(), false);
 assertEquals(nullCloudMetadataClient.needsSysTenantUser(), false);
 assertEquals(nullCloudMetadataClient.includeClusterNameForJdbcUsername(), true);
 assertEquals(nullCloudMetadataClient.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
 try {
 nullCloudMetadataClient.listInstances();
 fail("Should throw UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 try {
 nullCloudMetadataClient.listTenants("instanceId");
 fail("Should throw UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 try {
 nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 fail("Should throw UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 try {
 nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 fail("Should throw UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 assertEquals(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"), endpoint);
 try {
 nullCloudMetadataClient.getSysTenantUser("instanceId");
 fail("Should throw UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 }
}