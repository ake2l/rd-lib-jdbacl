/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.rapiddweller.jdbacl;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link JDBCDriverInfo} class.<br/>
 * <br/>
 * Created at 23.02.2009 10:14:26
 *
 * @author Volker Bergmann
 * @since 0.4.8
 */
public class JDBCDriverInfoTest {

  /**
   * Test set id.
   */
  @Test
  public void testSetId() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setId("42");
    assertEquals("42", jdbcDriverInfo.getId());
  }

  /**
   * Test set id 2.
   */
  @Test
  public void testSetId2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setId(null);
    assertNull(jdbcDriverInfo.getId());
  }

  /**
   * Test set id 3.
   */
  @Test
  public void testSetId3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setId("");
    assertNull(jdbcDriverInfo.getId());
  }

  /**
   * Test set name.
   */
  @Test
  public void testSetName() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setName("Name");
    assertEquals("Name", jdbcDriverInfo.getName());
  }

  /**
   * Test set name 2.
   */
  @Test
  public void testSetName2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setName(null);
    assertNull(jdbcDriverInfo.getName());
  }

  /**
   * Test set name 3.
   */
  @Test
  public void testSetName3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setName("");
    assertNull(jdbcDriverInfo.getName());
  }

  /**
   * Test set db system.
   */
  @Test
  public void testSetDbSystem() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDbSystem("Db System");
    assertEquals("Db System", jdbcDriverInfo.getDbSystem());
  }

  /**
   * Test set db system 2.
   */
  @Test
  public void testSetDbSystem2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDbSystem(null);
    assertNull(jdbcDriverInfo.getDbSystem());
  }

  /**
   * Test set db system 3.
   */
  @Test
  public void testSetDbSystem3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDbSystem("");
    assertNull(jdbcDriverInfo.getDbSystem());
  }

  /**
   * Test set url pattern.
   */
  @Test
  public void testSetUrlPattern() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setUrlPattern("https://example.org/example");
    assertEquals("https://example.org/example", jdbcDriverInfo.getUrlPrefix());
  }

  /**
   * Test set url pattern 2.
   */
  @Test
  public void testSetUrlPattern2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setUrlPattern(null);
    assertEquals("", jdbcDriverInfo.getUrlPrefix());
  }

  /**
   * Test set url pattern 3.
   */
  @Test
  public void testSetUrlPattern3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setUrlPattern("");
    assertEquals("", jdbcDriverInfo.getUrlPrefix());
  }

  /**
   * Test set download url.
   */
  @Test
  public void testSetDownloadUrl() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDownloadUrl("https://example.org/example");
    assertEquals("https://example.org/example", jdbcDriverInfo.getDownloadUrl());
  }

  /**
   * Test set download url 2.
   */
  @Test
  public void testSetDownloadUrl2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDownloadUrl(null);
    assertNull(jdbcDriverInfo.getDownloadUrl());
  }

  /**
   * Test set download url 3.
   */
  @Test
  public void testSetDownloadUrl3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDownloadUrl("");
    assertNull(jdbcDriverInfo.getDownloadUrl());
  }

  /**
   * Test set default port.
   */
  @Test
  public void testSetDefaultPort() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultPort("Default Port");
    assertEquals("Default Port", jdbcDriverInfo.getDefaultPort());
  }

  /**
   * Test set default port 2.
   */
  @Test
  public void testSetDefaultPort2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultPort(null);
    assertNull(jdbcDriverInfo.getDefaultPort());
  }

  /**
   * Test set default port 3.
   */
  @Test
  public void testSetDefaultPort3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultPort("");
    assertNull(jdbcDriverInfo.getDefaultPort());
  }

  /**
   * Test set jars.
   */
  @Test
  public void testSetJars() {
    String[] stringArray = new String[] {"foo", "foo", "foo"};
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setJars(stringArray);
    assertSame(stringArray, jdbcDriverInfo.getJars());
  }

  /**
   * Test set driver class.
   */
  @Test
  public void testSetDriverClass() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDriverClass("Driver Class");
    assertEquals("Driver Class", jdbcDriverInfo.getDriverClass());
  }

  /**
   * Test set driver class 2.
   */
  @Test
  public void testSetDriverClass2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDriverClass(null);
    assertNull(jdbcDriverInfo.getDriverClass());
  }

  /**
   * Test set driver class 3.
   */
  @Test
  public void testSetDriverClass3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDriverClass("");
    assertNull(jdbcDriverInfo.getDriverClass());
  }

  /**
   * Test set default user.
   */
  @Test
  public void testSetDefaultUser() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultUser("Default User");
    assertEquals("Default User", jdbcDriverInfo.getDefaultUser());
  }

  /**
   * Test set default user 2.
   */
  @Test
  public void testSetDefaultUser2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultUser(null);
    assertNull(jdbcDriverInfo.getDefaultUser());
  }

  /**
   * Test set default user 3.
   */
  @Test
  public void testSetDefaultUser3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultUser("");
    assertNull(jdbcDriverInfo.getDefaultUser());
  }

  /**
   * Test set default database.
   */
  @Test
  public void testSetDefaultDatabase() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultDatabase("Default Database");
    assertEquals("Default Database", jdbcDriverInfo.getDefaultDatabase());
  }

  /**
   * Test set default database 2.
   */
  @Test
  public void testSetDefaultDatabase2() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultDatabase(null);
    assertNull(jdbcDriverInfo.getDefaultDatabase());
  }

  /**
   * Test set default database 3.
   */
  @Test
  public void testSetDefaultDatabase3() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultDatabase("");
    assertNull(jdbcDriverInfo.getDefaultDatabase());
  }

  /**
   * Test set default schema.
   */
  @Test
  public void testSetDefaultSchema() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setDefaultSchema("Default Schema");
    assertEquals("Default Schema", jdbcDriverInfo.getDefaultSchema());
  }

  /**
   * Test get url prefix.
   */
  @Test
  public void testGetUrlPrefix() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setUrlPattern("https://example.org/example");
    assertEquals("https://example.org/example", jdbcDriverInfo.getUrlPrefix());
  }

  /**
   * Test jdbc url.
   */
  @Test
  public void testJdbcURL() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setUrlPattern("https://example.org/example");
    assertEquals("https://example.org/example",
        jdbcDriverInfo.jdbcURL("localhost", "https://example.org/example", "https://example.org/example"));
  }

  /**
   * Test hash code.
   */
  @Test
  public void testHashCode() {
    JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo();
    jdbcDriverInfo.setId("42");
    assertEquals(1662, jdbcDriverInfo.hashCode());
  }

  /**
   * Test equals.
   */
  @Test
  public void testEquals() {
    assertFalse((new JDBCDriverInfo()).equals("obj"));
    assertNotEquals(null, (new JDBCDriverInfo()));
  }

  /**
   * Test get instances.
   */
  @Test
  public void testGetInstances() {
    assertTrue(JDBCDriverInfo.getInstances().size() > 0);
    assertEquals(11, JDBCDriverInfo.getInstances().size());
  }

  /**
   * Test get instance.
   */
  @Test
  public void testGetInstance() {
    assertNull(JDBCDriverInfo.getInstance("Name"));
  }

  /**
   * Test hsql.
   */
  @Test
  public void testHSQL() {
    JDBCDriverInfo hsql = JDBCDriverInfo.HSQL;
    assertEquals("HSQL", hsql.getId());
    assertEquals("HSQL Server", hsql.getDbSystem());
    assertEquals("HSQL Server", hsql.getName());
    assertEquals("org.hsqldb.jdbcDriver", hsql.getDriverClass());
    assertEquals("9001", hsql.getDefaultPort());
    assertEquals("jdbc:hsqldb:hsql://{0}:{1}/{2}", hsql.getUrlPattern());
    assertEquals("PUBLIC", hsql.getDefaultSchema());
    assertEquals("sa", hsql.getDefaultUser());
    assertEquals("http://hsqldb.sourceforge.net/", hsql.getDownloadUrl());
    assertArrayEquals(new String[] {"hsqldb.jar"}, hsql.getJars());
    assertNull(hsql.getDefaultDatabase());
    assertEquals("jdbc:hsqldb:hsql://myhost:myport/mydb", hsql.jdbcURL("myhost", "myport", "mydb"));
  }

}
