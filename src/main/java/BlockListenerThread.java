public class BlockListenerThread extends Thread {
    long threadDelay;
    BlockListener blockListener;
    private boolean breakThread=false;
    BlockedRepository blockedRepository;
    private boolean blocked;


    public BlockListener getBlockListener() {
        return blockListener;
    }

    public void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    public BlockListenerThread() {
    }

    public boolean isBreakThread() {
        return breakThread;
    }

    public void setBreakThread(boolean breakThread) {
        this.breakThread = breakThread;
    }

    public long getThreadDelay() {
        return threadDelay;
    }

    public void setThreadDelay(long threadDelay) {
        this.threadDelay = threadDelay;
    }

    public BlockedRepository getBlockedRepository() {
        return blockedRepository;
    }

    public void setBlockedRepository(BlockedRepository blockedRepository) {
        this.blockedRepository = blockedRepository;
    }

    @Override
    public void run() {
        this.blockListener=new BlockListener();;
        this.blockListener.setCallBackVoid(b -> {
            blocked = b;
            synchronized (blockedRepository) {
                blockedRepository.setBlocked(b);
            }
            System.out.println("blocked_l=" + b);
        }
        );

        while(!breakThread){
             blockListener.listen();
        }
    }

}
