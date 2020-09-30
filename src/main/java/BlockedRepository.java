public class BlockedRepository {
    boolean isBlocked=false;

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
        System.out.println("blocked="+blocked);
    }
}
