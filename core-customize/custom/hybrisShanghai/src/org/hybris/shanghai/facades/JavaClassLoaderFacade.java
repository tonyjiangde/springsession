/**
 *
 */
package org.hybris.shanghai.facades;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.hybris.shanghai.data.tree.Node;


/**
 * @author i314119
 *
 */
public interface JavaClassLoaderFacade
{
	public LinkedHashMap<String, ArrayList<String>> getPlatformURLs();

	public LinkedHashMap<String, ArrayList<String>> getWebAppURLs();

	public Node findClass(final String cn);

	public Node getURLs();
}
