package tpnotesynchro;

import java.util.Properties;

/**
 * Defines the interface to optimization algorithms
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public interface Algorithm {
	
	/**
	 * Runs the algorithm and returns the solution
	 * @param config the algorithm's configuration
	 * @return the solution to the problem
	 */
	public Solution run(Properties config);
	
}
