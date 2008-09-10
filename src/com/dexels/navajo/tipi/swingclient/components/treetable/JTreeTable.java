package com.dexels.navajo.tipi.swingclient.components.treetable;

/*
 * @(#)JTreeTable.java	1.2 98/10/27
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 * This example shows how to create a simple JTreeTable component,
 * by using a JTree as a renderer (and editor) for the cells in a
 * particular column in the JTable.
 *
 * @version 1.2 10/27/98
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class JTreeTable
    extends JTable {
  /** A subclass of JTree. */
  private TreeTableModel myModel;
  protected TreeTableCellRenderer tree;
  ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
  private Map nodeColor = new HashMap();

  public JTreeTable() {
    super();
    setSelectionModel(selectionWrapper.getListSelectionModel());
  }

  public JTreeTable(TreeTableModel treeTableModel) {
    super();
    setSelectionModel(selectionWrapper.getListSelectionModel());
    setModel(treeTableModel);
  }

  public void addListSelectionListener(ListSelectionListener l) {
    selectionWrapper.getListSelectionModel().addListSelectionListener(l);
  }

  public void setSelectedRow(int i) {
    selectionWrapper.getListSelectionModel().setSelectionInterval(i,i+1);
  }

  public void expandAll() {
    tree.expandAll();
  }

  public void expandPath(TreePath tp, boolean expand) {
    tree.expandPath(tp, expand);
  }

  public void setNodeColor(String depth, Color c){
    myModel.setNodeColor(depth, c);
  }

  public Color getNodeColor(String depth){
    return myModel.getNodeColor(depth);
  }


  public void setModel(TreeTableModel treeTableModel) {
    myModel = treeTableModel;
    // Create the tree. It will be used as a renderer and editor.
    tree = new TreeTableCellRenderer(treeTableModel);
    //DefaultTreeCellEditor dce = (DefaultTreeCellEditor) tree.getCellEditor();
//        dce.
    tree.setSelectionModel(selectionWrapper);
    // Install a tableModel representing the visible rows in the tree.
    TreeTableModelAdapter ttma = new TreeTableModelAdapter(treeTableModel, tree);
    super.setModel(ttma);
    treeTableModel.setTreeTableAdapter(ttma);

    // Force the JTable and JTree to share their row selection models.
//	ListToTreeSelectionModelWrapper selectionWrapper = new
//	                        ListToTreeSelectionModelWrapper();
//	tree.setSelectionModel(selectionWrapper);
//	setSelectionModel(selectionWrapper.getListSelectionModel());

    // Install the tree editor renderer and editor.
    setDefaultRenderer(TreeTableModel.class, tree);
    setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

    // No grid.
    setShowGrid(false);

    // No intercell spacing
    setIntercellSpacing(new Dimension(0, 0));

    // And update the height of the trees row to match that of
    // the table.
    if (tree.getRowHeight() < 1) {
      // Metal looks better like this.
      setRowHeight(18);
    }
  }

  /**
   * Overridden to message super and forward the method to the tree.
   * Since the tree is not actually in the component hieachy it will
   * never receive this unless we forward it in this manner.
   */
  @Override
public void updateUI() {
    super.updateUI();
    if (tree != null) {
      tree.updateUI();
    }
    // Use the tree's default foreground and background colors in the
    // table.
//        LookAndFeel.installColorsAndFont(this, "Tree.background",
//                                         "Tree.foreground", "Tree.font");
  }

  /* Workaround for BasicTableUI anomaly. Make sure the UI never tries to
   * paint the editor. The UI currently uses different techniques to
   * paint the renderers and editors and overriding setBounds() below
   * is not the right thing to do for an editor. Returning -1 for the
   * editing row in this case, ensures the editor is never painted.
   */
  @Override
public int getEditingRow() {
    return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;
  }

  /**
   * Overridden to pass the new rowHeight to the tree.
   */
  @Override
public void setRowHeight(int rowHeight) {
    super.setRowHeight(rowHeight);
    if (tree != null && tree.getRowHeight() != rowHeight) {
      tree.setRowHeight(getRowHeight());
    }
  }

  /**
   * Returns the tree that is being shared between the model.
   */
  public JTree getTree() {
    return tree;
  }

