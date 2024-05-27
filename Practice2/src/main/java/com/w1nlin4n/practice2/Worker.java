package com.w1nlin4n.practice2;

public class Worker extends Thread{

	private final int id;
	private final Data data;
	
	public Worker(int id, Data d){
		this.id = id;
		data = d;
		this.start();
	}
	
	@Override
	public void run(){
		super.run();
		for (int i=0; i<5; i++){
			synchronized (data){
				switch (data.getState()) {
					case 1:
						data.Tic();
						break;
					case 2:
						data.Tac();
						break;
					case 3:
						data.Toe();
						break;
				}
			}
		}
	}
	
}
