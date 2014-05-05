package zutils.css;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import zutils.HttpUtils;
import zutils.StringUtils;
import zutils.UtilsConst;

/**
 * ie中css个数有小于32的限制，所以会将多个css文件合为一个css，用import加载，如http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default/system-all-import.css中的部分内容为：
 <code>
 @charset "utf-8";
 @import "../../../../../static-file/v_0/sz.commons.button/skin/default/button.css";
 @import "../../../../../static-file/v_0/sz.commons.checkbox/skin/default/checkbox.css";
 @import "../../../../../static-file/v_0/sz.commons.checkboxgroup/skin/default/checkboxgroup.css";
 </code>
 * 在查html的问题时，这样的方式不方便调试，需要将import转换为具体的css。如上面的import转换为具体的html为:
 <code>
 <link rel="stylesheet" href="http://localhost:4080/ciexam/static-file/v_0/sz.commons.button/skin/default/button.css" type="text/css" media="screen">
 <link rel="stylesheet" href="http://localhost:4080/ciexam/static-file/v_0/sz.commons.checkbox/skin/default/checkbox.css" type="text/css" media="screen">
 <link rel="stylesheet" href="http://localhost:4080/ciexam/static-file/v_0/sz.commons.checkboxgroup/skin/default/checkboxgroup.css" type="text/css" media="screen">
 <code>
 * <p>Copyright: Copyright (c) 2014<p>
 * <p>succez<p>
 * @author zhuchx
 * @createdate 2014年5月4日
 */
public class CSSImportResolve {
	public static List<String> importResolove(String url) throws HttpException, IOException {
		CSSImportResolve resolve = new CSSImportResolve(url);
		return resolve.resolve();
	}

	public static List<String> importResolove(String... url) throws HttpException, IOException {
		int len = url == null ? 0 : url.length;
		ArrayList<String> rs = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			rs.addAll(importResolove(url[i]));
		}
		return rs;
	}

	public static String importResolove2Str(String... url) throws HttpException, IOException {
		List<String> rs = importResolove(url);
		int size = rs.size();
		StringBuilder builder = new StringBuilder(size * 100);
		for (int i = 0; i < size; i++) {
			builder.append(rs.get(i));
		}
		return builder.toString();
	}

	private static final String PATH_PARENT = "../";

	private String baseurl;

	private CSSImportResolve(String url) {
		this.baseurl = url;
	}

	private List<String> resolve() throws HttpException, IOException {
		String content = HttpUtils.getContentStr(this.baseurl);
		String basestart = getBaseStartUrl();
		List<String> importList = resolveImport(content);
		int size = importList.size();

		List<String> rs = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			String url = importList.get(i);
			String link = generationCssUrl(basestart, url);
			rs.add(link);
		}
		return rs;
	}

	/**
	 * 根据../../../../../static-file/v_0/sz.commons.button/skin/default/button.css生成<link rel="stylesheet" href="http://localhost:4080/ciexam/static-file/v_0/sz.commons.button/skin/default/button.css" type="text/css" media="screen">
	 * @param url
	 * @return
	 */
	private String generationCssUrl(String basestart, String url) {
		String absurl = generationUrl(basestart, url);
		return "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"" + absurl + "\">";
	}

	/**
	 * 根据../../../../../static-file/v_0/sz.commons.button/skin/default/button.css生成http://localhost:4080/ciexam/static-file/v_0/sz.commons.button/skin/default/button.css
	 * @param url
	 * @return
	 */
	private String generationUrl(String basestart, String url) {
		int lindex = url.lastIndexOf(PATH_PARENT);
		if (lindex < 0) {
			return basestart + url;
		}
		else {
			int index = lindex + PATH_PARENT.length();
			String relativeUrl = url.substring(index);
			String pstr = url.substring(0, index);
			int count = StringUtils.split(pstr, "..").length;
			return removeParent(basestart, count) + relativeUrl;
		}
	}

	/**
	 * 删除basestart中得父路径
	 * removeParent("http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default/",1)="http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/"
	 * removeParent("http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default/",2)="http://localhost:4080/ciexam/static-files-aggcss/v_0/system/"
	 * removeParent("http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default",3)="http://localhost:4080/ciexam/static-files-aggcss/v_0/"
	 * 
	 * @param basestart
	 * @param count
	 * @return
	 */
	private String removeParent(String basestart, int count) {
		if (StringUtils.endsWith(basestart, "/")) {
			basestart = basestart.substring(0, basestart.length() - 1);
		}
		for (int i = 0; i < count; i++) {
			int index = basestart.lastIndexOf("/");
			basestart = basestart.substring(0, index);
		}
		if (StringUtils.endsWith(basestart, "/")) {
			return basestart;
		}
		return basestart + "/";
	}

	/**
	 * 根据http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default/system-all-import.css返回http://localhost:4080/ciexam/static-files-aggcss/v_0/system/skin/default/
	 * @return
	 */
	private String getBaseStartUrl() {
		int index = this.baseurl.lastIndexOf('/');
		return this.baseurl.substring(0, index + 1);
	}

	/**
	 * 获得@import "../../../../../static-file/v_0/sz.commons.button/skin/default/button.css";中得url地址:../../../../../static-file/v_0/sz.commons.button/skin/default/button.css
	 * @param content
	 * @return
	 */
	private List<String> resolveImport(String content) {
		String[] lines = content.split("\r|\n");
		int len = lines == null ? 0 : lines.length;

		List<String> rs = new ArrayList<String>(len);
		for (int i = 0; i < len; i++) {
			String line = lines[i];
			if (StringUtils.isEmpty(line)) {
				continue;
			}
			if (!StringUtils.startsWith(line, "@import")) {
				continue;
			}
			int sindex = line.indexOf("\"");
			int eindex = line.lastIndexOf("\"");
			String url = line.substring(sindex + 1, eindex);
			rs.add(url);
		}
		return rs;
	}
}
