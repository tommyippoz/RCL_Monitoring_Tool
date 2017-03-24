/**
 * 
 */
package ippoz.monitor.probes.main;

import java.awt.List;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * @author Tommy
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws ReflectionException 
	 * @throws InstanceNotFoundException 
	 * @throws IntrospectionException 
	 */
	public static void main(String[] args) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
		 final PrintWriter os = new PrintWriter(System.out);
	        os.println("<table>");

	        LinkedList<MBeanServer> servers = new LinkedList<MBeanServer>();
	        servers.add(ManagementFactory.getPlatformMBeanServer());
	        servers.addAll(MBeanServerFactory.findMBeanServer(null));
	        for (final MBeanServer server : servers) {
	            os.println(server.getClass().getName());

	            final Set<ObjectName> mbeans = new HashSet<ObjectName>();
	            mbeans.addAll(server.queryNames(null, null));
	            for (final ObjectName mbean : mbeans) {
	                os.println("MBean: " + mbean);

	                final MBeanAttributeInfo[] attributes = server.getMBeanInfo(mbean).getAttributes();
	                for (final MBeanAttributeInfo attribute : attributes) {
	                    //os.println(attribute.getName() + "-" + attribute.getType());

	                    try {
	                        final Object value = server.getAttribute(mbean, attribute.getName());
	                        if (value == null) {
	                           // os.print("<font color='#660000'>null</font>");
	                        } else {
	                            //os.print(value.toString());
	                        }
	                    } catch (Exception e) {
	                        os.print("<font color='#990000'>" + e.getMessage()
	                                + "</font>");
	                    }
	                }
	            }
	        }

	        os.println("</table>");
	        os.flush();
	}

}
