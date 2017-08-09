package Top.DouJiang.Main;

import Top.DouJiang.ServerSocket.*;
import Top.DouJiang.Static.StaticMap;
import Top.DouJiang.Tool.SocketTools;
import Top.DouJiang.Tool.SystemTools;
import Top.DouJiang.Util.Mysqls.ConnectionPool;
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
        if(cc.getCmdMap().get("Cmd").equalsIgnoreCase("Auth")) {
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
                        SuccessLoginMap.put("TypeId","0003");
                        s.Send(SuccessLoginMap);
                        s.setId(User);

                    }else{
                        //账号密码不匹配
                        Map<String,String> FailLoginMap=new HashMap<>();
                        FailLoginMap.put("Cmd","Return");
                        FailLoginMap.put("TypeId","0002");
                        s.Send(FailLoginMap);
                    }
            }else{
                    //账号不能存在
                    Map<String,String> NotFindUserMap=new HashMap<>();
                    NotFindUserMap.put("Cmd","Return");
                    NotFindUserMap.put("TypeId","0001");
                    s.Send(NotFindUserMap);
                }
        } catch (SQLException e) {
            //
        }
        return cr;
    }
}
