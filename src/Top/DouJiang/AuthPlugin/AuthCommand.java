package Top.DouJiang.AuthPlugin;

import Top.DouJiang.Main.CallEventClass;
import Top.DouJiang.ServerSocket.*;
import Top.DouJiang.Static.StaticMap;
import Top.DouJiang.Tool.SocketTools;
import Top.DouJiang.Tool.SystemTools;
import Top.DouJiang.Util.Mysqls.ConnectionPool;
import Top.DouJiang.plugin.AuthClass;
import Top.DouJiang.plugin.CommandClass;
import Top.DouJiang.plugin.CommandEvents;
import Top.DouJiang.plugin.CommandResult;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NicoNicoNi on 2017/8/8 0008.
 */
public class AuthCommand implements CommandEvents {
    @Override
    public CommandResult CommandEvent(CommandClass cc) {
        if(!cc.getCmdMap().get("Cmd").equalsIgnoreCase("Auth")) {
            return null;
        }
        CommandResult cr=new CommandResult();
        cr.SetCancel(true);
        Map<String,String> CmdMap=cc.getCmdMap();
        Sockets s=cc.getS();
        String User=CmdMap.get("User");
        String Pass = SocketTools.Base64Decrypt(CmdMap.get("Pass"));
        ConnectionPool.PooledConnection pool = StaticMap.pool;
        PreparedStatement ps = null;
        ResultSet rs = null;
            try {
                ps = pool.getPrepareStatement("select * from user where id=?;");
                ps.setString(1, User);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String pass2 = rs.getString("pass");
                    String salt = rs.getString("salt");
                    Pass = SocketTools.StringToMD5(SocketTools.StringToMD5(Pass) + salt);//加密后的
                    if (pass2.equalsIgnoreCase(Pass)) {
                        //成功登入
                        SystemTools.Print("Id: "+User+" 登入成功!",1,1);
                        Map<String,String> SuccessLoginMap=new HashMap<>();
                        SuccessLoginMap.put("Cmd","Return");
                        SuccessLoginMap.put("TypeId","Auth_003");
                        s.Send(SuccessLoginMap);
                        s.setId(User);
                        s.setAuth(true);
                        CallEventClass cec=new CallEventClass();
                        AuthClass ac=new AuthClass(s);
                        cec.CallAuthEvent(ac);
                        //登入成功执行
                    }else{
                        //账号密码不匹配
                        Map<String,String> FailLoginMap=new HashMap<>();
                        FailLoginMap.put("Cmd","Return");
                        FailLoginMap.put("TypeId","Auth_002");
                        s.Send(FailLoginMap);
                    }
            }else{
                    //账号不能存在
                    Map<String,String> NotFindUserMap=new HashMap<>();
                    NotFindUserMap.put("Cmd","Return");
                    NotFindUserMap.put("TypeId","Auth_001");
                    s.Send(NotFindUserMap);
                }
        } catch (SQLException e) {
            //
        }
        return cr;
    }
}
