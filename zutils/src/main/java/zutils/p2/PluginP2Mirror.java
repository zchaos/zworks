package zutils.p2;

import java.io.File;

public class PluginP2Mirror {
	/**
	<?xml version="1.0" encoding="UTF-8"?>
	<project name="project" default="download_eclipse_plugins">
		<target name="download_eclipse_plugins">
			<property name="jira" value="/zss/env/eclipse/plugins/jira" />
			<p2.mirror destination="file:/${jira}" description="jira" verbose="true">
				<source>
					<repository name="jira" location="http://update.atlassian.com/atlassian-eclipse-plugin/e3.8" />
				</source>
				<slicingOptions includeFeatures="true" followStrict="true" latestVersionOnly="true" />
			</p2.mirror>
		</target>
	</project>
	*/
	public static String createMirror(String name, String location, File dest) {
		StringBuilder builder = new StringBuilder(1000);
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<project name=\"").append(name).append("\" default=\"download_eclipse_plugins\">");
		builder.append("<target name=\"download_eclipse_plugins\">");

		builder.append("<property name=\"").append(name).append("\" value=\"").append(dest.getAbsolutePath()).append(
				"\" />");

		builder.append("<p2.mirror destination=\"file:/${").append(name).append("}\" description=\"").append(name).append(
				"\" verbose=\"true\">");
		builder.append("<source>");
		builder.append("<repository name=\"").append(name).append("\" location=\"").append(location).append("\" />");
		builder.append("</source>");

		builder.append("<slicingOptions includeFeatures=\"true\" followStrict=\"true\" latestVersionOnly=\"true\" />");
		builder.append("</p2.mirror>");

		builder.append("</target>");
		builder.append("</project>");
		return builder.toString();
	}
}
