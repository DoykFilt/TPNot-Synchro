package tpnotesynchro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implements an instance reader for TSPLIB files. Implements the builder design pattern.
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class InstanceReader {

	//The file holding the instance data
	private BufferedReader reader;
	//The instance being built
	private Instance i=null;
	/**
	 * Directs the instance construction
	 * @param inputFile the name of the file (path included) to read
	 */
	public void buildInstance(String inputFile) {
		//set up the buffered reader
		try {
			setBR(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//read coordinates from the file
		double[][] coordinates=readCoordinates();
		//build instance
		i=new Instance(EuclideanCalculator.calc(coordinates));
	}
	/**
	 * 
	 * @return the built instance
	 */
	public Instance getInstance() {
		if(i==null) throw new IllegalStateException("The instance has not been built");
		return i;
	}
	
	//Instantiates the buffered reader
	private void setBR(String fileName) throws FileNotFoundException{
		reader = new BufferedReader(new FileReader(fileName));
	}
	//Reads the coordinates
	private double[][] readCoordinates(){
		boolean readingHeader = true;
		boolean readingCoordinates = false;
		int i=0, dimension;
		double[][] coordinates=null;
		String headerName;
		String headerValue;
		String[] items;
		try{
			String line = reader.readLine().trim();
			while (!line.isEmpty() && !line.equals("EOF")){
				if (readingHeader){
					items= line.split(":");
					headerName = items[0].trim().toUpperCase();
					headerValue = items.length > 1 ? items[1].trim() : "";
					if (headerName.equals("DIMENSION")){
						dimension = Integer.parseInt(headerValue);
						coordinates=new double[dimension][2];
					}
					if (headerName.equals("NODE_COORD_SECTION")){
						readingHeader = false;
						readingCoordinates = true;
					}
				} else if (readingCoordinates) {
					items = line.split("(\\s)+");
					coordinates[i][0]= Double.parseDouble(items[1]);
					coordinates[i][1]= Double.parseDouble(items[2]);
					i++;
				}
				line = reader.readLine().trim();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return coordinates;
	}

}
