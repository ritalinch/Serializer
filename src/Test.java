public class Test implements Savable{
    private String path = "";

    public Test() { }

    public Test(String path) {
        this.path = path;
    }
    @Save
    private int a = 2;

    @Override
    public String toString() {
        return "Test{" +
                "path='" + path + '\'' +
                ", a=" + a +
                ", b=" + b +
                '}';
    }

    @Save
    private int b = 3;
    @Override
    public String getPath() {
        return path;
    }
}
