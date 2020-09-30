public class BlockEx extends Thread {
    @Override
    public void run(){
        BlockListener l=new BlockListener();
        l.listenLoop();
    }

    public static void main(String[] args) {
        BlockEx ix=new BlockEx();
        ix.start();
    }
}
