/**
 * @author jasimmonsv
 * @version 0.1
 * 
 * This program is the java equilivent of Stanfords Machine Learning class Homework 3.
 * v 0.1 implements ex3_nn and the needed functions and does not implement the first 
 * part of the homework.
 * 
 */
package com.jasimmonsv.machlearn;

import java.applet.Applet;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.lang.Math;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jasimmonsv.Jama.Matrix;
import com.panayotis.gnuplot.*;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class ex3 extends Applet{
	private static Scanner input; //class variable that holds file pointer
	//private int input_layer_size  = 400;  //20x20 Input Images of Digits
	//private int hidden_layer_size = 25;   // 25 hidden units
	//private int num_labels = 10;          // 10 labels, from 1 to 10   
	                          // (note that we have mapped "0" to label 10)
	private static double[][] xData; //initial dump of matrix data from file;
	private static Matrix yData; //initial dump of matrix data from file;
	private static int m=0;//how many records are in the test data
	private static int n=0;//how many columns in the test data
	private static Matrix Theta1; //Thetas for NN training
	private static Matrix Theta2; //Thetas for NN training
	private static JFrame aFrame = new JFrame("NN Display");
	private static Component a = new ex3();
	private static int m1; //how many rows in array
	private static int n1; //how many cols in array
	private static int example_width;//how wide the numbers are going to be
	private static int example_height; //how tall the numbers are going to be
	private static int display_rows; //define how many rows of numbers to display
	private static int display_cols; //define how many cols of numbers to display
	private static int pad = 1; //padding between border and numbers
	private static double[][] display_array;
	
	/**
	 * @param args not used
	 */
	public static void main(String[] args) {
		//===========================Part 1: Loading and visualizing data ===========================
		//Load Training data
		System.out.println("Loading and Visualizing Data ...");
		//open file and load data into memory
		openFile("./ex3data1.txt");
		readRecords();
		
		//testDisplayData(xData[20]);
		//displayData(randomArray(xData,50));//**************Define how many numbers to display
		
		/**In this part of the exercise, we load some pre-initialized 
		% neural network parameters.
		//================ Part 2: Loading Pameters ================
		*/
		System.out.println("Loading Saved Neural Network Parameters ...");
		//load theta data and save into memory
		openFile("./ex3weights.txt");//Theta1=>25x401 Theta2=>10x26
		readThetas();
		
		//================ Part 3: Implement Predict ================
		/**  After training the neural network, we would like to use it to predict
		*  the labels. You will now implement the "predict" function to use the
		*  neural network to predict the labels of the training set. This lets
		*  you compute the training set accuracy.
		*/
		double[][] pred = predict(Theta1, Theta2, xData);
		double[][] vars=new double[pred.length][1];
		double[][] num=new double[pred.length][1];
		for(int i=0;i<pred.length;i++){
			vars[i][0]=pred[i][0];
			num[i][0]=pred[i][1];
		}//end for
		
		System.out.println("Training Set Accuracy: "+ matrixMean(compare(new Matrix(num),yData)) * 100);
		System.out.println("Paused....Press Enter to Continue");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}//end try-catch
		
		//TODO Finish this last part
		//%  Randomly permute examples
		int count=0;
		int iterations=100000;
		Long startTime = System.currentTimeMillis();
		for (int i = 0;i<iterations;i++){//chage for iterations
			 
    		//System.out.println("Displaying Example Image");
    		int rp = (int)(Math.random() *xData.length);

    		pred = predict(Theta1, Theta2, xData[rp]);//returns array of guess
    		vars=new double[pred.length][1];//percentage guess
    		num=new double[pred.length][1];//position within answer array
    		vars[0][0]=pred[0][0];
    		num[0][0]=pred[0][1];
    		
    		/** This block for high iteration error display only**************
    		if (num[0][0]!=yData.get(rp, 0)){
    			//System.out.println(count+": "+ vars[0][0]*100+"% "+num[0][0] +" (digit "+ yData.get(rp, 0)+")");
    			count++;
    		}//end if
    		//    *************END high iteration error display block  */ 
    		//** This block for normal operations *****************************
    		displayData(xData[rp]);
    		System.out.println(count+": "+ vars[0][0]*100+"% "+num[0][0] +" (digit "+ yData.get(rp, 0)+")");
    		
    		// Pause after every result
    		System.out.println("Paused....Press Enter to Continue");
    		try {
    			System.in.read();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}//end try-catch*/
    		//    *************END normal iteration error display block  */ 
    		
		}//end for
		// Pause
		Long endTime = System.currentTimeMillis();
		double elapsedTime = (endTime-startTime)/1000;
		System.out.println("Time elapsed: "+elapsedTime+" seconds");
		System.out.println(iterations/elapsedTime+" digits/sec");
		System.out.println("End of Program....Press Enter to Continue. Error Count: "+count);
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}//end try-catch
	}//end method main
	
	private static void readThetas() {
		int matrixRows = 5000;
		int matrixCols = 400;
		String temp1 = new String();
		ArrayList<Double> t1Temp = new ArrayList<Double>();
		ArrayList<Double> t2Temp = new ArrayList<Double>();
		try {
			while (input.hasNext() ){
				temp1 = input.next().toString();
				if (temp1.matches("theta2"))break;
				t1Temp.add(Double.valueOf(temp1));
			}//end while
			while (input.hasNext() ){
				t2Temp.add(Double.valueOf(input.next().toString()));
			}//end while
		}//end try
		catch(NoSuchElementException elementException){
			System.err.println( "File improperly formed.");
			input.close();
			System.exit(1);
		}//end catch
		catch( IllegalStateException stateException){
			System.err.println( "Error reading from file.");
			input.close();
			System.exit(1);
		}//end catch
		
		//Process Stream
		int t1m = 25;
		int t1n = 401;
		int t2m = 10;
		int t2n = 26;
		Theta1 = new Matrix(t1m,t1n);
		
		int count = 0;
		for (int i = 0;i<t1m;i++){
			for (int j=0;j<t1n;j++){
				Theta1.set(i, j, t1Temp.get(count));
				count++;
			}//end inner for loop
		}//end outer for loop
		
		Theta2 = new Matrix(t2m,t2n);
		count = 0;
		for (int i = 0;i<t2m;i++){
			for (int j=0;j<t2n;j++){
				Theta2.set(i, j, t2Temp.get(count));
				count++;
			}//end inner for loop
		}//end outer for loop
	}

	/**
	 * This method adds up all values in a matrix and then averages them all using 
	 * getRowDimension and getColumnDimension
	 * 
	 * @param compare matrix to mean
	 * @return return the mean of all the values within Matrix
	 */
	private static double matrixMean(Matrix a) {
		double ret = 0.0;
		//Sum all values in matrix
		for (int i = 0;i<a.getRowDimension();i++){
			for(int j=0;j<a.getColumnDimension();j++){
				ret = ret + a.get(i, j);
			}//end inner for loop
		}//end outer for loop
		ret = ret/((a.getRowDimension())+(a.getColumnDimension()));
		return ret;
	}//end method matrixMean

	/**
	 * This method takes two matrices and compares them against each other and 
	 * returns a new matrix of 1's and 0's to indicate true or false
	 * 
	 * @param a first Matrix to compare
	 * @param b second Matrix to compare 
	 * @return a matrix of 1 for true and 0 for false
	 */
	private static Matrix compare(Matrix a, Matrix b) {
		if(a.getColumnDimension()==b.getColumnDimension()&&a.getRowDimension()==b.getRowDimension()){
			Matrix ret = new Matrix(a.getRowDimension(),a.getColumnDimension());
			for(int i=0;i<a.getRowDimension();i++){
				for (int j=0;j<a.getColumnDimension();j++){
					if (a.get(i, j)==b.get(i, j))ret.set(i, j, 1.0);
					else ret.set(i, j, 0.0);
				}//end inner for loop
			}//end outer for loop
			return ret;
		}//end if
		else{
			System.err.println("Matrices need to be same dimensions.");
			return null;
		}//end else
	}//end method compare

	/**
	 * This method opens the file as the "input" scanner.
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
	 * read in records of handwritten image data and saves data as double[][] xData 
	 * and double[][] yData (declared globally)
	 * 
	 */
	private static void readRecords()
	{
		int matrixRows = 5000;
		int matrixCols = 400;
		String temp1 = new String();
		ArrayList<Double> xTemp = new ArrayList<Double>();
		ArrayList<Double> yTemp = new ArrayList<Double>();
		try {
			while (input.hasNext() ){
				temp1 = input.next().toString();
				if (temp1.matches("y"))break;
				xTemp.add(Double.valueOf(temp1));
			}//end while
			while (input.hasNext() ){
				yTemp.add(Double.valueOf(input.next().toString()));
			}//end while
		}//end try
		catch(NoSuchElementException elementException){
			System.err.println( "File improperly formed.");
			input.close();
			System.exit(1);
		}//end catch
		catch( IllegalStateException stateException){
			System.err.println( "Error reading from file.");
			input.close();
			System.exit(1);
		}//end catch
		m = 5000;
		n = 400;
		xData = new double[m][n];
		yData = new Matrix(m,1);
		int count = 0;
		for (int i = 0;i<matrixRows;i++){
			for (int j=0;j<matrixCols;j++){
				xData[i][j] = xTemp.get(count);
				count++;
			}//end inner for loop
		}//end outer for loop
		
		/**
		 * double[][] display = new double[height][width];
		int count =0;
		for (int i=0;i<height;i++){
			for (int j=0; j<width;j++){
				display[i][j] = X[count];
				count++;
			}//end inner loop
		}//end outer loop
		return display;
		 */
		
		
		
		for (int j = 0;j < yTemp.size();j++) yData.set(j, 0, yTemp.get(j));
	}//end method readRecords
	
	private static void testDisplayData(double[] X){
		
		 //Var declerations
		
		//define new m and n for smaller x array
		int m1 = 1; //how many rows in array
		int n1 = X.length; //how many cols in array
		int example_width = (int) Math.sqrt(n1);//how wide the numbers are going to be
		int example_height = (int) n1/ example_width; //how tall the numbers are going to be
		int display_rows = (int) Math.floor(Math.sqrt(m1)); //define how many rows of numbers to display
		int display_cols = (int) Math.ceil(m1/display_rows); //define how many cols of numbers to display
		int pad = 1; //padding between border and numbers
		int curr_ex = 0; //counter for which handwritten number we are on
		
		//create a new frame to which we will add a canvas
	    
	    
	    ex3.aFrame.setSize(300,300);
	    ex3.aFrame.getContentPane().add(a, BorderLayout.CENTER);
	    //ex3.aFrame.setSize(display_rows*example_width, display_cols*example_height);
	    ex3.aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
			
		//build display_array
		double[][] display_array = new double[pad + display_rows * (example_height + pad)][pad + display_cols * (example_width + pad)];
			
		//init display_array to -1
		for(int i=0;i<display_array.length;i++){
			for(int j=0;j<display_array[0].length;j++)display_array[i][j]=-1;
		}//end outer loop
			
		double[][] tempX = new double[example_height][example_width];//temp matrix for reshaping
		for (int j = 1; j < display_rows+1;j++){ //one row at a time
			for (int i = 1; i< display_cols+1;i++){  //every column in the given row
				if (curr_ex > m1) break;//extra check for outofarraybounds 
				//Copy the patch
				//Get the max value of the patch
				tempX = reshape(X,example_height,example_width);
				int max_val = maxValue(X);//get max value for row "curr_ex"
				//Save new matrix into its allowed display_array matrix
				for (int k=0;k<example_height;k++){
					for (int l=0;l<example_width;l++){
						display_array[pad + (j - 1) * (example_height + pad) + (k)][pad + (i - 1) * (example_width + pad) + (l)] = tempX[k][l];//makes sense
					}//end inner for loop
				}//end outer for loop
				curr_ex++;//next area of display
			}//end inner for loop
			if (curr_ex > m1) break; //extra check for outofarraybounds
		}//end outer for loop
			
		//uses gnujavaplot to display results
			
	     //add the canvas
	     aFrame.setVisible(true);
		
	}//end testDisplayData
	
	
	public void paint(Graphics g){
		
		for (int x=0;x<display_array.length;x++){
			for (int y=0;y<display_array[0].length;y++){
				int rgb = (int) (125.5*display_array[x][y]+124);
				if (rgb > 254)rgb=254;
				else if (rgb<0)rgb=0;
				g.setColor(new Color(rgb,rgb,rgb));
				g.fillRect(x+30, y+30, 30, 30);
				g.setColor(Color.red);
			}//end inner loop
		}//end outer loop
	}//end method paint

	/**
	 * 
	 * @param X the matrix to display image data from
	 * !!!!!!!!!Specific to the given data format provided!!!!!!!!!
	 * 
	 */
	private static void displayData(double[][] X){
		
		//define new m and n for smaller x array
		m1 = X.length; //how many rows in array
		n1 = X[0].length; //how many cols in array
		example_width = (int) Math.sqrt(n);//how wide the numbers are going to be
		example_height = (int) n1/ example_width; //how tall the numbers are going to be
		display_rows = (int) Math.floor(Math.sqrt(m1)); //define how many rows of numbers to display
		display_cols = (int) Math.ceil(m1/display_rows); //define how many cols of numbers to display
		pad = 1; //padding between border and numbers
		int curr_ex = 0; //counter for which handwritten number we are on
		
		//build display_array
		double[][] display_array = new double[pad + display_rows * (example_height + pad)][pad + display_cols * (example_width + pad)];
		
		//init display_array to -1
		for(int i=0;i<display_array.length;i++){
			for(int j=0;j<display_array[0].length;j++)display_array[i][j]=-1;
		}//end outer loop
		
		double[][] tempX = new double[example_height][example_width];//temp matrix for reshaping
		for (int j = 1; j < display_rows;j++){ //one row at a time
			for (int i = 1; i< display_cols;i++){  //every column in the given row
					if (curr_ex > m1) break;//extra check for outofarraybounds 
					//Copy the patch
					tempX = reshape(X[curr_ex],example_height,example_width);
					
					//Get the max value of the patch
					//int max_val = maxValue(X[curr_ex]);//get max value for row "curr_ex"
					
					//Save new matrix into its allowed display_array matrix
					for (int k=0;k<example_height;k++){
						for (int l=0;l<example_width;l++){
							display_array[pad + (j - 1) * (example_height + pad) + (k)][pad + (i - 1) * (example_width + pad) + (l)] = tempX[k][l];//makes sense
						}//end inner for loop
					}//end outer for loop
					curr_ex++;//next area of display
			}//end inner for loop
				if (curr_ex > m1) break; //extra check for outofarraybounds
		}//end outer for loop
		
		//uses gnujavaplot to display results
		//TODO fix the grayscale shading, or replace with better display module
		
 
        //***************Drawing the display
       
	}//end method displayData
	
	/**
	 * Overloaded method of displayData
	 * @param X the matrix to display image data from
	 * !!!!!!!!!Specific to the given data format provided!!!!!!!!!
	 * 
	 */
	private static void displayData(double[] X){
		//Var declerations
	    
		//define new m and n for smaller x array
		m1 = 1; //how many rows in array
		n1 = X.length; //how many cols in array
		example_width = (int) Math.sqrt(n1);//how wide the numbers are going to be
		example_height = (int) n1/ example_width; //how tall the numbers are going to be
		display_rows = (int) Math.floor(Math.sqrt(m1)); //define how many rows of numbers to display
		display_cols = (int) Math.ceil(m1/display_rows); //define how many cols of numbers to display
		int curr_ex = 0; //counter for which handwritten number we are on
		
	    aFrame.setSize(display_rows*example_width*10, display_cols*example_height*10);
	    aFrame.getContentPane().setSize(1000, 1000);
	    aFrame.getContentPane().add(a, BorderLayout.CENTER);
	    aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    a.setSize(display_rows*example_width*10, display_cols*example_height*10);
	    aFrame.setAlwaysOnTop(true);
	    
		//build display_array
		display_array = new double[pad + display_rows * (example_height + pad)][pad + display_cols * (example_width + pad)];
		
		//init display_array to -1
		for(int i=0;i<display_array.length;i++){
			for(int j=0;j<display_array[0].length;j++)display_array[i][j]=-1;
		}//end outer loop
		
		double[][] tempX = new double[example_height][example_width];//temp matrix for reshaping
		for (int j = 1; j < display_rows+1;j++){ //one row at a time
			for (int i = 1; i< display_cols+1;i++){  //every column in the given row
					if (curr_ex > m1) break;//extra check for outofarraybounds 
					//Copy the patch
					
					//Convert a row of the data matrix into a width x height grayscale image
					tempX = reshape(X,example_height,example_width);
					int max_val = maxValue(X);//get max value for row "curr_ex"
					
					//Save new matrix into its allowed display_array matrix
					for (int k=0;k<example_height;k++){
						for (int l=0;l<example_width;l++){
							display_array[pad + (j - 1) * (example_height + pad) + (k)]
										 [pad + (i - 1) * (example_width + pad) + (l)] = 
										 tempX[k][l]/max_val;//makes sense
						}//end inner for loop
					}//end outer for loop
					curr_ex++;//next area of display
					
			}//end inner for loop
				if (curr_ex > m1) break; //extra check for outofarraybounds
		}//end outer for loop
		
		//TODO fix the grayscale shading, or replace with better display module
		aFrame.setVisible(true);
		ex3.aFrame.repaint();
        
	}//end method displayData

	/**
	 * This method will return the largest value of an array out of a vector
	 * 
	 * @param X Vector to search
	 * @return single int value of the largest value in the array
	 */
	private static int maxValue(double[] X){
		int max = -1;
		for (int i = 0;i<X.length;i++)max = Math.max(max, (int) Math.abs(X[i]));
		return max;
	}//end method maxValue
	
	/**
	 * This method will take a vector and turn it into a heightXwidth array
	 * 
	 * @param X Vector of image data
	 * @param height height of desired output matrix 
	 * @param width width of desired output matrix
	 * @return returns X data as desired matrix
	 */
	private static double[][] reshape(double[]X, int height, int width){
		double[][] display = new double[height][width];
		int count =0;
		for (int i=0;i<height;i++){
			for (int j=0; j<width;j++){
				display[i][j] = X[count];
				count++;
			}//end inner loop
		}//end outer loop
		return display;
	}//end method reshape
	
	/**
	 * This function selects random rows from a given array
	 * 
	 * @param X the matrix array to pick from
	 * @param num the number of random selections you want to perform
	 * @return the new smaller matrix array you wish to use 
	 */
	private static double[][] randomArray(double[][] X,int num){
		double [][]toPass =new double[num][X[0].length];
		int[] sel = new int[num];
		for (int i=0;i<num;i++)sel[i]=(int)(Math.random() *X.length);
		for (int i=0;i<num;i++)toPass[i]=X[sel[i]];
		return toPass;
	}//end method randomArray
	
	/**
	 * Predict the label of an input given a trained neural network
	 * p = PREDICT(Theta1, Theta2, X) outputs the predicted label of X given the
	 * trained weights of a neural network (Theta1, Theta2)
	 * @param Theta1 First round of neural network weights
	 * @param Theta2 second round of neural network weights
	 * @param X Sample of handwritten data
	 */
	private static double[][] predict(Matrix Theta1, Matrix Theta2, double[][] x1){
		
		Matrix X = new Matrix(x1); //build X data matrix
		
		X.addRow(new Matrix(m, 1, 1),':');//and ones vector to front of Matrix
		X = X.transpose();
		Matrix z = Theta1.times(X);
		Matrix a2 = sigmoid(z);
		
		a2 = a2.transpose();
		a2.addRow(new Matrix(m, 1, 1),':');
		z=Theta2.times(a2.transpose());
		Matrix a3=sigmoid(z);
		a3 = a3.transpose();
		double[][] maxA3 = a3.max(2);
		return maxA3;
	}//end method predict
	
	/**
	 * Overloaded method of predict
	 * @param Theta1
	 * @param Theta2
	 * @param x1
	 * @return
	 */
	private static double[][] predict(Matrix Theta1, Matrix Theta2, double[] x1){
		
		Matrix X = new Matrix(x1,1); //build X data matrix
		
		X.addRow(new Matrix(m, 1, 1),':');//and ones vector to front of Matrix
		X = X.transpose();
		Matrix z = Theta1.times(X);
		Matrix a2 = sigmoid(z);
		
		a2 = a2.transpose();
		a2.addRow(new Matrix(m, 1, 1),':');
		z=Theta2.times(a2.transpose());
		Matrix a3=sigmoid(z);
		a3 = a3.transpose();
		double[][] maxA3 = a3.max(2);
		return maxA3;
	}//end method predict
	
	/**
	 * 
	 * @param x matrix to calculate sigmoid from
	 * @return new sigmoid matrix
	 */
	private static Matrix sigmoid(Matrix z){
		//g = 1.0 ./ (1.0 + exp(-z)); ==Octave code
		
		for (int i=0;i<z.getRowDimension();i++){
			for (int j=0;j<z.getColumnDimension();j++){
				z.set(i, j, (1.0 / (1.0 + Math.exp(-z.get(i, j)))) );
			}//end inner loop
		}//end outer loop
		return z;
	}//end method sigmoid
	
	/**
	 * Test method to print the raw array into the cli
	 * @param X matrix to print in cli
	 */
	private static void printArray(double[][] X){
		for (int i =0;i<X.length;i++){
			for (int j=0;j<X[0].length;j++) System.out.print(X[i][j]+" ");
			System.out.println();
		}//end for
	}//end emthod printArray
	
}//end class ex3
