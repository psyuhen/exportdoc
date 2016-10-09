/**
 * 
 */
package com.huateng.plsql.exportdoc.word;

import org.junit.Test;

import com.huateng.plsql.exportdoc.ds.Oracle;

/**
 * @author pansen
 *
 */
public class WordServiceTest {

	@Test
	public void testCreateWord(){
		WordService service = new WordService();
		service.setDbOperation(new Oracle());
		
		service.createWord();
	}
}
