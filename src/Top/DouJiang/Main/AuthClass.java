package Top.DouJiang.Main;

/**
 * Created by NicoNicoNi on 2017/8/6 0006.
 */
public class AuthClass {
    public String getSerssion() {
        return Serssion;
    }

    private String Serssion=null;

    public String getId() {
        return Id;
    }

    private String Id=null;
    public AuthClass(String Serssion,String Id){
        this.Serssion=Serssion;
        this.Id=Id;
    }
}
