/**
 * 
 */
package com.huateng.plsql.exportdoc.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.huateng.plsql.exportdoc.model.Table;

/**
 * 表格
 * @author sam.pan
 *
 */
public class TableDao extends AbstractDao<Table> {

	@Override
	public Table mapping(ResultSet rs) throws SQLException {
		Table table = new Table();
		table.setTableName(rs.getString("tableName"));
		table.setFieldName(rs.getString("fieldName"));
		String fieldDesc = rs.getString("fieldDesc");
		
		fieldDesc = StringUtils.replace(fieldDesc, "（", "(");
		fieldDesc = StringUtils.replace(fieldDesc, "）", ")");
		String usedDesc = "";
		if(StringUtils.contains(fieldDesc,"(") && StringUtils.contains(fieldDesc, ")")){
			int firstIndex = StringUtils.indexOf(fieldDesc, "(");
			int endIndex = StringUtils.lastIndexOf(fieldDesc, ")");
			
			usedDesc = StringUtils.substring(fieldDesc, firstIndex + 1, endIndex);
			fieldDesc = StringUtils.substring(fieldDesc, 0, firstIndex);
		}
		
		table.setFieldDesc(fieldDesc);
		table.setFieldType(rs.getString("fieldType"));
		table.setFieldCheck(rs.getString("fieldCheck"));
		table.setFieldValueDesc(usedDesc + StringUtils.trimToEmpty(rs.getString("fieldValueDesc")));
		table.setFieldUsedDesc(rs.getString("fieldUsedDesc"));
		return table;
	}
}
