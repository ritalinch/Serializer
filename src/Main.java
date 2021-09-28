public class Main {

    public static void main(String[] args) {
        Test test = new Test("test.txt");
        Serializer<Test> serializer = new Serializer<>(Test.class);
        serializer.serialize(test);
        Test newTest = serializer.deserialize("test.txt");
        System.out.println(newTest);
    }
}
