package zutils.file;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import zutils.FileUtils;
import zutils.StringUtils;

/**
 * 目录比较
 * <p>Copyright: Copyright (c) 2014<p>
 * <p>succez<p>
 * @author zhuchx
 * @createdate 2014年5月5日
 */
public class DirCompare {
	private static final String SEP = "==========================";

	private File sfile;

	private File dfile;

	private boolean printlog;

	private List<File> diffSFiles = new ArrayList<File>(1000);//文件内容不同

	private List<File> diffDFiles = new ArrayList<File>(1000);//文件内容不同

	private List<File> diffTypeSFiles = new ArrayList<File>(1000);//文件类型不同，如一个是目录，一个是文件

	private List<File> diffTypeDFiles = new ArrayList<File>(1000);//文件类型不同，如一个是目录，一个是文件

	private List<File> moreFiles = new ArrayList<File>(1000);//sfile比dfile多的文件或目录

	private List<File> lessFiles = new ArrayList<File>(1000);//sfile比dfile少的文件或目录

	public DirCompare(File sfile, File dfile, boolean printlog) {
		this.sfile = sfile;
		this.dfile = dfile;
		this.printlog = printlog;
	}

	public List<File> getDiffSFiles() {
		return diffSFiles;
	}

	public List<File> getDiffDFiles() {
		return diffDFiles;
	}

	public List<File> getDiffTypeSFiles() {
		return diffTypeSFiles;
	}

	public List<File> getDiffTypeDFiles() {
		return diffTypeDFiles;
	}

	public List<File> getMoreFiles() {
		return moreFiles;
	}

	public List<File> getLessFiles() {
		return lessFiles;
	}

	public void compare() throws IOException {
		compareFile(sfile, dfile);
	}

	public String getResultStr() {
		StringBuilder builder = new StringBuilder(10000);

		nextLine(builder);
		addTitle(builder, "文件类型不同", 20, true);
		int len = diffTypeSFiles.size();
		for (int i = 0; i < len; i++) {
			File f1 = diffTypeSFiles.get(i);
			File f2 = diffTypeDFiles.get(i);
			builder.append(f1.getAbsolutePath());
			nextLine(builder);
			builder.append(f2.getAbsolutePath());
			nextLine(builder);
			nextLine(builder);
		}

		nextLine(builder);
		addTitle(builder, "多的文件", 20, true);
		len = moreFiles.size();
		for (int i = 0; i < len; i++) {
			File f = moreFiles.get(i);
			builder.append(f.getAbsolutePath());
			nextLine(builder);
		}

		nextLine(builder);
		addTitle(builder, "少的文件", 20, true);
		len = lessFiles.size();
		for (int i = 0; i < len; i++) {
			File f = lessFiles.get(i);
			builder.append(f.getAbsolutePath());
			nextLine(builder);
		}

		nextLine(builder);
		addTitle(builder, "内容不同的文件", 20, true);
		len = diffSFiles.size();
		for (int i = 0; i < len; i++) {
			File f1 = diffSFiles.get(i);
			File f2 = diffDFiles.get(i);
			builder.append(f1.getAbsolutePath());
			nextLine(builder);
			builder.append(f2.getAbsolutePath());
			nextLine(builder);
			nextLine(builder);
		}
		return builder.toString();
	}

	private void addTitle(StringBuilder builder, String title, int titlecount, boolean nextline) {
		builder.append(SEP);
		int len = title.length() * 2;//一个title占用2个位置
		int start = (titlecount - len) / 2;
		for (int i = 0; i < start; i++) {
			builder.append(" ");
		}
		builder.append(title);
		int end = titlecount - start - len;
		for (int i = 0; i < end; i++) {
			builder.append(" ");
		}
		builder.append(SEP);
		if (nextline) {
			nextLine(builder);
		}
	}

	private void nextLine(StringBuilder builder) {
		builder.append("\r\n");
	}

	public void printResult(Writer w) throws IOException {
		String content = getResultStr();
		if (StringUtils.isNotEmpty(content)) {
			w.write(content);
		}
	}

	/**
	 * 将结果输出到控制台
	 */
	public void printResultConsole() {
		String content = getResultStr();
		if (StringUtils.isNotEmpty(content)) {
			System.out.println(content);
		}
	}

