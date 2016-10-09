/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import java.util.List;

import com.huateng.plsql.exportdoc.model.Index;
import com.huateng.plsql.exportdoc.model.Table;
import com.huateng.plsql.exportdoc.model.TableDesc;

/**
 * 获取Oracle的表相关信息
 * @author sam.pan
 *
 */
public class Oracle implements DBOperation{
	
	@Override
	public List<TableDesc> getGroupTable() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select '' tableName,'' comments, classify_name classifyName  from table_mapping a ");
		sql.append(" group by classify_name order by classify_name");
		
		return JdbcManager.queryForList(sql.toString(), null, new TableDescDao());
	}
	
	@Override
	public List<TableDesc> getUserTableBy(String classifyName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.table_name tableName,b.comments ，'' classifyName from user_tables a, user_tab_comments b,table_mapping c ");
		sql.append(" where a.TABLE_NAME = b.table_name and a.TABLE_NAME = c.table_name and c.classify_name = ? ");
		
		return JdbcManager.queryForList(sql.toString(), new Object[]{classifyName}, new TableDescDao());
	}
	
	@Override
	public List<TableDesc> getUserTable() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.table_name tableName,b.comments  from user_tables a, user_tab_comments b ");
		sql.append(" where a.TABLE_NAME = b.table_name order by a.table_name");
		
		return JdbcManager.queryForList(sql.toString(), null, new TableDescDao());
	}

	@Override
	public List<Table> getTable(String tableName) {
		StringBuilder sql = new StringBuilder(100);
		sql.append("select b.TABLE_NAME tableName,");
		sql.append("       a.comments fieldDesc,"); 
		sql.append("       b.COLUMN_NAME fieldName,"); 
		sql.append("       decode(nvl(b.DATA_SCALE, '0'),"); 
		sql.append("              '0',"); 
		sql.append("              b.DATA_TYPE || '(' || decode(nvl(b.DATA_PRECISION, '0'),'0',b.DATA_LENGTH,b.DATA_PRECISION) || ')',"); 
		sql.append("              b.DATA_TYPE || '(' || decode(nvl(b.DATA_PRECISION, '0'),'0',b.DATA_LENGTH,b.DATA_PRECISION) || ',' || b.DATA_SCALE || ')') fieldType,"); 
		sql.append("       decode(b.NULLABLE,'N','NOT NULL','') fieldCheck,'' fieldValueDesc,'' fieldUsedDesc"); 
		sql.append("  from user_col_comments a, user_tab_columns b"); 
		sql.append(" where a.TABLE_NAME = b.TABLE_NAME"); 
		sql.append("   and a.column_name = b.COLUMN_NAME"); 
		sql.append("   and b.table_name = upper(?)");

		return JdbcManager.queryForList(sql.toString(), new Object[]{tableName}, new TableDao());
	}

	@Override
	public List<Index> getIndex(String tableName) {
		StringBuilder sql = new StringBuilder(100);
		sql.append("select b.TABLE_NAME tableName,");
		sql.append("       b.INDEX_NAME indexName,"); 
		sql.append("       decode(nvl(to_char(d.column_expression),'0'),'0',b.COLUMN_NAME, replace(d.column_expression,'\"','')) columnName, "); 
		sql.append("       decode(c.constraint_type, 'P', '主键', '索引') indexType,"); 
		sql.append("       a.uniqueness indexCheck,"); 
		sql.append("       '' indexUsedDesc"); 
		sql.append("  from user_indexes a, user_ind_columns b, user_constraints c, user_ind_expressions_bak d"); 
		sql.append(" where a.table_name = b.TABLE_NAME"); 
		sql.append("   and a.index_name = b.INDEX_NAME"); 
		sql.append("   and b.TABLE_NAME = c.table_name(+)"); 
		sql.append("   and b.INDEX_NAME = c.index_name(+)"); 
		sql.append("   and b.TABLE_NAME = d.table_name(+)"); 
		sql.append("   and b.INDEX_NAME = d.index_name(+)"); 
		sql.append("   and b.table_name = upper(?)");

		return JdbcManager.queryForList(sql.toString(), new Object[]{tableName}, new IndexDao());
	}
}
