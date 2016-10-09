/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import org.junit.Test;

/**
 * @author pansen
 *
 */
public class JdbcManagerTest {

	@Test
	public void testSql(){
		try {
			JdbcManager.executeSql("select * from sys_account");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
