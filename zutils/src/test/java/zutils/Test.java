package zutils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public class Test {
	public static void main(String[] args) throws HttpException, IOException {
		//		String str = CSSImportResolve.importResolove2Str(
		//				"http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default/system-all-import.css",
		//				"http://localhost:4080/ciexam/static-files-aggcss/v_0/sz.bi.api.aggregation/skin/default/sz-bi-api-all-import.css");
		String str = HttpUtils.mergeContent(
				"http://localhost:4080/ciexam/static-file/v_0/sz.commons.tablefreeze/skin/default/tablefreeze.css",
				"http://localhost:4080/ciexam/static-file/v_0/sz.bi.prst.prstbody/skin/default/prst-body.css");
		System.out.println(str);
	}
}