	/**
	 * 生成结果对应的目录
	 * @param dir
	 * @throws IOException 
	 */
	public void createResultDir(File dir) throws IOException {
		File typeFile = new File(dir, "difftype");
		typeFile.mkdirs();
		int len = diffTypeSFiles.size();
		if (len > 0) {
			File stypefile = new File(typeFile, "s");
			File dtypefile = new File(typeFile, "d");
			stypefile.mkdirs();
			dtypefile.mkdirs();
			for (int i = 0; i < len; i++) {
				File f = diffTypeSFiles.get(i);
				copy(f, sfile, stypefile);
			}

			for (int i = 0; i < len; i++) {
				File f = diffTypeDFiles.get(i);
				copy(f, dfile, dtypefile);
			}
		}

		File moreFile = new File(dir, "more");
		moreFile.mkdirs();
		len = moreFiles.size();
		for (int i = 0; i < len; i++) {
			File f = moreFiles.get(i);
			copy(f, sfile, moreFile);
		}

		File lessFile = new File(dir, "less");
		lessFile.mkdirs();
		len = lessFiles.size();
		for (int i = 0; i < len; i++) {
			File f = lessFiles.get(i);
			copy(f, dfile, lessFile);
		}

		File diffFile = new File(dir, "diffcontent");
		diffFile.mkdirs();
		len = diffSFiles.size();
		if (len > 0) {
			File sdifffile = new File(diffFile, "s");
			File ddifffile = new File(diffFile, "d");
			sdifffile.mkdirs();
			ddifffile.mkdirs();
			for (int i = 0; i < len; i++) {
				File f = diffSFiles.get(i);
				copy(f, sfile, sdifffile);
			}

			for (int i = 0; i < len; i++) {
				File f = diffDFiles.get(i);
				copy(f, dfile, ddifffile);
			}
		}
	}

	private void copy(File file, File oldParent, File newParent) throws IOException {
		File nfile = replaceParent(file, oldParent, newParent);

		if (file.isDirectory()) {
			FileUtils.copyDirectory(file, nfile);
		}
		else {
			FileUtils.copyFile(file, nfile);
		}
	}

	private File replaceParent(File file, File oldParent, File newParent) {
		String path = file.getAbsolutePath();
		String op = oldParent.getAbsolutePath();
		String np = newParent.getAbsolutePath();
		String p = np + path.substring(op.length());
		return new File(p);
	}

	private void compareFile(File file1, File file2) throws IOException {
		if (printlog) {
			System.out.println(file1.getAbsolutePath());
		}
		boolean e1 = file1.exists();
		boolean e2 = file2.exists();
		if (!e1 && !e2) {//都不存在
			return;
		}
		if (!e1 || !e2) {//有一个不存在
			if (e1) {
				moreFiles.add(file1);
			}
			else {
				lessFiles.add(file2);
			}
			return;
		}
		//都存在
		boolean d1 = file1.isDirectory();
		boolean d2 = file2.isDirectory();
		if (d1 != d2) {
			diffTypeSFiles.add(file1);
			diffTypeDFiles.add(file2);
			return;
		}
		if (!d1) {//都是文件
			if (!FileUtils.contentEquals(file1, file2)) {
				diffSFiles.add(file1);
				diffDFiles.add(file2);
			}
			return;
		}
		compareDir(file1, file2);
	}

	/**
	 * 比较目录，两个文件都是存在的
	 * @param file1
	 * @param file2
	 * @throws IOException
	 */
	private void compareDir(File file1, File file2) throws IOException {
		File[] list1 = file1.listFiles();
		File[] list2 = file2.listFiles();
		HashMap<String, File> map = files2Map(list2);

		int len = list1 == null ? 0 : list1.length;
		for (int i = 0; i < len; i++) {
			File f1 = list1[i];
			String name = f1.getName();
			File f2 = map.remove(name);

			if (f2 == null) {
				moreFiles.add(f1);
			}
			else {
				compareFile(f1, f2);
			}
		}

		Iterator<File> it = map.values().iterator();
		while (it.hasNext()) {
			File f2 = it.next();
			lessFiles.add(f2);
		}
	}

	private HashMap<String, File> files2Map(File[] list) {
		int len = list == null ? 0 : list.length;
		HashMap<String, File> map = new HashMap<String, File>(len);
		for (int i = 0; i < len; i++) {
			File file = list[i];
			map.put(file.getName(), file);
		}
		return map;
	}
}
