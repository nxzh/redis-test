package fun.code4.redis;

public class ThreadLocalDemo {
  private static final ThreadLocal<Integer> TL_INT = ThreadLocal.withInitial(() -> 6);
  private static final ThreadLocal<String> TL_STRING = ThreadLocal.withInitial(() -> "Hello, world");

  public static void main(String... args) throws InterruptedException {
    // 6
    System.out.println(TL_INT.get());
    TL_INT.set(TL_INT.get() + 1);

    Thread t = new Thread(() -> {
      TL_INT.set(TL_INT.get() + 1);
      System.out.println("==========" + TL_INT.get());
    });
    t.start();
    Thread.sleep(100);
    // 7
    System.out.println(TL_INT.get());
    TL_INT.remove();
    // 会重新初始化该value，6
    System.out.println(TL_INT.get());
  }
}
