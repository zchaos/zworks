package zchaos.plugins.dependencies.sort;

import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;

@SuppressWarnings("restriction")
public class DependenciesSort {

	public void sort() {
		PackageExplorerPart part = getPart();
		if (part == null) {
			return;
		}
		TreeViewer view = part.getTreeViewer();

		ViewerComparator comparator = view.getComparator();
		if (!(comparator instanceof JavaElementComparator)) {
			return;
		}

		if (!(comparator instanceof JavaElementComparator_Dependencies)) {
			JavaElementComparator_Dependencies dc = new JavaElementComparator_Dependencies();
			view.setComparator(dc);
		}
	}

	private PackageExplorerPart getPart() {
		return PackageExplorerPart.getFromActivePerspective();
	}
}
