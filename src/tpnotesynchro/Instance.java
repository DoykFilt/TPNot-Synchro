package tpnotesynchro;

import java.text.DecimalFormat;
/**
 * Provides a concrete implementation of a TSP instance.
 * Instances of this class are inmutable.
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class Instance{
	/**
	 * The distance matrix
	 */
	private final double[][] matrix;
	/**
	 * Constructs a new TSP instance
	 * @param n number of nodes in the underlying graph
	 */
	public Instance(double matrix[][]){
		this.matrix=matrix.clone();
	}
	/**
	 * 
	 * @param i the first node
	 * @param j the second node
	 * @return the distance between node <code>i</code> and node <code>j</code>
	 */
	public double getDistance(int i, int j){
		return this.matrix[i][j];
	}
	/**
	 * 
	 * @return the number of nodes in the instance
	 */
	public int getN(){
		return matrix.length;
	}
	/**
	 * 
	 * @return a copy of the distance matrix
	 */
	public double[][] getDistanceMatrix(){
		return this.matrix.clone();
	}
	
	/**
	 * Prints the distance matrix to the standard output
	 * @param format decimal format (e.g., #.0000 for rounding to 4 decimals)
	 */
	public void printDistanceMatrix(String format){
		DecimalFormat df=new DecimalFormat(format);
		for(int i=0; i<this.matrix.length;i++){
			for(int j=0; j<this.matrix[0].length; j++){
				System.out.print(df.format(matrix[i][j])+"\t");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
}
