/**
 * 
 */
package com.huateng.plsql.exportdoc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.huateng.plsql.exportdoc.model.Table;

/**
 * POI 表格的操作例子
 * 
 * @author sam.pan
 *
 */
public class POITableUtilTest {

//	@Test
	public void createWord() {
		// Blank Document
		XWPFDocument document = new XWPFDocument();

		// Write the Document in file system
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File("create_table.docx"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// create table
		XWPFTable table = document.createTable();
		// create first row
		XWPFTableRow tableRowOne = table.getRow(0);
		tableRowOne.getCell(0).setText("col one, row one");
		tableRowOne.addNewTableCell().setText("col two, row one");
		tableRowOne.addNewTableCell().setText("col three, row one");
		// create second row
		XWPFTableRow tableRowTwo = table.createRow();
		tableRowTwo.getCell(0).setText("col one, row two");
		tableRowTwo.getCell(1).setText("col two, row two");
		tableRowTwo.getCell(2).setText("col three, row two");
		// create third row
		XWPFTableRow tableRowThree = table.createRow();
		tableRowThree.getCell(0).setText("col one, row three");
		tableRowThree.getCell(1).setText("col two, row three");
		tableRowThree.getCell(2).setText("col three, row three");

		try {
			document.write(out);
			out.close();
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("create_table.docx written successully");
	}
	
	private void addCustomHeadingStyle(XWPFDocument docxDocument, XWPFStyles styles, String strStyleId, int headingLevel, int pointSize, String hexColor) {

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

	    CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
	    size.setVal(new BigInteger(String.valueOf(pointSize)));
	    CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
	    size2.setVal(new BigInteger("24"));

	    CTFonts fonts = CTFonts.Factory.newInstance();
	    fonts.setAscii("Loma" );

	    CTRPr rpr = CTRPr.Factory.newInstance();
	    rpr.setRFonts(fonts);
	    rpr.setSz(size);
	    rpr.setSzCs(size2);

	    CTColor color=CTColor.Factory.newInstance();
	    color.setVal(hexToBytes(hexColor));
	    rpr.setColor(color);
	    style.getCTStyle().setRPr(rpr);
	    // is a null op if already defined

	    style.setType(STStyleType.PARAGRAPH);
	    styles.addStyle(style);

	}

	public byte[] hexToBytes(String hexString) {
	     HexBinaryAdapter adapter = new HexBinaryAdapter();
	     byte[] bytes = adapter.unmarshal(hexString);
	     return bytes;
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
	
	@Test
	public void createWordTemplate(){
		XWPFDocument doc = new XWPFDocument();
		
		String strStyleId = "标题 10";
	    addCustomHeadingStyle(doc, strStyleId, 1);
	    XWPFParagraph paragraph = doc.createParagraph();
	    paragraph.setStyle(strStyleId);
	    XWPFRun run = paragraph.createRun();
	    run.setText("Hello World");

	    paragraph.setStyle(strStyleId);
	    
	    
		
		XWPFParagraph p0 = doc.createParagraph();
		XWPFRun r0 = p0.createRun();
		r0.setBold(true);
		r0.setFontFamily("楷体_GB2312");
		r0.setFontSize(14);
		r0.setText("黑名单有效时间配置表TBL_BLACK_EFFECTIVE");
		
		XWPFParagraph p1 = doc.createParagraph();
		XWPFRun r1 = p1.createRun();
		r1.setBold(true);
		r1.setFontFamily("宋体");
		r1.setText("用途说明：");
		
		XWPFParagraph p2 = doc.createParagraph();
		XWPFRun r2 = p2.createRun();
		r2.setBold(true);
		r2.setFontFamily("宋体");
		r2.setText("索引描述：");
		
		XWPFParagraph p3 = doc.createParagraph();
		XWPFRun r3 = p3.createRun();
		r3.setBold(true);
		r3.setFontFamily("宋体");
		r3.setText("操作量化分析：");
		
		XWPFParagraph p4 = doc.createParagraph();
		XWPFRun r4 = p4.createRun();
		r4.setBold(true);
		r4.setFontFamily("宋体");
		r4.setText("数据项说明：");
		
		List<Table> list = new ArrayList<>();
		XWPFTable createTable = doc.createTable(list.size() + 1, 6);
		CTTblPr tblPr = createTable.getCTTbl().getTblPr();
		
		CTTblBorders borders = tblPr.addNewTblBorders();
		borders.addNewBottom().setVal(STBorder.SINGLE); 
		borders.addNewLeft().setVal(STBorder.DOUBLE);
		borders.addNewRight().setVal(STBorder.DOUBLE);
		borders.addNewTop().setVal(STBorder.DOUBLE);
		//also inner borders
		borders.addNewInsideH().setVal(STBorder.SINGLE);
		borders.addNewInsideV().setVal(STBorder.SINGLE);
		
		
		tblPr.addNewTblStyle().setVal("StyledTable");
		tblPr.getTblW().setType(STTblWidth.DXA);
		tblPr.getTblW().setW(new BigInteger("8000"));
		
		createTable.setCellMargins(10, 10, 10, 10);
		
		
		/*XWPFParagraph p = doc.createParagraph();//创建段落
		p.setAlignment(ParagraphAlignment.CENTER);//居中
		p.setVerticalAlignment(TextAlignment.TOP);//TOP对齐
		XWPFRun createRun = p.createRun();
		createRun.setBold(true);
		createRun.setFontFamily("宋体");
		createRun.setFontSize(10);
		createRun.setText("helloworld");*/
		
		//表格标题
		XWPFTableRow row = createTable.getRow(0);
		CTTrPr addNewTrPr = row.getCtRow().addNewTrPr();
		
		
		List<XWPFTableCell> tableCells = row.getTableCells();
		XWPFTableCell xwpfTableCell = tableCells.get(0);
		CTTcPr addNewTcPr = xwpfTableCell.getCTTc().addNewTcPr();
		addNewTcPr.addNewVAlign().setVal(STVerticalJc.CENTER);
//		addNewTcPr.addNewTcW().setW(BigInteger.valueOf(3000));
		XWPFParagraph xwpfParagraph = xwpfTableCell.getParagraphs().get(0);
		XWPFRun createRun = xwpfParagraph.createRun();
		createRun.setBold(true);
		createRun.setText("中文数据名称");
		
		xwpfTableCell.setColor("cccccc");//背景填充
//		xwpfTableCell.setParagraph(p);
//		xwpfTableCell.setText("中文数据名称");
		tableCells.get(1).setText("数据名称");
		tableCells.get(2).setText("类型");
		tableCells.get(3).setText("使用说明");
		tableCells.get(4).setText("取值说明");
		tableCells.get(5).setText("约束");
		
		
		List<XWPFTableRow> rows = createTable.getRows();
		int length = rows.size();
		for (int i = 1; i < length; i++) {
			List<XWPFTableCell> tableCells2 = rows.get(i).getTableCells();
			tableCells2.get(0).setText("中文数据名称");
			tableCells2.get(1).setText("数据名称");
			tableCells2.get(2).setText("类型");
			tableCells2.get(3).setText("使用说明");
			tableCells2.get(4).setText("取值说明");
			tableCells2.get(5).setText("约束");
		}
		
		FileOutputStream out;
		try {
			out = new FileOutputStream("myword2007.docx");
			doc.write(out);
			out.close();
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("success");
		
	}

//	@Test
	public void createWord2007() {
		XWPFDocument doc = new XWPFDocument();
		XWPFParagraph p1 = doc.createParagraph();

		XWPFTable table = doc.createTable(11, 4);
		// CTTblBorders borders=table.getCTTbl().getTblPr().addNewTblBorders();
		CTTblPr tblPr = table.getCTTbl().getTblPr();
		tblPr.getTblW().setType(STTblWidth.DXA);
		tblPr.getTblW().setW(new BigInteger("7000"));

		// 设置上下左右四个方向的距离，可以将表格撑大
		table.setCellMargins(20, 20, 20, 20);

		// 表格
		List<XWPFTableCell> tableCells = table.getRow(0).getTableCells();

		XWPFTableCell cell = tableCells.get(0);
		XWPFParagraph newPara = new XWPFParagraph(cell.getCTTc().addNewP(),
				cell);
		XWPFRun run = newPara.createRun();
		/** 内容居中显示 **/
		newPara.setAlignment(ParagraphAlignment.CENTER);
		// run.getCTR().addNewRPr().addNewColor().setVal("FF0000");/**FF0000红色*/
		// run.setUnderline(UnderlinePatterns.THICK);
		run.setText("第一 数据");

		tableCells.get(1).setText("第一 数据");
		tableCells.get(2).setText("第一 据");
		tableCells.get(3).setText("第 据");

		tableCells = table.getRow(1).getTableCells();
		tableCells.get(0).setText("第数据");
		tableCells.get(1).setText("第一 数据");
		tableCells.get(2).setText("第一 据");
		tableCells.get(3).setText("第 据");

		// 设置字体对齐方式
		p1.setAlignment(ParagraphAlignment.CENTER);
		p1.setVerticalAlignment(TextAlignment.TOP);

		// 第一页要使用p1所定义的属性
		XWPFRun r1 = p1.createRun();

		// 设置字体是否加粗
		r1.setBold(true);
		r1.setFontSize(20);

		// 设置使用何种字体
		r1.setFontFamily("Courier");

		// 设置上下两行之间的间距
		r1.setTextPosition(20);
		r1.setText("标题");

		FileOutputStream out;
		try {
			out = new FileOutputStream("word2007.docx");
			// 以下代码可进行文件下载
			// response.reset();
			// response.setContentType("application/x-msdownloadoctet-stream;charset=utf-8");
			// response.setHeader("Content-Disposition",
			// "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8"));
			// OutputStream out = response.getOutputStream();
			// this.doc.write(out);
			// out.flush();

			doc.write(out);
			out.close();
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("success");
	}
}
