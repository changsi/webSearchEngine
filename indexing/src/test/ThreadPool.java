package test;

import java.util.LinkedList;

public class ThreadPool
{
//    @SuppressWarnings("unused")
	private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList<Runnable> queue;
    
    public ThreadPool(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList<Runnable>();
        threads = new PoolWorker[nThreads];
        
    }
    
    public  PoolWorker[] get_threadpool(){
    	return this.threads;
    }
    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
        }
    }
    
    public void threads_run(){
    	for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }
    
    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;
            while (true) {
                synchronized(queue) {
                    if (queue.isEmpty()) {
                        break;
                    }
                    r = (Runnable) queue.removeFirst();
                }
                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();      
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
}
