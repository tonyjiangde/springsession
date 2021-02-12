/**
 *
 */
package org.hybris.shanghai.data.tree;

import java.util.ArrayList;


/**
 * @author i314119
 *
 */
public class Node
{
	private String text;
	private String parent;
	private String id;
	private boolean duplicated;

	/**
	 * @return the duplicated
	 */
	public boolean isDuplicated()
	{
		return duplicated;
	}

	/**
	 * @param duplicated
	 *           the duplicated to set
	 */
	public void setDuplicated(final boolean duplicated)
	{
		this.duplicated = duplicated;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *           the id to set
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * @return the parent
	 */
	public String getParent()
	{
		return parent;
	}

	/**
	 * @param parent
	 *           the parent to set
	 */
	public void setParent(final String parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 *           the text to set
	 */
	public void setText(final String text)
	{
		this.text = text;
	}

	private boolean expanded;
	private boolean leaf;
	private ArrayList<Node> children;
	private String filename;


	/**
	 * @return the filename
	 */
	public String getFilename()
	{
		return filename;
	}

	/**
	 * @param filename
	 *           the filename to set
	 */
	public void setFilename(final String filename)
	{
		this.filename = filename;
	}

	/**
	 * @return the expanded
	 */
	public boolean isExpanded()
	{
		return expanded;
	}

	/**
	 * @param expanded
	 *           the expanded to set
	 */
	public void setExpanded(final boolean expanded)
	{
		this.expanded = expanded;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf()
	{
		return leaf;
	}

	/**
	 * @param leaf
	 *           the leaf to set
	 */
	public void setLeaf(final boolean leaf)
	{
		this.leaf = leaf;
	}

	/**
	 * @return the children
	 */
	public ArrayList<Node> getChildren()
	{
		return children;
	}

	/**
	 * @param children
	 *           the children to set
	 */
	public void setChildren(final ArrayList<Node> children)
	{
		this.children = children;
	}


}
