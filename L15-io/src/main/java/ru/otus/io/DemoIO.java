package ru.otus.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author sergey
 * created on 27.01.19.
 */
public class DemoIO {

    private static final String PERSON_FILE = "person.bin";
    private static final String TEXT_FILE = "textFile.txt";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("current dir: " + System.getProperty("user.dir"));
        //copyFile();
        //writeObject();
        //readObject();
        //writeTextFile();
        //readTextFile();

    }

    //Обратите внимание на цепочку декораторов
    private static void copyFile() throws IOException {
        try (var bufferedInputStream = new BufferedInputStream(new FileInputStream(TEXT_FILE));
             var zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(TEXT_FILE + "_copy.zip")))) {

            ZipEntry zipEntry = new ZipEntry(TEXT_FILE);
            zipOut.putNextEntry(zipEntry);
            byte[] buffer = new byte[2];
            int size;
            while ((size = bufferedInputStream.read(buffer, 0, buffer.length)) > 0) {
                zipOut.write(buffer, 0, size);
            }
            zipOut.closeEntry();
        }
    }

    private static void writeObject() throws IOException {
        try (var fileOutputStream = new FileOutputStream(PERSON_FILE);
             var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            var person = new Person(92, "SerialPerson", "hidden");
            System.out.println("serializing:" + person);
            objectOutputStream.writeObject(person);
            objectOutputStream.flush();
        }
    }

    private static void readObject() throws IOException, ClassNotFoundException {
        try (var fileInputStream = new FileInputStream(PERSON_FILE);
             var objectInputStream = new ObjectInputStream(fileInputStream)) {

            var person = (Person) objectInputStream.readObject();
            System.out.println("read object:" + person);
        }
    }

    private static void writeTextFile() throws IOException {
        var line1 = "Hello Java, str1";
        var line2 = "Hello Java, str2";
        try (var bufferedWriter = new BufferedWriter(new FileWriter(TEXT_FILE));) {
            bufferedWriter.write(line1);
            bufferedWriter.newLine();
            bufferedWriter.write(line2);
        }
    }

    private static void readTextFile() throws IOException {
        try (var bufferedReader = new BufferedReader(new FileReader(TEXT_FILE))) {
            System.out.println("text from the file:");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
