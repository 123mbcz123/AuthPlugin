package Top.DouJiang.AuthPlugin;

import Top.DouJiang.Tool.SystemTools;
import Top.DouJiang.plugin.CommandClass;
import Top.DouJiang.plugin.CommandEvents;
import Top.DouJiang.plugin.CommandResult;
import Top.DouJiang.plugin.PluginMain;

import java.util.Map;

/**
 * Created by NicoNicoNi on 2017/8/5 0005.
 */
public class AuthPlugin implements PluginMain {

    @Override
    public void onEnable() {
        SystemTools.Print("[AuthMe]插件加载成功!",1,1);
    }

    @Override
    public void onDisable() {
        SystemTools.Print("[AuthMe]插件卸载成功!",1,1);
    }

}
