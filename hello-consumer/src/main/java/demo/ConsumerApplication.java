package demo;

public class ConsumerApplication {
    public static void main(String[] argv) throws Exception {
//        exe1();
//        exe2();
        exe3();
    }

    private static void exe1() throws Exception {
        Receive.run();
    }

    private static void exe2() throws Exception {
        final int workerNum = 2;
        for(int i = 0; i < workerNum; i++)
        {
            Worker.run(i);
        }
    }

    private static void exe3() throws Exception {
        ReceiveLogs.run();
    }
}
