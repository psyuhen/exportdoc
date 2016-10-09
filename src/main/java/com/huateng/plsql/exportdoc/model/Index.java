/**
 * 
 */
package com.huateng.plsql.exportdoc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 索引
 * @author sam.pan
 *
 */
public class Index {
	private @Setter @Getter String tableName;//表名
	private @Setter @Getter String indexName;//名称
	private @Setter @Getter String columnName;//字段名
	private @Setter @Getter String indexType;//类型
	private @Setter @Getter String indexCheck;//约束
	private @Setter @Getter String indexUsedDesc;//用途
	
	public String toString(){
		return "{" + tableName + "," + indexName + "," + columnName + "," + indexType + "," + indexCheck + "," + indexUsedDesc + "}";
	}
}
