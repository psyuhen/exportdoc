/**
 * 
 */
package com.huateng.plsql.exportdoc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 表格描述
 * @author sam.pan
 *
 */
public class TableDesc {
	private @Setter @Getter String tableNumber;//项目编号
	private @Setter @Getter String tableName;//表名
	private @Setter @Getter String comments;//描述
	private @Setter @Getter String classifyName;//分类 
	
	public String toString(){
		return "{" +tableName+ ","+comments+"}";
	}
}
