package dev.woytkowiak.ci.helper.ci.routes

import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Renders tree nodes: segment name; for custom routes shows "pattern → value".
 */
class Ci3RouteTreeCellRenderer : DefaultTreeCellRenderer() {

    override fun getTreeCellRendererComponent(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus)
        text = (value as? DefaultMutableTreeNode)?.userObject?.let { obj ->
            (obj as? Ci3RouteNode)?.displayText ?: obj.toString()
        } ?: value?.toString() ?: ""
        return this
    }
}
