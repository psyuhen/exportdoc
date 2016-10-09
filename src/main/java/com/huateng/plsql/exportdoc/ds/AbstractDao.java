/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sam.pan
 *
 */
public abstract class AbstractDao<E> {

	/**
	 * 返回对应的对象
	 * @param rs 数据集
	 * @return
	 */
	public abstract E mapping(ResultSet rs)  throws SQLException;
}
