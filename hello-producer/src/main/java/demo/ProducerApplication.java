package demo;

public class ProducerApplication {
    public static void main(String[] argv) throws Exception {
//        exe1();
//        exe2();
        exe3();
    }

    private static void exe1() throws Exception {
        Send.run();
    }

    private static void exe2() throws Exception {
        String[][] tasks = new String[][]
                {
                    new String[] {"[0]", "heading north", "3", "..."},
                    new String[] {"[1]", "heading south", "5", "....."},
                    new String[] {"[2]", "heading east", "6", "......"},
                    new String[] {"[3]", "heading west", "2", ".."},
                    new String[] {"[4]", "heading east", "3", "..."},
                    new String[] {"[5]", "heading sourth", "2", ".."},
                };
        for(String[] task : tasks){
            NewTask.run(task);
        }
    }

    private static void exe3() throws Exception {
        String[][] tasks = new String[][]
                {
                        new String[] {"[0]", "log information 3", "..."},
                        new String[] {"[1]", "log information 5", "....."},
                        new String[] {"[2]", "log information 6", "......"},
                        new String[] {"[3]", "log information 2", ".."},
                        new String[] {"[4]", "log information 3", "..."},
                        new String[] {"[5]", "log information 2", ".."},
                };
        for(String[] task : tasks){
            EmitLog.run(task);
        }
    }

}
