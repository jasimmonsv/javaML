package com.jasimmonsv.machlearn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.jasimmonsv.Jama.*;
import com.panayotis.gnuplot.*;
import com.panayotis.gnuplot.layout.StripeLayout;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;


public class ex1 {
	private static String filePath = "./ex1data1.txt"; //default filepath where data is held
	private static Scanner input; //class variable that holds file pointer
	private static double[][] data; //initial dump of matrix data from file;
	private static int m=0;//how many records are in the test data
	private static int n=0;//how many columns in the test data
	private static Matrix theta;
	private static Matrix X;
	private static float[] J_history;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// PART 1 Basic Functions************************************************************
		System.out.println("Running warmUpExercise...");
		System.out.println("5x5 Identity Matrix...");
		Matrix idMatrix = eye(5); 
		idMatrix.print(5,5);
		System.out.println("Paused....");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}//end try-catch
		// PART 2: Plotting************************************************************
		System.out.println("Plotting Data...(Not utilized)");
		
		//TODO add filepath arguement
		//TODO read in test data ex1data1.txt
		openFile(filePath);
		readRecords();
		closeFile();
		double[][] matX = new double[m][1];//init X vector
		double [][] matY = new double[m][1];//init y vector
		double[] y = new double[m];//init Y vector
		for (int i = 0;i<m;i++){
			matX[i][0] = data[i][0]; //build X vector from data matrix
			matY[i][0] = data[i][1]; //build X vector from data matrix
			y[i] = data[i][1]; //build Y vector from data matrix
		}//end for
		X = new Matrix(matX);
		
		Matrix Y = new Matrix(matY);
		//TODO Plotting ex1
		plotData(X, Y);
		System.out.println("Paused....");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}//end try-catch
		// PART 3 Gradiant Decent ************************************************************
		System.out.println("Gradiant Decent...(building)");
		
		X.addRow(new Matrix(m, 1, 1),':');
		theta = new Matrix(2,1,0);
		//Some gradiant descent settings
		int iterations = 1500;
		double alpha = 0.01;
		computeCost(X,y,theta);
		theta = gradientDescent(X, y, theta, alpha, iterations);
		System.out.println("Theta found by gradient descent: ");
		System.out.println(theta.get(0, 0)+" "+theta.get(1,0));
		
		//TODO Graph Part 3
		System.out.println("Paused....");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}//end try-catch
		// Part 4: Visualizing J(theta_0, theta_1) ************************************************************
		System.out.println("Visualizing J...(Not utilized)");
				//TODO Part 4
	}

	/**
	 * 
	 * @param x int of how big identity matrix should be
	 * @return identity matrix
	 */
	private static Matrix eye(int x){
		Matrix rtnMatrix = new Matrix(x,x);
		rtnMatrix.identity(x, x);
		return rtnMatrix;
	}//end method eye

	/**
	 * @params x first vector of data (x axis)
	 * @params y second vector of data (y axis)
	 * @params theta (hypothesis)
	 * @return
	 */
	private static double computeCost(Matrix X, double[] y, Matrix theta){
	float J=0;
	Matrix pred = X.times(theta);
	Matrix Y = new Matrix(y.length,1);
	for(int i =0;i<Y.getRowDimension();i++)Y.set(i, 0, y[i]);
	double tempSum = 0;
	//J= 1/(2*m) * sum((prediction-y).^2);
	J = (float) 1/(2*m);
	Matrix temp = pred.minus(Y);
	temp = temp.arrayTimesEquals(temp);
	for (int i=0;i<temp.getColumnDimension();i++)tempSum += temp.get(i, 0);
	J *= tempSum;
	return J;	
	}//end method computeCost
	
	/**
	 * 
	 * @param path Path of filename to open
	 */
	private static void openFile(String path){
		try
		{
			input = new Scanner( new File(path));
		}//end try
		catch (FileNotFoundException fileNotFoundException)
		{
			System.err.println("Error opening file.");
			System.exit( 1 );
		}//end catch
	}//end method openFile
	
	/**
	 * reads records from class var "input"
	 */
	public static void readRecords()
	{
		ArrayList<String> temp = new ArrayList<String>();
		try {
			while (input.hasNext() ){
				temp.add(input.next().toString());
			}//end while
		}//end try
		catch(NoSuchElementException elementException){
			System.err.println( "File improperly formed.");
			input.close();
			System.exit(1);
		}//end catch
		catch( IllegalStateException stateException){
			System.err.println( "Error reading from file.");
			System.exit(1);
		}//end catch
		m = temp.size();
		n = temp.get(0).split(",").length;
		String[] hold = new String[n];//hold will have x and y hold spots
		data = new double[m][n];
		for (int i = 0;i<m;i++){
			hold=temp.get(i).split(",");
			data[i][0]=Double.valueOf(hold[0]);
			data[i][1]=Double.valueOf(hold[1]);
		}//end for
	}//end method readRecords
	
	/**
	 * closes open class var "input"
	 */
	private static void closeFile(){
		if (input!=null) input.close();//close file
	}//end method closeFile
	
	/**
	 * 
	 * @param x number of columns in array
	 * @param y number of rows in array
	 * @return returns array filled with "1"
	 */
	private Double[][] ones(int x, int y){
		Double[][] retMat = new Double[x][y];
		for (int i = 0;i<x;i++){
			for (int j = 0;j<y;j++){
				retMat[i][j]=1.0;
			}//end inner for
		}//end for
		return retMat;
	}//end method ones
	
	/**
	 * 
	 * @param x number of columns in array
	 * @return ones vector
	 */
	private Double[] ones(int x){
		Double[] retMat = new Double[x];
		for (int i = 0;i<x;i++)retMat[i]=1.0;
		return retMat;
	}//end method ones
	
	private static void plotData(Matrix x ,Matrix y){
		 JavaPlot p = new JavaPlot();
	     //JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
	        
	     p.setTitle("Default Terminal Title");
	     p.getAxis("x").setLabel("X axis", "Arial", 20);
	     p.getAxis("y").setLabel("Y axis");

	     p.getAxis("x").setBoundaries(5, 25);
	     p.getAxis("y").setBoundaries(-5, 25);
	     p.setKey(JavaPlot.Key.TOP_RIGHT);
	     double[][] plot = new double[x.getRowDimension()][2];
	     for (int i=0;i<m;i++){
	    	 plot[i][0]= x.get(i, 0);
	     }
	     for (int i=0;i<m;i++){
	    	 plot[i][1]= y.get(i, 0);
	     }
	     DataSetPlot s = new DataSetPlot(plot);
	     p.addPlot(x.getArray());
	    /** PlotStyle stl = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();
	     stl.setStyle(Style.POINTS);
	     stl.setLineType(NamedPlotColor.GOLDENROD);
	     stl.setPointType(5);
	     stl.setPointSize(8);*/
	     //p.addPlot("sin(x)");

	   
	     StripeLayout lo = new StripeLayout();
	     lo.setColumns(9999);
	     p.getPage().setLayout(lo);
	     p.plot();
        
	}//end method plotData
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param theta
	 * @param alpha
	 * @param iterations
	 * @rwturn
	 */
	private static Matrix gradientDescent(Matrix x, double[] y, Matrix theta,
			double alpha, int iterations) {
		J_history = new float[iterations]; 
		for (int i = 0;i<iterations;i++){
			Matrix pred=X.times(theta);
			Matrix Y = new Matrix(y.length,1);
			for(int j =0;j<Y.getRowDimension();j++){
				Y.set(j, 0, y[j]);
			}//end for
			Matrix delta = (pred.minus(Y));
			delta = delta.transpose();
			delta = delta.times(X);
			 //theta = theta - ((alpha/m)*delta)';
			Matrix temp = delta.times(alpha/m);
			temp = temp.transpose();
			theta = theta.minus(temp);
			
			J_history[i]= (float) computeCost(X, y, theta);
		}//end for loop
		return theta;
	}
}//end class ex1
