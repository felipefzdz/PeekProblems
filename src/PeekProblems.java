import java.util.stream.IntStream;

public class PeekProblems {

    public static void main(String[] args) {
        IntStream stream = createAStreamAndPerformSomeSideEffectWithPeek();
        System.out.println("2. I should be the second group of prints");
        consumeTheStream(stream);
    }

    private static IntStream createAStreamAndPerformSomeSideEffectWithPeek() {
        return IntStream.of(1, 2, 3)
                .peek(number -> System.out.println(String.format("1. My number is %d", number)))
                .map(number -> number + 1);
    }

    private static void consumeTheStream(IntStream stream) {
        stream.filter(number -> number % 2 == 0)
                .forEach(number -> System.out.println(String.format("3. My number is %d", number)));
    }
}
