import java.io.Serializable;
import java.util.Date;

public class WorkoutCache implements Serializable {
    private Date date;
    private long counter;


    public WorkoutCache() {
        counter=0;
    }

    public WorkoutCache(Date date) {
        this();
        this.date=date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    @Override
    public String toString(){
        return "WorkoutCache{"
                +"date="
                +date
                +"counter="+ counter;
    }
}
