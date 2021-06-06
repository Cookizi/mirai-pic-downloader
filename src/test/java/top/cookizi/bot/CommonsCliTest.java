package top.cookizi.bot;

import org.apache.commons.cli.*;
import org.junit.jupiter.api.Test;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdOption;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CommonsCliTest {


    @Test
    public void parseTest() throws ParseException {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

// create the Options
        Options options = new Options();
        options.addOption( "a", "all", false, "do not hide entries starting with ." );
        options.addOption( "A", "almost-all", false, "do not list implied . and .." );
        options.addOption( "b", "escape", false, "print octal escapes for nongraphic "
                + "characters" );
        options.addOption( OptionBuilder.withLongOpt( "block-size" )
                .withDescription( "use SIZE-byte blocks" )
                .hasArg()
                .withArgName("SIZE")
                .create() );
        options.addOption( "B", "ignore-backups", false, "do not list implied entried "
                + "ending with ~");
        options.addOption( "c", false, "with -lt: sort by, and show, ctime (time of last "
                + "modification of file status information) with "
                + "-l:show ctime and sort by name otherwise: sort "
                + "by ctime" );
        options.addOption( "C", false, "list entries by columns" );
        options.addOption( "d", true, "有参数测试" );

        String[] args = "-a -d=d参数  xxx.jpg".split("\\s+");

        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );
            System.out.println(line.getArgList());
            System.out.println(line.getOptionValue("d"));

            // validate that block-size has been set
            if( line.hasOption( "block-size" ) ) {
                // print the value of block-size
                System.out.println( line.getOptionValue( "block-size" ) );
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }

      /*  StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(printWriter,HelpFormatter.DEFAULT_WIDTH,"ls","",options,4,4,"");

        String s = writer.toString();
        System.out.println(s);
*/
    }

    @Test
    public void reflectTest() {
        RefTest test = new RefTest();
        Class<? extends RefTest> clazz = test.getClass();
        for (Method method : clazz.getMethods()) {
            for (Parameter parameter : method.getParameters()) {
                MiraiCmdOption annotation = parameter.getAnnotation(MiraiCmdOption.class);
                String name = annotation.name();
                System.out.println(name);
            }

        }
    }

    @Test
    public void refParamTest() throws InvocationTargetException, IllegalAccessException {
        RefTest test = new RefTest();
        Class<? extends RefTest> clazz = test.getClass();
        Method method = clazz.getMethods()[0];
        Object invoke = method.invoke(test, "1",2);
        System.out.println(invoke);
    }


}

class RefTest {


    @MiraiCmdDefine(name = "test")
    public void xx(@MiraiCmdOption(name = "testParam_1", desc = "测试_1") String t1,
                   @MiraiCmdOption(name = "testParam_2", desc = "测试_2") int t2) {
        System.out.println("测试方法被执行了,t1=" + t1 + ",t2=" + t2);
    }

}
