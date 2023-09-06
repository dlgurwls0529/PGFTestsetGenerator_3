import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Folder_GEN_CONCURRENT {

    public static void main(String[] args) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println(availableProcessors);
        concurrentInsert(SLAVE.INNO, 5000);
    }

    public static enum SLAVE{INNO, ISAM};
    private static final String[] names = {"Folder_SLAVE_INNO", "Folder_SLAVE_ISAM"};

    public static void concurrentInsert(SLAVE slave, int n_thread) {

        CountDownLatch count = new CountDownLatch(n_thread);
        ExecutorService executorService = Executors.newFixedThreadPool(n_thread);

        List<Integer> response_times = Collections.synchronizedList(new ArrayList<>());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
                String username = "root";
                String password = "1234";

                Connection connection = null;

                String folderCP = generateRandomString(60);
                String title = generateRandomString(20);

                Long begin = System.currentTimeMillis();

                try {
                    connection = DriverManager.getConnection(url, username, password);
                    connection.setAutoCommit(false);

                    PreparedStatement p1 = connection.prepareStatement("""
                                INSERT INTO
                                    Folder (folderCP, isTitleOpen, title, symmetricKeyEWF, lastChangedDate)
                                    VALUES (?, TRUE, ?, 'sym_TEST', NOW());
                            """);
                    p1.setString(1, folderCP);
                    p1.setString(2, title);
                    p1.execute();

                    PreparedStatement p2 = connection.prepareStatement("" +
                                    "INSERT INTO " + names[slave.ordinal()] + "(folderCP, title) " +
                                    " VALUES (?, ?);");
                    p2.setString(1, folderCP);
                    p2.setString(2, title);
                    p2.execute();

                    connection.commit();

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Long end = System.currentTimeMillis();
                    response_times.add((int) (end - begin));
                    count.countDown();
                }
            }
        };

        for (int i = 0; i < n_thread; i++) {
            executorService.execute(runnable);
        }

        try {
            count.await();

            int sum = 0;
            for (Integer response_time : response_times) {
                sum += response_time;
            }
            float avg = ((float)sum) / ((float)response_times.size());

            System.out.println("average response time : " + avg + " ms");
            System.out.println("succeed : " + response_times.size());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}
