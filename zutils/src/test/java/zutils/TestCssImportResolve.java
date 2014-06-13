package zutils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import zutils.css.CSSImportResolve;

public class TestCssImportResolve {
	public static void main(String[] args) throws HttpException, IOException {
		String url = "http://192.168.2.88:8082/static-files-aggcss/sz.bi.api.aggregation/skin/default/sz-bi-api-all-import.css";
		String rs = CSSImportResolve.importResolove2Str(url);
		System.out.println(rs);
	}
}
