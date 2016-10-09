/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import java.util.List;

import com.huateng.plsql.exportdoc.model.Index;
import com.huateng.plsql.exportdoc.model.Table;
import com.huateng.plsql.exportdoc.model.TableDesc;

/**
 * 数据的相关操作
 * @author sam.pan
 *
 */
public interface DBOperation {

	/**
	 * 获取所有分类
	 * @return
	 */
	List<TableDesc> getGroupTable();
	/**
	 * 获取所有分类
	 * @return
	 */
	List<TableDesc> getUserTableBy(String classifyName);
	/**
	 * 获取所有表名
	 * @return
	 */
	List<TableDesc> getUserTable();
	/**
	 * 根据表名获取其相关信息，字段等
	 * @param tableName 表名
	 * @return
	 */
	List<Table> getTable(String tableName);
	
	/**
	 * 根据表名获取其索引等
	 * @param tableName 表名
	 * @return
	 */
	List<Index> getIndex(String tableName);
}
