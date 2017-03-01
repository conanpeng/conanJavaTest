package com.conan.javaTest.groovy

/**
 * @author huangjinsheng on 2017/2/28.
 */
class CliBuilderTest1 {
    public static void main(String[] args){
        def cli = new CliBuilder(usage: "ls")
        cli.a("aaaa")
        cli.b("bbbb")
        cli.c("cccc")

        def options = cli.parse(args)

        if(options.a){
            System.out.println("invoke ................... a")
        }
    }
}
