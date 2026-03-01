package dev.woytkowiak.ci.helper.ci.routes

import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode

/**
 * Tree node: segment label + optional route item (for navigation).
 * User object is [Ci3RouteNode] so we can render and navigate.
 */
data class Ci3RouteNode(
    val segment: String,
    val item: Ci3RouteItem?
) {
    val displayText: String
        get() = when (val i = item) {
            is Ci3RouteItem.FromRoutesFile -> "$segment → ${i.displayValue}"
            else -> segment
        }

    val hasNavigation: Boolean get() = item != null
}

/**
 * Builds a nested tree: Controllers (by path segments) and Custom routes (from routes.php, by pattern segments).
 */
fun buildRoutesTree(
    controllerRoutes: List<Ci3RouteItem.FromController>,
    customRoutes: List<Ci3RouteItem.FromRoutesFile>
): DefaultTreeModel {
    val root = DefaultMutableTreeNode(Ci3RouteNode("", null))

    val controllersRoot = DefaultMutableTreeNode(Ci3RouteNode("Controllers", null))
    root.add(controllersRoot)
    for (entry in controllerRoutes) {
        val segments = entry.routePath.split("/").filter { it.isNotBlank() }
        if (segments.isEmpty()) continue
        addPathToTree(controllersRoot, segments, entry)
    }

    val customRoot = DefaultMutableTreeNode(Ci3RouteNode("Custom routes", null))
    root.add(customRoot)
    for (entry in customRoutes) {
        val segments = entry.pattern.split("/").filter { it.isNotBlank() }
        if (segments.isEmpty()) {
            customRoot.add(DefaultMutableTreeNode(Ci3RouteNode(entry.pattern, entry)))
        } else {
            addPathToTree(customRoot, segments, entry)
        }
    }

    return DefaultTreeModel(root)
}

private fun addPathToTree(
    parent: DefaultMutableTreeNode,
    segments: List<String>,
    item: Ci3RouteItem
) {
    var current = parent
    for (i in segments.indices) {
        val segment = segments[i]
        val isLast = i == segments.lastIndex
        val nodeItem = if (isLast) item else null
        val child = findOrCreateChild(current, segment, nodeItem)
        current = child
    }
}

private fun findOrCreateChild(
    parent: DefaultMutableTreeNode,
    segment: String,
    item: Ci3RouteItem?
): DefaultMutableTreeNode {
    val node = Ci3RouteNode(segment, item)
    for (i in 0 until parent.childCount) {
        val child = parent.getChildAt(i) as DefaultMutableTreeNode
        val childData = child.userObject as? Ci3RouteNode ?: continue
        if (childData.segment == segment) {
            if (item != null && childData.item == null) {
                child.setUserObject(Ci3RouteNode(segment, item))
            }
            return child
        }
    }
    val newChild = DefaultMutableTreeNode(node)
    parent.add(newChild)
    return newChild
}
