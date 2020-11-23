package com.yijiinfo.common.util;

/**
 * 时间工具类
 */
public class DataUtils{
    /**
     * 类型转换
     *
     * @return hikType 返回海康平台人员类型
     */
    public static String transPersonType(String originalType) {
        String hikType = null;
        switch (originalType){
            case "1":
            case "12":
                hikType = "1";
                break;
//            case "2":
//            case "3":
//            case "4":
//                hikType = "2";
//                break;
            case "5":
            case "7":
            case "9":
            case "10":
            case "11":
            case "32":
            case "30":
            case "31":
                hikType = "3";
                break;
            case "6":
            case "8":
            case "13":
            case "14":
            case "23":
            case "24":
            case "25":
            case "26":
            case "27":
            case "28":
            case "29":
                hikType = "4";
                break;
            default:
                hikType = "2";
        }
        return hikType;
    }


}