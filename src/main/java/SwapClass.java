import java.io.*;

public class SwapClass {
    private String filename;
  //  ObjectOutputStream objectOutputStream;

/*
    public void initSwap(String filename) throws IOException {
        this.filename=filename;
        //this.initSwap();
    }
*/

/*
    public void initSwap() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    }
*/

    public  void save(WorkoutCache c) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(c);
        objectOutputStream.close();
    }

    public  WorkoutCache load() throws Exception {
        WorkoutCache c=new WorkoutCache();
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object o= objectInputStream.readObject();
        if (o instanceof WorkoutCache) c= (WorkoutCache) o;
            else throw new Exception("Неверный тип данных, при загрузке из дампа...");

        objectInputStream.close();
        return c;
    }


/*
    public void close() throws IOException {
        if (objectOutputStream!=null) objectOutputStream.close();
    }
*/

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