//    public void setSelectionModel(ListSelectionModel lsm) {
//      super.setSelectionModel(lsm);
//    }

  public ListSelectionModel getTreeTableSelectionModel() {
    ListToTreeSelectionModelWrapper lt = (ListToTreeSelectionModelWrapper) tree.getSelectionModel();
    return lt.getListSelectionModel();
  }

  /**
   * A TreeCellRenderer that displays a JTree.
   */
  public class TreeTableCellRenderer
      extends JTree
      implements TableCellRenderer {

    public void expandAll() {
      expandAll(this, true);
    }

    /**
     * PASTED FROM: http://javaalmanac.com/
     */

    public void expandAll(JTree tree, boolean expand) {
//      System.err.println("Class: "+tree.getModel().getRoot().getClass());
      TreeNode root = (TreeNode) tree.getModel().getRoot();

      // Traverse tree from root
      expandAll(tree, new TreePath(root), expand);
    }

    private final void expandAll(JTree tree, TreePath parent, boolean expand) {
      // Traverse children

      TreeNode node = (TreeNode) parent.getLastPathComponent();
      if (node==null) {
        return;
      }

      if (node.getChildCount() >= 0) {
        Enumeration e = node.children();
        if (e==null) {
          return;
        }

        while (e.hasMoreElements()) {
          TreeNode n = (TreeNode) e.nextElement();
          if (n!=null) {
            TreePath path = parent.pathByAddingChild(n);
            expandAll(tree, path, expand);
          }
        }
      }

      // Expansion or collapse must be done bottom-up
      if (expand) {
        tree.expandPath(parent);
      }
      else {
        tree.collapsePath(parent);
      }
    }

    public void expandPath(TreePath path, boolean expand) {
      expandAll(getTree(), path, expand);
    }

    /**
     * END OF PASTE
     */

    /** Last table/tree row asked to renderer. */
    protected int visibleRow;

    public int getVisibleRow() {
      return visibleRow;
    }

    public TreeTableCellRenderer(TreeModel model) {
      super(model);
      setCellRenderer(new CustomTreeCellRenderer((TreeTableModel)model));
      updateUI();
    }

    /**
     * updateUI is overridden to set the colors of the Tree's renderer
     * to match that of the table.
     */
    @Override
	public void updateUI() {
      super.updateUI();
      // Make the tree's cell renderer use the table's cell selection
      // colors.
      TreeCellRenderer tcr = getCellRenderer();
      if (tcr instanceof DefaultTreeCellRenderer) {
        DefaultTreeCellRenderer dtcr = ( (DefaultTreeCellRenderer) tcr);
        // For 1.1 uncomment this, 1.2 has a bug that will cause an
        // exception to be thrown if the border selection color is
        // null.
        // dtcr.setBorderSelectionColor(null);
        dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
        dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
      }
    }

    /**
     * Sets the row height of the tree, and forwards the row height to
     * the table.
     */
    @Override
	public void setRowHeight(int rowHeight) {
      if (rowHeight > 0) {
        super.setRowHeight(rowHeight);
        if (JTreeTable.this != null && JTreeTable.this.getRowHeight() != rowHeight) {
          JTreeTable.this.setRowHeight(getRowHeight());
        }
      }
    }

    /**
     * This is overridden to set the height to match that of the JTable.
     */
    @Override
	public void setBounds(int x, int y, int w, int h) {
      super.setBounds(x, 0, w, JTreeTable.this.getHeight());
    }

    /**
     * Sublcassed to translate the graphics such that the last visible
     * row will be drawn at 0,0.
     */
    @Override
	public void paint(Graphics g) {
      g.translate(0, -visibleRow * getRowHeight());
      super.paint(g);
    }

    /**
     * TreeCellRenderer method. Overridden to update the visible row.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
      if (isSelected) {
        setBackground(table.getSelectionBackground());
      }
      else {
        setBackground(table.getBackground());
      }
      setComponentColor(this, isSelected, row);

      visibleRow = row;
      return this;
    }

//  public Component getTreeCellRendererComponent(JTree tree,Object value,boolean isSelected,boolean expanded,boolean leaf, int row, boolean hasFocus) {
//    System.err.println("Using custom tree cell renderer");
//    Component c = super.getTreeCellRendererComponent( tree,value ,isSelected ,expanded ,leaf ,  row,false);
//    int ii = ((VisibleRowable)tree).getVisibleRow();
//    System.err.println("BTW, the component class is: "+c.getClass());
//    System.err.println("And my tree instance is: "+tree.getClass());
//    return c;
//  }

    private final void setComponentColor(Component c, boolean isSelected, int row) {
      /** @todo Check this a bit */
      JComponent cc = (JComponent) c;
      cc.setBorder(null);
      Color fg = ((TreeTableModel)getModel()).getNodeColor(""+row);
      c.setForeground(fg);

      if (isSelected) {
        c.setBackground(new Color(220, 220, 255));
      }
      else {
        if (row % 2 == 0) {
          c.setBackground(Color.white);
        }
        else {
          c.setBackground(new Color(240, 240, 240));
        }
      }
      c.setForeground(fg);
    }

    public void updateZebra(int row) {
    }

  }

  /**
   * TreeTableCellEditor implementation. Component returned is the
   * JTree.
   */
  public class TreeTableCellEditor
      extends AbstractCellEditor
      implements TableCellEditor {
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
      return tree;
    }

    /**
     * Overridden to return false, and if the event is a mouse event
     * it is forwarded to the tree.<p>
     * The behavior for this is debatable, and should really be offered
     * as a property. By returning false, all keyboard actions are
     * implemented in terms of the table. By returning true, the
     * tree would get a chance to do something with the keyboard
     * events. For the most part this is ok. But for certain keys,
     * such as left/right, the tree will expand/collapse where as
     * the table focus should really move to a different column. Page
     * up/down should also be implemented in terms of the table.
     * By returning false this also has the added benefit that clicking
     * outside of the bounds of the tree node, but still in the tree
     * column will select the row, whereas if this returned true
     * that wouldn't be the case.
     * <p>By returning false we are also enforcing the policy that
     * the tree will never be editable (at least by a key sequence).
     */
    @Override
	public boolean isCellEditable(EventObject e) {
      if (e instanceof MouseEvent) {
        for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
          if (getColumnClass(counter) == TreeTableModel.class) {
            MouseEvent me = (MouseEvent) e;
            MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(),
                                              me.getX() - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(),
                                              me.isPopupTrigger());
            tree.dispatchEvent(newME);
            break;
          }
        }
      }
      return false;
    }
  }

  /**
   * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
   * to listen for changes in the ListSelectionModel it maintains. Once
   * a change in the ListSelectionModel happens, the paths are updated
   * in the DefaultTreeSelectionModel.
   */
  class ListToTreeSelectionModelWrapper
      extends DefaultTreeSelectionModel {
    /** Set to true when we are updating the ListSelectionModel. */
    protected boolean updatingListSelectionModel;

    public ListToTreeSelectionModelWrapper() {
      super();
      getListSelectionModel().addListSelectionListener(createListSelectionListener());
    }

    /**
     * Returns the list selection model. ListToTreeSelectionModelWrapper
     * listens for changes to this model and updates the selected paths
     * accordingly.
     */
    ListSelectionModel getListSelectionModel() {
      return listSelectionModel;
    }

    /**
     * This is overridden to set <code>updatingListSelectionModel</code>
     * and message super. This is the only place DefaultTreeSelectionModel
     * alters the ListSelectionModel.
     */ @Override
	public void resetRowSelection() {
      if (!updatingListSelectionModel) {
        updatingListSelectionModel = true;
        try {
          super.resetRowSelection();
        }
        finally {
          updatingListSelectionModel = false;
        }
      }
      // Notice how we don't message super if
      // updatingListSelectionModel is true. If
      // updatingListSelectionModel is true, it implies the
      // ListSelectionModel has already been updated and the
      // paths are the only thing that needs to be updated.
    }

    /**
     * Creates and returns an instance of ListSelectionHandler.
     */
    protected ListSelectionListener createListSelectionListener() {
      return new ListSelectionHandler();
    }

    /**
     * If <code>updatingListSelectionModel</code> is false, this will
     * reset the selected paths from the selected rows in the list
     * selection model.
     */
    protected void updateSelectedPathsFromSelectedRows() {
      if (!updatingListSelectionModel) {
        updatingListSelectionModel = true;
        try {
          // This is way expensive, ListSelectionModel needs an
          // enumerator for iterating.
          int min = listSelectionModel.getMinSelectionIndex();
          int max = listSelectionModel.getMaxSelectionIndex();

//          clearSelection();
          if (min != -1 && max != -1) {
            for (int counter = min; counter <= max; counter++) {
              if (listSelectionModel.isSelectedIndex(counter)) {
                TreePath selPath = tree.getPathForRow(counter);
//                System.err.println("TREEPATH: "+selPath);
                if (selPath != null) {
                  addSelectionPath(selPath);
                }
              }
            }
          }
        }
        finally {
          updatingListSelectionModel = false;
        }
      }
    }

    /**
     * Class responsible for calling updateSelectedPathsFromSelectedRows
     * when the selection of the list changse.
     */
    class ListSelectionHandler
        implements ListSelectionListener {
      public void valueChanged(ListSelectionEvent e) {
//        updatingListSelectionModel = false;
//        System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ AAP: "+e.getFirstIndex()+" - "+e.getLastIndex());
        updateSelectedPathsFromSelectedRows();
      }
    }
  }

}
