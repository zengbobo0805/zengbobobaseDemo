package com.zengbobobase.demo.plugin;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2015/12/3 15:13
 * 修改人：bobo
 * 修改时间：2015/12/3 15:13
 * 修改备注：
 */
public class TestPlugin {
    public void intPluginFirst(){
        if(ClassVerifier.PREVENT_VERIFY){
            PluginFirst first = new PluginFirst();
            first.setName("PluginFirst");
            System.out.println("TestPlugin intPluginFirst first.name:"+first.getName());
        }
    }
}
