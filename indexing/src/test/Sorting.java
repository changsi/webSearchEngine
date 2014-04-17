package test;

import java.util.ArrayList;

public class Sorting{
//    private static int MAX_STACK_SIZE=4096;
//    private static int THRESHOLD=10;
    /* (non-Javadoc)
     * @see org.rut.util.algorithm.SortUtil.Sort#sort(int[])
     */
    
    public static void swap(ArrayList<posting> data, int i, int j) {
        posting temp = data.get(i);
        data.add(i, data.get(j));
        data.add(j,temp);
    }
    
//    public void sort(ArrayList<posting> data) {
//        int[] stack=new int[MAX_STACK_SIZE];
//        
//        int top=-1;
//        int pivot;
//        int pivotIndex,l,r;
//        
//        stack[++top]=0;
//        stack[++top]=data.size()-1;
//        
//        while(top>0){
//            int j=stack[top--];
//            int i=stack[top--];
//            
//            pivotIndex=(i+j)/2;
//            pivot=data[pivotIndex];
//            
//            swap(data,pivotIndex,j);
//            
//            //partition
//            l=i-1;
//            r=j;
//            do{
//                while(data[++l]<pivot);
//                while((r!=0)&&(data[--r]>pivot));
//                swap(data,l,r);
//            }
//            while(l<r);
//            swap(data,l,r);
//            swap(data,l,j);
//            
//            if((l-i)>THRESHOLD){
//                stack[++top]=i;
//                stack[++top]=l-1;
//            }
//            if((j-l)>THRESHOLD){
//                stack[++top]=l+1;
//                stack[++top]=j;
//            }
//            
//        }
//        //new InsertSort().sort(data);
//        insertSort(data);
//    }
//    /**
//     * @param data
//     */
//    private void insertSort(int[] data) {
//        int temp;
//        for(int i=1;i<data.length;i++){
//            for(int j=i;(j>0)&&(data[j]<data[j-1]);j--){
//                swap(data,j,j-1);
//            }
//        }       
//    }
}
