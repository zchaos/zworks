package zutils;

import java.io.File;
import java.io.IOException;

import zutils.file.DirCompare;

public class TestDirCompare {
	public static void main(String[] args) throws IOException {
		String pp = "/ztmp/compare/";
		String p1 = pp + "eclipse-java-kepler-SR2-macosx-cocoa-x86_64/eclipse";
		String p2 = pp + "kepler4.3.2-java-jetty-jira5-aptana/eclipse";
		String rp = pp + "compareresult";

		File sfile = new File(p1);
		File dfile = new File(p2);
		File result = new File(rp);

		DirCompare cp = new DirCompare(sfile, dfile, true);
		cp.compare();

		cp.printResultConsole();
		cp.createResultDir(result);
	}
}
