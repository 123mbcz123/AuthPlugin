package Top.DouJiang.Main;

import Top.DouJiang.Tool.SocketTools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NicoNicoNi on 2017/8/8 0008.
 */
public class Test {
    public static void main(String[ ] args){
        Map<String,String> TestMap=new HashMap<>();
        TestMap.put("Cmd","Return");
        TestMap.put("TypeId","0001");
        System.out.println(SocketTools.MapToJson(TestMap));
    }
}
