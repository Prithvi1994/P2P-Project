package edu.ufl.cise.cnt5106c.file;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;

/**
 * Created by adalton on 3/22/15.
 */
public class Destination {

    private File _file;
    private File  _partsDir;
    private static final String partsLocation = "./files/parts/";

    public Destination(String fileName){
        _partsDir = new File(partsLocation + fileName);
        _partsDir.mkdirs();
        _file = new File(_partsDir.getParent() + "/../" + fileName);
    }

    public byte[][] getAllPartsAsByteArrays(){
        FileFilter fileFilter = new NameFileFilter("^\\d+$");
        File[] files = _partsDir.listFiles(fileFilter);
        byte[][] ba = new byte[files.length][getPartAsByteArray(1).length];
        for (int i = 0; i < files.length; i++) {
            ba[Integer.parseInt(files[i].getName())] = getByteArrayFromFile(files[i]);
        }
        return ba;
    }

    public byte[] getPartAsByteArray(int partId){
        byte[] fileBytes = null;
        File file = new File(_partsDir.getAbsolutePath() + "/" + partId);
        fileBytes = getByteArrayFromFile(file);
        return fileBytes;
    }

    public void writeByteArrayAsFilePart(byte[] part, int partId){
        FileOutputStream fos;
        File ofile = new File(_partsDir.getAbsolutePath() + "/" + partId);
        try {
            fos = new FileOutputStream(ofile, true);
            fos.write(part);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getByteArrayFromFile(File file){
        byte[] fileBytes = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            int bytesRead = fis.read(fileBytes, 0, (int) file.length());
            fis.close();
            assert (bytesRead == fileBytes.length);
            assert (bytesRead == (int) file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;

    }


    public void splitFile(int partSize){
        SplitFile sf = new SplitFile();
        sf.process(_file, partSize);
    }

}