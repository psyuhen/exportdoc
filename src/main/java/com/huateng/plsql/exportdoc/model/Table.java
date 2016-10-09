/**
 * 
 */
package com.huateng.plsql.exportdoc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 表
 * @author sam.pan
 *
 */
public class Table {
	private @Setter @Getter String tableName;//表名
	private @Setter @Getter String fieldName;//英文
	private @Setter @Getter String fieldDesc;//中文
	private @Setter @Getter String fieldType;//类型
	private @Setter @Getter String fieldCheck;//约束
	private @Setter @Getter String fieldValueDesc;//取值说明
	private @Setter @Getter String fieldUsedDesc;//使用说明
	
	
	public String toString(){
		return "{" + tableName + "," + fieldName + "," + fieldDesc + "," + fieldType + "," + fieldCheck + "," + fieldValueDesc + "," + fieldUsedDesc + "}";
	}
}
