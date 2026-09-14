package advanced.customwritable;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.checkerframework.checker.units.qual.Temperature;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class FireAvgTempWritable implements Writable{

    // set private attributes:
    private float temp;
    private int frequence;

    // create the getters and setters

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    // empty class constructor
    public FireAvgTempWritable(){};

    // create constructor that receives both parameters
    public FireAvgTempWritable(float temp, int frequence) {
        this.temp = temp;
        this.frequence = frequence;
    };


    @Override
    public void write(DataOutput out) throws IOException {
        // converts to string!
        out.writeUTF(String.valueOf(temp));
        out.writeUTF(String.valueOf(frequence));
        // output "3.5", "1"

    }

    //le os campos, ou seja, lê os atributos
    @Override
    public void readFields(DataInput in) throws IOException {
        // read the data that comes from HADOOP, meaning it comes with different types
        // parses the writeable to float
        temp = Float.parseFloat(in.readUTF());
        frequence = Integer.parseInt(in.readUTF());

    }
}
