package com.course.generator.server;

import com.course.generator.util.CommonUtil;
import com.course.generator.util.DbUtil;
import com.course.generator.util.Field;
import com.course.generator.util.FreemarkerUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

public class VueGenerator {

    static String toVuePath = "admin\\src\\views\\admin\\";

    public static void main(String[] args) throws Exception {


//        Map<String, Object> map = CommonUtil.CommonGenerator();
//
//        // 生成vue
//        FreemarkerUtil.initConfig("vue.ftl");
//        FreemarkerUtil.generator(toVuePath + map.get("domain") + ".vue", map);

        // 提取二
        CommonUtil.CommonGenerator1("vue.ftl",".vue",toVuePath);
    }

}
