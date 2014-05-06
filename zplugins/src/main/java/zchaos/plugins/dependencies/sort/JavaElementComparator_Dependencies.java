package zchaos.plugins.dependencies.sort;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jface.viewers.Viewer;

@SuppressWarnings("restriction")
public class JavaElementComparator_Dependencies extends JavaElementComparator {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof JarPackageFragmentRoot
				&& e2 instanceof JarPackageFragmentRoot) {
			JarPackageFragmentRoot j1 = (JarPackageFragmentRoot) e1;
			JarPackageFragmentRoot j2 = (JarPackageFragmentRoot) e2;
			IPath p1 = j1.getPath();
			IPath p2 = j2.getPath();
			String s1 = p1.toFile().getName();
			String s2 = p2.toFile().getName();
			return s1.compareToIgnoreCase(s2);
		}
		return super.compare(viewer, e1, e2);
	}
}
