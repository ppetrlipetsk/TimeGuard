public class BlockExx extends Thread {

    @Override
    public void run(){
        BlockListener l=new BlockListener();
        l.setCallBackVoid(b-> System.out.println("locked="+b));
        while(true)
        {
            l.listen();
            //System.out.println("block="+b);
        }
    }

    public static void main(String[] args) {
        BlockExx ix=new BlockExx();
        ix.start();
    }

}
