/**
 *
 */
package org.hybris.shanghai.facades;

import java.util.ArrayList;

import org.hybris.shanghai.data.tree.BDetails;
import org.hybris.shanghai.data.tree.BInfo;
import org.hybris.shanghai.data.tree.Node;


/**
 * @author i314119
 *
 */
public interface SpringContextBrowserFacade
{
	public Node getAllSpringContext();

	public ArrayList<BInfo> getBeans(final String contextid);

	public BDetails getPropertyInfos(final String contextid, final String beanid);

	public Node findContextContainingBean(final String beanid, final String beanalias, final String beanclass);

	public ArrayList<BInfo> findBeaninContext(final String contextid, final String beanid, final String beanalias,
			final String beanclass);
}
