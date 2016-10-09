/**
 * 
 */
package com.huateng.plsql.exportdoc.word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.huateng.plsql.exportdoc.ds.DBOperation;
import com.huateng.plsql.exportdoc.model.Index;
import com.huateng.plsql.exportdoc.model.Table;
import com.huateng.plsql.exportdoc.model.TableDesc;
import com.huateng.plsql.exportdoc.util.PropertiesReader;

/**
 * 
 * @author sam.pan
 *
 */
public class WordService {
	
	private DBOperation dbOperation;
	public void setDbOperation(DBOperation dbOperation) {
		this.dbOperation = dbOperation;
	}
	
	
	/**
	 * word整体样式
	 */
	private static CTStyles wordStyles = null;

	/**
	 * Word整体样式
	 */
	static {
		PropertiesConfiguration reader = PropertiesReader.getReader();
		
		XWPFDocument template;
		try {
			// 读取模板文档
			template = new XWPFDocument(new FileInputStream(reader.getString("template.path")));
			// 获得模板文档的整体样式
			wordStyles = template.getStyle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlException e) {
			e.printStackTrace();
		}
	}
	
	public void createWord(){
		XWPFDocument doc = new XWPFDocument();
		XWPFStyles createStyles = doc.createStyles();
		createStyles.setStyles(wordStyles);
		addCustomHeadingStyle(doc, "标题 3", 3);
		addCustomHeadingStyle(doc, "标题 4", 4);
		
		String title3 = "1.1.{0}";
		String title4 = "1.1.{0}.{1}";
		String replaced3 = "";
		String replaced4 = "";
		
		List<TableDesc> groupTable = dbOperation.getGroupTable();
		for (int i = 0, length = groupTable.size(); i < length; i++) {
			TableDesc tableDesc = groupTable.get(i);
			
			replaced3 =  StringUtils.replaceOnce(title3, "{0}", "" + (i + 1));
			
			String classifyName = tableDesc.getClassifyName();
			createParagraph(doc, "楷体_GB2312", 14,StringUtils.trimToEmpty(replaced3) +"  "+ classifyName, true, "标题 3");

			List<TableDesc> userTableBy = dbOperation.getUserTableBy(classifyName);
			for (int j = 0, len = userTableBy.size(); j < len; j++) {
				TableDesc td = userTableBy.get(j);
				
				replaced4 =  StringUtils.replaceOnce(title4, "{0}", "" + (i + 1));
				replaced4 =  StringUtils.replaceOnce(replaced4, "{1}", "" + (j + 1));
				td.setTableNumber(replaced4);
				
				System.err.println(td.getTableName());
				createDBBook(doc,td);
			}
		}
		
		
		
		FileOutputStream out;
		try {
			PropertiesConfiguration reader = PropertiesReader.getReader();
			out = new FileOutputStream(reader.getString("docx.path") + "新一代CFRM数据库说明书.docx");
			doc.write(out);
			out.close();
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("success");
	}
	
	private void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {
	    CTStyle ctStyle = CTStyle.Factory.newInstance();
	    ctStyle.setStyleId(strStyleId);

	    CTString styleName = CTString.Factory.newInstance();
	    styleName.setVal(strStyleId);
	    ctStyle.setName(styleName);

	    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
	    indentNumber.setVal(BigInteger.valueOf(headingLevel));

	    // lower number > style is more prominent in the formats bar
	    ctStyle.setUiPriority(indentNumber);

	    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
	    ctStyle.setUnhideWhenUsed(onoffnull);

	    // style shows up in the formats bar
	    ctStyle.setQFormat(onoffnull);

	    // style defines a heading of the given level
	    CTPPr ppr = CTPPr.Factory.newInstance();
	    ppr.setOutlineLvl(indentNumber);
	    ctStyle.setPPr(ppr);

	    XWPFStyle style = new XWPFStyle(ctStyle);

	    // is a null op if already defined
	    XWPFStyles styles = docxDocument.createStyles();
	    
	    style.setType(STStyleType.PARAGRAPH);
	    styles.addStyle(style);

	}
	
	private void createDBBook(XWPFDocument doc, TableDesc tableDesc){
		String tableName = tableDesc.getTableName();
		String tableNumber = tableDesc.getTableNumber();
		String comments = tableDesc.getComments();
		
		createParagraph(doc, "楷体_GB2312", 14,StringUtils.trimToEmpty(tableNumber) +"  "+ StringUtils.trimToEmpty(comments) + tableName, true, "标题 4");
		createParagraph(doc, "宋体", 11, "用途说明：", true);
		createParagraph(doc, "宋体", 11, "索引描述：", true);
		
		List<Index> index = dbOperation.getIndex(tableName);
		createIndex(doc, index);
		
		createParagraph(doc, "宋体", 11, "操作量化分析：", true);
		createParagraph(doc, "宋体", 11, "数据项说明：", true);
		
		
		List<Table> table = dbOperation.getTable(tableName);
		createTable(doc, table);
	}
	
	private void createParagraph(XWPFDocument doc,String fontFamily, int fontSize, String text, boolean isBold, String styleId){
		XWPFParagraph p0 = doc.createParagraph();
		p0.setStyle(styleId);
		XWPFRun r0 = p0.createRun();
		r0.setBold(isBold);
		r0.setFontFamily(fontFamily);
		r0.setFontSize(fontSize);
		r0.setText(text);
	}
	
	private void createParagraph(XWPFDocument doc,String fontFamily, int fontSize, String text, boolean isBold){
		createParagraph(doc, fontFamily, fontSize, text, isBold, null);
	}
	
	private void createText(XWPFTableCell xwpfTableCell,String fontFamily, String text){
		CTTcPr addNewTcPr = xwpfTableCell.getCTTc().addNewTcPr();
		addNewTcPr.addNewVAlign().setVal(STVerticalJc.CENTER);
		XWPFParagraph xwpfParagraph = xwpfTableCell.getParagraphs().get(0);
		XWPFRun createRun = xwpfParagraph.createRun();
		createRun.setFontFamily(fontFamily);
		createRun.setText(text);
	}
	
	private void createIndex(XWPFDocument doc,List<Index> list){
		XWPFTable createTable = doc.createTable(list.size() + 1, 4);
		CTTblPr tblPr = createTable.getCTTbl().getTblPr();
		tblPr.getTblW().setType(STTblWidth.DXA);
		tblPr.getTblW().setW(new BigInteger("8500"));
		createTable.setCellMargins(10, 10, 10, 10);
		
		//表格边框格式
		CTTblBorders borders = tblPr.addNewTblBorders();
		borders.addNewBottom().setVal(STBorder.SINGLE); 
		borders.addNewLeft().setVal(STBorder.DOUBLE);
		borders.addNewRight().setVal(STBorder.DOUBLE);
		borders.addNewTop().setVal(STBorder.DOUBLE);
		
		//标题的生成
		XWPFTableRow row = createTable.getRow(0);
		List<XWPFTableCell> tableCells = row.getTableCells();
		String[] titles = {"字段名","索引名","约束","用途"};
		for (int i = 0,length = tableCells.size(); i < length; i++) {
			XWPFTableCell xwpfTableCell = tableCells.get(i);
			createText(xwpfTableCell, "黑体", titles[i]);
			xwpfTableCell.setColor("cccccc");//背景填充
		}
		
		List<XWPFTableRow> rows = createTable.getRows();
		int length = rows.size();
		for (int i = 1; i < length; i++) {
			List<XWPFTableCell> tableCells2 = rows.get(i).getTableCells();
			Index index = list.get(i-1);
			
			createText(tableCells2.get(0), "楷体_GB2312", index.getColumnName());
			createText(tableCells2.get(1), "楷体_GB2312", index.getIndexName());
			createText(tableCells2.get(2), "楷体_GB2312", index.getIndexCheck());
			createText(tableCells2.get(3), "楷体_GB2312", index.getIndexUsedDesc());
		}
	}
	
	private void createTable(XWPFDocument doc,List<Table> list){
		XWPFTable createTable = doc.createTable(list.size() + 1, 6);
		CTTblPr tblPr = createTable.getCTTbl().getTblPr();
		tblPr.getTblW().setType(STTblWidth.DXA);
		tblPr.getTblW().setW(new BigInteger("8500"));
		createTable.setCellMargins(10, 10, 10, 10);
		
		//表格边框格式
		CTTblBorders borders = tblPr.addNewTblBorders();
		borders.addNewBottom().setVal(STBorder.SINGLE); 
		borders.addNewLeft().setVal(STBorder.DOUBLE);
		borders.addNewRight().setVal(STBorder.DOUBLE);
		borders.addNewTop().setVal(STBorder.DOUBLE);
		
		//标题的生成
		XWPFTableRow row = createTable.getRow(0);
		List<XWPFTableCell> tableCells = row.getTableCells();
		String[] titles = {"中文数据名称","数据名称","类型","使用说明","取值说明","约束"};
		for (int i = 0,length = tableCells.size(); i < length; i++) {
			XWPFTableCell xwpfTableCell = tableCells.get(i);
			CTTcPr addNewTcPr = xwpfTableCell.getCTTc().addNewTcPr();
			addNewTcPr.addNewVAlign().setVal(STVerticalJc.CENTER);
			XWPFParagraph xwpfParagraph = xwpfTableCell.getParagraphs().get(0);
			XWPFRun createRun = xwpfParagraph.createRun();
			createRun.setBold(true);
			createRun.setText(titles[i]);
			xwpfTableCell.setColor("cccccc");//背景填充
		}
		
		List<XWPFTableRow> rows = createTable.getRows();
		int length = rows.size();
		for (int i = 1; i < length; i++) {
			List<XWPFTableCell> tableCells2 = rows.get(i).getTableCells();
			Table t = list.get(i-1);
			createText(tableCells2.get(0), "楷体_GB2312", t.getFieldDesc());
			createText(tableCells2.get(1), "楷体_GB2312", t.getFieldName());
			createText(tableCells2.get(2), "楷体_GB2312", t.getFieldType());
			createText(tableCells2.get(3), "楷体_GB2312", t.getFieldUsedDesc());
			createText(tableCells2.get(4), "楷体_GB2312", t.getFieldValueDesc());
			createText(tableCells2.get(5), "楷体_GB2312", t.getFieldCheck());
		}
		
	}
}
