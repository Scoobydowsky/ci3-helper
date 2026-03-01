package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

/**
 * Tool window content: nested tree of CI3 routes (controllers + config/routes.php) with refresh and double-click to open.
 */
class Ci3RoutesPanel(private val project: Project) : SimpleToolWindowPanel(false, true) {

    private val tree = JTree(DefaultTreeModel(DefaultMutableTreeNode("Loading..."))).apply {
        isRootVisible = false
        showsRootHandles = true
        cellRenderer = Ci3RouteTreeCellRenderer()
    }

    init {
        toolbar = createToolbar()
        setContent(JBScrollPane(tree))
        tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val path = tree.getPathForLocation(e.x, e.y) ?: return
                    val node = path.lastPathComponent as? DefaultMutableTreeNode ?: return
                    val routeNode = node.userObject as? Ci3RouteNode ?: return
                    routeNode.item?.let { navigateTo(it) }
                }
            }
        })
        loadRoutes()
    }

    private fun createToolbar(): javax.swing.JComponent {
        val toolbar = com.intellij.openapi.actionSystem.ActionManager.getInstance()
            .createActionToolbar("Ci3Routes", Ci3RoutesToolbarActions(project, this), true)
        toolbar.setTargetComponent(this)
        return toolbar.component
    }

    fun navigateTo(item: Ci3RouteItem) {
        when (item) {
            is Ci3RouteItem.FromController -> {
                val line = item.lineNumber?.let { (it - 1).coerceAtLeast(0) } ?: 0
                OpenFileDescriptor(project, item.file, line, 0).navigate(true)
            }
            is Ci3RouteItem.FromRoutesFile -> {
                val line = (item.lineNumber - 1).coerceAtLeast(0)
                OpenFileDescriptor(project, item.routesFile, line, 0).navigate(true)
            }
        }
    }

    fun loadRoutes() {
        val controllerRoutes = Ci3RoutesCollector.collectControllerRoutes(project)
        val customRoutes = Ci3RoutesCollector.collectCustomRoutes(project)
        val model = buildRoutesTree(controllerRoutes, customRoutes)
        tree.model = model
        expandAllNodes(tree, 0, tree.rowCount)
    }

    private fun expandAllNodes(tree: JTree, startIndex: Int, endIndex: Int) {
        for (i in startIndex until endIndex) {
            tree.expandRow(i)
        }
        val newCount = tree.rowCount
        if (newCount > endIndex) expandAllNodes(tree, endIndex, newCount)
    }
}
