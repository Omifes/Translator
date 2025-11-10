public class Service {

    public void publicAdd(int a, int b) {
        System.out.println("publicAdd: " + (a + b));
    }

    public String PublicEcho(String s) {
        return "publicEcho: " + s;
    }

    @Repeat(2)
    protected void protectedLog(String msg, int code) {
        System.out.println("protectedLog: " + (msg == null ? "<null>" : msg) + " [" + code + "]");
    }

    @Repeat(3)
    protected int protectedSum(int... nums) {
        int sum = 0;
        if (nums != null) {
            for (int n : nums) sum += n;
        }
        System.out.println("protectedSum: " + sum);
        return sum;
    }

    @Repeat(1)
    private void privateHello(String name) {
        System.out.println("privateHello: " + (name == null ? "guest" : name));
    }

    @Repeat(4)
    private int privateMul(int x, int y) {
        int r = x * y;
        System.out.println("privateMul: " + r);
        return r;
    }
}
