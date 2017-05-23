package com.abao.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  ×ÔÖ÷¿É¿Ø¹¤³ÌÖÐÐÄÆ½Ì¨¼Ü¹¹(TAIJI Security Controllable Platform)
 * <p>com.taiji.tscp.common
 * <p>File: AbsTscpFactory.java create time:2015-5-8ÏÂÎ?07:57:37</p> 
 * <p>Title: abstract factory class </p>
 * <p>Description: store jvm information. Developer can get jvm of the system </p>
 * <p>Copyright: Copyright (c) 2015 taiji.com.cn</p>
 * <p>Company: taiji.com.cn</p>
 * <p>module: common abstract class</p>
 * @author  Ö£³ÐÀÚ
 * @version 1.0
 * @history ÐÞ¶©ÀúÊ·£¨Àú´ÎÐÞ¶©ÄÚÈÝ¡¢ÐÞ¶©ÈË¡¢ÐÞ¶©Ê±¼äµÈ£©
 */
public class FileHandler {
	
	private LinkedBlockingQueue<String> queue;
	private int consumerThreadNum;
	private File rootDir;
	private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String,Integer>();
	private CountDownLatch latch;
	private String endFlag = "-----#######%#%%%%%=======";
	
	
	/**
	 * @param queueDeap ¶ÓÁÐÉûÒÈ
	 * @param consumerThreadNum Ïû·ÑÏß³ÌÊ?
	 * @param rootDir ÎÄ¼?¸ùÄ¿Â¼
	 */
	public FileHandler(int queueDeap,int consumerThreadNum,File rootDir) {
		super();
		init(queueDeap,consumerThreadNum,rootDir);
	}


	private void init(int queueDeap,int consumerThreadNum,File rootDir){
		this.consumerThreadNum = consumerThreadNum;
		queue = new LinkedBlockingQueue<String>(queueDeap);
		this.rootDir = rootDir;
		latch = new CountDownLatch(consumerThreadNum);
		produce();
		consumer();
	}
	
	
	private void produce(){
		new Thread(new Runnable() {
			public void run() {
				try {
					if(rootDir == null || !rootDir.exists())
						throw new FileNotFoundException();
					List<File> lisFile = new ArrayList<File>();
					loopFindFile(rootDir,lisFile);
					for(File file:lisFile)
						readFile(file);
					for(int i=0;i<consumerThreadNum;i++)
						queue.offer(endFlag);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	private void readFile(File file) throws IOException{
		BufferedReader reader = null ; 
		try{
			reader = new BufferedReader(new FileReader(file));
			String tmp;
			while((tmp = reader.readLine()) != null)
				this.queue.offer(tmp);
		}finally{
			if(reader != null) reader.close();
		}
		
	}
	
	private  void loopFindFile(File file,List<File> filelis){
		if(!file.isFile()){
			for(File chirldren:file.listFiles())
				loopFindFile(chirldren,filelis);
		}else
			filelis.add(file);
	}
	
	
	private void consumer(){
		for(int i=0;i<this.consumerThreadNum;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try{
						String tmp = queue.take();
						if(tmp == null||"".equals(tmp))
							continue;
						if(endFlag.equals(tmp)){
							latch.countDown();
							break;
						}
					       for(String key:tmp.split("//s")){
					    	   if(map.get(key)!= null)
					    		   map.put(key,map.get(key)+1);
					    	   else
					    		   map.put(key, 1);
					       }
						}catch(Throwable ex){
							ex.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
	
	
	public void printWordCount() throws InterruptedException{
		
		latch.await();
		System.out.println("===============´òÓ¡Í³¼Æµ¥´ÊÊ?Á¿===============");
		for(Entry<String,Integer> entry:map.entrySet())
			System.out.println(entry.getKey()+":"+entry.getValue());
		System.out.println("===============´òÓ¡Í³¼Æµ¥´ÊÊ?Á¿½áÊ?===============");

	}
	
	public void printRangeWord(){
		List<Map<String,Integer>> lis = new ArrayList<Map<String,Integer>>();
		for(Entry<String,Integer> entry:map.entrySet()){
			if(lis.size()<3){
				Map<String,Integer> map = new HashMap<String,Integer>();
				map.put(entry.getKey(), entry.getValue());
				lis.add(map);
			}else if(lis.size() == 2){
				for(int i = 0;i<lis.size()-1;i++)
					for(int j = 1;j<lis.size();j++)
						for(Entry<String,Integer>innerSeti:lis.get(i).entrySet())
							for(Entry<String,Integer>innerSetj:lis.get(j).entrySet())
							  if(innerSeti.getValue()<innerSetj.getValue()){
								  Map<String,Integer> tm = new HashMap<String,Integer>();
								  tm.put(innerSetj.getKey(), innerSetj.getValue());
								  lis.add(j, lis.get(j));lis.add(i, tm);
							  }
			}else{
				boolean flag = false;
				for(int i = 0;i<lis.size();i++){
					for(Entry<String,Integer>mapentry:lis.get(i).entrySet())
						if(mapentry.getValue()<entry.getValue()){
							  Map<String,Integer> tm = new HashMap<String,Integer>();
							  tm.put(entry.getKey(), entry.getValue());
							  lis.add(i, tm);
							  flag = true;
							  break;
						}
				if(flag)
					break;
				}
			}
		}
		System.out.println("===============¿ªÊ¼´òÓ¡Ç°È?Ãûµ¥´Ê===============");
		for(Map<String,Integer> map:lis)
			for(Entry<String,Integer> entry:map.entrySet())
			System.out.println(entry.getKey()+":"+entry.getValue());
		System.out.println("===============´òÓ¡Ç°È?Ãûµ¥´Ê½áÊ?===============");
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		
		FileHandler handler = new FileHandler(200,3,new File("E:/bak"));
		handler.printWordCount();
		handler.printRangeWord();
	}
	

}
