import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCounter extends Thread{
    private long limit;
    private long delayTime;
    //private long threadDelay;
    //private long workCounter;
    private boolean breakThread;
    private BlockedRepository blockedRepository;
    private int swapInterval;
    private WorkoutCache workoutCache;
    private SwapClass swapClass;


    private void process(){
        int swapCounter=0;
        long workCounter=this.workoutCache.getCounter();
        long threadDelay=delayTime*1000;

        try {
        while ((workCounter<limit)&&(!breakThread)){
            Thread.currentThread().sleep(threadDelay);
            synchronized (blockedRepository)
            {
                if (!blockedRepository.isBlocked()) {
                    workCounter += delayTime;
                    swapCounter++;
                }
                else
                    System.out.println("System blocked");
            }
            System.out.println("counter="+workCounter);

            if (swapCounter==swapInterval){
                System.out.println("swap");
                swapCounter=0;
                workoutCache.setDate(new Date());
                workoutCache.setCounter(workCounter);
                swapToDisk();
            }
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workoutCache.setCounter(workCounter);
        System.out.println("TimeCounter was finished");
    }

    private void swapToDisk() {
        if (swapClass!=null) {
            try {
                swapClass.save(workoutCache);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка I/O"+e.toString());
            }
        }
    }


     void setSwapInterval(int swapInterval) {
        this.swapInterval = swapInterval;
    }

    @Override
    public void run() {
        this.process();
    }

     void setWorkoutCache(WorkoutCache workoutCache) {
        this.workoutCache = workoutCache;
    }

     void setLimit(long limit) {
        this.limit = limit;
    }

     void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

     void setBlockedRepository(BlockedRepository blockedRepository) {
        this.blockedRepository = blockedRepository;
    }

     void setSwapClass(SwapClass swapClass) {
        this.swapClass = swapClass;
    }
}
