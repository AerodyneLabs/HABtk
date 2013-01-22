package com.aerodynelabs.habtk.help;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * A class to display a help window.
 * @author Ethan Harstad
 *
 */
public class HelpWindow {
	
	private static JFrame frame;
	private JTree tree;
	private JEditorPane view;
	
	/**
	 * Show the help window if it is hidden, otherwise create it.
	 */
	public static void showHelp() {
		if(frame != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(frame.getExtendedState() == JFrame.ICONIFIED) {
						frame.setExtendedState(JFrame.NORMAL);
					}
					frame.setVisible(true);
					frame.toFront();
					frame.repaint();
				}
			});
		} else {
			new HelpWindow();
		}
	}
	
	/**
	 * Create help window.
	 */
	public HelpWindow() {
		frame = new JFrame("HABtk - Help");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(layout);
		frame.getContentPane().add(panel);
		
		tree = new HelpTree(addNodes(null, new File("Documentation/")));
		tree.setRootVisible(false);
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if(path != null) {
					if(e.getClickCount() == 2) {
						File picked = (File) ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
						if(picked.isFile() == false) return;
						try {
							view.setPage("file:///" + picked.getAbsolutePath());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		JScrollPane treeScroll = new JScrollPane(tree);
		panel.add(treeScroll);
		
		view = new JEditorPane();
		view.setEditable(false);
		try {
			view.setPage("file:///" + new File("Documentation/index.html").getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		JScrollPane viewScroll = new JScrollPane(view);
		panel.add(viewScroll);
		
		frame.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screen.width - frame.getWidth()) / 2, (screen.height - frame.getHeight()) / 2);
		frame.setVisible(true);
	}
	
	/**
	 * Load help hierarchy.
	 * @param top
	 * @param dir
	 * @return
	 */
	private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode top, File dir) {
		DefaultMutableTreeNode cur = new DefaultMutableTreeNode(dir);
		if(top != null) top.add(cur);
		
		File children[] = dir.listFiles();
		if(children == null) return cur;
		for(int i = 0; i < children.length; i++) {
			if(dir.isDirectory()) {
				cur.add(addNodes(cur, children[i]));
			} else {
				cur.add(new DefaultMutableTreeNode(children[i]));
			}
		}
		
		return cur;
	}

}
