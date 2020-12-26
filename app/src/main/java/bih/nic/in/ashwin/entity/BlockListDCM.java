package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class BlockListDCM implements KvmSerializable {
    public static Class<BlockListDCM> BlockListDCM_CLASS = BlockListDCM.class;

    //private String Blk_Code, Blk_Name, dist_code,Block_NAME_HN;
    private String OtherDist,OtherBlock,BlockCode,DistrictCode,DistrictName,BlockName;

    public BlockListDCM(SoapObject sobj) {
        this.OtherDist = sobj.getProperty("OtherDist").toString();
        this.OtherBlock = sobj.getProperty("OtherBlock").toString();
        this.BlockCode = sobj.getProperty("BlockCode").toString();
        this.DistrictCode = sobj.getProperty("DistrictCode").toString();
        this.DistrictName = sobj.getProperty("DistrictName").toString();
        this.BlockName = sobj.getProperty("BlockName").toString();

    }

    public BlockListDCM() {

    }

    public String getOtherDist() {
        return OtherDist;
    }

    public void setOtherDist(String otherDist) {
        OtherDist = otherDist;
    }

    public String getOtherBlock() {
        return OtherBlock;
    }

    public void setOtherBlock(String otherBlock) {
        OtherBlock = otherBlock;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
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
}
