package com.andrei.DataAccess;

import com.andrei.BusinessLogic.Client;
import com.andrei.BusinessLogic.MenuItem;
import com.andrei.BusinessLogic.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class serializes all the information about the application.
 * @param <T> -> type parameter to be serialized for lists. Can be client or menuItem.
 */
public class Serializer<T> {
    private final Class<T> type;

    public Serializer(Class<T> type) {
        this.type = type;
    }

    /**
     * Serializes T from a file with name "Ts.dat". Calls the read
     * object method in a while loop until the end of file is reached.
     * Should any exceptions happen, the list will be returned empty.
     * @return a list of objects read from the serialized file
     */
    @SuppressWarnings("unchecked")
    public List<T> readFromFile()
    {
        List<T> toRead = new ArrayList<>();
        try {
            ObjectInputStream file = new ObjectInputStream(new FileInputStream("serial\\"
                    +type.getSimpleName().toLowerCase()+"s.dat"));
            boolean eof = false;
            while (!eof) {
                try {
                    T entry = (T) file.readObject();
                    toRead.add(entry);
                    if(type== Client.class) System.out.println("Read " + entry.toString());
                } catch (EOFException e) {
                    eof = true;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Couldn't find exiting file to read from! Collection is empty. type=" + type.getSimpleName());
        }
        catch (IOException ex)
        {
            System.err.println("IOException when reading from serialised file! type=" + type.getSimpleName());
            ex.printStackTrace();
        }
        return toRead;
    }

    /**
     * This method serializes all the data from the list to a file with name
     * "Ts.dat". An object output stream is used, and each object is written
     * with the toWrite method. Should an error occur, application data will not
     * be serialized to the file
     * @param toWrite -> list of T to be serialized
     */
    public void writeToFile(List<T> toWrite)
    {
        try {
            ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream("serial\\"
                    +type.getSimpleName().toLowerCase()+"s.dat"));
            for(T t: toWrite)
            {
                file.writeObject(t);
            }
        }
        catch (IOException ex)
        {
            System.err.println("IOException when writing to serialised file! type=" + type.getSimpleName());
            ex.printStackTrace();
        }
    }

    /**
     * This method serializes a hashmap. Because the map is not a child of collection,
     * a special method is required in order to serialize it. Thankfully, HashMap is
     * serializable, so a single call to write object is enough to complete the serialization.
     * @param toWrite -> hashmap to serialize
     */
    public static void writeHashMap(HashMap<Order, ArrayList<MenuItem>> toWrite)
    {
        try {
            ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream("serial\\"
                    +"orderDetails.dat"));
            file.writeObject(toWrite);
        }
        catch (IOException ex)
        {
            System.err.println("IOException when writing to serialised file! type= HashMap");
            ex.printStackTrace();
        }
    }

    /**
     * This method will read the order details hash map from a file. A single call
     * to read object is enough to get all the data, unlike when working with a list.
     * @return serialized hash map of order details
     */
    @SuppressWarnings("unchecked")
    public static HashMap<Order, ArrayList<MenuItem>> readHashMap()
    {
        try {
            ObjectInputStream file = new ObjectInputStream(new FileInputStream("serial\\orderDetails.dat"));
            HashMap<Order, ArrayList<MenuItem>> newHashMap;
            newHashMap = (HashMap<Order, ArrayList<MenuItem>>)file.readObject();
            return newHashMap;
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Couldn't find the serialised file");
            return new HashMap<>();
        }
        catch (IOException ex)
        {
            System.err.println("Error when reading from serialised file!");
            return  new HashMap<>();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
