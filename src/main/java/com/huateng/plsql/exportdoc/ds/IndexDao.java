/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.huateng.plsql.exportdoc.model.Index;

/**
 * Index
 * @author sam.pan
 *
 */
public class IndexDao extends AbstractDao<Index> {

	@Override
	public Index mapping(ResultSet rs) throws SQLException {
		Index index = new Index();
		index.setTableName(rs.getString("tableName"));
		index.setIndexName(rs.getString("indexName"));
		index.setColumnName(rs.getString("columnName"));
		index.setIndexType(rs.getString("indexType"));
		index.setIndexCheck(rs.getString("indexCheck"));
		index.setIndexUsedDesc(rs.getString("indexUsedDesc"));
		return index;
	}
}
