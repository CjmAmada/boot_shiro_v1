package com.cjm.shiro.boot_shiro_v1.p1;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * @author chenjm
 * @version V1.0
 * @Title: Demo1
 * @Package: com.cjm.shiro.boot_shiro_v1.p1
 * @Description: TOTO
 * @date 2019 2019/8/21 9:41
 **/
public class Demo1 {
    private static final Joiner joiner = Joiner.on(",").skipNulls();
    private static final Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

    public static void main(String[] args) {
        String json = joiner.join(Lists.newArrayList("a", null, "b"));
        System.err.println("join="+json);

        Iterable<String> split = splitter.split("a,   , b, ,");
        for (String s : split) {
            System.err.println("|" + s + "|");
        }
    }
}
