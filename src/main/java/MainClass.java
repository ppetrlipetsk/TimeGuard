import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainClass {
    private static final int SWAP_INTERVAL=5;
    private static final long TIME_LIMIT = 20L;
    private static final long  DELAY_TIME=1L;
    private static final String FILENAME="c://files//swap.swp";
    private static List<String> users;
    static {
        users=new ArrayList<>();
        users.add("96-paliy");
        users.add("7-7");
    }


    public static void main(String[] args) throws Exception {
        if (users.contains(System.getProperty("user.name"))) {


            BlockedRepository blockedRepository = new BlockedRepository();


            SwapClass swapClass = new SwapClass();
            swapClass.setFilename(FILENAME);

            WorkoutCache workoutCache = initFromSwap(swapClass);
            if (workoutCache.getCounter() >= TIME_LIMIT) {
                System.out.println("Time limit is over...");

            } else {
                TimeCounter tc = new TimeCounter();
                tc.setWorkoutCache(workoutCache);
                tc.setSwapInterval(SWAP_INTERVAL);
                tc.setLimit(TIME_LIMIT);
                tc.setDelayTime(DELAY_TIME);
                tc.setBlockedRepository(blockedRepository);
                tc.setSwapClass(swapClass);

                BlockListenerThread blockedListenerThread = new BlockListenerThread();
                blockedListenerThread.setBlockedRepository(blockedRepository);

                blockedListenerThread.start();
                tc.start();

                tc.join();
                blockedListenerThread.interrupt();
                blockedListenerThread.setBreakThread(true);
                Thread.sleep(2000);
                System.out.println("TimeGuard was finished");
                swapClass.save(workoutCache);
            }
            System.exit(0);

        }
    }
    private static boolean compareDateField(Calendar c1, Calendar c2, int field){
        return c1.get(field)==c2.get(field);
    }

    private static boolean dateEquals(Date d1, Date d2){
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        return compareDateField(c1,c2, Calendar.YEAR)&&compareDateField(c1,c2,Calendar.MONTH)&&compareDateField(c1,c2,Calendar.DAY_OF_MONTH);
    }


    private static WorkoutCache initFromSwap(SwapClass swapClass) {
        Date now=new Date();
        if ((new File(swapClass.getFilename()).exists())) {
            WorkoutCache cache = null;

            try {
                cache = swapClass.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (cache != null) {
                Date cacheDate = cache.getDate();
                if (dateEquals(cacheDate, now)) return cache;
                else
                    return new WorkoutCache(now);
            }
        }
        return new WorkoutCache(now);
    }

}
