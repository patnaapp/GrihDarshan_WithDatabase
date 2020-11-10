package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Block_List implements KvmSerializable {
    public static Class<Block_List> Block_Name_CLASS = Block_List.class;

    private String Blk_Code, Blk_Name, dist_code,Block_NAME_HN;

    public Block_List(SoapObject sobj) {
        this.Blk_Code = sobj.getProperty("Block_CODE").toString();
        this.Blk_Name = sobj.getProperty("Block_NAME").toString();
        this.Block_NAME_HN = sobj.getProperty("Block_NAME_HN").toString();

    }

    public Block_List() {

    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value) {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

    }

    public String getBlk_Code() {
        return Blk_Code;
    }

    public void setBlk_Code(String blk_Code) {
        Blk_Code = blk_Code;
    }

    public String getBlk_Name() {
        return Blk_Name;
    }

    public void setBlk_Name(String blk_Name) {
        Blk_Name = blk_Name;
    }

    public String getDist_code() {
        return dist_code;
    }

    public void setDist_code(String dist_code) {
        this.dist_code = dist_code;
    }

    public String getBlock_NAME_HN() {
        return Block_NAME_HN;
    }

    public void setBlock_NAME_HN(String block_NAME_HN) {
        Block_NAME_HN = block_NAME_HN;
    }

    public static interface CentreAddDeductInterface {
    }
}
