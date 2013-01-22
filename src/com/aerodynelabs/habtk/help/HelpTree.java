package com.aerodynelabs.habtk.help;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A class to represent the help hierarchy
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class HelpTree extends JTree {
	
	public HelpTree(DefaultMutableTreeNode node) {
		super(node);
	}

	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		String name = ((File) ((DefaultMutableTreeNode) value).getUserObject()).getName();
		int i = name.lastIndexOf('.');
		if(i < 1) return name;
		return name.substring(0, i);
	}

}
