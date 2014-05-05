package zutils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpUtils {
	public static byte[] getContent(String url) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			int status = client.executeMethod(getMethod);
			if (status != HttpStatus.SC_OK) {
				throw new RuntimeException("Method failed: " + getMethod.getStatusLine());
			}
			return getMethod.getResponseBody();
		}
		finally {
			getMethod.releaseConnection();
		}
	}

	public static String getContentStr(String url) throws HttpException, IOException {
		byte[] contentBytes = HttpUtils.getContent(url);
		return new String(contentBytes, UtilsConst.ENCODING_UTF8);
	}

	/**
	 * 将多个url的内容合并为一个字符串返回。用于将多个css文件的内容合并为一个字符串
	 * @param urls
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String mergeContent(String... urls) throws HttpException, IOException {
		int len = urls == null ? 0 : urls.length;
		StringBuilder builder = new StringBuilder(10000);
		for (int i = 0; i < len; i++) {
			String content = getContentStr(urls[i]);
			builder.append(content);
		}
		return builder.toString();
	}
}
