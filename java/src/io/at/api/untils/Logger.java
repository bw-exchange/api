package io.at.api.untils;

/**
 * @Author:
 * @Description:
 * @Date: Created in 2018/8/20  下午1:49
 * @Modified By:
 */
public class Logger {
    public static void error(String message){
        System.out.println(message);
    }


    public static void info(String message){
        System.out.println(message);
    }

    public static void error(String message,Exception e){
        System.out.println(buildMessage(message,e));
    }


    public static void info(String message,Exception e){
        System.out.println(buildMessage(message,e));
    }

    private static String buildMessage(Object msg, Throwable exception) {
        String stackMessage = "";
        if (exception == null) {
            return msg.toString();
        } else {
            do {
                stackMessage = stackMessage + exception.getClass().getCanonicalName() + ": " + exception.getMessage()+ exception.getStackTrace();
                exception = exception.getCause();
            } while(exception != null);

            return (msg.toString().isEmpty() ? "" : msg + " => ") + stackMessage;
        }
    }

}
