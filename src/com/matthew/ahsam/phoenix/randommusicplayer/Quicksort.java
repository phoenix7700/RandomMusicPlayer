package com.matthew.ahsam.phoenix.randommusicplayer;
/*
 Name: QuickSort
  Author: Matthew AhSam
  Date: 3/10/2014
  Description: QuickSort.java
  Requirements: @Override Comparison function - int compare (int,int) - T1 < T2 return -1 T1 == T2 return 0 T1 > T2 return 1;
                Overloaded = operator - Makes use of = operator as a copy
 
 */
public abstract class Quicksort <T> {
	
	public T fullArray [];
	public int fullArraySize;
	protected abstract int compare (T t1,T t2);
	
	Quicksort () {
		
	}
	
	public void setFullArray (T [] array) {
		fullArray = array;
		fullArraySize = array.length;
	}
	
	public Quicksort (T [] arrayPointer ) {
		fullArraySize = arrayPointer.length;
		fullArray = arrayPointer;
	}
	
	public void DisplayArray() {
		System.out.print ("Array: {" + fullArray[0]);
		for (int i = 1; i < fullArraySize; i++) {
			System.out.print (", " + fullArray[i]);
		}
		System.out.print("}\n");
	}
	
	public T [] Sort () {
		return SortPriv (fullArraySize, fullArray);
	}
	
	
	private T [] SortPriv (int arraySize, T [] sortArray ) {
		if (sortArray.length < 1) {
			return fullArray;
		}
		//LeftSelector
		int leftCurrIndex = 0;
	    //RightSelector
	    int rightCurrIndex = arraySize-1;
	    //Find random seed for pivot
	    int seed = (int)(Math.random()*arraySize);
	    //System.out.println("Seed " + seed);
	    //System.out.println("Len " + sortArray.length);
	    T pivot = sortArray[seed];//Random pivot
	    //Keeping track of multiple pivots
	    boolean hasMoved;
	    int movedCounter = 0;
	    
	    T temp;
	    
	    /*System.out.println ("START SORT FUNCTION!"
	    		+ "\nPivot " + pivot 
	    		+ "\nArray Size: " + arraySize +"\n");*/
	    
	        while (leftCurrIndex != rightCurrIndex) {
	            hasMoved = false;
	            //DisplayArray();
	            //System.out.println("Left " + sortArray[leftCurrIndex] + " ");
	            while (1 == (compare (pivot,sortArray[leftCurrIndex]))) { //Keep getting next element while pivot is greater than left selector
	                if (leftCurrIndex == rightCurrIndex){
	                    break;
	                }
	                leftCurrIndex++;
	                //System.out.println("Picking LEFT: " + sortArray[leftCurrIndex] + " ");
	                hasMoved = true;
	            }
	            //System.out.println("Right " + sortArray[rightCurrIndex] + " ");
	            while (-1 == (compare (pivot,sortArray[rightCurrIndex]))) { //Keep getting next element while pivot is less then right selector
	                if (leftCurrIndex == rightCurrIndex){
	                    break;
	                }
	                rightCurrIndex--;
	                //System.out.println("Picking RIGHT: " + sortArray[rightCurrIndex] + " ");
	                hasMoved = true;
	            }
	            
	            temp = sortArray[leftCurrIndex];
	            sortArray[leftCurrIndex] = sortArray[rightCurrIndex];
	            sortArray[rightCurrIndex] = temp;
	            
	            if (hasMoved == false && sortArray[leftCurrIndex] == sortArray[rightCurrIndex]) { //If there are multiples of the chosen pivot this if statement 
	                if(leftCurrIndex+1 == rightCurrIndex){
	                    break; //Array is sorted if the pivots are adjacent
	                }
	                if (leftCurrIndex> rightCurrIndex){
	                	System.out.println("Something went wrong and the indexes passed each other");
	                	break;
	                }
	                if (leftCurrIndex == 0 + movedCounter) { //If leftselector is farthest left move to next element
	                    leftCurrIndex++;
	                } else {
	                    //Switch leftSelector with far left position	                	
	                	temp = sortArray[leftCurrIndex];
	    	            sortArray[leftCurrIndex] = sortArray[movedCounter];
	    	            sortArray[movedCounter] = temp;
	                }
	                
	                if (rightCurrIndex == arraySize - 1 - movedCounter) { //If rightselector is farthest right move to next element
	                    rightCurrIndex--;   
	                } else {
	                    //Switch rightSelector with far right position
	                	temp = sortArray[rightCurrIndex];
	    	            sortArray[rightCurrIndex] = sortArray[arraySize - 1 - movedCounter];
	    	            sortArray[arraySize - 1 - movedCounter] = temp;
	                }
	                
	                movedCounter++;
	            }
	            //DisplayArray();
	            //system("PAUSE");
	        }
	        //Put moved pivots back in middle
	        int endPoint = compare (pivot,sortArray[leftCurrIndex]);
	        
	        for (int i = 0 ;i < movedCounter; i++) {
	            if (-1 == endPoint){
	            	//If End Point Value is less than pivot swap moved left pivot into place
	            	temp = sortArray[leftCurrIndex-i];
    	            sortArray[leftCurrIndex-i] = sortArray[i];
    	            sortArray[i] = temp;
	            	
	                temp = sortArray[leftCurrIndex+i+1];
    	            sortArray[leftCurrIndex+i+1] = sortArray[arraySize - 1-i];
    	            sortArray[arraySize - 1-i] = temp;
	            	
	            } else {
	                if (0 == endPoint) {
	                	//If end point value is equal to pivot swap left of current with moved left pivot and
	                	//	right of current with moved right pivot
	                	temp = sortArray[leftCurrIndex-i-1];
	    	            sortArray[leftCurrIndex-i-1] = sortArray[i];
	    	            sortArray[i] = temp;

		                temp = sortArray[leftCurrIndex+i+1];
	    	            sortArray[leftCurrIndex+i+1] = sortArray[arraySize - 1-i];
	    	            sortArray[arraySize - 1-i] = temp;
	                } else { // *leftSelector > pivot
	                	//If end point value is greater than pivot swap move right pivot into place
		                temp = sortArray[leftCurrIndex-i-1];
	    	            sortArray[leftCurrIndex-i-1] = sortArray[i];
	    	            sortArray[i] = temp;
		            	
		                temp = sortArray[leftCurrIndex+i];
	    	            sortArray[leftCurrIndex+i] = sortArray[arraySize - 1-i];
	    	            sortArray[arraySize - 1-i] = temp;
	                }
	                
	            }
	        }
	        //DisplayArray();
	        //system("PAUSE");
	        if (arraySize > 2) {
	            int countSize = 0;
	            while (compare(pivot,sortArray[countSize]) != 0) {
	                countSize++;
	            }
	            if (countSize >0) {
	            	@SuppressWarnings("unchecked")
	            	T [] leftSubArray = (T[]) new Object[countSize];
	            	for (int i = 0; i < countSize;i++) {
	            		leftSubArray[i] = sortArray[i];
	            	}
	                SortPriv(countSize,leftSubArray);
	                for (int i = 0; i < countSize;i++) {
	            		sortArray[i] = leftSubArray[i];
	            	}
	            }
	            int sizeCounter = arraySize - 1;
	            countSize = 0;
	            while(compare(pivot,sortArray[sizeCounter]) != 0) {
	                sizeCounter--;
	                countSize++;
	            }
	            //Move counter back to start of 2nd sub-array
	            sizeCounter++;
	            if (countSize >0) {
	            	@SuppressWarnings("unchecked")
	            	T [] rightSubArray = (T[]) new Object[countSize];
	            	for (int i = 0; i < countSize;i++) {
	            		rightSubArray[i] = sortArray[sizeCounter + i];
	            	}
	                SortPriv(countSize,rightSubArray);
	                for (int i = 0; i < countSize;i++) {
	            		sortArray[sizeCounter + i] = rightSubArray[i];
	            	}
	            }
	        }
	    return fullArray;
	}
	
};
