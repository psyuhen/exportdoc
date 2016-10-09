/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.huateng.plsql.exportdoc.model.TableDesc;

/**
 * Map
 * @author sam.pan
 *
 */
public class TableDescDao extends AbstractDao<TableDesc> {

	@Override
	public TableDesc mapping(ResultSet rs) throws SQLException {
		TableDesc tableDesc = new TableDesc();
		tableDesc.setTableName(rs.getString("tableName"));
		tableDesc.setComments(rs.getString("comments"));
		tableDesc.setClassifyName(rs.getString("classifyName"));
		return tableDesc;
	}
}
