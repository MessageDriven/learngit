package com.abao.demo;

import java.util.Arrays;

public class SortExmple {



public static void insertSort(){  
    int a[]={49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};  
    int temp=0;  
    for(int i=1;i<a.length;i++){
       int j=i-1;  
       temp=a[i];
       for(;j>=0&&temp<a[j];j--){
           a[j+1]=a[j];  //将大于temp的值整体后移一个单位  
       }  
       a[j+1]=temp;
    }  
  
    for(int i=0;i<a.length;i++){  
       System.out.println(a[i]);
    }  
} 

public static void shellSort(){

    int a[]={1,54,6,3,78,34,12,45,56,100};
    double d1=a.length;
    int temp=0;

    while(true){
       d1= Math.ceil(d1/2);
       int d=(int) d1;
       for(int x=0;x<d;x++){

           for(int i=x+d;i<a.length;i+=d){
              int j=i-d;
              temp=a[i];
              for(;j>=0&&temp<a[j];j-=d){
                   a[j+d]=a[j];
              }
              a[j+d]=temp;
           }
       }

       if(d==1){
           break;
       }

    for(int i=0;i<a.length;i++){
       System.out.println(a[i]);
    }
}
}


public static void main(String args[]){
	insertSort();
}




public class HeapSort {  
    int a[]={49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};  
    public  HeapSort(){  
       heapSort(a);  
    }  
  
    public  void heapSort(int[] a){  
        System.out.println("开始排序");  
        int arrayLength=a.length;  
        //循环建堆  
        for(int i=0;i<arrayLength-1;i++){  
            //建堆  
            buildMaxHeap(a,arrayLength-1-i);  
            //交换堆顶和最后一个元素  
            swap(a,0,arrayLength-1-i);  
            System.out.println(Arrays.toString(a));  
        }  
    }  
  
   
  
    private  void swap(int[] data, int i, int j) {  
        // TODO Auto-generated method stub  
        int tmp=data[i];  
        data[i]=data[j];  
        data[j]=tmp;  
    }  
  
    //对data数组从0到lastIndex建大顶堆  
    private void buildMaxHeap(int[] data, int lastIndex) {  
        // TODO Auto-generated method stub  
        //从lastIndex处节点（最后一个节点）的父节点开始  
  
        for(int i=(lastIndex-1)/2;i>=0;i--){  
            //k保存正在判断的节点  
            int k=i;  
            //如果当前k节点的子节点存在  
            while(k*2+1<=lastIndex){  
                //k节点的左子节点的索引  
                int biggerIndex=2*k+1;  
                //如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在  
                if(biggerIndex<lastIndex){  
                    //若果右子节点的值较大  
                    if(data[biggerIndex]<data[biggerIndex+1]){  
                        //biggerIndex总是记录较大子节点的索引  
                        biggerIndex++;  
                    }  
                }  
  
                //如果k节点的值小于其较大的子节点的值  
               if(data[k]<data[biggerIndex]){  
                    //交换他们  
                    swap(data,k,biggerIndex);  
                    //将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值  
                    k=biggerIndex;  
                }else{  
                    break;  
                }  
            }  
        }  
    }  
}  
}